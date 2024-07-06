package tig.server.member.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import tig.server.member.domain.Member;
import tig.server.member.dto.MemberDTO;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    tig.server.member.mapper.MemberMapper INSTANCE = Mappers.getMapper(tig.server.member.mapper.MemberMapper.class);

    @Mapping(target = "id", ignore = true)
    Member requestToEntity(MemberDTO.Request memberRequest);

    MemberDTO.Response entityToResponse(Member member);

    Member responseToEntity(MemberDTO.Response meberResponse);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Member updateFromRequest(MemberDTO.Request memberRequest, @MappingTarget Member member);
}