package tig.server.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageSetReviewWithSummaryResponse {
    @Schema(type = "integer", example = "1")
    private Long packageSetId;

    @Schema(type = "string", example = "골프 패키지")
    private String packageSetName;

    @Schema(type = "number", example = "4.2")
    private Double averageRating;

    @Schema(type = "integer", example = "15")
    private Long totalReviewCount;

    @Schema(description = "패키지 리뷰 목록")
    private List<PackageSetReviewResponse> reviews;

    @Schema(type = "string", example = "AI가 요약한 패키지 리뷰 내용")
    private String reviewSummary;
}