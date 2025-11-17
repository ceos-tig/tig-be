package tig.server.packageSet.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tig.server.enums.PackageCategory;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "package_price")
public class PackagePrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_set_id")
    private PackageSet packageSet;

    @Column(name = "option_type")
    private String optionType;

    @Column(name = "option_value")
    private String optionValue;

    @Column(name = "price")
    private Integer price;

    @Column(name = "description")
    private String description;

    @Column(name = "is_default")
    private Boolean isDefault;

    public static PackagePrice createGolfPrice(PackageSet packageSet, String holeType, Integer price) {
        return PackagePrice.builder()
                .packageSet(packageSet)
                .optionType("HOLE_COUNT")
                .optionValue(holeType)
                .price(price)
                .description(holeType + " 골프")
                .isDefault("18홀".equals(holeType))
                .build();
    }

    public static PackagePrice createBusPrice(PackageSet packageSet, String busType, Integer price) {
        return PackagePrice.builder()
                .packageSet(packageSet)
                .optionType("BUS_TYPE")
                .optionValue(busType)
                .price(price)
                .description(busType + " 버스")
                .isDefault("왕복 45인승".equals(busType))
                .build();
    }

    public static PackagePrice createCateringPrice(PackageSet packageSet, String cateringType, Integer price) {
        return PackagePrice.builder()
                .packageSet(packageSet)
                .optionType("CATERING_TYPE")
                .optionValue(cateringType)
                .price(price)
                .description(cateringType + " 케이터링")
                .isDefault("일반".equals(cateringType))
                .build();
    }

    public static PackagePrice createUniformPrice(PackageSet packageSet, String size, Integer price) {
        return PackagePrice.builder()
                .packageSet(packageSet)
                .optionType("SIZE")
                .optionValue(size)
                .price(price)
                .description("사이즈 " + size)
                .isDefault("L".equals(size))
                .build();
    }

    public static PackagePrice createPensionPrice(PackageSet packageSet, Integer price) {
        return PackagePrice.builder()
                .packageSet(packageSet)
                .optionType("DEFAULT")
                .optionValue("STANDARD")
                .price(price)
                .description("펜션 이용료")
                .isDefault(true)
                .build();
    }

    public static PackagePrice createLunchBoxPrice(PackageSet packageSet, Integer price) {
        return PackagePrice.builder()
                .packageSet(packageSet)
                .optionType("DEFAULT")
                .optionValue("STANDARD")
                .price(price)
                .description("도시락")
                .isDefault(true)
                .build();
    }
}