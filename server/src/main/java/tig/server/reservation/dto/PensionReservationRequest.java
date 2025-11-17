package tig.server.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tig.server.reservation.PackageReservationRequest;

import java.time.LocalDate;

@JsonTypeName("PENSION")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PensionReservationRequest implements PackageReservationRequest {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private int personCount;

    @Override
    public String getCategory() {
        return "PENSION";
    }
}