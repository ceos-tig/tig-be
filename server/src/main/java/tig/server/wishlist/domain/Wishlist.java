package tig.server.wishlist.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import tig.server.base.BaseTimeEntity;

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

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "club_id")
    private Long clubId;

}
