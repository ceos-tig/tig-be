package tig.server.reservation.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import tig.server.base.BaseTimeEntity;
import tig.server.club.domain.Club;
import tig.server.enums.ProgramEnum;
import tig.server.enums.Status;
import tig.server.member.domain.Member;
import tig.server.review.domain.Review;

import java.time.LocalDateTime;

import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "is_deleted = false")
public class Reservation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @Builder.Default
    private boolean isDeleted = Boolean.FALSE;

    private Integer adultCount;
    private Integer teenagerCount;
    private Integer kidsCount;
    private Integer price;
    private Integer gameCount;


    private LocalDateTime date;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Status status;
    private String paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    private String message;

    // 예약 정보 입력시 입력하는 필드
    private String userName;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private ProgramEnum programEnum;

}
