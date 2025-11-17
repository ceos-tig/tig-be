package tig.server.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tig.server.packageSet.dto.PackageSetResultDto;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageSetSearchResultDto {
    private List<PackageSetResultDto> searchList;
    private Boolean isResult;

    public static PackageSetSearchResultDto of(List<PackageSetResultDto> searchList, Boolean isResult) {
        return PackageSetSearchResultDto.builder()
                .searchList(searchList)
                .isResult(isResult)
                .build();
    }
}
