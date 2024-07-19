package tig.server.search.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchResultDto {
    private List<SearchResponseDto> searchList;
    private Float avgLatitude;
    private Float avgLongitude;

    public SearchResultDto(List<SearchResponseDto> searchList, Float avgLatitude, Float avgLongitude) {
        this.searchList = searchList;
        this.avgLatitude = avgLatitude;
        this.avgLongitude = avgLongitude;
    }
}
