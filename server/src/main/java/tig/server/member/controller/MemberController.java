package tig.server.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tig.server.annotation.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tig.server.club.dto.ClubDTO;
import tig.server.error.ApiResponse;
import tig.server.member.domain.Member;
import tig.server.member.dto.MemberDTO;
import tig.server.member.dto.RefreshTokenRequestDto;
import tig.server.member.service.MemberService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
@Tag(name = "member", description = "유저(멤버) API")
public class MemberController {
    private final MemberService memberService;

    /**
     * refresh token을 통한 access token 재발급
     */
    @PostMapping("/reissue")
    @Operation(summary = "리프레시 토큰 발급")
    public ApiResponse<Object> reissueAccessToken(@LoginUser Member member,
                                                  @RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        String newAccessToken = memberService.reissueAccessToken(member, refreshTokenRequestDto);
        return ApiResponse.builder()
                .result(newAccessToken)
                .resultMsg("Access token 재발급")
                .resultCode(200)
                .build();
    }

    @GetMapping("")
    @Operation(summary = "전체 유저 조회")
    public ResponseEntity<List<MemberDTO.Response>> getAllMembers() {
        List<MemberDTO.Response> memberResponses = memberService.getAllMembers();
        return ResponseEntity.ok(memberResponses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 유저 조회")
    public ResponseEntity<MemberDTO.Response> getMemberById(@PathVariable Long id) {
        MemberDTO.Response memberResponse = memberService.getMemberById(id);
        return ResponseEntity.ok(memberResponse);
    }
}
