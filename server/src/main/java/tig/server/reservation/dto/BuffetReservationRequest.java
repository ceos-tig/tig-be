package tig.server.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tig.server.reservation.PackageReservationRequest;

import java.time.LocalDate;
import java.time.LocalTime;

@JsonTypeName("BUFFET")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuffetReservationRequest implements PackageReservationRequest {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime pickupTime;

    private String address;

    private int personCount;

    private BuffetOption option; // 일반/프리미엄

    @Override
    public String getCategory() {
        return "BUFFET";
    }

    public enum BuffetOption {
        STANDARD("일반"),
        PREMIUM("프리미엄");

        private final String description;

        BuffetOption(String description) {
            this.description = description;
        }
    }
}
