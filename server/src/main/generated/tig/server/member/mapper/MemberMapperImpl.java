package tig.server.member.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tig.server.member.domain.Member;
import tig.server.member.dto.MemberRequest;
import tig.server.member.dto.MemberResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-18T20:47:29+0900",
    comments = "version: 1.5.1.Final, compiler: javac, environment: Java 17.0.5 (Oracle Corporation)"
)
@Component
public class MemberMapperImpl implements MemberMapper {

    @Override
    public Member requestToEntity(MemberRequest memberRequest) {
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
    public MemberResponse entityToResponse(Member member) {
        if ( member == null ) {
            return null;
        }

        MemberResponse.MemberResponseBuilder memberResponse = MemberResponse.builder();

        memberResponse.id( member.getId() );
        memberResponse.name( member.getName() );
        memberResponse.email( member.getEmail() );
        memberResponse.phoneNumber( member.getPhoneNumber() );
        memberResponse.profileImage( member.getProfileImage() );
        memberResponse.refreshToken( member.getRefreshToken() );
        memberResponse.memberRoleEnum( member.getMemberRoleEnum() );

        return memberResponse.build();
    }

    @Override
    public Member responseToEntity(MemberResponse meberResponse) {
        if ( meberResponse == null ) {
            return null;
        }

        Member.MemberBuilder member = Member.builder();

        member.id( meberResponse.getId() );
        member.name( meberResponse.getName() );
        member.email( meberResponse.getEmail() );
        member.phoneNumber( meberResponse.getPhoneNumber() );
        member.profileImage( meberResponse.getProfileImage() );
        member.refreshToken( meberResponse.getRefreshToken() );
        member.memberRoleEnum( meberResponse.getMemberRoleEnum() );

        return member.build();
    }

    @Override
    public Member updateFromRequest(MemberRequest memberRequest, Member member) {
        if ( memberRequest == null ) {
            return member;
        }

        if ( memberRequest.getName() != null ) {
            member.setName( memberRequest.getName() );
        }
        if ( memberRequest.getEmail() != null ) {
            member.setEmail( memberRequest.getEmail() );
        }
        if ( memberRequest.getPhoneNumber() != null ) {
            member.setPhoneNumber( memberRequest.getPhoneNumber() );
        }
        if ( memberRequest.getProfileImage() != null ) {
            member.setProfileImage( memberRequest.getProfileImage() );
        }
        if ( memberRequest.getRefreshToken() != null ) {
            member.setRefreshToken( memberRequest.getRefreshToken() );
        }
        if ( memberRequest.getMemberRoleEnum() != null ) {
            member.setMemberRoleEnum( memberRequest.getMemberRoleEnum() );
        }

        return member;
    }
}
