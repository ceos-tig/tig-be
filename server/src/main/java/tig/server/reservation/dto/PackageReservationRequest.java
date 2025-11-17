package tig.server.reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tig.server.enums.PackageCategory;

import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageReservationRequest {
    
    // 패키지 정보 (필수)
    @Schema(type = "integer", example = "1", description = "패키지 ID", required = true)
    private Long packageSetId;

    @Schema(type = "string", allowableValues = {"GOLF_COURSE", "PENSION", "BUS", "CATERING", "GROUP_UNIFORM", "LUNCH_BOX"}, 
            example = "GOLF_COURSE", description = "패키지 카테고리", required = true)
    private PackageCategory packageCategory;

    @Schema(type = "object", example = "{\"HOLE_COUNT\": \"18홀\"}", description = "패키지별 선택 옵션")
    private Map<String, String> packageOptions;

    // 예약자 정보
    @Schema(type = "string", example = "홍길동", description = "예약자 이름", required = true)
    private String userName;

    @Schema(type = "string", example = "010-1234-5678", description = "예약자 전화번호", required = true)
    private String phoneNumber;

    // 인원 정보
    @Schema(type = "integer", example = "2", description = "성인 인원수")
    private Integer adultCount;

    @Schema(type = "integer", example = "0", description = "청소년 인원수")
    private Integer teenagerCount;

    @Schema(type = "integer", example = "0", description = "어린이 인원수")
    private Integer kidsCount;

    // 예약 일시 정보
    @Schema(type = "string", example = "2024-12-25", description = "예약 날짜")
    private String date;

    @Schema(type = "string", example = "2024-12-25T09:00:00", description = "시작 시간")
    private String startTime;

    @Schema(type = "string", example = "2024-12-25T13:00:00", description = "종료 시간")
    private String endTime;

    // 가격 정보
    @Schema(type = "integer", example = "180000", description = "총 예약 금액")
    private Integer totalPrice;

    // 메시지
    @Schema(type = "string", example = "특별한 요청사항이 있습니다.", description = "예약 메시지")
    private String message;

    // 패키지별 옵션 접근 메서드들
    public String getGolfHoleCount() {
        return packageOptions != null ? packageOptions.get("HOLE_COUNT") : null;
    }

    public String getBusType() {
        return packageOptions != null ? packageOptions.get("BUS_TYPE") : null;
    }

    public String getCateringType() {
        return packageOptions != null ? packageOptions.get("CATERING_TYPE") : null;
    }

    public String getUniformSize() {
        return packageOptions != null ? packageOptions.get("SIZE") : null;
    }

    // 전체 인원수 계산
    public Integer getTotalParticipants() {
        return (adultCount != null ? adultCount : 0) + 
               (teenagerCount != null ? teenagerCount : 0) + 
               (kidsCount != null ? kidsCount : 0);
    }

    // 패키지 카테고리별 유효성 검증
    public boolean isValidForCategory() {
        if (packageCategory == null) return false;
        
        switch (packageCategory) {
            case GOLF_COURSE:
                return packageOptions != null && packageOptions.containsKey("HOLE_COUNT");
            case BUS:
                return packageOptions != null && packageOptions.containsKey("BUS_TYPE");
            case CATERING:
                return packageOptions != null && packageOptions.containsKey("CATERING_TYPE");
            case GROUP_UNIFORM:
                return packageOptions != null && packageOptions.containsKey("SIZE");
            case PENSION:
            case LUNCH_BOX:
                return true; // 기본 패키지는 추가 옵션 불필요
            default:
                return false;
        }
    }
}