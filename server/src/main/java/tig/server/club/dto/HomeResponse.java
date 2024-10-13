package tig.server.club.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tig.server.club.domain.Club;
import tig.server.enums.Category;

import java.util.List;
import java.util.Map;

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

    Map<Category, List<CategoryClubResponse>> nearestClubsByCategory;

    @Schema(type = "boolean", example = "true")
    private Boolean isHeart;
}
