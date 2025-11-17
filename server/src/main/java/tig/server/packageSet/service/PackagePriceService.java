package tig.server.packageSet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tig.server.global.exception.BusinessExceptionHandler;
import tig.server.global.code.ErrorCode;
import tig.server.packageSet.domain.PackagePrice;
import tig.server.packageSet.domain.PackageSet;
import tig.server.packageSet.dto.PackagePriceDto;
import tig.server.packageSet.repository.PackagePriceRepository;
import tig.server.packageSet.repository.PackageSetRepository;
import tig.server.packageSet.controller.PackagePriceController.PackagePriceCreateRequest;
import tig.server.packageSet.controller.PackagePriceController.PackagePriceUpdateRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PackagePriceService {

    private final PackagePriceRepository packagePriceRepository;
    private final PackageSetRepository packageSetRepository;

    public List<PackagePriceDto> getPackagePrices(Long packageSetId) {
        List<PackagePrice> prices = packagePriceRepository.findByPackageSetId(packageSetId);
        return prices.stream()
                .map(PackagePriceDto::from)
                .collect(Collectors.toList());
    }

    public PackagePriceDto getDefaultPrice(Long packageSetId) {
        PackagePrice defaultPrice = packagePriceRepository.findDefaultPriceByPackageSetId(packageSetId)
                .orElseThrow(() -> new BusinessExceptionHandler("Default price not found for package", ErrorCode.NOT_FOUND_ERROR));
        return PackagePriceDto.from(defaultPrice);
    }

    public PackagePriceDto getPriceByOption(Long packageSetId, String optionType, String optionValue) {
        PackagePrice price = packagePriceRepository.findByPackageSetIdAndOptionTypeAndOptionValue(packageSetId, optionType, optionValue)
                .orElseThrow(() -> new BusinessExceptionHandler("Price not found for the specified option", ErrorCode.NOT_FOUND_ERROR));
        return PackagePriceDto.from(price);
    }

    @Transactional
    public PackagePriceDto addPackagePrice(Long packageSetId, PackagePriceCreateRequest request) {
        PackageSet packageSet = packageSetRepository.findById(packageSetId)
                .orElseThrow(() -> new BusinessExceptionHandler("Package not found", ErrorCode.NOT_FOUND_ERROR));

        // 기본값이 true로 설정되는 경우, 기존의 기본값들을 false로 변경
        if (request.isDefault != null && request.isDefault) {
            resetDefaultPrices(packageSetId);
        }

        PackagePrice packagePrice = PackagePrice.builder()
                .packageSet(packageSet)
                .optionType(request.optionType)
                .optionValue(request.optionValue)
                .price(request.price)
                .description(request.description)
                .isDefault(request.isDefault != null ? request.isDefault : false)
                .build();

        PackagePrice savedPrice = packagePriceRepository.save(packagePrice);
        return PackagePriceDto.from(savedPrice);
    }

    @Transactional
    public PackagePriceDto updatePackagePrice(Long priceId, PackagePriceUpdateRequest request) {
        PackagePrice existingPrice = packagePriceRepository.findById(priceId)
                .orElseThrow(() -> new BusinessExceptionHandler("Package price not found", ErrorCode.NOT_FOUND_ERROR));

        // 기본값이 true로 변경되는 경우, 기존의 기본값들을 false로 변경
        if (request.isDefault != null && request.isDefault && !existingPrice.getIsDefault()) {
            resetDefaultPrices(existingPrice.getPackageSet().getId());
        }

        PackagePrice updatedPrice = PackagePrice.builder()
                .id(existingPrice.getId())
                .packageSet(existingPrice.getPackageSet())
                .optionType(request.optionType != null ? request.optionType : existingPrice.getOptionType())
                .optionValue(request.optionValue != null ? request.optionValue : existingPrice.getOptionValue())
                .price(request.price != null ? request.price : existingPrice.getPrice())
                .description(request.description != null ? request.description : existingPrice.getDescription())
                .isDefault(request.isDefault != null ? request.isDefault : existingPrice.getIsDefault())
                .build();

        PackagePrice savedPrice = packagePriceRepository.save(updatedPrice);
        return PackagePriceDto.from(savedPrice);
    }

    @Transactional
    public void deletePackagePrice(Long priceId) {
        PackagePrice price = packagePriceRepository.findById(priceId)
                .orElseThrow(() -> new BusinessExceptionHandler("Package price not found", ErrorCode.NOT_FOUND_ERROR));
        
        packagePriceRepository.deleteById(priceId);
        
        // 삭제된 가격이 기본값이었다면, 다른 가격 중 하나를 기본값으로 설정
        if (price.getIsDefault()) {
            List<PackagePrice> remainingPrices = packagePriceRepository.findByPackageSetId(price.getPackageSet().getId());
            if (!remainingPrices.isEmpty()) {
                PackagePrice firstPrice = remainingPrices.get(0);
                PackagePrice newDefaultPrice = PackagePrice.builder()
                        .id(firstPrice.getId())
                        .packageSet(firstPrice.getPackageSet())
                        .optionType(firstPrice.getOptionType())
                        .optionValue(firstPrice.getOptionValue())
                        .price(firstPrice.getPrice())
                        .description(firstPrice.getDescription())
                        .isDefault(true)
                        .build();
                packagePriceRepository.save(newDefaultPrice);
            }
        }
    }

    @Transactional
    public void initializePackagePrices(PackageSet packageSet) {
        switch (packageSet.getCategory()) {
            case GOLF_COURSE:
                packagePriceRepository.save(PackagePrice.createGolfPrice(packageSet, "9홀", 50000));
                packagePriceRepository.save(PackagePrice.createGolfPrice(packageSet, "18홀", 90000));
                break;
            case PENSION:
                packagePriceRepository.save(PackagePrice.createPensionPrice(packageSet, 80000));
                break;
            case BUS:
                packagePriceRepository.save(PackagePrice.createBusPrice(packageSet, "편도 24인승", 150000));
                packagePriceRepository.save(PackagePrice.createBusPrice(packageSet, "편도 45인승", 250000));
                packagePriceRepository.save(PackagePrice.createBusPrice(packageSet, "왕복 24인승", 280000));
                packagePriceRepository.save(PackagePrice.createBusPrice(packageSet, "왕복 45인승", 480000));
                break;
            case CATERING:
                packagePriceRepository.save(PackagePrice.createCateringPrice(packageSet, "일반", 15000));
                packagePriceRepository.save(PackagePrice.createCateringPrice(packageSet, "프리미엄", 25000));
                break;
            case GROUP_UNIFORM:
                packagePriceRepository.save(PackagePrice.createUniformPrice(packageSet, "S", 30000));
                packagePriceRepository.save(PackagePrice.createUniformPrice(packageSet, "M", 30000));
                packagePriceRepository.save(PackagePrice.createUniformPrice(packageSet, "L", 30000));
                packagePriceRepository.save(PackagePrice.createUniformPrice(packageSet, "XL", 32000));
                break;
            case LUNCH_BOX:
                packagePriceRepository.save(PackagePrice.createLunchBoxPrice(packageSet, 12000));
                break;
        }
    }

    private void resetDefaultPrices(Long packageSetId) {
        List<PackagePrice> existingPrices = packagePriceRepository.findByPackageSetId(packageSetId);
        for (PackagePrice price : existingPrices) {
            if (price.getIsDefault()) {
                PackagePrice updatedPrice = PackagePrice.builder()
                        .id(price.getId())
                        .packageSet(price.getPackageSet())
                        .optionType(price.getOptionType())
                        .optionValue(price.getOptionValue())
                        .price(price.getPrice())
                        .description(price.getDescription())
                        .isDefault(false)
                        .build();
                packagePriceRepository.save(updatedPrice);
            }
        }
    }
}