package tig.server.price.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tig.server.club.domain.Club;
import tig.server.enums.DayOfWeek;
import tig.server.enums.ProgramEnum;

import java.time.LocalTime;

@Entity
@Table(name = "GOLF_PRICE")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GolfPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id")
    private Long priceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;  // 각 골프장이 속한 클럽 정보

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;  // 요일 정보 (MON, TUE, WED 등)

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;  // 시작 시간

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;  // 종료 시간

    @Column(name = "price", nullable = false)
    private Integer price;  // 해당 시간대의 가격

    @Column(name = "duration", nullable = true)
    private Integer duration;  // 시간 단위 (예: 30분, 60분 등)

    @Column(name = "holes", nullable = true)
    private Integer holes;  // 18, 9홀

    @Enumerated(EnumType.STRING)
    @Column(name = "program_name", nullable = false)
    private ProgramEnum programName;
}
