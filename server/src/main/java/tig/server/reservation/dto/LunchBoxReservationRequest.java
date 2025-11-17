package tig.server.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tig.server.reservation.PackageReservationRequest;

import java.time.LocalDate;
import java.time.LocalTime;

@JsonTypeName("LUNCH_BOX")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LunchBoxReservationRequest implements PackageReservationRequest {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime pickupTime;

    private String address;

    private int personCount;

    @Override
    public String getCategory() {
        return "LUNCH_BOX";
    }
}
