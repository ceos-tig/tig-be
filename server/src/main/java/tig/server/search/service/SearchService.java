package tig.server.search.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tig.server.club.domain.Club;
import tig.server.club.mapper.ClubMapper;
import tig.server.club.repository.ClubRepository;
import tig.server.club.service.ClubService;
import tig.server.global.code.ErrorCode;
import tig.server.global.exception.BusinessExceptionHandler;
import tig.server.operatinghours.dto.OperatingHoursResponse;
import tig.server.operatinghours.repository.OperatingHoursRepository;
import tig.server.price.dto.*;
import tig.server.price.repository.*;
import tig.server.search.dto.AvgPointDto;
import tig.server.search.dto.SearchLogDto;
import tig.server.search.dto.SearchResponseDto;
import tig.server.search.dto.SearchResultDto;
import tig.server.search.mapper.SearchMapper;
import tig.server.wishlist.repository.WishlistRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {
    private final ClubRepository clubRepository;
    private final WishlistRepository wishlistRepository;
    private final TableTennisPriceRepository tableTennisPriceRepository;
    private final BallingPriceRepository ballingPriceRepository;
    private final BaseballPriceRepository baseballPriceRepository;
    private final BilliardsPriceRepository billiardsPriceRepository;
    private final FootballPriceRepository footballPriceRepository;
    private final GolfPriceRepository golfPriceRepository;
    private final SquashPriceRepository squashPriceRepository;
    private final TennisPriceRepository tennisPriceRepository;
    private final OperatingHoursRepository operatingHoursRepository;

    private final SearchLogService searchLogService;
    private final ClubService clubService;

    private final SearchMapper searchMapper = SearchMapper.INSTANCE;
    private final ClubMapper clubMapper = ClubMapper.INSTANCE;

    public SearchResultDto findClubByNameContain(Long memberId, String request, boolean isKeyword) {
        String keywordWithoutSpaces = request.replaceAll(" ", "");
        List<Club> clubList = clubRepository.searchByKeyword(keywordWithoutSpaces);

        Boolean isResult = true;
        List<SearchResponseDto> searchResponseDtoList = new ArrayList<>();
        AvgPointDto avgPointDto;

        if (clubList.isEmpty()) { // 검색 결과 없을 때
            isResult = false;
            List<Club> recommendedClubList = clubService.getRecommendedClubs(2).stream()
                    .map(clubMapper::responseToEntity)
                    .toList();
            avgPointDto = calculateMidPoint(clubList);

            for (Club recommendedClub : recommendedClubList) {
                SearchResponseDto searchResponseDto = buildSearchResponseDto(recommendedClub, memberId, avgPointDto);
                searchResponseDtoList.add(searchResponseDto);
            }
        } else {
            avgPointDto = calculateMidPoint(clubList);

            for (Club club : clubList) {
                SearchResponseDto searchResponseDto = buildSearchResponseDto(club, memberId, avgPointDto);
                searchResponseDtoList.add(searchResponseDto);
            }
        }

        if (isKeyword) { // 검색어를 입력했다면
            String now = LocalDateTime.now().toString();
            SearchLogDto searchLogDto = new SearchLogDto(request, now);
            searchLogService.saveRecentSearchLog(memberId, searchLogDto);
        }

        return new SearchResultDto(
                searchResponseDtoList,
                avgPointDto.getAvgLatitude(),
                avgPointDto.getAvgLongitude(),
                isResult
        );
    }

    private SearchResponseDto buildSearchResponseDto(Club club, Long memberId, AvgPointDto avgPointDto) {
        Float distance = calculateDistance(avgPointDto, club);
        boolean isHeart = wishlistRepository.existsByClubIdAndMemberId(club.getId(), memberId);

        SearchResponseDto searchResponseDto = searchMapper.entityToResponse(club);
        searchResponseDto.setIsHeart(isHeart);
        searchResponseDto.setDistance(distance);

        // 가격 정보 분기 처리
        List<?> priceResponses = getPriceResponsesByCategory(club);
        searchResponseDto.setPrices(priceResponses);

        // 운영 시간 정보 설정
        List<OperatingHoursResponse> operatingHoursResponses = operatingHoursRepository.findByClub_Id(club.getId()).stream()
                .map(hours -> new OperatingHoursResponse(
                        hours.getDayOfWeek(),
                        hours.getStartTime(),
                        hours.getEndTime()))
                .toList();
        searchResponseDto.setOperatingHours(operatingHoursResponses);

        // Rating 초기화 (null일 경우)
        if (searchResponseDto.getRatingSum() == null) {
            searchResponseDto.setRatingSum(0f);
        }
        if (searchResponseDto.getRatingCount() == null) {
            searchResponseDto.setRatingCount(0);
        }


        return searchResponseDto;
    }

    private List<?> getPriceResponsesByCategory(Club club) {
        switch (club.getCategory()) {
            case TABLE_TENNIS:
                return tableTennisPriceRepository.findByClub(club)
                        .stream()
                        .map(price -> new TableTennisPriceResponse(
                                price.getProgramName(), price.getPrice(), price.getDuration()))
                        .collect(Collectors.toList());
            case BALLING:
                return ballingPriceRepository.findByClub(club)
                        .stream()
                        .map(price -> new BallingPriceResponse(
                                price.getProgramName(), price.getDayOfWeek(),
                                price.getStartTime(), price.getEndTime(),
                                price.getGameCount(), price.getPrice()))
                        .collect(Collectors.toList());
            case GOLF:
                return golfPriceRepository.findByClub(club)
                        .stream()
                        .map(price -> new GolfPriceResponse(
                                price.getProgramName(), price.getDayOfWeek(),
                                price.getStartTime(), price.getEndTime(), price.getPrice(),
                                price.getHoles(), price.getDuration()))
                        .collect(Collectors.toList());
            case BILLIARDS:
                return billiardsPriceRepository.findByClub(club)
                        .stream()
                        .map(price -> new BilliardsPriceResponse(
                                price.getProgramName(), price.getDuration(), price.getPrice()))
                        .collect(Collectors.toList());
            case FOOTBALL:
                return footballPriceRepository.findByClub(club)
                        .stream()
                        .map(price -> new FootballPriceResponse(
                                price.getProgramName(), price.getDuration(), price.getPrice()))
                        .collect(Collectors.toList());
            case BASEBALL:
                return baseballPriceRepository.findByClub(club)
                        .stream()
                        .map(price -> new BaseballPriceResponse(
                                price.getProgramName(),
                                price.getInning(),
                                price.getDuration(),
                                price.getStartTime(),
                                price.getEndTime(),
                                price.getPrice()))
                        .collect(Collectors.toList());
            case TENNIS:
                return tennisPriceRepository.findByClub(club)
                        .stream()
                        .map(price -> new TennisPriceResponse(
                                price.getProgramName(), price.getDayOfWeek(),
                                price.getDuration(), price.getPrice(),
                                price.getCountPerWeek()))
                        .collect(Collectors.toList());
            case SQUASH:
                return squashPriceRepository.findByClub(club)
                        .stream()
                        .map(price -> new SquashPriceResponse(
                                price.getProgramName(), price.getDurationInMonths(),
                                price.getLessonCount(), price.getDuration(),
                                price.getPrice()))
                        .collect(Collectors.toList());
            default:
                throw new BusinessExceptionHandler("Invalid program type", ErrorCode.NOT_VALID_ERROR);
        }
    }

    public SearchResultDto findClubByNameContainIfNoLogin(String request) {
        String keywordWithoutSpaces = request.replaceAll(" ", "");
        List<Club> clubList = clubRepository.searchByKeyword(keywordWithoutSpaces);

        Boolean isResult = true;
        List<SearchResponseDto> searchResponseDtoList = new ArrayList<>();
        AvgPointDto avgPointDto;

        if (clubList.isEmpty()) { // 검색 결과가 없을 때
            isResult = false;
            List<Club> recommendedClubList = clubService.getRecommendedClubs(2).stream()
                    .map(clubMapper::responseToEntity)
                    .toList();
            avgPointDto = calculateMidPoint(recommendedClubList);
            for (Club recommendedClub : recommendedClubList) {
                SearchResponseDto searchResponseDto = buildSearchResponseIfNoLogin(recommendedClub, avgPointDto);
                searchResponseDtoList.add(searchResponseDto);
            }
        } else { // 검색 결과가 있을 때
            avgPointDto = calculateMidPoint(clubList);
            for (Club club : clubList) {
                SearchResponseDto searchResponseDto = buildSearchResponseIfNoLogin(club, avgPointDto);
                searchResponseDtoList.add(searchResponseDto);
            }
        }

        return new SearchResultDto(
                searchResponseDtoList,
                avgPointDto.getAvgLatitude(),
                avgPointDto.getAvgLongitude(),
                isResult
        );
    }

    private SearchResponseDto buildSearchResponseIfNoLogin(Club club, AvgPointDto avgPointDto) {
        Float distance = calculateDistance(avgPointDto, club);
        SearchResponseDto searchResponseDto = searchMapper.entityToResponse(club);
        searchResponseDto.setDistance(distance);

        // 가격 정보 설정
        List<?> priceResponses = getPriceResponsesByCategory(club);
        searchResponseDto.setPrices(priceResponses);

        // 운영 시간 정보 설정
        List<OperatingHoursResponse> operatingHoursResponses = operatingHoursRepository.findByClub_Id(club.getId()).stream()
                .map(hours -> new OperatingHoursResponse(
                        hours.getDayOfWeek(),
                        hours.getStartTime(),
                        hours.getEndTime()))
                .toList();
        searchResponseDto.setOperatingHours(operatingHoursResponses);

        // 평점 초기화 (null일 경우)
        if (searchResponseDto.getRatingSum() == null) {
            searchResponseDto.setRatingSum(0f);
        }
        if (searchResponseDto.getRatingCount() == null) {
            searchResponseDto.setRatingCount(0);
        }

        return searchResponseDto;
    }

    public static AvgPointDto calculateMidPoint(List<Club> clubList) {
        Float sumOfLatitude = 0.0f;
        Float sumOfLongitude = 0.0f;
        int totalSize = clubList.size();

        for (Club club : clubList) {
            sumOfLongitude += club.getLongitude();
            sumOfLatitude += club.getLatitude();
        }
        return AvgPointDto.fromPoint(sumOfLongitude / totalSize, sumOfLatitude / totalSize);
    }

    public static Float calculateDistance(AvgPointDto avgPointDto, Club club) {
        final float R = 6371.0f;

        // AvgPointDto의 위도와 경도
        Float avgPointDtoLatitude = avgPointDto.getAvgLatitude();
        Float avgPointDtoLongitude = avgPointDto.getAvgLongitude();

        // Club의 위도와 경도
        Float clubLatitude = club.getLatitude();
        Float clubLongitude = club.getLongitude();

        // 위도와 경도를 라디안으로 변환
        float latDistance = (float) Math.toRadians(clubLatitude - avgPointDtoLatitude);
        float lonDistance = (float) Math.toRadians(clubLongitude - avgPointDtoLongitude);

        float a = (float) (Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(avgPointDtoLatitude)) * Math.cos(Math.toRadians(clubLatitude))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2));
        float c = (float) (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));

        // 거리 계산
        float distance = R * c;

        // km를 Float로 변환하여 반환
        return distance;
    }
}
