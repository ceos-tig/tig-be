package tig.server.wishlist.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tig.server.wishlist.domain.Wishlist;
import tig.server.wishlist.dto.WishlistRequest;
import tig.server.wishlist.dto.WishlistResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-25T22:46:56+0900",
    comments = "version: 1.5.1.Final, compiler: javac, environment: Java 17.0.5 (Oracle Corporation)"
)
@Component
public class WishlistMapperImpl implements WishlistMapper {

    @Override
    public Wishlist requestToEntity(WishlistRequest wishlistRequest) {
        if ( wishlistRequest == null ) {
            return null;
        }

        Wishlist.WishlistBuilder wishlist = Wishlist.builder();

        wishlist.member( wishlistRequest.getMember() );
        wishlist.club( wishlistRequest.getClub() );

        return wishlist.build();
    }

    @Override
    public WishlistResponse entityToResponse(Wishlist wishlist) {
        if ( wishlist == null ) {
            return null;
        }

        WishlistResponse.WishlistResponseBuilder wishlistResponse = WishlistResponse.builder();

        wishlistResponse.member( wishlist.getMember() );
        wishlistResponse.club( wishlist.getClub() );
        wishlistResponse.createdAt( wishlist.getCreatedAt() );
        wishlistResponse.updatedAt( wishlist.getUpdatedAt() );

        return wishlistResponse.build();
    }

    @Override
    public Wishlist updateFromRequest(WishlistRequest wishlistRequest, Wishlist wishlist) {
        if ( wishlistRequest == null ) {
            return wishlist;
        }

        if ( wishlistRequest.getMember() != null ) {
            wishlist.setMember( wishlistRequest.getMember() );
        }
        if ( wishlistRequest.getClub() != null ) {
            wishlist.setClub( wishlistRequest.getClub() );
        }

        return wishlist;
    }
}
