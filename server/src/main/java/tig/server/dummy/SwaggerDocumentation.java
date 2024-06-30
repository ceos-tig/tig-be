package tig.server.dummy;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.*;

@RestController
@RequestMapping("/api/v0/dummy")
@Tag(name = "Dummy", description = "Dummy API")
public class SwaggerDocumentation {

    @PostMapping("/sports/recommend")
    @Operation(summary = "사용자 위치 기반 스포츠 시설 추천")
    @Parameters({
            @Parameter(name = "longtitude", example = "위도", schema = @Schema(type = "float"), required = true),
            @Parameter(name = "latitude", example = "경도", schema = @Schema(type = "float"),  required = true),
    })
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "성공",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = DummyDto.LeisureRecommendation.class)))
    })
    public void getLeisureRecommendation() {}


    @GetMapping("/sports/event")
    @Operation(summary = "이벤트 중인 스포츠 조회")
//    @Parameters({
//            @Parameter(name = "longtitude", example = "위도", schema = @Schema(type = "float"), required = true),
//            @Parameter(name = "latitude", example = "경도", schema = @Schema(type = "float"),  required = true),
//    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DummyDto.EventLeisure.class)))
    })
    public void getEventSports() {}
}
