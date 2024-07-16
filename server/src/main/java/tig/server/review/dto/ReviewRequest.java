package tig.server.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequest {
    @Schema(type = "integer", example = "1")
    private Long reservationId;

    @Schema(type = "integer", example = "4")
    private Float rating;

    @Schema(type = "string", example = "재미있어요. 티그볼링장 추천합니다.")
    private String contents;

}
