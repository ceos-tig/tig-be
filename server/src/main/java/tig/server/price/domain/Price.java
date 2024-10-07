package tig.server.price.domain;

import jakarta.persistence.*;
import lombok.*;
import tig.server.club.domain.Club;
import tig.server.enums.DayOfWeek;
import tig.server.enums.ProgramEnum;
import tig.server.enums.Type;
import tig.server.program.domain.Program;

import java.time.LocalTime;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;  // Program 엔티티와의 관계 설정

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;  // 시작 시간을 LocalTime으로 저장 (HH:mm:ss 형식)

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;  // 종료 시간을 LocalTime으로 저장

    @Column(name = "price", nullable = false)
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;  // 요일 정보 추가

}


