package tig.server.wishlist.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tig.server.club.domain.Club;
import tig.server.member.domain.Member;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistRequest {

    @Schema(type = "date")
    private LocalDateTime createdAt;

    @Schema(type = "member", example = "")
    private Member member;

    @Schema(type = "club", example = "")
    private Club club;


}
