package tig.server.search.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tig.server.club.domain.Club;
import tig.server.club.mapper.ClubMapper;
import tig.server.club.repository.ClubRepository;
import tig.server.club.service.ClubService;
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

        if (clubList.isEmpty()) { // 검색 결과 없을떄
            isResult = false;
            List<Club> recommendedClubList = clubService.getRecommendedClubs(2).stream()
                    .map(clubMapper::responseToEntity)
                    .toList();
            avgPointDto = calculateMidPoint(clubList);
            for (Club recommendedClub : recommendedClubList) {
                Float distance = calculateDistance(avgPointDto, recommendedClub);
                boolean isHeart = wishlistRepository.existsByClubIdAndMemberId(recommendedClub.getId(), memberId);
                SearchResponseDto searchResponseDto = searchMapper.entityToResponse(recommendedClub);
                searchResponseDto.setIsHeart(isHeart);
                searchResponseDto.setDistance(distance);
                // set ratingSum and ratingCount 0 if null
                if (searchResponseDto.getRatingSum() == null) {
                    searchResponseDto.setRatingSum(0f);
                }
                if (searchResponseDto.getRatingCount() == null) {
                    searchResponseDto.setRatingCount(0);
                }
                searchResponseDtoList.add(searchResponseDto);
            }
        } else {
            avgPointDto = calculateMidPoint(clubList);
            for (Club club : clubList) {
                Float distance = calculateDistance(avgPointDto, club);
                boolean isHeart = wishlistRepository.existsByClubIdAndMemberId(club.getId(), memberId);
                SearchResponseDto searchResponseDto = searchMapper.entityToResponse(club);
                searchResponseDto.setIsHeart(isHeart);
                searchResponseDto.setDistance(distance);

                // set ratingSum and ratingCount 0 if null
                if (searchResponseDto.getRatingSum() == null) {
                    searchResponseDto.setRatingSum(0f);
                }
                if (searchResponseDto.getRatingCount() == null) {
                    searchResponseDto.setRatingCount(0);
                }

                searchResponseDtoList.add(searchResponseDto);
            }
        }
        if (isKeyword) { // 검색어를 입력 했다면
            String now = LocalDateTime.now().toString();
            SearchLogDto searchLogDto = new SearchLogDto(request,now);
            searchLogService.saveRecentSearchLog(memberId,searchLogDto);
        }

        return new SearchResultDto(searchResponseDtoList, avgPointDto.getAvgLatitude(), avgPointDto.getAvgLongitude(), isResult);
    }

    public SearchResultDto findClubByNameContainIfNoLogin(String request) {
        String keywordWithoutSpaces = request.replaceAll(" ", "");
        List<Club> clubList = clubRepository.searchByKeyword(keywordWithoutSpaces);

        Boolean isResult = true;
        List<SearchResponseDto> searchResponseDtoList = new ArrayList<>();
        AvgPointDto avgPointDto;

        if (clubList.isEmpty()) { // 검색 결과 없을때
            isResult = false;
            List<Club> recommendedClubList = clubService.getRecommendedClubs(2).stream()
                    .map(clubMapper::responseToEntity)
                    .toList();
            avgPointDto = calculateMidPoint(recommendedClubList);
            for (Club recommendedClub : recommendedClubList) {
                Float distance = calculateDistance(avgPointDto, recommendedClub);
                SearchResponseDto searchResponseDto = searchMapper.entityToResponse(recommendedClub);
                searchResponseDto.setDistance(distance);
                // set ratingSum and ratingCount 0 if null
                if (searchResponseDto.getRatingSum() == null) {
                    searchResponseDto.setRatingSum(0f);
                }
                if (searchResponseDto.getRatingCount() == null) {
                    searchResponseDto.setRatingCount(0);
                }
                searchResponseDtoList.add(searchResponseDto);
            }
        } else {
            avgPointDto = calculateMidPoint(clubList);
            for (Club club : clubList) {
                Float distance = calculateDistance(avgPointDto, club);
                SearchResponseDto searchResponseDto = searchMapper.entityToResponse(club);
                searchResponseDto.setDistance(distance);

                // set ratingSum and ratingCount 0 if null
                if (searchResponseDto.getRatingSum() == null) {
                    searchResponseDto.setRatingSum(0f);
                }
                if (searchResponseDto.getRatingCount() == null) {
                    searchResponseDto.setRatingCount(0);
                }

                searchResponseDtoList.add(searchResponseDto);
            }
        }


        return new SearchResultDto(searchResponseDtoList, avgPointDto.getAvgLatitude(), avgPointDto.getAvgLongitude(), isResult);
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
