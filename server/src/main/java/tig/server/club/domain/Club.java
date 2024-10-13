package tig.server.club.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import tig.server.base.BaseTimeEntity;
import tig.server.enums.Category;
import tig.server.enums.Type;
import tig.server.reservation.domain.Reservation;

import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Where(clause = "is_deleted = false")
public class Club extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id")
    private Long id;

    @Builder.Default
    private boolean isDeleted = Boolean.FALSE;

    private String clubName;
    private String address;

    private String phoneNumber;
    private String snsLink;

    private Float ratingSum = 0f;
    private Integer ratingCount = 0;

    // enums
    @Enumerated(EnumType.STRING)
    private Category category;
    @Enumerated(EnumType.STRING)
    private Type type; //TODO : @Enumerated 넣어야 함

    // coordinates
    private Float latitude;
    private Float longitude;

    @ElementCollection
    @CollectionTable(name = "club_services", joinColumns = @JoinColumn(name = "club_id"))
    @Column(name = "services")
    private List<String> services;

    @ElementCollection
    @CollectionTable(name = "club_images", joinColumns = @JoinColumn(name = "club_id"))
    @Column(name = "image_url")
    private List<String> imageUrls;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "club")
    private List<Reservation> reservations;

    public void increaseRatingCount(Float rating) {
        if (this.ratingCount == null) {
            this.ratingCount = 0;
        }
        if (rating != null) {
            this.ratingCount++;
        }
    }

    public void accumulateRatingSum(Float addition) {
        if (this.ratingSum == null) {
            this.ratingSum = 0f;
        }
        if (addition == null) {
            addition = 0f;
        }
        this.ratingSum += addition;
    }

    public void reduceRatingSum(Float subtraction) {
        if (this.ratingSum != null) {
            this.ratingSum -= subtraction;
        }
    }
}
