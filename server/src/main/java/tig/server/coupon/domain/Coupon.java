package tig.server.coupon.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tig.server.member.domain.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
}
