package tig.server.search.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tig.server.kakao.dto.LoginAccessTokenResponseDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AvgPointDto {
    private Float avgLongitude;
    private Float avgLatitude;

    public static AvgPointDto fromPoint(Float avgLongitude, Float avgLatitude) {
        return new AvgPointDto(
                avgLongitude,
                avgLatitude
        );
    }
}
