package tig.server.reservation.dto;

import lombok.*;
import tig.server.enums.PackageCategory;
import tig.server.packageSet.dto.PackagePriceDto;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationPackageSetResponse {
    private String packageSetName;
    private String address;
    private List<PackagePriceDto> prices;
    private PackageCategory category;
    private Double averageRating;
    private Long reviewCount;
}