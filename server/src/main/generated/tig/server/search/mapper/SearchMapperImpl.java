package tig.server.search.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tig.server.club.domain.Club;
import tig.server.club.dto.ClubRequest;
import tig.server.search.dto.SearchResponseDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-12T23:12:44+0900",
    comments = "version: 1.5.1.Final, compiler: javac, environment: Java 17.0.7 (Oracle Corporation)"
)
@Component
public class SearchMapperImpl implements SearchMapper {

    @Override
    public SearchResponseDto entityToResponse(Club club) {
        if ( club == null ) {
            return null;
        }

        SearchResponseDto searchResponseDto = new SearchResponseDto();

        searchResponseDto.setClubId( club.getId() );
        searchResponseDto.setClubName( club.getClubName() );
        searchResponseDto.setAddress( club.getAddress() );
        searchResponseDto.setRatingSum( club.getRatingSum() );
        searchResponseDto.setRatingCount( club.getRatingCount() );
        searchResponseDto.setPhoneNumber( club.getPhoneNumber() );
        searchResponseDto.setSnsLink( club.getSnsLink() );
        searchResponseDto.setLatitude( club.getLatitude() );
        searchResponseDto.setLongitude( club.getLongitude() );
        searchResponseDto.setCategory( club.getCategory() );
        searchResponseDto.setType( club.getType() );
        List<String> list = club.getImageUrls();
        if ( list != null ) {
            searchResponseDto.setImageUrls( new ArrayList<String>( list ) );
        }

        return searchResponseDto;
    }

    @Override
    public Club updateFromRequest(ClubRequest clubRequest, Club club) {
        if ( clubRequest == null ) {
            return club;
        }

        if ( clubRequest.getClubName() != null ) {
            club.setClubName( clubRequest.getClubName() );
        }
        if ( clubRequest.getAddress() != null ) {
            club.setAddress( clubRequest.getAddress() );
        }
        if ( clubRequest.getPhoneNumber() != null ) {
            club.setPhoneNumber( clubRequest.getPhoneNumber() );
        }
        if ( clubRequest.getSnsLink() != null ) {
            club.setSnsLink( clubRequest.getSnsLink() );
        }
        if ( clubRequest.getRatingSum() != null ) {
            club.setRatingSum( clubRequest.getRatingSum() );
        }
        if ( clubRequest.getRatingCount() != null ) {
            club.setRatingCount( clubRequest.getRatingCount() );
        }
        if ( clubRequest.getCategory() != null ) {
            club.setCategory( clubRequest.getCategory() );
        }
        if ( clubRequest.getType() != null ) {
            club.setType( clubRequest.getType() );
        }
        if ( clubRequest.getLatitude() != null ) {
            club.setLatitude( clubRequest.getLatitude() );
        }
        if ( clubRequest.getLongitude() != null ) {
            club.setLongitude( clubRequest.getLongitude() );
        }
        if ( club.getImageUrls() != null ) {
            List<String> list = clubRequest.getImageUrls();
            if ( list != null ) {
                club.getImageUrls().clear();
                club.getImageUrls().addAll( list );
            }
        }
        else {
            List<String> list = clubRequest.getImageUrls();
            if ( list != null ) {
                club.setImageUrls( new ArrayList<String>( list ) );
            }
        }

        return club;
    }
}
