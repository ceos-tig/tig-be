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
import tig.server.review.dto.ReviewWithSummaryResponseDto;
import tig.server.review.dto.PackageSetReviewRequest;
import tig.server.review.dto.PackageSetReviewResponse;
import tig.server.review.dto.PackageSetReviewWithSummaryResponse;
import tig.server.review.service.ReviewService;
import tig.server.review.service.PackageSetReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
@Tag(name = "review", description = "리뷰 API")
public class ReviewController {
    private final ReviewService reviewService;
    private final PackageSetReviewService packageSetReviewService;

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
    public ResponseEntity<ApiResponse<ReviewWithSummaryResponseDto>> getClubReviews(@PathVariable Long clubId) {
        ReviewWithSummaryResponseDto reviewsByClubId = reviewService.getReviewsByClubId(clubId);
        ApiResponse<ReviewWithSummaryResponseDto> response = ApiResponse.of(200, "successfully get club's reviews", reviewsByClubId);
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

    // PackageSet Review APIs
    @PostMapping("/package-set")
    @Operation(summary = "패키지 세트 리뷰 작성")
    public ResponseEntity<ApiResponse<PackageSetReviewResponse>> createPackageSetReview(@LoginUser Member member,
                                                                                       @RequestBody PackageSetReviewRequest request) {
        PackageSetReviewResponse result = packageSetReviewService.createPackageSetReview(member.getId(), request);
        ApiResponse<PackageSetReviewResponse> response = ApiResponse.of(200, "successfully created package set review", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/package-set/{reviewId}")
    @Operation(summary = "특정 패키지 세트 리뷰 조회")
    public ResponseEntity<ApiResponse<PackageSetReviewResponse>> getPackageSetReview(@PathVariable("reviewId") Long reviewId) {
        PackageSetReviewResponse result = packageSetReviewService.getPackageSetReviewById(reviewId);
        ApiResponse<PackageSetReviewResponse> response = ApiResponse.of(200, "successfully retrieved package set review", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/package-set/package/{packageSetId}")
    @Operation(summary = "특정 패키지 세트의 모든 리뷰 조회")
    public ResponseEntity<ApiResponse<PackageSetReviewWithSummaryResponse>> getPackageSetReviews(@PathVariable("packageSetId") Long packageSetId) {
        PackageSetReviewWithSummaryResponse result = packageSetReviewService.getPackageSetReviewsByPackageSetId(packageSetId);
        ApiResponse<PackageSetReviewWithSummaryResponse> response = ApiResponse.of(200, "successfully retrieved package set reviews", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/package-set/member")
    @Operation(summary = "내가 작성한 패키지 세트 리뷰 조회")
    public ResponseEntity<ApiResponse<List<PackageSetReviewResponse>>> getMyPackageSetReviews(@LoginUser Member member) {
        List<PackageSetReviewResponse> result = packageSetReviewService.getPackageSetReviewsByMemberId(member.getId());
        ApiResponse<List<PackageSetReviewResponse>> response = ApiResponse.of(200, "successfully retrieved my package set reviews", result);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/package-set/{reviewId}")
    @Operation(summary = "패키지 세트 리뷰 수정")
    public ResponseEntity<ApiResponse<PackageSetReviewResponse>> modifyPackageSetReview(@PathVariable("reviewId") Long reviewId,
                                                                                       @RequestBody PackageSetReviewRequest request) {
        PackageSetReviewResponse result = packageSetReviewService.modifyPackageSetReview(reviewId, request);
        ApiResponse<PackageSetReviewResponse> response = ApiResponse.of(200, "successfully modified package set review", result);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/package-set/{reviewId}")
    @Operation(summary = "패키지 세트 리뷰 삭제")
    public ResponseEntity<ApiResponse<Void>> deletePackageSetReview(@PathVariable("reviewId") Long reviewId) {
        packageSetReviewService.deletePackageSetReview(reviewId);
        ApiResponse<Void> response = ApiResponse.of(200, "successfully deleted package set review", null);
        return ResponseEntity.ok(response);
    }
}
