package tig.server.club.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tig.server.annotation.LoginUser;
import tig.server.club.dto.ClubRequest;
import tig.server.club.dto.ClubResponse;
import tig.server.club.dto.HomeRequest;
import tig.server.club.dto.HomeResponse;
import tig.server.club.service.ClubService;
import tig.server.global.response.ApiResponse;
import tig.server.member.domain.Member;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/club")
@Tag(name = "club", description = "업체 API")
public class ClubController {

    private final ClubService clubService;

    @GetMapping("")
    @Operation(summary = "전체 업체 조회")
    public ResponseEntity<ApiResponse<List<ClubResponse>>> getAllClubs() {
        List<ClubResponse> clubResponses = clubService.getAllClubs();
        ApiResponse<List<ClubResponse>> response = ApiResponse.of(200, "successfully retrieved all clubs", clubResponses);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{clubId}")
    @Operation(summary = "특정 업체 조회 : 로그인 된 사용자를 위함")
    public ResponseEntity<ApiResponse<ClubResponse>> getClubById(@LoginUser Member member, @PathVariable Long clubId) {
        ClubResponse clubResponse = clubService.getClubByIdForLoginUser(member.getId(), clubId);
        ApiResponse<ClubResponse> response = ApiResponse.of(200, "successfully retrieved club", clubResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/guest/{clubId}")
    @Operation(summary = "특정 업체 조회 : 로그인 안된 사용자를 위함")
    public ResponseEntity<ApiResponse<ClubResponse>> getClubByIdForLoginUser(@PathVariable Long clubId) {
        ClubResponse clubResponse = clubService.getClubById(clubId);
        ApiResponse<ClubResponse> response = ApiResponse.of(200, "successfully retrieved club", clubResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping("")
    @Operation(summary = "업체 업로드")
    public ResponseEntity<ApiResponse<ClubResponse>> createClub(@RequestBody ClubRequest clubRequest) {
        ClubResponse createdClub = clubService.createClub(clubRequest);
        ApiResponse<ClubResponse> response = ApiResponse.of(200, "successfully added club", createdClub);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/home")
    @Operation(summary = "홈 화면")
    public ResponseEntity<ApiResponse<List<HomeResponse>>> getHomecClubs(@RequestBody HomeRequest homeRequest) {
        HomeResponse homeResponse = clubService.getHomeClubs(homeRequest);
        ApiResponse<List<HomeResponse>> response = ApiResponse.of(200, "successfully retrieved home clubs", List.of(homeResponse));
        return ResponseEntity.ok(response);
    }
}