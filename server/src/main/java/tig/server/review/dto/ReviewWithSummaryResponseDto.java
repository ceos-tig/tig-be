package tig.server.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewWithSummaryResponseDto {
    private List<ReviewResponse> reviews;
    private String reviewSummary;

    // static factory method
    public static ReviewWithSummaryResponseDto from(List<ReviewResponse> reviews, String reviewSummary) {
        return new ReviewWithSummaryResponseDto(reviews,reviewSummary);
    }
}
