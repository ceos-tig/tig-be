package tig.server.club.mapper;

import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tig.server.club.domain.Club;
import tig.server.club.dto.ClubDTO;
import tig.server.enums.Category;
import tig.server.enums.Type;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-03T19:39:32+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.5 (Oracle Corporation)"
)
@Component
public class ClubMapperImpl implements ClubMapper {

    @Override
    public Club requestToEntity(ClubDTO.Request clubRequest) {
        if ( clubRequest == null ) {
            return null;
        }

        String clubName = null;
        String address = null;
        Float rating = null;
        Integer price = null;
        String phoneNumber = null;
        String snsLink = null;
        String businessHours = null;
        Category category = null;
        Type type = null;
        Float latitude = null;
        Float longitude = null;

        clubName = clubRequest.getClubName();
        address = clubRequest.getAddress();
        rating = clubRequest.getRating();
        price = clubRequest.getPrice();
        phoneNumber = clubRequest.getPhoneNumber();
        snsLink = clubRequest.getSnsLink();
        businessHours = clubRequest.getBusinessHours();
        category = clubRequest.getCategory();
        type = clubRequest.getType();
        latitude = clubRequest.getLatitude();
        longitude = clubRequest.getLongitude();

        Long id = null;
        List<String> services = null;
        List<String> imageUrls = null;

        Club club = new Club( id, clubName, address, rating, price, phoneNumber, snsLink, businessHours, category, type, latitude, longitude, services, imageUrls );

        return club;
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

        return response.build();
    }

    @Override
    public Club updateFromRequest(ClubDTO.Request portfolioRequest, Club club) {
        if ( portfolioRequest == null ) {
            return club;
        }

        if ( portfolioRequest.getClubName() != null ) {
            club.setClubName( portfolioRequest.getClubName() );
        }
        if ( portfolioRequest.getAddress() != null ) {
            club.setAddress( portfolioRequest.getAddress() );
        }
        if ( portfolioRequest.getRating() != null ) {
            club.setRating( portfolioRequest.getRating() );
        }
        if ( portfolioRequest.getPrice() != null ) {
            club.setPrice( portfolioRequest.getPrice() );
        }
        if ( portfolioRequest.getPhoneNumber() != null ) {
            club.setPhoneNumber( portfolioRequest.getPhoneNumber() );
        }
        if ( portfolioRequest.getSnsLink() != null ) {
            club.setSnsLink( portfolioRequest.getSnsLink() );
        }
        if ( portfolioRequest.getBusinessHours() != null ) {
            club.setBusinessHours( portfolioRequest.getBusinessHours() );
        }
        if ( portfolioRequest.getCategory() != null ) {
            club.setCategory( portfolioRequest.getCategory() );
        }
        if ( portfolioRequest.getType() != null ) {
            club.setType( portfolioRequest.getType() );
        }
        if ( portfolioRequest.getLatitude() != null ) {
            club.setLatitude( portfolioRequest.getLatitude() );
        }
        if ( portfolioRequest.getLongitude() != null ) {
            club.setLongitude( portfolioRequest.getLongitude() );
        }

        return club;
    }
}
