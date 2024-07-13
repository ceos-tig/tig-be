package tig.server.wishlist.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tig.server.wishlist.domain.Wishlist;
import tig.server.wishlist.dto.WishlistDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-13T15:56:59+0900",
    comments = "version: 1.5.1.Final, compiler: javac, environment: Java 17.0.5 (Oracle Corporation)"
)
@Component
public class WishlistMapperImpl implements WishlistMapper {

    @Override
    public Wishlist requestToEntity(WishlistDTO.Request wishlistRequest) {
        if ( wishlistRequest == null ) {
            return null;
        }

        Wishlist.WishlistBuilder wishlist = Wishlist.builder();

        wishlist.memberId( wishlistRequest.getMemberId() );
        wishlist.clubId( wishlistRequest.getClubId() );

        return wishlist.build();
    }

    @Override
    public WishlistDTO.Response entityToResponse(Wishlist wishlist) {
        if ( wishlist == null ) {
            return null;
        }

        WishlistDTO.Response.ResponseBuilder response = WishlistDTO.Response.builder();

        response.memberId( wishlist.getMemberId() );
        response.clubId( wishlist.getClubId() );
        response.createdAt( wishlist.getCreatedAt() );
        response.updatedAt( wishlist.getUpdatedAt() );

        return response.build();
    }

    @Override
    public Wishlist updateFromRequest(WishlistDTO.Request wishlistRequest, Wishlist wishlist) {
        if ( wishlistRequest == null ) {
            return wishlist;
        }

        if ( wishlistRequest.getMemberId() != null ) {
            wishlist.setMemberId( wishlistRequest.getMemberId() );
        }
        if ( wishlistRequest.getClubId() != null ) {
            wishlist.setClubId( wishlistRequest.getClubId() );
        }

        return wishlist;
    }
}
