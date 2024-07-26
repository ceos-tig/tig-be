package tig.server.amenity.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tig.server.club.domain.Club;
import tig.server.enums.Facility;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Amenity {
    @Id
    @GeneratedValue
    @Column(name = "amenity_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "facility", nullable = false)
    private Facility name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;
}
