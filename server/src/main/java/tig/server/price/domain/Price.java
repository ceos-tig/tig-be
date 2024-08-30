package tig.server.price.domain;

import jakarta.persistence.*;
import lombok.*;
import tig.server.club.domain.Club;
import tig.server.enums.Type;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long price_id;

    private Integer hour;
    private Integer price;

    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;
}


