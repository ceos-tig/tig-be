package tig.server.wishlist.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import tig.server.wishlist.domain.Wishlist;
import tig.server.wishlist.dto.WishlistDTO;

@Mapper(componentModel = "spring")
public interface WishlistMapper {
    WishlistMapper INSTANCE = Mappers.getMapper(WishlistMapper.class);

    @Mapping(target = "id", ignore = true)
    Wishlist requestToEntity(WishlistDTO.Request wishlistRequest);

    WishlistDTO.Response entityToResponse(Wishlist wishlist);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Wishlist updateFromRequest(WishlistDTO.Request wishlistRequest, @MappingTarget Wishlist wishlist);
}
