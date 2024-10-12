package tig.server.price.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tig.server.enums.ProgramEnum;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TableTennisPriceResponse {
    private ProgramEnum programName;  // 일반 탁구, 스쿼시 등
    private Integer price;  // 가격
    private Integer durationType; // 30분, 1시간
}
