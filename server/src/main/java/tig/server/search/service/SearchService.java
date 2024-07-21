package tig.server.search.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tig.server.club.domain.Club;
import tig.server.club.repository.ClubRepository;
import tig.server.search.dto.AvgPointDto;
import tig.server.search.dto.SearchResponseDto;
import tig.server.search.dto.SearchResultDto;
import tig.server.search.mapper.SearchMapper;
import tig.server.wishlist.repository.WishlistRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {
    private final ClubRepository clubRepository;
    private final WishlistRepository wishlistRepository;

    private final SearchMapper searchMapper = SearchMapper.INSTANCE;

    public SearchResultDto findClubByNameContain(Long memberId, String request) {
        String keywordWithoutSpaces = request.replaceAll(" ", "");
        System.out.println("keywordWithoutSpaces = " + keywordWithoutSpaces);
        List<Club> clubList = clubRepository.searchByKeyword(keywordWithoutSpaces);

        AvgPointDto avgPointDto = calculateMidPoint(clubList);
        List<SearchResponseDto> searchResponseDtoList = new ArrayList<>();
        for (Club club : clubList) {
            Float distance = calculateDistance(avgPointDto, club);
            boolean isHeart = wishlistRepository.existsByClubIdAndMemberId(club.getId(), memberId);
            SearchResponseDto searchResponseDto = searchMapper.entityToResponse(club);
            searchResponseDto.setIsHeart(isHeart);
            searchResponseDto.setDistance(distance);
            searchResponseDtoList.add(searchResponseDto);
        }
        return new SearchResultDto(searchResponseDtoList, avgPointDto.getAvgLatitude(), avgPointDto.getAvgLongitude());
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
