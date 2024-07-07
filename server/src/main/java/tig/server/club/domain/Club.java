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
@SQLDelete(sql = "UPDATE club SET is_deleted = true WHERE club_id = ?")
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
    private Float rating;
    private Integer price;
    private String phoneNumber;
    private String snsLink;
    private String businessHours;

    // enums
    private Category category;
    private Type type;

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

}
