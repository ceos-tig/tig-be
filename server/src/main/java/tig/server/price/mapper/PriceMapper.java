package tig.server.price.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import tig.server.club.domain.Club;
import tig.server.club.dto.ClubRequest;
import tig.server.club.dto.ClubResponse;

@Mapper(componentModel = "spring")
public interface PriceMapper {
    tig.server.price.mapper.PriceMapper INSTANCE = Mappers.getMapper(tig.server.price.mapper.PriceMapper.class);

    @Mapping(target = "id", ignore = true)
    Club requestToEntity(ClubRequest clubRequest);

    ClubResponse entityToResponse(Club club);

    Club responseToEntity(ClubResponse clubResponse);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Club updateFromRequest(ClubRequest clubRequest, @MappingTarget Club club);
}
