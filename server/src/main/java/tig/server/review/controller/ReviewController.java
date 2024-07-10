package tig.server.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tig.server.annotation.LoginUser;
import tig.server.error.ApiResponse;
import tig.server.member.domain.Member;
import tig.server.review.dto.ReviewDTO;
import tig.server.review.dto.ReviewWithReservationDTO;
import tig.server.review.service.ReviewService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/{reservationId}")
    public ResponseEntity<ApiResponse<Void>> createReview(@LoginUser Member member,
                                                          @PathVariable("reservationId") Long reservationId,
                                                          @RequestBody ReviewDTO.Request request) {
        reviewService.createReview(member.getId(), reservationId, request);
        ApiResponse<Void> response = ApiResponse.of(200, "successfully added review", null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewWithReservationDTO>> getReview(@LoginUser Member member,
                                                                           @PathVariable("reviewId") Long reviewId) {
        ReviewWithReservationDTO result = reviewService.getReviewById(reviewId);
        ApiResponse<ReviewWithReservationDTO> response = ApiResponse.of(200, "successfully retrieved reivew", result);
        return ResponseEntity.ok(response);
    }
}
