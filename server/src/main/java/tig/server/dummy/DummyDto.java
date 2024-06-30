package tig.server.dummy;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


public class DummyDto {

    @Getter
    @Setter
    public static class LeisureRecommendation {

        @Schema(type = "string", example = "티그볼링장")
        private String leisureName;

        @Schema(type = "integer", example = "14")
        private Integer leisureId;

        @Schema(type = "integer", example = "1")
        private Integer categoryCode;

        @Schema(type = "integer", example = "1012214")
        private Integer imageId;
    }

    @Getter
    @Setter
    public static class EventLeisure {

        @Schema(type = "string", example = "티그볼링장")
        private String leisureName;

        @Schema(type = "integer", example = "14")
        private Integer leisureId;

        @Schema(type = "integer", example = "1")
        private Integer categoryCode;

        @Schema(type = "integer", example = "1012214")
        private Integer imageId;
    }
}
