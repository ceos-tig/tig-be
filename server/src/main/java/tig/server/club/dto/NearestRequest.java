package tig.server.club.dto;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NearestRequest {

    private double latitude;

    private double longitude;

}
