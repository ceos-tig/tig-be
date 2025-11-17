package tig.server.packageSet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tig.server.enums.PackageCategory;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageSetResultDto {
    private Long id;
    private String name;
    private String address;
    private Double rating;
    private Integer ratingCount;
    private String price;
    private Boolean isHeart;
    private PackageCategory category;

    public static PackageSetResultDto of(Long id, String name, String address, Double rating, Integer ratingCount, String price, Boolean isHeart, PackageCategory category) {
        return PackageSetResultDto.builder()
                .id(id)
                .name(name)
                .address(address)
                .rating(rating)
                .ratingCount(ratingCount)
                .price(price)
                .isHeart(isHeart)
                .category(category)
                .build();
    }
}
