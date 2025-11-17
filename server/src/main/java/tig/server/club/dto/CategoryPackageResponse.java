package tig.server.club.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tig.server.enums.PackageCategory;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryPackageResponse {
    private Long id;
    private String name;
    private String address;
    private String price;
    private Integer ratingSum;
    private Integer ratingCount;
    private PackageCategory category;
    private List<String> imageUrls;
    private List<String> presignedImageUrls;

    // 평균 평점 계산 메서드
    public Double getAverageRating() {
        if (ratingCount == null || ratingCount == 0) {
            return 0.0;
        }
        return (double) ratingSum / ratingCount;
    }

    public void setPresignedImageUrls(List<String> imageUrls) {
        this.presignedImageUrls = imageUrls;
    }
}
