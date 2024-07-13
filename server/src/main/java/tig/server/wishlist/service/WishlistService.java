package tig.server.wishlist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tig.server.club.domain.Club;
import tig.server.club.dto.ClubResponse;
import tig.server.club.mapper.ClubMapper;
import tig.server.club.service.ClubService;
import tig.server.error.BusinessExceptionHandler;
import tig.server.error.ErrorCode;
import tig.server.member.domain.Member;
import tig.server.member.dto.MemberResponse;
import tig.server.member.mapper.MemberMapper;
import tig.server.member.service.MemberService;
import tig.server.wishlist.domain.Wishlist;
import tig.server.wishlist.dto.WishlistRequest;
import tig.server.wishlist.dto.WishlistResponse;
import tig.server.wishlist.mapper.WishlistMapper;
import tig.server.wishlist.repository.WishlistRepository;

import java.util.ArrayList;
import java.util.List;

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

    //사용자 아이디로 위시리스트 조회
    public List<ClubResponse> getWishlistByUserId(Long memberId) {
        try {
            List<WishlistResponse> responseList = wishlistRepository.findAllByMemberId(memberId).stream()
                    .map(wishlistMapper::entityToResponse)
                    .toList();

            List<ClubResponse> reponseList = new ArrayList<>();
            for (WishlistResponse wishlist : responseList) {
                ClubResponse club = clubService.getClubById(wishlist.getClub().getId());
                reponseList.add(club);
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

            // 중복 체크 로직 추가
            if (wishlistRepository.existsByClubIdAndMemberId(clubId, memberId)) {
                throw new BusinessExceptionHandler("이미 위시리스트에 존재하는 항목입니다.", ErrorCode.BAD_REQUEST_ERROR);
            }

            WishlistRequest request = WishlistRequest.builder()
                    .club(club)
                    .member(member)
                    .build();

            Wishlist wishlist = wishlistMapper.requestToEntity(request); // createdAt, updatedAt은 추가해야함.
            wishlistRepository.save(wishlist);
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
