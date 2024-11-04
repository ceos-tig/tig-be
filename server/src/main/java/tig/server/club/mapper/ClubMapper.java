package tig.server.club.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import tig.server.club.domain.Club;
import tig.server.club.dto.ClubRequest;
import tig.server.club.dto.ClubResponse;

@Mapper(componentModel = "spring")
public interface ClubMapper {
    ClubMapper INSTANCE = Mappers.getMapper(ClubMapper.class);

    @Mapping(target = "id", ignore = true)
    Club requestToEntity(ClubRequest clubRequest);

    @Mapping(source = "id", target = "clubId") // id를 clubId로 매핑
    ClubResponse entityToResponse(Club club);

    @Mapping(source = "clubId", target = "id") // clubId를 id로 매핑
    Club responseToEntity(ClubResponse clubResponse);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Club updateFromRequest(ClubRequest clubRequest, @MappingTarget Club club);
}