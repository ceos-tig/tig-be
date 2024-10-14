package tig.server.price.domain;

import jakarta.persistence.*;
import lombok.*;
import tig.server.club.domain.Club;
import tig.server.enums.ProgramEnum;

@Entity
@Table(name = "SQUASH_PRICE")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SquashPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id")
    private Long priceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;  // 해당 클럽 정보

    @Enumerated(EnumType.STRING)
    @Column(name = "program_name", nullable = false)
    private ProgramEnum programName;  // 스쿼시 이용 형태 (예: 원데이클래스, 스쿼시레슨, 주1회 등)

    @Column(name = "duration_in_months", nullable = true)
    private Integer durationInMonths;  // 기간 (개월 단위, null 가능)

    @Column(name = "lesson_count", nullable = true)
    private Integer lessonCount;  // 레슨 1회, 레슨 2회 등등

    @Column(name = "price", nullable = false)
    private Integer price;  // 해당 프로그램 가격
}
