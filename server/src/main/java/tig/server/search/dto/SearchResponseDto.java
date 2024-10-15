package tig.server.search.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import tig.server.enums.Category;
import tig.server.enums.Type;
import tig.server.operatinghours.dto.OperatingHoursResponse;
import tig.server.wishlist.domain.Wishlist;

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

    // 가격과 운영 시간은 별도 리스트로 처리
    @Schema(type = "array", description = "클럽의 가격 정보 리스트")
    private List<?> prices;  // PriceResponse 리스트 추가

    @Schema(type = "array", description = "클럽의 운영 시간 정보 리스트")
    private List<OperatingHoursResponse> operatingHours;  // OperatingHoursResponse 리스트 추가

    @Schema(type = "string", example = "010-1234-5678")
    private String phoneNumber;

    @Schema(type = "string", example = "https://instagram.com/tigballing")
    private String snsLink;

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

    @Schema(type = "float", example = "17.28")
    private Float distance;

    public Float getAvgRating() {
        if (ratingCount == 0) {
            return 0.0f;
        }
        if (ratingSum == 0) {
            return 0.0f;
        }
        // round at 1 decimal place
        return Math.round(ratingSum / ratingCount * 10) / 10.0f;
    }
}