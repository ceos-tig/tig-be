package tig.server.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {
    @Schema(type = "integer", example = "1")
    private Long reservationId;

    @Schema(type = "integer", example = "4")
    private Float rating;

    @Schema(type = "string", example = "재미있어요. 티그볼링장 추천합니다.")
    private String contents;

    @Schema(type = "string", example = "김티그")
    private String userName;

    @Schema(type = "integer", example = "2")
    private Integer adultCount;

    @Schema(type = "integer", example = "0")
    private Integer teenagerCount;

    @Schema(type = "integer", example = "0")
    private Integer kidsCount;

    @Schema(type = "string", example = "2007-12-03T10:15:30")
    private String startTime;

    @Schema(type = "string", example = "AI 가 요약한 리뷰 내용")
    private String reviewSummary;
}
