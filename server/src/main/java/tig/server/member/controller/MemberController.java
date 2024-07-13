package tig.server.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tig.server.annotation.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tig.server.error.ApiResponse;
import tig.server.member.domain.Member;
import tig.server.member.dto.MemberResponse;
import tig.server.member.dto.RefreshTokenRequestDto;
import tig.server.member.service.MemberService;

import java.util.List;

@Slf4j
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
    public ResponseEntity<List<MemberResponse>> getAllMembers() {
        List<MemberResponse> memberResponses = memberService.getAllMembers();
        return ResponseEntity.ok(memberResponses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 유저 조회")
    public ResponseEntity<MemberResponse> getMemberById(@PathVariable Long id) {
        MemberResponse memberResponse = memberService.getMemberById(id);
        return ResponseEntity.ok(memberResponse);
    }

    @GetMapping("/member")
    @Operation(summary = "로그인 된 사용자 조회")
    public ResponseEntity<Void> getLoginMember(@LoginUser Member member) {
        log.info("[Member name] : {}", member.getName());
        log.info("[Member uniqueId] : {}", member.getUniqueId());
        log.info("[Member email] : {}", member.getEmail());

        return ResponseEntity.ok(null);
    }

    @PatchMapping("/name")
    @Operation(summary = "사용자 이름 변경")
    public ResponseEntity<ApiResponse<MemberResponse>> changeName(@LoginUser Member member,
                                                         @RequestParam("newName") String newName) {
        MemberResponse response = memberService.changeName(member.getId(), newName);
        ApiResponse<MemberResponse> apiResponse = ApiResponse.of(200, "successfully changed name", response);
        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping("/phoneNumber")
    @Operation(summary = "사용자 전화번호 변경")
    public ResponseEntity<ApiResponse<MemberResponse>> changePhoneNumber(@LoginUser Member member,
                                                                @RequestParam("newPhoneNumber") String newPhoneNumber) {
        MemberResponse response = memberService.changePhoneNumber(member.getId(), newPhoneNumber);
        ApiResponse<MemberResponse> apiResponse = ApiResponse.of(200, "successfully changed phone number", response);
        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping("/email")
    @Operation(summary = "사용자 이메일 변경")
    public ResponseEntity<ApiResponse<MemberResponse>> changeEmail(@LoginUser Member member,
                                                                       @RequestParam("newEmail") String newEmail) {
        MemberResponse response = memberService.changeEmail(member.getId(), newEmail);
        ApiResponse<MemberResponse> apiResponse = ApiResponse.of(200, "successfully changed email", response);
        return ResponseEntity.ok(apiResponse);
    }
}
