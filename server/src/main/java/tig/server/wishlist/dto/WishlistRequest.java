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

}
