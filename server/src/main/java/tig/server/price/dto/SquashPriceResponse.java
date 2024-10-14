package tig.server.price.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tig.server.enums.ProgramEnum;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SquashPriceResponse {

    private ProgramEnum programName;  // 원데이클래스, 스쿼시레슨, 주1회 등
    private Integer durationInMonths;  // 기간 (개월 단위, null 가능)
    private Integer price;  // 해당 프로그램 가격
    private Integer lessonCount; // 레슨 1회, 2회
}
