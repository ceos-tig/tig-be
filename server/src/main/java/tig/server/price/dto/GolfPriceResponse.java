package tig.server.price.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tig.server.enums.DayOfWeek;
import tig.server.enums.ProgramEnum;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GolfPriceResponse {

    private ProgramEnum programName;  // 일일 이용권 등
    private DayOfWeek dayOfWeek;  // 요일 정보 (MON, TUE, ...)
    private LocalTime startTime;  // 시작 시간
    private LocalTime endTime;  // 종료 시간
    private Integer price;  // 가격
    private Integer holes; // 9홀 ,18홀
    private Integer duration; // 60분, 90분
}
