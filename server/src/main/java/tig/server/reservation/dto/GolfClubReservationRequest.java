package tig.server.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tig.server.reservation.PackageReservationRequest;

import java.time.LocalDate;
import java.time.LocalTime;

// GOLFCLUB 요청
@JsonTypeName("GOLFCLUB")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GolfClubReservationRequest implements PackageReservationRequest {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    private int personCount;

    @JsonProperty("type")
    private GolfType golfType; // 9홀/18홀

    @Override
    public String getCategory() {
        return "GOLFCLUB";
    }

    public enum GolfType {
        NINE_HOLE("9홀"),
        EIGHTEEN_HOLE("18홀");

        private final String description;

        GolfType(String description) {
            this.description = description;
        }
    }
}