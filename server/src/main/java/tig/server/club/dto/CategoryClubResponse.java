package tig.server.club.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryClubResponse {

    @Schema(type = "list", example = "https://tig-s3.s3.ap-northeast-2.amazonaws.com/1/1.jpg")
    private List<String> presignedImageUrls;

    @Schema(type = "list", example = "https://tig-s3.s3.ap-northeast-2.amazonaws.com/1/1.jpg")
    private List<String> imageUrls;

    @Schema(type = "string", example = "탁구")
    private String category;

    @Schema(type = "long", example = "1")
    private Long id;

    @Schema(type = "string", example = "티그 탁구장")
    private String clubName;

    @Schema(type = "string", example = "서울시 강남구 역삼동 123-45")
    private String address;

    @Schema(type = "boolean", example = "true")
    private Boolean isHeart;
}