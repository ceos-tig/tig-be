package tig.server.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tig.server.annotation.LoginUser;
import tig.server.global.response.ApiResponse;
import tig.server.member.domain.Member;
import tig.server.review.dto.ReviewRequest;
import tig.server.review.dto.ReviewResponse;
import tig.server.review.dto.ReviewWithReservationDTO;
import tig.server.review.service.ReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
@Tag(name = "review", description = "리뷰 API")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/{reservationId}")
    @Operation(summary = "리뷰 작성")
    public ResponseEntity<ApiResponse<Void>> createReview(@LoginUser Member member,
                                                          @PathVariable("reservationId") Long reservationId,
                                                          @RequestBody ReviewRequest request) {
        reviewService.createReview(member.getId(), reservationId, request);
        ApiResponse<Void> response = ApiResponse.of(200, "successfully added review", null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{reviewId}")
    @Operation(summary = "특정 리뷰 조회")
    public ResponseEntity<ApiResponse<ReviewWithReservationDTO>> getReview(@PathVariable("reviewId") Long reviewId) {
        ReviewWithReservationDTO result = reviewService.getReviewById(reviewId);
        ApiResponse<ReviewWithReservationDTO> response = ApiResponse.of(200, "successfully retrieved reivew", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/club/{clubId}")
    @Operation(summary = "특정 업체의 모든 리뷰 조회")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getClubReviews(@PathVariable Long clubId) {
        List<ReviewResponse> clubReviews = reviewService.getReviewsByClubId(clubId);
        ApiResponse<List<ReviewResponse>> response = ApiResponse.of(200, "successfully get club's reviews", clubReviews);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{reviewId}")
    @Operation(summary = "특정 리뷰 수정")
    public ResponseEntity<ApiResponse<Void>> modifyReview(@PathVariable("reviewId") Long reviewId,
                                                          @RequestBody ReviewRequest request) {
        reviewService.modifyReview(reviewId, request);
        ApiResponse<Void> response = ApiResponse.of(200, "successfully modified review", null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "특정 리뷰 삭제")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable("reviewId") Long reviewId) {
        reviewService.deleteReview(reviewId);
        ApiResponse<Void> response = ApiResponse.of(200, "successfully deleted review", null);
        return ResponseEntity.ok(response);
    }
}
