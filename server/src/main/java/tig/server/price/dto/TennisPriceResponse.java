package tig.server.price.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tig.server.enums.DayOfWeek;
import tig.server.enums.ProgramEnum;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TennisPriceResponse {

    private ProgramEnum programType;  //볼머신 등
    private DayOfWeek dayOfWeek;  // 요일 정보 (평일, 주말)
    private Integer duration;  // 볼머신 이용 시간(분 단위), null 가능, 1회 , 2회
    private Integer price;  // 가격 정보
    private Integer countPerWeek;  // 주 2회
}
