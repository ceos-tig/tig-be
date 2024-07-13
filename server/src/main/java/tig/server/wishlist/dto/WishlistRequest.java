package tig.server.wishlist.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistRequest {

    @Schema(type = "integer", example = "1")
    private Long clubId;

    @Schema(type = "string", example = "2024-07-08T10:00:00")
    private LocalDateTime createdAt;

    @Schema(type = "string", example = "2024-07-08T10:00:00")
    private LocalDateTime updatedAt;
}
