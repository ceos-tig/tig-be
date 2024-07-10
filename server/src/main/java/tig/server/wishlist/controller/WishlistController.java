package tig.server.wishlist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tig.server.annotation.LoginUser;
import tig.server.club.dto.ClubDTO;
import tig.server.error.ApiResponse;
import tig.server.member.domain.Member;
import tig.server.wishlist.dto.WishlistDTO;
import tig.server.wishlist.service.WishlistService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;

    /**
     * 현재 로그인한 사용자의 위시리스트 조회
     */
    @GetMapping("")
    public ResponseEntity<ApiResponse<List<ClubDTO.Response>>> getWishlist(@LoginUser Member member) {
        List<ClubDTO.Response> wishlist = wishlistService.getWishlistByUserId(member);
        ApiResponse<List<ClubDTO.Response>> response = ApiResponse.of(200, "successfully retrived wishlist", wishlist);
        return ResponseEntity.ok(response);
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<Void>> addWishlist(@RequestBody WishlistDTO.Request request) {
        wishlistService.addWishlist(request);
        ApiResponse<Void> response = ApiResponse.of(200, "successfully added to wishlist", null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("")
    public ResponseEntity<ApiResponse<Void>> removeWishlist(@RequestBody WishlistDTO.Request request) {
        wishlistService.removeWishlist(request);
        ApiResponse<Void> response = ApiResponse.of(200, "successfully deleted from wishlist", null);
        return ResponseEntity.ok(response);
    }
}
