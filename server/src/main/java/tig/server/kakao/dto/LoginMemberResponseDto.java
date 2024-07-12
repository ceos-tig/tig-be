package tig.server.kakao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tig.server.enums.MemberRoleEnum;
import tig.server.member.domain.Member;

@Getter
@AllArgsConstructor
public class LoginMemberResponseDto {

    private String name;

    private String email;

    private String phoneNumber;

    private String uniqueId;

    private String profileImage;

    private String refreshToken;

    private String accessToken;

    private MemberRoleEnum memberRoleEnum;

    // Static factory method
    public static LoginMemberResponseDto fromMember(Member member,String accessToken) {
        return new LoginMemberResponseDto(
                member.getName(),
                member.getEmail(),
                member.getPhoneNumber(),
                member.getUniqueId(),
                member.getProfileImage(),
                member.getRefreshToken(),
                accessToken,
                member.getMemberRoleEnum()
                );
    }
}
