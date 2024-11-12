package tig.server.coupon.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import tig.server.member.domain.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Where(clause = "is_deleted = false") // 기본적으로 isDeleted가 false인 데이터만 조회
public class Coupon {

    @Id
    @GeneratedValue
    @Column(name = "coupon_id")
    private Long id;

    private String name;

    private Integer discount;

    private String description;

    private LocalDateTime expireDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder.Default
    private boolean isDeleted = false; // 기본값은 false로 설정하여 삭제되지 않은 것으로 간주

    // Soft delete 메서드
    public void markAsDeleted() {
        this.isDeleted = true;
    }
}
