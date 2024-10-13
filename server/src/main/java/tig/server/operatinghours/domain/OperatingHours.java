package tig.server.operatinghours.domain;

import jakarta.persistence.*;
import lombok.Getter;
import tig.server.club.domain.Club;
import tig.server.enums.DayOfWeek;

import java.time.LocalTime;

@Entity
@Getter
@Table(name = "operating_hours")
public class OperatingHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operating_hours_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;  // 요일 정보

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;  // 시작 시간

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;  // 종료 시간
}