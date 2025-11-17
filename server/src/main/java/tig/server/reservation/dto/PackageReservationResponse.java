package tig.server.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tig.server.reservation.domain.PackageReservation;
import tig.server.packageSet.dto.PackagePriceDto;
import tig.server.enums.PackageCategory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageReservationResponse {
    // 공통 기본 정보
    private Long reservationId;
    private String memberName;
    private Long packageSetId;
    private String packageSetName;
    private String packageAddress;
    private String category;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 카테고리별 상세 정보 (JSON 그대로 전달)
    private Object reservationDetails;

    // 패키지 가격 정보
    private List<PackagePriceDto> availablePrices;
    private PackagePriceDto selectedPrice;
    private Integer totalPrice;

    // 예약 정보
    private String date;
    private String startTime;
    private String endTime;
    private Integer adultCount;
    private Integer teenagerCount;
    private Integer kidsCount;
    private String userName;
    private String phoneNumber;
    private String message;

    // 패키지별 선택 옵션
    private Map<String, String> packageOptions;

    public static PackageReservationResponse from(PackageReservation reservation) {
        return PackageReservationResponse.builder()
                .reservationId(reservation.getId())
                .memberName(reservation.getMember().getName())
                .packageSetId(reservation.getPackageSet().getId())
                .packageSetName(reservation.getPackageSet().getName())
                .packageAddress(reservation.getPackageSet().getAddress())
                .category(reservation.getCategory().name())
                .status(reservation.getStatus().name())
                .message(reservation.getMessage())
                .createdAt(reservation.getCreatedAt())
                .updatedAt(reservation.getUpdatedAt())
                .reservationDetails(reservation.getReservationDetails())
                .build();
    }

    // 전체 인원수 계산
    public Integer getTotalParticipants() {
        return (adultCount != null ? adultCount : 0) + 
               (teenagerCount != null ? teenagerCount : 0) + 
               (kidsCount != null ? kidsCount : 0);
    }

    // 패키지 카테고리별 표시명
    public String getPackageCategoryDisplayName() {
        if (category == null) return "미분류";
        
        try {
            PackageCategory packageCategory = PackageCategory.valueOf(category);
            switch (packageCategory) {
                case GOLF_COURSE: return "골프장";
                case PENSION: return "펜션";
                case BUS: return "단체버스";
                case CATERING: return "케이터링";
                case GROUP_UNIFORM: return "단체복";
                case LUNCH_BOX: return "도시락";
                default: return category;
            }
        } catch (IllegalArgumentException e) {
            return category;
        }
    }

    // 선택된 옵션 요약
    public String getSelectedOptionsText() {
        if (packageOptions == null || packageOptions.isEmpty()) {
            return "기본 옵션";
        }

        StringBuilder sb = new StringBuilder();
        packageOptions.forEach((key, value) -> {
            if (sb.length() > 0) sb.append(", ");
            
            switch (key) {
                case "HOLE_COUNT":
                    sb.append("홀수: ").append(value);
                    break;
                case "BUS_TYPE":
                    sb.append("버스: ").append(value);
                    break;
                case "CATERING_TYPE":
                    sb.append("케이터링: ").append(value);
                    break;
                case "SIZE":
                    sb.append("사이즈: ").append(value);
                    break;
                default:
                    sb.append(key).append(": ").append(value);
            }
        });
        
        return sb.toString();
    }
}
