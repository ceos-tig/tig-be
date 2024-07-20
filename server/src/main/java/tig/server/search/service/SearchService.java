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

        System.out.println("clubList.size() = " + clubList.size());

        AvgPointDto avgPointDto = calculateMidPoint(clubList);
        List<SearchResponseDto> searchResponseDtoList = new ArrayList<>();
        for (Club club : clubList) {
            boolean isHeart = wishlistRepository.existsByClubIdAndMemberId(club.getId(), memberId);
            SearchResponseDto searchResponseDto = searchMapper.entityToResponse(club);
            searchResponseDto.setIsHeart(isHeart);
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
}
