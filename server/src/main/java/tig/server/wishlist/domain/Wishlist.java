package tig.server.wishlist.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import tig.server.base.BaseTimeEntity;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "is_deleted = false")
public class Wishlist extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlist_id")
    private Long id;

    @Builder.Default
    private boolean isDeleed = Boolean.FALSE;

}
