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
public class BallingPriceResponse {

    private ProgramEnum programName;  // 일반 볼링, 락 볼링 등
    private DayOfWeek dayOfWeek;  // 요일 정보 (MON, TUE 등)
    private LocalTime startTime;  // 시작 시간
    private LocalTime endTime;  // 종료 시간
    private Integer price;  // 해당 시간의 가격
}
