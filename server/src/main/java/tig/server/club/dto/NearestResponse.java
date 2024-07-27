package tig.server.club.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NearestResponse {

    @Schema(type = "string", description = "강남구")
    private String nearestDistrict;
}
