package tig.server.price.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tig.server.club.domain.Club;
import tig.server.enums.DayOfWeek;
import tig.server.enums.ProgramEnum;

@Entity
@Table(name = "TENNIS_PRICE")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TennisPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id")
    private Long priceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;  // 해당 클럽 정보

    @Enumerated(EnumType.STRING)
    @Column(name = "program_name", nullable = false)
    private ProgramEnum programName;  // 볼머신, 테니스코트, 테니스레슨

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;  // 평일, 주말 구분

    @Column(name = "duration", nullable = true)
    private Integer duration;  // 볼머신 이용 시간(분 단위), 주 1회/2회의 경우 null로 설정 가능

    @Column(name = "count_per_week", nullable = true)
    private Integer countPerWeek;  // 주에 몇회 인지

    @Column(name = "price", nullable = false)
    private Integer price;  // 가격 정보
}
