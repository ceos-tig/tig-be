package tig.server.wishlist.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import tig.server.base.BaseTimeEntity;
import tig.server.club.domain.Club;
import tig.server.member.domain.Member;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "is_deleted = false")
@Table(name = "wishlist", uniqueConstraints = {@UniqueConstraint(columnNames = {"member_id", "club_id"})})
public class Wishlist extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlist_id")
    private Long id;

    @Builder.Default
    private boolean isDeleted = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;
}
