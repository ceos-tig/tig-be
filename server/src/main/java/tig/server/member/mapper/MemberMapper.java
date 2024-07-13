package tig.server.member.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import tig.server.member.domain.Member;
import tig.server.member.dto.MemberRequest;
import tig.server.member.dto.MemberResponse;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    tig.server.member.mapper.MemberMapper INSTANCE = Mappers.getMapper(tig.server.member.mapper.MemberMapper.class);

    @Mapping(target = "id", ignore = true)
    Member requestToEntity(MemberRequest memberRequest);

    MemberResponse entityToResponse(Member member);

    Member responseToEntity(MemberResponse meberResponse);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Member updateFromRequest(MemberRequest memberRequest, @MappingTarget Member member);
}