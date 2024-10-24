package tig.server.price.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tig.server.enums.ProgramEnum;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BaseballPriceResponse {

    private ProgramEnum programType;  // 메이저룸, 마이너룸, 몇 회
    private Integer inning;  // 몇 이닝
    private Integer duration;  // 시간 단위 (예: 60분, 30분 등)
    private LocalTime startTime; // 시작 시간
    private LocalTime endTime; // 종료 시간
    private Integer price;  // 해당 시간에 따른 가격
}
