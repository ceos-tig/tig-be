package tig.server.operatinghours.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import tig.server.enums.DayOfWeek;

import java.time.LocalTime;

@Getter
public class OperatingHoursResponse {
    private DayOfWeek dayOfWeek;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime endTime;

    public OperatingHoursResponse(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
