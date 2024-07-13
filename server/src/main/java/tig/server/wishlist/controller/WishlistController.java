package tig.server.wishlist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tig.server.annotation.LoginUser;
import tig.server.club.dto.ClubResponse;
import tig.server.error.ApiResponse;
import tig.server.member.domain.Member;
import tig.server.wishlist.domain.Wishlist;
import tig.server.wishlist.dto.WishlistRequest;
import tig.server.wishlist.service.WishlistService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wishlist")
@Tag(name = "wishlist", description = "위시리스트 API")
public class WishlistController {
    private final WishlistService wishlistService;

    /**
     * 현재 로그인한 사용자의 위시리스트 조회
     */
    @GetMapping("")
    @Operation(summary = "위시리스트 목록 조회")
    public ResponseEntity<ApiResponse<List<ClubResponse>>> getWishlist(@LoginUser Member member) {
        List<ClubResponse> wishlists = wishlistService.getWishlistByUserId(member.getId());
        ApiResponse<List<ClubResponse>> response = ApiResponse.of(200, "successfully retrived wishlist", wishlists);

        return ResponseEntity.ok(response);
    }

    @PostMapping("{clubId}")
    @Operation(summary = "위시리스트 추가")
    public ResponseEntity<ApiResponse<Void>> addWishlist(@LoginUser Member member,
                                                         @PathVariable Long clubId) {
        wishlistService.addWishlist(clubId, member.getId());
        ApiResponse<Void> response = ApiResponse.of(200, "successfully added to wishlist", null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{clubId}")
    @Operation(summary = "위시리스트 삭제")
    public ResponseEntity<ApiResponse<Void>> removeWishlist(@LoginUser Member member,
                                                            @PathVariable Long clubId) {
        wishlistService.removeWishlist(clubId, member.getId());
        ApiResponse<Void> response = ApiResponse.of(200, "successfully deleted from wishlist", null);
        return ResponseEntity.ok(response);
    }
}
