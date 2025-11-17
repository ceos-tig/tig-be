package tig.server.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageSetReviewRequest {
    @Schema(type = "integer", example = "1")
    private Long packageReservationId;

    @Schema(type = "integer", example = "4")
    private Float rating;

    @Schema(type = "string", example = "패키지 서비스가 매우 만족스러웠습니다.")
    private String contents;
}