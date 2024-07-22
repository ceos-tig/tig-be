package tig.server.search.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tig.server.annotation.LoginUser;
import tig.server.global.response.ApiResponse;
import tig.server.member.domain.Member;
import tig.server.search.dto.SearchResultDto;
import tig.server.search.service.SearchService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
public class SearchController {
    private final SearchService searchService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<SearchResultDto>> search(@LoginUser Member member,
                                                    @RequestParam("search") String request) {
        String passRequest = request.replaceAll("\\p{Z}", "");
        if (passRequest.endsWith("/")) {
            passRequest = passRequest.substring(0, passRequest.length() - 1);
        }
        SearchResultDto clubList = searchService.findClubByNameContain(member.getId(), passRequest);
        ApiResponse<SearchResultDto> response = ApiResponse.of(200, "successfully searched!", clubList);
        return ResponseEntity.ok(response);
    }
}
