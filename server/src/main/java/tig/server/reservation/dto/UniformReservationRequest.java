package tig.server.reservation.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tig.server.reservation.PackageReservationRequest;

@JsonTypeName("UNIFORM")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UniformReservationRequest implements PackageReservationRequest {
    private String address;

    private int sCount;
    private int mCount;
    private int lCount;
    private int xlCount;
    private int xxlCount;

    @Override
    public String getCategory() {
        return "UNIFORM";
    }
}
