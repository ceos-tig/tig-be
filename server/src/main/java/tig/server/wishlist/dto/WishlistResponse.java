package tig.server.wishlist.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tig.server.club.domain.Club;
import tig.server.member.domain.Member;

import javax.swing.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistResponse {
    @Schema(type = "integer", example = "1")
    private Long wishlistId;

    @Schema(type = "member", example = "")
    private Member member;

    @Schema(type = "club", example = "")
    private Club club;

    @Schema(type = "string", example = "2024-07-08T10:00:00")
    private LocalDateTime createdAt;

    @Schema(type = "string", example = "2024-07-08T10:00:00")
    private LocalDateTime updatedAt;
}
