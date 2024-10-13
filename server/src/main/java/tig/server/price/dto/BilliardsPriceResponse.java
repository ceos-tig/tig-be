package tig.server.price.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tig.server.enums.ProgramEnum;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BilliardsPriceResponse {

    private ProgramEnum programName;  // 중대, 대대, 3구, 4구, 포켓볼 등
    private Integer duration;  // 시간 단위 (예: 10분, 1시간)
    private Integer price;  // 해당 단위 시간의 가격
}
