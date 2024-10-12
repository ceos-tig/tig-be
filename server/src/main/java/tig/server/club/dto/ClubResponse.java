package tig.server.club.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tig.server.enums.Category;
import tig.server.enums.Facility;
import tig.server.enums.Type;
import tig.server.operatinghours.dto.OperatingHoursResponse;
import tig.server.price.dto.PriceResponse;
import tig.server.program.dto.ProgramResponse;

import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubResponse {

    @Schema(type = "integer", example = "1")
    private Long id;

    @Schema(type = "string", example = "티그볼링장")
    private String clubName;

    @Schema(type = "string", example = "서울시 서대문구 52-15")
    private String address;

    @Schema(type = "float", example = "13123.23")
    private Float ratingSum;

    @Schema(type = "integer", example = "320")
    private Integer ratingCount;

    @Schema(type = "integer", example = "4.2")
    private Float avgRating;

//    @Schema(type = "integer", example = "30000")
//    private Integer price;

    // 가격과 운영 시간은 별도 리스트로 처리
    @Schema(type = "array", description = "클럽의 가격 정보 리스트")
    private List<?> prices;  // PriceResponse 리스트 추가

    @Schema(type = "array", description = "클럽의 운영 시간 정보 리스트")
    private List<OperatingHoursResponse> operatingHours;  // OperatingHoursResponse 리스트 추가

    @Schema(type = "array", description = "클럽의 프로그램 정보 리스트")
    private List<ProgramResponse> programs;  // 프로그램 정보 리스트

    @Schema(type = "string", example = "010-1234-5678")
    private String phoneNumber;

    @Schema(type = "string", example = "https://instagram.com/tigballing")
    private String snsLink;

//    @Schema(type = "string", example = "10:00 - 3:00")
//    private String businessHours;

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

    @Schema(type = "array", example = "[\"무선 인터넷\", \"간편 결제\"]")
    private List<Facility> amenities;
}
