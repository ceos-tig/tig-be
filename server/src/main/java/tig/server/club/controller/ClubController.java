package tig.server.club.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tig.server.club.dto.ClubRequest;
import tig.server.club.dto.ClubResponse;
import tig.server.club.dto.HomeRequest;
import tig.server.club.dto.HomeResponse;
import tig.server.club.service.ClubService;
import tig.server.error.ApiResponse;

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

    @GetMapping("/{id}")
    @Operation(summary = "특정 업체 조회")
    public ResponseEntity<ApiResponse<ClubResponse>> getClubById(@PathVariable Long id) {
        ClubResponse clubResponse = clubService.getClubById(id);
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

    @GetMapping("/home")
    @Operation(summary = "홈 화면")
    public ResponseEntity<ApiResponse<List<HomeResponse>>> getHomecClubs(@RequestBody HomeRequest homeRequest) {
        HomeResponse homeResponse = clubService.getHomeClubs(homeRequest);
        ApiResponse<List<HomeResponse>> response = ApiResponse.of(200, "successfully retrieved home clubs", List.of(homeResponse));
        return ResponseEntity.ok(response);
    }
}