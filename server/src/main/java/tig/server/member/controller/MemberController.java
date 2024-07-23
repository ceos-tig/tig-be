package tig.server.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import tig.server.annotation.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tig.server.global.response.ApiResponse;
import tig.server.global.exception.BusinessExceptionHandler;
import tig.server.global.code.ErrorCode;
import tig.server.member.domain.Member;
import tig.server.member.dto.MemberResponse;
import tig.server.member.dto.RefreshTokenResponseDto;
import tig.server.member.mapper.MemberMapper;
import tig.server.member.service.MemberService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
@Tag(name = "member", description = "유저(멤버) API")
public class MemberController {
    private final MemberService memberService;
    private final MemberMapper memberMapper = MemberMapper.INSTANCE;

    /**
     * refresh token을 통한 access token 재발급
     */
    @PostMapping("/reissue")
    @Operation(summary = "리프레시 토큰을 통한 AT,RT 재발급")
    public ResponseEntity<ApiResponse<RefreshTokenResponseDto>> reissueAccessToken(@CookieValue(value = "refreshToken", required = false) String refreshToken,
                                                                                   HttpServletResponse response) {
        if (refreshToken == null) {
            throw new BusinessExceptionHandler("No refresh token found in cookies", ErrorCode.BAD_REQUEST_ERROR);
        }
        RefreshTokenResponseDto refreshTokenResponseDto = memberService.reissueAccessToken(refreshToken);
        ApiResponse<RefreshTokenResponseDto> resultResponse = ApiResponse.of(200, "successfully reissued Access Token & Refresh Token", refreshTokenResponseDto);

        // Refresh Token 쿠키 설정
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshTokenResponseDto.getRefreshToken())
                .httpOnly(true)
                .path("/")
                .secure(true) // HTTPS를 사용할 경우에만 true로 설정
                .maxAge(14 * 24 * 60 * 60) // 2주
                .sameSite("None")
                .build();

        // 쿠키를 응답 헤더에 추가
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        return ResponseEntity.ok(resultResponse);
    }

    @GetMapping("")
    @Operation(summary = "전체 유저 조회")
    public ResponseEntity<ApiResponse<List<MemberResponse>>> getAllMembers() {
        List<MemberResponse> memberResponses = memberService.getAllMembers();
        ApiResponse<List<MemberResponse>> response = ApiResponse.of(200, "successfully retrieved all members", memberResponses);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{memberId}")
    @Operation(summary = "admin : 특정 유저 조회")
    public ResponseEntity<ApiResponse<MemberResponse>> getMemberById(@PathVariable Long memberId) {
        MemberResponse memberResponse = memberService.getMemberById(memberId);
        ApiResponse<MemberResponse> response = ApiResponse.of(200, "successfully retrieved member", memberResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/member")
    @Operation(summary = "로그인 된 사용자 조회")
    public ResponseEntity<ApiResponse<MemberResponse>> getLoginMember(@LoginUser Member member) {
        log.info("[Member name] : {}", member.getName());
        log.info("[Member uniqueId] : {}", member.getUniqueId());
        log.info("[Member email] : {}", member.getEmail());
        MemberResponse memberResponse = memberMapper.entityToResponse(member);
        ApiResponse<MemberResponse> response = ApiResponse.of(200, "Current logged in member info", memberResponse);

        return ResponseEntity.ok(response);
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
