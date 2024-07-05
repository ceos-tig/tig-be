package tig.server.member.domain;

import jakarta.persistence.*;
import lombok.*;
import tig.server.enums.MemberRoleEnum;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member {

    public Member(String name,
                  String email,
                  String phoneNumber,
                  String uniqueId,
                  MemberRoleEnum memberRoleEnum,
                  String profileImage) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.uniqueId = uniqueId;
        this.memberRoleEnum = memberRoleEnum;
        this.profileImage = profileImage;
    }

    // PK
    @Id
    @GeneratedValue
    private Long memberId;

    // Properties
    private String name;

    private String email;

    private String phoneNumber;

    private String uniqueId;

    private String profileImage;

    private String refreshToken;

    // Enum
    @Enumerated(EnumType.STRING)
    private MemberRoleEnum memberRoleEnum;

    //== 편의 메서드 ==//
    public void updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }
}
