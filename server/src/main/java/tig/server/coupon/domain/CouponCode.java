package tig.server.coupon.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Where(clause = "is_used = false")
public class CouponCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, updatable = false)
    private String code; // 고유 쿠폰 코드

    @Column(name = "is_used", nullable = false)
    private boolean isUsed = false; // 쿠폰 사용 여부

    // 쿠폰 사용 시 isUsed를 true로 업데이트하는 메서드
    public void markAsUsed() {
        this.isUsed = true;
    }
}
