package tig.server.review.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import tig.server.base.BaseTimeEntity;
import tig.server.member.domain.Member;
import tig.server.reservation.domain.PackageReservation;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "is_deleted = false")
@Table(name = "package_set_review")
public class PackageSetReview extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_set_review_id")
    private Long id;

    @Builder.Default
    private boolean isDeleted = Boolean.FALSE;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "package_reservation_id")
    private PackageReservation packageReservation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private Float rating;

    @Column(length = 500)
    private String contents;
}