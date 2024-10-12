package tig.server.price.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tig.server.enums.ProgramEnum;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FootballPriceResponse {

    private ProgramEnum programName;  // 축구장 면 또는 프로그램 (정규수업, 레슨 등)
    private Integer duration;  // 시간 단위 (예: 1시간, 2시간 등)
    private Integer price;  // 해당 단위 시간의 가격
}
