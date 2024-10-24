package tig.server.price.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tig.server.club.domain.Club;
import tig.server.enums.ProgramEnum;

import java.time.LocalTime;

@Entity
@Table(name = "BASEBALL_PRICE")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseballPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id")
    private Long priceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;  // 해당 구장의 클럽 정보

    @Enumerated(EnumType.STRING)
    @Column(name = "program_name", nullable = false)
    private ProgramEnum programName;  // 예: 메이저룸, 마이너룸, 또는 타격 회수 등

    @Column(name = "inning", nullable = true)
    private Integer inning; // 3회, 6회, 9회

    @Column(name = "duration", nullable = true)
    private Integer duration; // 분 단위로 시간 기록. 이닝을 기록 했을경우에는 null 가능

    @Column(name = "start_time")
    private LocalTime startTime;  // 시작 시간

    @Column(name = "end_time")
    private LocalTime endTime;  // 종료 시간

    @Column(name = "price", nullable = false)
    private Integer price;  // 해당 시간에 따른 가격
}
