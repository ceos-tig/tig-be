package tig.server.club.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import tig.server.club.domain.Club;
import tig.server.club.dto.ClubDTO;

@Mapper(componentModel = "spring")
public interface ClubMapper {
    ClubMapper INSTANCE = Mappers.getMapper(ClubMapper.class);

    @Mapping(target = "id", ignore = true)
    Club requestToEntity(ClubDTO.Request clubRequest);

    ClubDTO.Response entityToResponse(Club club);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Club updateFromRequest(ClubDTO.Request clubRequest, @MappingTarget Club club);
}