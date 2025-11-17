package tig.server.reservation.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import tig.server.enums.PackageCategory;
import tig.server.enums.Status;
import tig.server.member.domain.Member;
import tig.server.packageSet.domain.PackageSet;
import tig.server.reservation.PackageReservationRequest;
import tig.server.reservation.converter.ReservationDetailsConverter;

import java.time.LocalDateTime;

@Entity
@Table(name = "package_reservation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_set_id")
    private PackageSet packageSet;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private PackageCategory category;

    @Column(name = "reservation_details", columnDefinition = "JSON")
    @Convert(converter = ReservationDetailsConverter.class)
    private PackageReservationRequest reservationDetails;

    @Column(name = "message", length = 1000)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
