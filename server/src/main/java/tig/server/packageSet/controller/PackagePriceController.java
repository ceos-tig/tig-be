package tig.server.packageSet.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tig.server.packageSet.dto.PackagePriceDto;
import tig.server.packageSet.service.PackagePriceService;

import java.util.List;

@RestController
@RequestMapping("/api/package-prices")
@RequiredArgsConstructor
@Tag(name = "PackagePrice", description = "패키지 가격 관리 API")
public class PackagePriceController {

    private final PackagePriceService packagePriceService;

    @GetMapping("/package/{packageSetId}")
    @Operation(summary = "패키지별 가격 조회", description = "특정 패키지의 모든 가격 옵션을 조회합니다.")
    public ResponseEntity<List<PackagePriceDto>> getPackagePrices(@PathVariable Long packageSetId) {
        List<PackagePriceDto> prices = packagePriceService.getPackagePrices(packageSetId);
        return ResponseEntity.ok(prices);
    }

    @GetMapping("/package/{packageSetId}/default")
    @Operation(summary = "패키지 기본 가격 조회", description = "특정 패키지의 기본 가격을 조회합니다.")
    public ResponseEntity<PackagePriceDto> getDefaultPrice(@PathVariable Long packageSetId) {
        PackagePriceDto defaultPrice = packagePriceService.getDefaultPrice(packageSetId);
        return ResponseEntity.ok(defaultPrice);
    }

    @GetMapping("/package/{packageSetId}/options")
    @Operation(summary = "패키지 가격 옵션 조회", description = "특정 패키지의 가격 옵션을 타입과 값으로 조회합니다.")
    public ResponseEntity<PackagePriceDto> getPriceByOption(
            @PathVariable Long packageSetId,
            @RequestParam String optionType,
            @RequestParam String optionValue) {
        PackagePriceDto price = packagePriceService.getPriceByOption(packageSetId, optionType, optionValue);
        return ResponseEntity.ok(price);
    }

    @PostMapping("/package/{packageSetId}")
    @Operation(summary = "패키지 가격 추가", description = "특정 패키지에 새로운 가격 옵션을 추가합니다.")
    public ResponseEntity<PackagePriceDto> addPackagePrice(
            @PathVariable Long packageSetId,
            @RequestBody PackagePriceCreateRequest request) {
        PackagePriceDto createdPrice = packagePriceService.addPackagePrice(packageSetId, request);
        return ResponseEntity.ok(createdPrice);
    }

    @PutMapping("/{priceId}")
    @Operation(summary = "패키지 가격 수정", description = "기존 패키지 가격을 수정합니다.")
    public ResponseEntity<PackagePriceDto> updatePackagePrice(
            @PathVariable Long priceId,
            @RequestBody PackagePriceUpdateRequest request) {
        PackagePriceDto updatedPrice = packagePriceService.updatePackagePrice(priceId, request);
        return ResponseEntity.ok(updatedPrice);
    }

    @DeleteMapping("/{priceId}")
    @Operation(summary = "패키지 가격 삭제", description = "기존 패키지 가격을 삭제합니다.")
    public ResponseEntity<Void> deletePackagePrice(@PathVariable Long priceId) {
        packagePriceService.deletePackagePrice(priceId);
        return ResponseEntity.noContent().build();
    }

    public static class PackagePriceCreateRequest {
        public String optionType;
        public String optionValue;
        public Integer price;
        public String description;
        public Boolean isDefault;
    }

    public static class PackagePriceUpdateRequest {
        public String optionType;
        public String optionValue;
        public Integer price;
        public String description;
        public Boolean isDefault;
    }
}