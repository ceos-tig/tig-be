package tig.server.price.dto;

import lombok.Getter;
import tig.server.enums.DayOfWeek;

import java.time.LocalTime;

@Getter
public class PriceResponse {
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer price;

    public PriceResponse(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, Integer price) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
    }
}
