package tig.server.price.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tig.server.club.domain.Club;
import tig.server.enums.ProgramEnum;

@Entity
@Table(name = "BILLIARDS_PRICE")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BilliardsPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id")
    private Long priceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;  // 각 당구장이 속한 클럽 정보

    @Enumerated(EnumType.STRING)
    @Column(name = "program_type", nullable = false)
    private ProgramEnum programName;  // 중대, 대대, 3구, 4구, 포켓볼 등

    @Column(name = "duration", nullable = true)
    private Integer duration;  // 시간 단위 (예: 10분, 1시간)

    @Column(name = "price", nullable = false)
    private Integer price;  // 해당 단위 시간의 가격
}
