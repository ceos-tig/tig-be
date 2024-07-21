package tig.server.search.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import tig.server.enums.Category;
import tig.server.enums.Type;

import java.util.List;

@Getter
@Setter
public class SearchResponseDto {
    @Schema(type = "integer", example = "1")
    private Long clubId;

    @Schema(type = "string", example = "티그볼링장")
    private String clubName;

    @Schema(type = "string", example = "서울시 서대문구 52-15")
    private String address;

    @Schema(type = "float", example = "13123.23")
    private Float ratingSum;

    @Schema(type = "integer", example = "320")
    private Integer ratingCount;

    @Schema(type = "integer", example = "30000")
    private Integer price;

    @Schema(type = "string", example = "010-1234-5678")
    private String phoneNumber;

    @Schema(type = "string", example = "https://instagram.com/tigballing")
    private String snsLink;

    @Schema(type = "string", example = "10:00 - 3:00")
    private String businessHours;

    @Schema(type = "float", example = "129.0921")
    private Float latitude;

    @Schema(type = "float", example = "52.0123")
    private Float longitude;

    @Schema(type = "string", example = "BALLING")
    private Category category;

    @Schema(type = "string", example = "GAME")
    private Type type;

    @Schema(type = "array", example = "[\"src/club/img1.jpg\", \"src/club/img2.jpg\"]")
    private List<String> imageUrls;

    @Schema(type = "array", example = "[\"https://s3.amazonaws.com/bucket/club1.jpg\", \"https://s3.amazonaws.com/bucket/club2.jpg\"]", description = "각각 10분동안 유효하며, 보낸 이미지 순서대로 presigned URL이 반환됩니다.")
    private List<String> presignedImageUrls;

    @Schema(type = "boolean", example = "true")
    private Boolean isHeart;

    private Float avgLongitude;

    private Float avgLatitude;

    @Schema(type = "float", example = "17.28")
    private Float distance;

    public Float getAvgRating() {
        return (ratingCount != null && ratingCount != 0) ? ratingSum / ratingCount : 0.0f;
    }
}