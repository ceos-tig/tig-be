package tig.server.club.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryClubResponse {

    @Schema(type = "list", description = "클럽 이미지 URL 리스트")
    private List<String> imageUrls;

    @Schema(type = "string", description = "탁구")
    private String category;

    @Schema(type = "long", description = "id")
    private Long clubId;

    @Schema(type = "string", description = "티그 탁구장")
    private String clubName;

    @Schema(type = "string", description = "서울시 강남구 역삼동 123-45")
    private String address;
}