package tig.server.reservation.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationClubResponse {
    private String clubName;
    private String address;
    private Integer price;
    private String businessHours;
}
