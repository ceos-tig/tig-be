package tig.server.club.domain;

import jakarta.persistence.*;
import lombok.*;
import tig.server.base.BaseTimeEntity;
import tig.server.enums.Category;
import tig.server.enums.Type;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Club extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id")
    private Long id;

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

    // private reservations(FK)
}
