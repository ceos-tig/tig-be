package tig.server.amenity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tig.server.enums.Facility;

@Getter
@AllArgsConstructor
public class AmenityResponseDto {
    private Facility facility;

    public static AmenityResponseDto fromAmenity(Facility facility) {
        return new AmenityResponseDto(facility);
    }
}
