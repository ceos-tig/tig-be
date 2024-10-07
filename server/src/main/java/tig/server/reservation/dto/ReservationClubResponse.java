package tig.server.reservation.dto;

import lombok.*;
import tig.server.operatinghours.dto.OperatingHoursResponse;
import tig.server.price.dto.PriceResponse;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationClubResponse {
    private String clubName;
    private String address;
    private List<PriceResponse> prices;
    private List<OperatingHoursResponse> operatingHours;
}
