package tig.server.packageSet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackagePriceDto {
    private Long id;
    private String optionType;
    private String optionValue;
    private Integer price;
    private String description;
    private Boolean isDefault;

    public static PackagePriceDto from(tig.server.packageSet.domain.PackagePrice packagePrice) {
        return PackagePriceDto.builder()
                .id(packagePrice.getId())
                .optionType(packagePrice.getOptionType())
                .optionValue(packagePrice.getOptionValue())
                .price(packagePrice.getPrice())
                .description(packagePrice.getDescription())
                .isDefault(packagePrice.getIsDefault())
                .build();
    }
}