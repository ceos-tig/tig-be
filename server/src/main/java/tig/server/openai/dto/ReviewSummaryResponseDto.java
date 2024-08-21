package tig.server.openai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewSummaryResponseDto {
    private String reviewSummary;

    // Static factory method
    public static ReviewSummaryResponseDto fromReviewSummary(String reviewSummary) {
        return new ReviewSummaryResponseDto(reviewSummary);
    }
}
