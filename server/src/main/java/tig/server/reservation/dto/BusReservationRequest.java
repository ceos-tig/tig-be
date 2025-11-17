package tig.server.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tig.server.reservation.PackageReservationRequest;

import java.time.LocalDate;

@JsonTypeName("BUS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusReservationRequest implements PackageReservationRequest {
    private BusType busType; // 왕복/편도

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    private String startLocation;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String endLocation;

    private BusOption option; // 24인승/45인승

    @Override
    public String getCategory() {
        return "BUS";
    }

    public enum BusType {
        ROUND_TRIP("왕복"),
        ONE_WAY("편도");

        private final String description;

        BusType(String description) {
            this.description = description;
        }
    }

    public enum BusOption {
        SEAT_24("24인승"),
        SEAT_45("45인승");

        private final String description;

        BusOption(String description) {
            this.description = description;
        }
    }
}
