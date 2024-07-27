package tig.server.search.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchResultDto {
    private List<SearchResponseDto> searchList;
    private Float avgLatitude;
    private Float avgLongitude;
    private Boolean isResult;

    public SearchResultDto(List<SearchResponseDto> searchList, Float avgLatitude, Float avgLongitude, Boolean isResult) {
        this.searchList = searchList;
        this.avgLatitude = avgLatitude;
        this.avgLongitude = avgLongitude;
        this.isResult = isResult;
    }
}
