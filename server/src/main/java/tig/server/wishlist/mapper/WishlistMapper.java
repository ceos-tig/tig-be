package tig.server.wishlist.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import tig.server.dummy.DummyDto;
import tig.server.wishlist.domain.Wishlist;
import tig.server.wishlist.dto.WishlistRequest;
import tig.server.wishlist.dto.WishlistResponse;

@Mapper(componentModel = "spring")
public interface WishlistMapper {
    WishlistMapper INSTANCE = Mappers.getMapper(WishlistMapper.class);

    @Mapping(target = "id", ignore = true)
    Wishlist requestToEntity(WishlistRequest wishlistRequest);

    WishlistResponse entityToResponse(Wishlist wishlist);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Wishlist updateFromRequest(WishlistRequest wishlistRequest, @MappingTarget Wishlist wishlist);
}
