package tig.server.packageSet.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tig.server.packageSet.domain.PackageSet;
import tig.server.packageSet.dto.PackageSetResultDto;
import tig.server.search.dto.PackageSetSearchResultDto;
import tig.server.packageSet.repository.PackageSetRepository;
import tig.server.search.dto.AvgPointDto;
import tig.server.search.dto.SearchLogDto;
import tig.server.search.service.SearchLogService;
import tig.server.wishlist.repository.WishlistRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PackageSetService {

    private final SearchLogService searchLogService;
    private final PackageSetRepository packageSetRepository;
    private final WishlistRepository wishlistRepository;

    public PackageSetSearchResultDto findPackageByNameContain(Long memberId, String request, boolean isKeyword) {
        String keywordWithoutSpaces = request.replaceAll(" ", "");
        List<PackageSet> packageSetList = packageSetRepository.findAll();

        Boolean isResult = true;
        List<PackageSetResultDto> searchResponseDtoList = new ArrayList<>();
        AvgPointDto avgPointDto;

        if (packageSetList.isEmpty()) { // 검색 결과 없을 때
            isResult = false;
            List<PackageSet> randomPackageSetList = packageSetRepository.findRandomPackages(2);
            for (PackageSet packageSet : randomPackageSetList) {
                PackageSetResultDto packageSetResultDto = buildSearchResponseDto(packageSet, 0L);
                searchResponseDtoList.add(packageSetResultDto);
            }
        } else {
            for (PackageSet packageSet : packageSetList) {
                PackageSetResultDto packageSetResultDto = buildSearchResponseDto(packageSet, memberId);
                searchResponseDtoList.add(packageSetResultDto);
            }
        }

        if (isKeyword) { // 검색어를 입력했다면
            String now = LocalDateTime.now().toString();
            SearchLogDto searchLogDto = new SearchLogDto(request, now);
            searchLogService.saveRecentSearchLog(memberId, searchLogDto);
        }

        return PackageSetSearchResultDto.of(searchResponseDtoList, isResult);
    }

    private PackageSetResultDto buildSearchResponseDto(PackageSet packageSet, Long memberId) {
        boolean isHeart = false;
        if(memberId != 0){
            isHeart = wishlistRepository.existsByClubIdAndMemberId(packageSet.getId(), memberId);
        }
        double rating = 0.0;
        if(packageSet.getRatingCount() != 0){
            rating = (double) packageSet.getRatingSum() / packageSet.getRatingCount();
        }
        String price = packageSet.getDefaultPriceString();
        return PackageSetResultDto.of(packageSet.getId(), packageSet.getName(), packageSet.getAddress(), rating, packageSet.getRatingCount(), price, isHeart, packageSet.getCategory());
    }

    public PackageSetSearchResultDto findClubByNameContainIfNoLogin(String request) {
        String keywordWithoutSpaces = request.replaceAll(" ", "");
        List<PackageSet> packageSetList = packageSetRepository.findAll();

        Boolean isResult = true;
        List<PackageSetResultDto> searchResponseDtoList = new ArrayList<>();

        if (packageSetList.isEmpty()) { // 검색 결과 없을 때
            isResult = false;
            List<PackageSet> randomPackageSetList = packageSetRepository.findRandomPackages(2);
            for (PackageSet packageSet : randomPackageSetList) {
                PackageSetResultDto packageSetResultDto = buildSearchResponseDto(packageSet, 0L);
                searchResponseDtoList.add(packageSetResultDto);
            }
        } else {
            for (PackageSet packageSet : packageSetList) {
                PackageSetResultDto packageSetResultDto = buildSearchResponseDto(packageSet, 0L);
                searchResponseDtoList.add(packageSetResultDto);
            }
        }
        return PackageSetSearchResultDto.of(searchResponseDtoList, isResult);
    }
}
