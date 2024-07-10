package tig.server.review.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import tig.server.base.BaseTimeEntity;
import tig.server.reservation.domain.Reservation;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "is_deleted = false")
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Builder.Default
    private boolean isDeleted = Boolean.FALSE;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private Reservation reservation;

    private Integer rating;
    private String contents;
}
