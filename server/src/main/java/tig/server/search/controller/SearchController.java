package tig.server.search.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tig.server.error.ApiResponse;
import tig.server.search.dto.SearchResultDto;
import tig.server.search.service.SearchService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
public class SearchController {
    private final SearchService searchService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<SearchResultDto>> search(/*@LoginUser Member member,*/
                                                    @RequestParam("search") String request) {
        SearchResultDto clubList = searchService.findClubByNameContain(1L, request);
        ApiResponse<SearchResultDto> response = ApiResponse.of(200, "successfully searched!", clubList);
        return ResponseEntity.ok(response);
    }
}
