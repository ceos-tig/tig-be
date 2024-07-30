package tig.server.wishlist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tig.server.club.domain.Club;
import tig.server.club.dto.ClubResponse;
import tig.server.club.mapper.ClubMapper;
import tig.server.club.service.ClubService;
import tig.server.config.S3Uploader;
import tig.server.global.exception.BusinessExceptionHandler;
import tig.server.global.code.ErrorCode;
import tig.server.member.domain.Member;
import tig.server.member.dto.MemberResponse;
import tig.server.member.mapper.MemberMapper;
import tig.server.member.service.MemberService;
import tig.server.wishlist.domain.Wishlist;
import tig.server.wishlist.dto.WishlistRequest;
import tig.server.wishlist.dto.WishlistResponse;
import tig.server.wishlist.mapper.WishlistMapper;
import tig.server.wishlist.repository.WishlistRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final ClubService clubService;
    private final MemberService memberService;

    private final WishlistMapper wishlistMapper = WishlistMapper.INSTANCE;
    private final ClubMapper clubMapper = ClubMapper.INSTANCE;
    private final MemberMapper memberMapper = MemberMapper.INSTANCE;
    private final S3Uploader s3Uploader;

    //사용자 아이디로 위시리스트 조회
    public List<ClubResponse> getWishlistByUserId(Long memberId) {
        try {
            List<WishlistResponse> responseList = wishlistRepository.findAllByMemberId(memberId).stream()
                    .map(wishlistMapper::entityToResponse)
                    .toList();

            List<ClubResponse> reponseList = new ArrayList<>();
            for (WishlistResponse wishlist : responseList) {
                ClubResponse clubResponse = clubService.getClubByIdForLoginUser(memberId, wishlist.getClub().getId());
                clubResponse.setPresignedImageUrls(s3Uploader.getPresignedUrls(clubResponse.getId(), clubResponse.getImageUrls()));
                reponseList.add(clubResponse);
            }
            return reponseList;
        } catch (Exception e) {
            throw new BusinessExceptionHandler("위시리스트 조회 중 에러 : " + e.getMessage(), ErrorCode.IO_ERROR);
        }
    }

    @Transactional
    public void addWishlist(Long clubId, Long memberId) {
        try {
            ClubResponse clubResponse = clubService.getClubById(clubId); // 있는 클럽인지 검사
            Club club = clubMapper.responseToEntity(clubResponse); // createdAt, updatedAt은 추가해야함.

            MemberResponse memberResponse = memberService.getMemberById(memberId); // 있는 멤버인지 검사
            Member member = memberMapper.responseToEntity(memberResponse); // createdAt, updatedAt은 추가해야함.

            // 삭제된 애들 & 삭제 안된 애들 둘다 조회됨
            Optional<Wishlist> existingWishlist = wishlistRepository.findAllByClubIdAndMemberId(clubId, memberId);

            if (existingWishlist.isEmpty()) {
                WishlistRequest request = WishlistRequest.builder()
                        .createdAt(LocalDateTime.now())
                        .club(club)
                        .member(member)
                        .build();

                Wishlist wishlist = wishlistMapper.requestToEntity(request);
                wishlistRepository.save(wishlist);
            } else {
                Wishlist wishlist = existingWishlist.get();
                if (wishlist.isDeleted()) {
                    wishlistRepository.restoreWishlist(clubId, memberId);
                } else {
                    throw new BusinessExceptionHandler("이미 위시리스트에 존재하는 항목입니다.", ErrorCode.BAD_REQUEST_ERROR);
                }
            }
        } catch (Exception e){
            throw new BusinessExceptionHandler("위시리스트에 추가 하는 과정에서 에러 : " + e.getMessage(), ErrorCode.IO_ERROR);
        }
    }

    @Transactional
    public void removeWishlist(Long clubId, Long memberId) {
        try {
            clubService.getClubById(clubId); // 있는 클럽인지 검사
            memberService.getMemberById(memberId); // 있는 멤버인지 검사

            Wishlist wishlist = wishlistRepository.findByMemberIdAndClubId(memberId, clubId)
                    .orElseThrow(() -> new BusinessExceptionHandler("해당하는 위시리스트 목록이 없습니다", ErrorCode.BAD_REQUEST_ERROR));

            wishlistRepository.softDeleteById(wishlist.getId()); // wishlist에서 soft delete
        } catch (Exception e) {
            throw new BusinessExceptionHandler("위시리스트 삭제 과정에서 에러 : " + e.getMessage(), ErrorCode.BAD_REQUEST_ERROR);
        }
    }
}
