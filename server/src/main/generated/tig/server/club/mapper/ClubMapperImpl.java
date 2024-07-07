package tig.server.club.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tig.server.club.domain.Club;
import tig.server.club.dto.ClubDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-07T03:42:40+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.5 (Oracle Corporation)"
)
@Component
public class ClubMapperImpl implements ClubMapper {

    @Override
    public Club requestToEntity(ClubDTO.Request clubRequest) {
        if ( clubRequest == null ) {
            return null;
        }

        Club.ClubBuilder club = Club.builder();

        club.clubName( clubRequest.getClubName() );
        club.address( clubRequest.getAddress() );
        club.rating( clubRequest.getRating() );
        club.price( clubRequest.getPrice() );
        club.phoneNumber( clubRequest.getPhoneNumber() );
        club.snsLink( clubRequest.getSnsLink() );
        club.businessHours( clubRequest.getBusinessHours() );
        club.category( clubRequest.getCategory() );
        club.type( clubRequest.getType() );
        club.latitude( clubRequest.getLatitude() );
        club.longitude( clubRequest.getLongitude() );
        List<String> list = clubRequest.getImageUrls();
        if ( list != null ) {
            club.imageUrls( new ArrayList<String>( list ) );
        }

        return club.build();
    }

    @Override
    public ClubDTO.Response entityToResponse(Club club) {
        if ( club == null ) {
            return null;
        }

        ClubDTO.Response.ResponseBuilder response = ClubDTO.Response.builder();

        response.clubName( club.getClubName() );
        response.address( club.getAddress() );
        response.rating( club.getRating() );
        response.price( club.getPrice() );
        response.phoneNumber( club.getPhoneNumber() );
        response.snsLink( club.getSnsLink() );
        response.businessHours( club.getBusinessHours() );
        response.latitude( club.getLatitude() );
        response.longitude( club.getLongitude() );
        response.category( club.getCategory() );
        response.type( club.getType() );
        List<String> list = club.getImageUrls();
        if ( list != null ) {
            response.imageUrls( new ArrayList<String>( list ) );
        }

        return response.build();
    }

    @Override
    public Club responseToEntity(ClubDTO.Response clubResponse) {
        if ( clubResponse == null ) {
            return null;
        }

        Club.ClubBuilder club = Club.builder();

        club.clubName( clubResponse.getClubName() );
        club.address( clubResponse.getAddress() );
        club.rating( clubResponse.getRating() );
        club.price( clubResponse.getPrice() );
        club.phoneNumber( clubResponse.getPhoneNumber() );
        club.snsLink( clubResponse.getSnsLink() );
        club.businessHours( clubResponse.getBusinessHours() );
        club.category( clubResponse.getCategory() );
        club.type( clubResponse.getType() );
        club.latitude( clubResponse.getLatitude() );
        club.longitude( clubResponse.getLongitude() );
        List<String> list = clubResponse.getImageUrls();
        if ( list != null ) {
            club.imageUrls( new ArrayList<String>( list ) );
        }

        return club.build();
    }

    @Override
    public Club updateFromRequest(ClubDTO.Request clubRequest, Club club) {
        if ( clubRequest == null ) {
            return club;
        }

        if ( clubRequest.getClubName() != null ) {
            club.setClubName( clubRequest.getClubName() );
        }
        if ( clubRequest.getAddress() != null ) {
            club.setAddress( clubRequest.getAddress() );
        }
        if ( clubRequest.getRating() != null ) {
            club.setRating( clubRequest.getRating() );
        }
        if ( clubRequest.getPrice() != null ) {
            club.setPrice( clubRequest.getPrice() );
        }
        if ( clubRequest.getPhoneNumber() != null ) {
            club.setPhoneNumber( clubRequest.getPhoneNumber() );
        }
        if ( clubRequest.getSnsLink() != null ) {
            club.setSnsLink( clubRequest.getSnsLink() );
        }
        if ( clubRequest.getBusinessHours() != null ) {
            club.setBusinessHours( clubRequest.getBusinessHours() );
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
