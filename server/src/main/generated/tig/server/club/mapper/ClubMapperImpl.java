package tig.server.club.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tig.server.club.domain.Club;
import tig.server.club.dto.ClubRequest;
import tig.server.club.dto.ClubResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-18T20:47:29+0900",
    comments = "version: 1.5.1.Final, compiler: javac, environment: Java 17.0.5 (Oracle Corporation)"
)
@Component
public class ClubMapperImpl implements ClubMapper {

    @Override
    public Club requestToEntity(ClubRequest clubRequest) {
        if ( clubRequest == null ) {
            return null;
        }

        Club.ClubBuilder club = Club.builder();

        club.clubName( clubRequest.getClubName() );
        club.address( clubRequest.getAddress() );
        club.price( clubRequest.getPrice() );
        club.phoneNumber( clubRequest.getPhoneNumber() );
        club.snsLink( clubRequest.getSnsLink() );
        club.businessHours( clubRequest.getBusinessHours() );
        club.ratingSum( clubRequest.getRatingSum() );
        club.ratingCount( clubRequest.getRatingCount() );
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
    public ClubResponse entityToResponse(Club club) {
        if ( club == null ) {
            return null;
        }

        ClubResponse.ClubResponseBuilder clubResponse = ClubResponse.builder();

        clubResponse.id( club.getId() );
        clubResponse.clubName( club.getClubName() );
        clubResponse.address( club.getAddress() );
        clubResponse.ratingSum( club.getRatingSum() );
        clubResponse.ratingCount( club.getRatingCount() );
        clubResponse.price( club.getPrice() );
        clubResponse.phoneNumber( club.getPhoneNumber() );
        clubResponse.snsLink( club.getSnsLink() );
        clubResponse.businessHours( club.getBusinessHours() );
        clubResponse.latitude( club.getLatitude() );
        clubResponse.longitude( club.getLongitude() );
        clubResponse.category( club.getCategory() );
        clubResponse.type( club.getType() );
        List<String> list = club.getImageUrls();
        if ( list != null ) {
            clubResponse.imageUrls( new ArrayList<String>( list ) );
        }

        return clubResponse.build();
    }

    @Override
    public Club responseToEntity(ClubResponse clubResponse) {
        if ( clubResponse == null ) {
            return null;
        }

        Club.ClubBuilder club = Club.builder();

        club.id( clubResponse.getId() );
        club.clubName( clubResponse.getClubName() );
        club.address( clubResponse.getAddress() );
        club.price( clubResponse.getPrice() );
        club.phoneNumber( clubResponse.getPhoneNumber() );
        club.snsLink( clubResponse.getSnsLink() );
        club.businessHours( clubResponse.getBusinessHours() );
        club.ratingSum( clubResponse.getRatingSum() );
        club.ratingCount( clubResponse.getRatingCount() );
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
