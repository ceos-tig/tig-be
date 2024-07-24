package tig.server.search.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tig.server.annotation.LoginUser;
import tig.server.global.response.ApiResponse;
import tig.server.member.domain.Member;
import tig.server.search.domain.SearchLog;
import tig.server.search.dto.SearchLogDto;
import tig.server.search.service.SearchLogService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/search/logs")
public class SearchLogController {
    private final SearchLogService searchLogService;

    @Operation(summary = "최근 검색 기록: 조회")
    @GetMapping("")
    public ResponseEntity<ApiResponse<List<SearchLogDto>>> findRecentSearchLog(@LoginUser Member member) {
        List<SearchLogDto> searchLogDtoList = searchLogService.findRecentSearchLogs(member.getId());
        ApiResponse<List<SearchLogDto>> response = ApiResponse.of(200, "successfully retrieved recent search logs", searchLogDtoList);
        return ResponseEntity.ok(response);
    }
}
