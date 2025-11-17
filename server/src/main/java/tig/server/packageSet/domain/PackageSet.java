package tig.server.packageSet.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tig.server.enums.PackageCategory;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "package_set")
public class PackageSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private PackageCategory category;

    @Column(name = "rating_sum")
    private Integer ratingSum;

    @Column(name = "rating_count")
    private Integer ratingCount;

    @OneToMany(mappedBy = "packageSet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PackagePrice> packagePrices;

    public PackagePrice getDefaultPrice() {
        return packagePrices.stream()
                .filter(PackagePrice::getIsDefault)
                .findFirst()
                .orElse(packagePrices.isEmpty() ? null : packagePrices.get(0));
    }

    public String getDefaultPriceString() {
        PackagePrice defaultPrice = getDefaultPrice();
        return defaultPrice != null ? defaultPrice.getPrice().toString() : "0";
    }
}
