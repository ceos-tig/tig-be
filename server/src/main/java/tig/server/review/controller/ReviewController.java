package tig.server.review.controller;

import com.google.protobuf.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tig.server.annotation.LoginUser;
import tig.server.error.ApiResponse;
import tig.server.member.domain.Member;
import tig.server.review.dto.ReviewDTO;
import tig.server.review.dto.ReviewWithReservationDTO;
import tig.server.review.service.ReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/{reservationId}")
    @Operation(summary = "리뷰 작성")
    public ResponseEntity<ApiResponse<Void>> createReview(@LoginUser Member member,
                                                          @PathVariable("reservationId") Long reservationId,
                                                          @RequestBody ReviewDTO.Request request) {
        reviewService.createReview(member.getId(), reservationId, request);
        ApiResponse<Void> response = ApiResponse.of(200, "successfully added review", null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{reviewId}")
    @Operation(summary = "특정 리뷰 조회")
    public ResponseEntity<ApiResponse<ReviewWithReservationDTO>> getReview(@LoginUser Member member,
                                                                           @PathVariable("reviewId") Long reviewId) {
        ReviewWithReservationDTO result = reviewService.getReviewById(reviewId);
        ApiResponse<ReviewWithReservationDTO> response = ApiResponse.of(200, "successfully retrieved reivew", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/club/{clubId}")
    @Operation(summary = "특정 업체의 모든 리뷰 조회")
    public ResponseEntity<ApiResponse<List<ReviewDTO.Response>>> getClubReviews(@PathVariable Long clubId) {
        List<ReviewDTO.Response> clubReviews = reviewService.getReviewsByClubId(clubId);
        ApiResponse<List<ReviewDTO.Response>> response = ApiResponse.of(200, "successfully get club's reviews", clubReviews);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{reviewId}")
    @Operation(summary = "특정 리뷰 수정")
    public ResponseEntity<ApiResponse<Void>> modifyReview(@PathVariable("reviewId") Long reviewId,
                                                          @RequestBody ReviewDTO.Request request) {
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
