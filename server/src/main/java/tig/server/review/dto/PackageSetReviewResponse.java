package tig.server.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tig.server.enums.PackageCategory;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageSetReviewResponse {
    @Schema(type = "integer", example = "1")
    private Long reviewId;

    @Schema(type = "integer", example = "1")
    private Long packageReservationId;

    @Schema(type = "integer", example = "1")
    private Long packageSetId;

    @Schema(type = "string", example = "골프 패키지")
    private String packageSetName;

    @Schema(type = "string", example = "GOLF")
    private PackageCategory category;

    @Schema(type = "integer", example = "4")
    private Float rating;

    @Schema(type = "string", example = "패키지 서비스가 매우 만족스러웠습니다.")
    private String contents;

    @Schema(type = "string", example = "김티그")
    private String userName;

    @Schema(type = "string", example = "2023-12-03T10:15:30")
    private LocalDateTime createdAt;
}