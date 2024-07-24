package tig.server.search.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class SearchLogDto {
    private String name;
    private String createdAt;

    public static SearchLogDto fromLog(String name, String createdAt){
        return new SearchLogDto(name, createdAt);
    }
}
