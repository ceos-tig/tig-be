package tig.server.club.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeResponse {
    private List<ClubResponse> nearestClubs;

    private List<ClubResponse> popularClubs;

    private List<ClubResponse> recommendedClubs;
}
