package tig.server.reservation.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import tig.server.base.BaseTimeEntity;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE reservation SET is_deleted = true WHERE reservation_id = ?")
@Where(clause = "is_deleted = false")
public class Reservation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @Builder.Default
    private boolean isDeleted = Boolean.FALSE;

    private Integer adultCount;
    private Integer teenagerCount;
    private Integer kidsCount;

    private String date;
    private String startTime;
    private String endTime;

    private Integer price;

    private Boolean isReviewed = false;
}
