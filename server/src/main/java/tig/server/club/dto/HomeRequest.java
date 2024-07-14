package tig.server.club.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeRequest {

    @Schema(type = "float", example = "129.0921")
    private Float latitude;

    @Schema(type = "float", example = "52.0123")
    private Float longitude;

}
