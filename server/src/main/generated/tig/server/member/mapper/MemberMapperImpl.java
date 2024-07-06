package tig.server.member.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tig.server.member.domain.Member;
import tig.server.member.dto.MemberDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-07T03:42:40+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.5 (Oracle Corporation)"
)
@Component
public class MemberMapperImpl implements MemberMapper {

    @Override
    public Member requestToEntity(MemberDTO.Request memberRequest) {
        if ( memberRequest == null ) {
            return null;
        }

        Member.MemberBuilder member = Member.builder();

        member.name( memberRequest.getName() );
        member.email( memberRequest.getEmail() );
        member.phoneNumber( memberRequest.getPhoneNumber() );
        member.profileImage( memberRequest.getProfileImage() );
        member.refreshToken( memberRequest.getRefreshToken() );
        member.memberRoleEnum( memberRequest.getMemberRoleEnum() );

        return member.build();
    }

    @Override
    public MemberDTO.Response entityToResponse(Member member) {
        if ( member == null ) {
            return null;
        }

        MemberDTO.Response.ResponseBuilder response = MemberDTO.Response.builder();

        response.name( member.getName() );
        response.email( member.getEmail() );
        response.phoneNumber( member.getPhoneNumber() );
        response.profileImage( member.getProfileImage() );
        response.refreshToken( member.getRefreshToken() );
        response.memberRoleEnum( member.getMemberRoleEnum() );

        return response.build();
    }

    @Override
    public Member responseToEntity(MemberDTO.Response meberResponse) {
        if ( meberResponse == null ) {
            return null;
        }

        Member.MemberBuilder member = Member.builder();

        member.name( meberResponse.getName() );
        member.email( meberResponse.getEmail() );
        member.phoneNumber( meberResponse.getPhoneNumber() );
        member.profileImage( meberResponse.getProfileImage() );
        member.refreshToken( meberResponse.getRefreshToken() );
        member.memberRoleEnum( meberResponse.getMemberRoleEnum() );

        return member.build();
    }

    @Override
    public Member updateFromRequest(MemberDTO.Request memberRequest, Member member) {
        if ( memberRequest == null ) {
            return member;
        }

        return member;
    }
}
