package tig.server.club.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tig.server.club.dto.ClubDTO;
import tig.server.club.service.ClubService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/club")
@Tag(name = "portfolio", description = "업체 API")
public class ClubController {

    private final ClubService clubService;

    @GetMapping("")
    @Operation(summary = "전체 업체 조회")
    public ResponseEntity<List<ClubDTO.Response>> getAllClubs() {
        List<ClubDTO.Response> clubResponses = clubService.getAllClubs();
        return ResponseEntity.ok(clubResponses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 업체 조회")
    public ResponseEntity<ClubDTO.Response> getPortfolioById(@PathVariable Long id) {
        ClubDTO.Response clubResponse = clubService.getClubById(id);
        return ResponseEntity.ok(clubResponse);
    }
}