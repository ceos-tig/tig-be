package tig.server.wishlist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tig.server.club.dto.ClubResponse;
import tig.server.club.service.ClubService;
import tig.server.error.BusinessExceptionHandler;
import tig.server.error.ErrorCode;
import tig.server.member.domain.Member;
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

    private final WishlistMapper wishlistMapper = WishlistMapper.INSTANCE;
    private final MemberService memberService;

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
    public void addWishlist(WishlistRequest request, Long memberId) {
        try {
            clubService.getClubById(request.getClubId()); // 있는 클럽인지 검사
            memberService.getMemberById(memberId); // 있는 멤버인지 검사
            Wishlist wishlist = wishlistMapper.requestToEntity(request); // createdAt, updatedAt은 추가해야함.
            wishlistRepository.save(wishlist);
        } catch (Exception e){
            throw new BusinessExceptionHandler("위시리스트에 추가 하는 과정에서 에러 : " + e.getMessage(), ErrorCode.IO_ERROR);
        }
    }

    @Transactional
    public void removeWishlist(WishlistRequest request, Long memberId) {
        try {
            clubService.getClubById(request.getClubId()); // 있는 클럽인지 검사
            memberService.getMemberById(memberId); // 있는 멤버인지 검사
            Wishlist wishlist = wishlistRepository.findByMemberIdAndClubId(memberId, request.getClubId())
                    .orElseThrow(() -> new BusinessExceptionHandler("해당하는 위시리스트 목록이 없습니다", ErrorCode.BAD_REQUEST_ERROR));

            wishlistRepository.softDeleteById(wishlist.getId()); // wishlist에서 soft delete
        } catch (Exception e) {
            throw new BusinessExceptionHandler("위시리스트 삭제 과정에서 에러 : " + e.getMessage(), ErrorCode.BAD_REQUEST_ERROR);
        }
    }
}
