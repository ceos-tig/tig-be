package tig.server.member.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import tig.server.enums.MemberRoleEnum;
import tig.server.reservation.domain.Reservation;
import tig.server.wishlist.domain.Wishlist;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Where(clause = "is_deleted = false")
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Builder.Default
    private boolean isDeleted = Boolean.FALSE;

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

    public void updateName(String newName) {
        this.name = newName;
    }

    public void updatePhoneNumber(String newPhoneNumber) {
        this.phoneNumber = newPhoneNumber;
    }

    public void updateEmail(String newEmail) {
        this.email = newEmail;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    private List<Reservation> reservations;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    private List<Wishlist> wishlist;

}
