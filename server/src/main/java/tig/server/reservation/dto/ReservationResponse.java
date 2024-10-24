package tig.server.reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import tig.server.enums.ProgramEnum;
import tig.server.enums.Status;
import tig.server.enums.Type;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponse {
    @Schema(type = "integer", example = "2")
    private Integer adultCount;

    @Schema(type = "integer", example = "0")
    private Integer teenagerCount;

    @Schema(type = "integer", example = "0")
    private Integer kidsCount;

    @Schema(type = "string", example = "2007-12-03T10:15:30")
    private String date;

    @Schema(type = "string", example = "2007-12-03T10:15:30")
    private String startTime;

    @Schema(type = "string", example = "2007-12-03T10:15:30")
    private String endTime;

    @Schema(type = "integer", example = "3")
    private Integer gameCount;

    @Schema(type = "integer", example = "30000")
    private Integer price;

    @Schema(type = "string", allowableValues = {"TBC", "CONFIRMED", "CANCELED", "DECLINED", "DONE", "REVIEWED"}, example = "CONFIRMED")
    private Status status;

    @Schema(type = "integer", example = "2")
    private Long memberId;

    @Schema(type = "integer", example = "3")
    private Long clubId;

    @Schema(type = "string", example = "GAME")
    private Type type;

    @Schema(type = "string", example = "티그볼링장")
    private String clubName;

    @Schema(type = "string", example = "02-1234-5678")
    private String clubPhoneNumber;

    @Schema(type = "string", example = "010-1234-5678")
    private String phoneNumber;

    @Schema(type = "string", example = "서울시 동교동 14-2")
    private String clubAddress;

    @Schema(type = "integer", example = "3")
    private Long reservationId;

    @Schema(type = "string", example = "홍길동")
    private String userName;

    @Schema(type = "boolean", example = "true")
    private boolean isReviewed;

    @Schema(type = "string", example = "2313dasd2asasd7d8as7d")
    private String paymentId;

    @Schema(type = "integer", example = "3")
    private Long reviewId;

    @Schema(type = "string", example = "TOSSPAY")
    private String provider;

    @Schema(type = "string", example = "2024-07-23T13:22:36.907525084Z")
    private String updatedAt;

    @Schema(type = "string", example = "잘 부탁 드립니다.")
    private String message;

    @Schema(type = "array", example = "[\"src/club/img1.jpg\", \"src/club/img2.jpg\"]")
    private List<String> imageUrls;

    @Schema(type = "string", example = "서브 프로그램")
    private ProgramEnum programEnum;
}
