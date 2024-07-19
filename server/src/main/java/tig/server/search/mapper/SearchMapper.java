package tig.server.search.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import tig.server.club.domain.Club;
import tig.server.club.dto.ClubRequest;
import tig.server.club.dto.ClubResponse;
import tig.server.club.mapper.ClubMapper;
import tig.server.search.dto.SearchResponseDto;

@Mapper(componentModel = "spring")
public interface SearchMapper {
    SearchMapper INSTANCE = Mappers.getMapper(SearchMapper.class);

    @Mapping(source = "id", target = "clubId")
    SearchResponseDto entityToResponse(Club club);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Club updateFromRequest(ClubRequest clubRequest, @MappingTarget Club club);
}
