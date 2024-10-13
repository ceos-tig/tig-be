package tig.server.price.domain;

import jakarta.persistence.*;
import lombok.Getter;
import tig.server.club.domain.Club;
import tig.server.enums.ProgramEnum;

@Entity
@Getter
public class TableTennisPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id")
    private Long priceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "program_name", nullable = false)
    private ProgramEnum programName;  // 일반 볼링, 락 볼링 등

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)  // Club 엔티티 참조
    private Club club;

    @Column(name = "duration_type", nullable = false)
    private Integer durationType;  // 시간 단위 (예: 30분, 1시간 등)

    @Column(name = "price", nullable = false)
    private Integer price;

}
