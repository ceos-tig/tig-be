package tig.server.club.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tig.server.enums.PackageCategory;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomePackageResponse {
    private List<PackageResponse> randomPackages;
    private List<PackageResponse> popularPackages;
    private List<PackageResponse> recommendedPackages;
    private Map<PackageCategory, List<CategoryPackageResponse>> randomPackagesByCategory;
}
