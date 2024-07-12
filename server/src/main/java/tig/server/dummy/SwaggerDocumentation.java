//package tig.server.dummy;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.Parameters;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.constraints.Email;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import lombok.*;
//
//@RestController
//@RequestMapping("/api/v0/dummy")
//@Tag(name = "Dummy", description = "Dummy API")
//public class SwaggerDocumentation {
//
//    /**
//     * 홈 화면 API
//     * */
//    @PostMapping("/sports/recommend")
//    @Operation(summary = "사용자 위치 기반 스포츠 시설 추천")
//    @Parameters({
//            @Parameter(name = "longtitude", example = "위도", schema = @Schema(type = "float"), required = true),
//            @Parameter(name = "latitude", example = "경도", schema = @Schema(type = "float"),  required = true),
//    })
//    @ApiResponses(value = {
//        @ApiResponse(
//                responseCode = "200",
//                description = "성공",
//                content = @Content(
//                        mediaType = "application/json",
//                        schema = @Schema(implementation = DummyDto.LeisureRecommendation.class)))
//    })
//    public void getLeisureRecommendation() {}
//
//
//    @GetMapping("/sports/event")
//    @Operation(summary = "이벤트 중인 스포츠 조회")
////    @Parameters({
////            @Parameter(name = "longtitude", example = "위도", schema = @Schema(type = "float"), required = true),
////            @Parameter(name = "latitude", example = "경도", schema = @Schema(type = "float"),  required = true),
////    })
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "성공",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = DummyDto.EventLeisure.class)))
//    })
//    public void getEventSports() {}
//
//
//    /**
//     * 검색 화면 API
//     * */
//    @PostMapping("/search")
//    @Operation(summary = "검색 바 터치 시 나오는 키워드")
//    @Parameters({
//            @Parameter(name = "accessToken", example = "ey298dns9..", schema = @Schema(type = "string"), required = true),
//    })
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "성공",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = DummyDto.SearchKeyword.class)))
//    })
//    public void searchBarKeyword() {}
//
//    @DeleteMapping("/searchedList")
//    @Operation(summary = "검색했던 리스트 '전체삭제' 버튼을 눌렀을 때")
//    @Parameters({
//            @Parameter(name = "accessToken", example = "ey298dns9..", schema = @Schema(type = "string"), required = true),
//    })
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "성공",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = DummyDto.BooleanOnlyResponse.class)))
//    })
//    public void deleteSearchedList() {}
//
//    //TODO : 에러가 발생했을때 무슨 에러인지 넘겨주어야 한다. ex) 403은 무슨 에러에요? 이런 질문 안받도록
//
//    @PostMapping("/leisure/like")
//    @Operation(summary = "업체에 좋아요(하트) 클릭")
//    @Parameters({
//            @Parameter(name = "leisureId", schema = @Schema(type = "string"), required = true),
//    })
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "성공",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = DummyDto.BooleanOnlyResponse.class)))
//    })
//    public void pressLikeToLeisure() {}
//
//    @GetMapping("/leisureList")
//    @Operation(summary = "업체 리스트 조회")
//    @Parameters({
//            @Parameter(name = "categoryName", example = "종목 명", schema = @Schema(type = "string"), required = true),
//            @Parameter(name = "longtitude", example = "위도", schema = @Schema(type = "float"), required = true),
//            @Parameter(name = "latitude", example = "경도", schema = @Schema(type = "float"),  required = true),
//    })
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "성공",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = DummyDto.LeisureListResponse.class)))
//    })
//    public void getLeisureList() {}
//
//
//    @GetMapping("/recommendation")
//    @Operation(summary = "'이런 곳은 어때요?' 를 위한 API")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "성공",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = DummyDto.LeisureListResponse.class)))
//    })
//    public void getRecommendationList() {}
//
//    @GetMapping("/{leisureId}/details")
//    @Operation(summary = "업체의 상세페이지 조회")
////    @Parameters({
////            @Parameter(name = "leisureId", example = "업체 아이디", schema = @Schema(type = "Long"), required = true),
////    })
////  쿼리 파라미터로 업체 아이디를 넘겨주신다 함
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "성공",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = DummyDto.LeisureDetailResponse.class)))
//    })
//    public void getLeisureDetails(@PathVariable("leisureId") Long leisureId) {}
//
//
//    @PostMapping("/reservation/time-based")
//    @Operation(summary = "시간당 레저스포츠 예약")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "성공",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = DummyDto.ReservationResponse.class)))
//    })
//    public void timeBasedReservation(@RequestBody DummyDto.ReservationRequest reservationRequest) {}
//
//    @PostMapping("/reservation/time-based/timeselect")
//    @Operation(summary = "시간당 레저스포츠 예약에서 시간 선택")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "성공",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = DummyDto.TimeSelectResponse.class)))
//    })
//    public void timeBasedReservationSelectTime(@RequestBody DummyDto.TimeSelectRequest timeSelectRequest) {}
//
//    @PostMapping("/reservation/game-based")
//    @Operation(summary = "게임당 레저스포츠 예약")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "성공",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = DummyDto.ReservationResponse.class)))
//    })
//    public void gameBasedReservation(@RequestBody DummyDto.ReservationRequest reservationRequest) {}
//
//
//
//    /**
//     * 결제 화면 API
//     * */
//    @GetMapping("/user-info")
//    @Operation(summary = "현재 로그인 된 사용자 정보 조회")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "성공",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = DummyDto.LoginUserResponse.class)))
//    })
//    public void getLoginUser() {}
//
//
//    /**
//     * 리뷰 화면 API
//     * */
//    @PostMapping("/{reservationId}/review")
//    @Operation(summary = "예약한 내역에 대한 리뷰 작성")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "성공",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = DummyDto.ReviewReservationResponse.class)))
//    })
//    public void review(@PathVariable("reservationId") Long reservationId) {}
//
//    @GetMapping("/{reviewId}") // req에는 reviewId 와 leisureId 가 모두 필요하다고 써있는데, 리뷰 내역 안에 업체아이디가 있으니까 후자는 없어도 되지 않을 까 생각
//    @Operation(summary = "작성한 리뷰 확인 화면")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "성공",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = DummyDto.ReviewResponse.class)))
//    })
//    public void getReview(@PathVariable("reservationId") Long reservationId) {}
//
//    /* 예약 내역 API 는 좀 많아서 지금 못끝낼 것 같아서 위시 리스트 부터 진행 할게요! */
//    /**
//     * 예약 내역 API
//     * */
//
//    @GetMapping("/reservationList")
//    @Operation(summary = "마이페이지 내의 예약 내역 화면 조회")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "성공",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = DummyDto.ReservationReviewDetailsResponse.class)))
//    })
//    public void getReservationList() {}
//
//
//    @GetMapping("/review/{reviewId}")
//    @Operation(summary = "마이페이지 내의 작성한 리뷰 조회")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "성공",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = DummyDto.ReviewResponse.class)))
//    })
//    public void getReviewList(@PathVariable("reviewId") Long reviewId) {}
//
//
//    @GetMapping("/reservationList/{reservationId}")
//    @Operation(summary = "마이페이지 내의 예약 상세 조회")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "성공",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = DummyDto.ReservationDetails.class)))
//    })
//    public void getReservationDetails(@PathVariable("reservationId") Long reservationId) {}
//
//    @PatchMapping("/reservationList/{reservationId}")
//    @Operation(summary = "예약내역 페이지에서 예약 취소")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "성공",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = DummyDto.ReservationDetails.class)))
//    })
//    public void cancleReservation(@PathVariable("reservationId") Long reservationId) {}
//
//    /**
//     * 위시 리스트 API
//     * */
//    @GetMapping("/wishlist")
//    @Operation(summary = "위시리스트 페이지")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "성공",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = DummyDto.WishlistResponse.class)))
//    })
//    public void getWishlist() {}
//
//    @PostMapping("/{leisureId}/wishlist/remove") // 일단 POST로 해두었지만, DELETE가 맞지 않을까..?
//    @Operation(summary = "위시리스트 페이지에서 제거")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "성공" // 실패시, 실패 이유 리턴해주는 로직도 고민해보아야 함
//            )
//    })
//    public void deleteWishlist(@PathVariable("leisureId") Long leisureId) {}
//
//
//    /**
//     * 마이 페이지 API
//     * */
//
//    @DeleteMapping("/refresh-token")
//    @Operation(summary = "리프레시 토큰 제거")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "성공"
//            )
//    })
//    public void removeRefreshToken() {}
//
//    @PatchMapping("/rename")
//    @Operation(summary = "사용자 이름 변경")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "성공",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = DummyDto.UserInfoResponse.class))) // 일단 사용자가 이름만 바꾸어도 이름, 번호, 이메일, 프로필 이미지 까지 모두 프론트로 넘겨주는 걸로 함.
//    })
//    public void changeUsername(@RequestBody DummyDto.UsernameRequest usernameRequest) {}
//
//    @PatchMapping("/phoneNumber")
//    @Operation(summary = "사용자 전화 번호 변경")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "성공",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = DummyDto.UserInfoResponse.class))) // 일단 사용자가 이름만 바꾸어도 이름, 번호, 이메일, 프로필 이미지 까지 모두 프론트로 넘겨주는 걸로 함.
//    })
//    public void changePhoneNumber(@RequestBody DummyDto.PhoneNumberRequest phoneNumberRequest) {}
//
//    @PatchMapping("/email")
//    @Operation(summary = "사용자 이메일 변경")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "성공",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(implementation = DummyDto.UserInfoResponse.class))) // 일단 사용자가 이름만 바꾸어도 이름, 번호, 이메일, 프로필 이미지 까지 모두 프론트로 넘겨주는 걸로 함.
//    })
//    public void changeEmail(@RequestBody DummyDto.EmailRequest emailRequest) {}
//
//}
