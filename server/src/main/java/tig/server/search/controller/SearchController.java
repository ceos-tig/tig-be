package tig.server.search.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tig.server.annotation.LoginUser;
import tig.server.enums.MajorCategory;
import tig.server.global.response.ApiResponse;
import tig.server.member.domain.Member;
import tig.server.packageSet.service.PackageSetService;
import tig.server.search.dto.PackageSetSearchResultDto;
import tig.server.search.dto.SearchResultDto;
import tig.server.search.service.SearchService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
public class SearchController {
    private final SearchService searchService;
    private final PackageSetService packageSetService;

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<SearchResultDto>> search(@LoginUser Member member,
                                                               @RequestParam("search") String request,
                                                               @RequestParam("isKeyword") boolean isKeyword) {
        String passRequest = request.replaceAll("\\p{Z}", "");
        if (passRequest.endsWith("/")) {
            passRequest = passRequest.substring(0, passRequest.length() - 1);
        }
        SearchResultDto clubList = searchService.findClubByNameContain(member.getId(), passRequest, isKeyword);
        ApiResponse<SearchResultDto> response = ApiResponse.of(200, "successfully searched!", clubList);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/guest")
    public ResponseEntity<ApiResponse<SearchResultDto>> searchIfNotLogin(@RequestParam("search") String request) {
        String passRequest = request.replaceAll("\\p{Z}", "");
        if (passRequest.endsWith("/")) {
            passRequest = passRequest.substring(0, passRequest.length() - 1);
        }
        SearchResultDto clubList = searchService.findClubByNameContainIfNoLogin(passRequest);
        ApiResponse<SearchResultDto> response = ApiResponse.of(200, "successfully searched!", clubList);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/package/user")
    public ResponseEntity<ApiResponse<PackageSetSearchResultDto>> searchPackage(@LoginUser Member member,
                                                               @RequestParam("search") String request,
                                                               @RequestParam("isKeyword") boolean isKeyword) {
        String passRequest = request.replaceAll("\\p{Z}", "");
        if (passRequest.endsWith("/")) {
            passRequest = passRequest.substring(0, passRequest.length() - 1);
        }
        PackageSetSearchResultDto packageByNameContain = packageSetService.findPackageByNameContain(member.getId(), passRequest, isKeyword);
        ApiResponse<PackageSetSearchResultDto> response = ApiResponse.of(200, "successfully searched!", packageByNameContain);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/package/guest")
    public ResponseEntity<ApiResponse<PackageSetSearchResultDto>> searchPackageIfNotLogin(@RequestParam("search") String request) {
        String passRequest = request.replaceAll("\\p{Z}", "");
        if (passRequest.endsWith("/")) {
            passRequest = passRequest.substring(0, passRequest.length() - 1);
        }
        PackageSetSearchResultDto packageByNameContain = packageSetService.findClubByNameContainIfNoLogin(passRequest);
        ApiResponse<PackageSetSearchResultDto> response = ApiResponse.of(200, "successfully searched!", packageByNameContain);
        return ResponseEntity.ok(response);
    }

}
