package tig.server.dummy;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


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

    @Getter
    @Setter
    public static class SearchKeyword {
        @Schema(type = "string[]", example = "신촌, 홍대, 상수")
        private String[] searchedPlaces;
    }

    @Getter
    @Setter
    public static class BooleanOnlyResponse {
        @Schema(type = "boolean", example = "true")
        private boolean isSuccess;
    }

    @Getter
    @Setter
    public static class LeisureList {
        @Schema(type = "string", example = "티그볼링장")
        private String leisureName;

        @Schema(type = "string", example = "서울특별시 마포구 동교동")
        private String address;

        @Schema(type = "int", example = "4.6")
        private int rating;

        @Schema(type = "int", example = "20")
        private int reviewCount;

        @Schema(type = "boolean", example = "true")
        private boolean isTimeBased; //true 라면 시간당 게임에 해당됨.

        @Schema(type = "int", example = "32000")
        private int price;

        @Schema(type = "Long", example = "5")
        private Long leisureId;
    }

    @Getter
    @Setter
    public static class LeisureListResponse {
        @Schema(type = "array", implementation = LeisureList.class)
        private List<LeisureList> results;

        @Schema(type = "boolean", example = "true")
        private boolean isAnyData;

        @Schema(type = "boolean", example = "false")
        private boolean isDiscountingNow;
    }

//    @Getter
//    @Setter
//    public static class LeisureInfo {
//        @Schema(type = "string", example = "티그볼링장")
//        private String name;
//
//        @Schema(type = "string", example = "볼링")
//        private String category;
//
//        @Schema(type = "float", example = "4.6")
//        private float rating;
//
//        @Schema(type = "string", example = "서울특별시 마포구 동교동")
//        private String location;
//
//        @Schema(type = "int", example = "20000")
//        private int amount;
//
//        @Schema(type = "string", example = "09:00-21:00")
//        private String hours;
//
//        @Schema(type = "string", example = "010-1234-5678")
//        private String contact;
//
//        @Schema(type = "string", example = "http://example.com")
//        private String website;
//
//        @Schema(type = "string", example = "볼링, 다트, 탁구")
//        private String service;
//    }

    @Getter
    @Setter
    public static class LeisureReview {
        @Schema(type = "string", example = "홍길동")
        private String reviewerName;

        @Schema(type = "LocalDateTime", example = "2023-07-01T14:30:00")
        private LocalDateTime reviewDate;

        @Schema(type = "int", example = "5")
        private int headCount;

        @Schema(type = "float", example = "4.8")
        private float rating;

        @Schema(type = "string", example = "정말 좋은 경험이었습니다!")
        private String reviewContent;
    }

    @Getter
    @Setter
    public static class LeisureDetailResponse {

        @Schema(type = "array", example = "[\"http://example.com/image1.jpg\", \"http://example.com/image2.jpg\"]")
        private List<String> imageUrl;

        @Schema(type = "string", example = "볼링장")
        private String leisureType; // 업장 종류: 볼링장인지, 탁구장인지

        @Schema(type = "string", example = "티그볼링장")
        private String leisureName; // 업장명

        @Schema(type = "float", example = "4.5")
        private float avgRating; // 평균 평점

        @Schema(type = "string", example = "서울특별시 마포구 동교동")
        private String location; // 위치

        @Schema(type = "boolean", example = "true")
        private boolean isTimeBased; // true 라면 시간당

        @Schema(type = "string", example = "09:00")
        private String startTime; // 운영시간 시작

        @Schema(type = "string", example = "21:00")
        private String endTime; // 운영시간 종료

        @Schema(type = "string", example = "010-1234-5678")
        private String phoneNumber; // 전화번호

        @Schema(type = "string", nullable = true, example = "http://example.com/sns")
        private String snsAddress; // SNS 주소 (nullable)

        @Schema(type = "array", example = "[\"무료 Wi-Fi\", \"주차장\"]")
        private List<String> facilities; // 편의시설 또는 서비스

        @Schema(type = "number", example = "4.3")
        private float avgReviewRating; // 리뷰 평균 평점

        @Schema(type = "array", implementation = LeisureReview.class)
        private List<LeisureReview> reviewList; // 리뷰 리스트
    }

    @Getter
    @Setter
    public static class ReservationRequest {

        @Schema(type = "Date", example = "2023-07-01T14:30:00")
        private LocalDateTime reservingDate; // 예약 날짜

        @Schema(type = "string", example = "14:00")
        private String startTime; // 시작 시간

        @Schema(type = "string", example = "16:00")
        private String endTime; // 종료 시간

        @Schema(type = "integer", example = "2")
        private int adultCount; // 어른 숫자

        @Schema(type = "integer", example = "1")
        private int youngManCount; // 청소년 숫자

        @Schema(type = "integer", example = "1")
        private int kidCount; // 어린이 숫자

        @Schema(type = "number", example = "50000")
        private float totalCost; // 총 비용

        @Schema(type = "string", example = "010-1234-5678")
        private String phoneNumber; // 확인 문자 받을 전화번호
    }

    @Getter
    @Setter
    public static class ReservationResponse{
        @Schema(type = "boolean", example = "true")
        private boolean isSuccess;
    }

    @Getter
    @Setter
    public static class TimeSelectResponse{
        @Schema(type = "array", example = "10-12")
        private List<String> notAvailableTimes;
    }

    @Getter
    @Setter
    public static class TimeSelectRequest{
        @Schema(type = "array", example = "5")
        private Long leisureId;

        @Schema(type = "Date", example = "2023-07-01T14:30:00")
        private LocalDateTime date;
    }

    @Getter
    @Setter
    public static class LoginUserResponse { // 근데 이건 커스텀어노테이션으로 해결할 것 같아서 필요할지는 모르겠음
        @Schema(type = "string", example = "김티그")
        private String username;

        @Schema(type = "string", example = "010-1234-5678")
        private String phoneNumber;
    }

    @Getter
    @Setter
    public static class ReviewReservationResponse {
        @Schema(type = "string", example = "티그볼링장")
        private String leisureName;

        @Schema(type = "string", example = "서울특별시 마포구 동교동")
        private String address;

        @Schema(type = "string", example = "2023-07-01T14:30:00")
        private String date;

        @Schema(type = "string", example = "09:00")
        private String startTime; // 운영시간 시작

        @Schema(type = "string", example = "21:00")
        private String endTime; // 운영시간 종료

        @Schema(type = "int", example = "5")
        private int headCount; // 예약 인원수
    }

    @Getter
    @Setter
    public static class ReviewResponse {
        @Schema(type = "string", example = "티그볼링장")
        private String leisureName;

        @Schema(type = "string", example = "서울특별시 마포구 동교동")
        private String address;

        @Schema(type = "string", example = "2023-07-01T14:30:00")
        private String date;

        @Schema(type = "string", example = "09:00")
        private String startTime; // 운영시간 시작

        @Schema(type = "string", example = "21:00")
        private String endTime; // 운영시간 종료

        @Schema(type = "int", example = "5")
        private int headCount; // 예약 인원수

        @Schema(type = "string", example = "홍길동")
        private String username; // 사용자 이름

        @Schema(type = "int", example = "4")
        private int rating; // 사용자 이름

        @Schema(type = "string", example = "너무 좋았어요!")
        private String reviewContent; // 사용자 이름
    }

    @Getter
    @Setter
    public static class LeisureType {

        @Schema(type = "string", example = "티그볼링장")
        private String name; // 업체명

        @Schema(type = "string", example = "서울특별시 마포구 동교동")
        private String address; // 주소

        @Schema(type = "float", example = "4.5")
        private float rating; // 평점

        @Schema(type = "int", example = "120")
        private int reviewCount; // 리뷰 수

        @Schema(type = "boolean", example = "true")
        private boolean isTimeBased; // 시간당인지 게임당인지

        @Schema(type = "int", example = "30000")
        private int price; // 가격

        @Schema(type = "Long", example = "1234")
        private Long leisureId; // 업체 ID
    }

    @Getter
    @Setter
    public static class WishlistResponse {
        @Schema(type = "array", implementation = LeisureType.class)
        private List<LeisureType> results; // 결과 리스트
    }

    @Getter
    @Setter
    public static class UserInfoResponse { // 근데 이건 커스텀어노테이션으로 해결할 것 같아서 필요할지는 모르겠음
        @Schema(type = "string", example = "김티그")
        private String username;

        @Schema(type = "string", example = "010-1234-5678")
        private String phoneNumber;

        @Schema(type = "string", example = "abc@gmail.com")
        private String email;

        @Schema(type = "string", example = "abc.jpg")
        private String profileImgUrl;
    }

    @Getter
    @Setter
    public static class UsernameRequest {
        @Schema(type = "string", example = "김티그")
        private String username;
    }

    @Getter
    @Setter
    public static class PhoneNumberRequest {
        @Schema(type = "string", example = "010-1234-5678")
        private String phoneNumber;
    }

    @Getter
    @Setter
    public static class EmailRequest {
        @Schema(type = "string", example = "abc@gmail.com")
        private String email;
    }

    @Getter
    @Setter
    public static class ReservationReviewDetails {
        @Schema(type = "Long", example = "10")
        private Long reservationId;

        @Schema(type = "string", example = "예약확정")
        private String status; // TODO: ENUM으로 바꾸어야 함.

        @Schema(type = "string", example = "티그볼링장")
        private String leisureName;

        @Schema(type = "string", example = "서울특별시 마포구 동교동")
        private String address;

        @Schema(type = "date", example = "2023-07-01T14:30:00")
        private LocalDateTime date;

        @Schema(type = "string", example = "9:00")
        private String startTime;

        @Schema(type = "string", example = "12:00")
        private String endTime;

        @Schema(type = "int", example = "12:00")
        private int headCount;

        @Schema(type = "boolean", example = "true")
        private boolean reviewWritten;

        @Schema(type = "Long", example = "5")
        private Long reviewId;

    }

    @Getter
    @Setter
    public static class ReservationReviewDetailsResponse {
        @Schema(type = "array", implementation = ReservationReviewDetails.class)
        private List<ReservationReviewDetails> reservationReviewDetails;
    }

    @Getter
    @Setter
    public static class ReservationDetails {

        @Schema(type = "string", example = "티그볼링장")
        private String leisureName; // 업체명

        @Schema(type = "string", example = "서울특별시 마포구 동교동")
        private String address; // 주소

        @Schema(type = "date", example = "2023-07-01T14:30:00")
        private LocalDateTime date; // 예약날짜

        @Schema(type = "string", example = "14:00")
        private String startTime; // 예약시간start

        @Schema(type = "string", example = "16:00")
        private String endTime; // 예약시간end

        @Schema(type = "number", example = "4")
        private int headCount; // 예약인원

        @Schema(type = "Long", example = "14")
        private Long reservationId; // 예약번호

        @Schema(type = "string", example = "정기민")
        private String reserverName; // 예약자

        @Schema(type = "string", example = "010-1234-5678")
        private String phoneNumber; // 연락처

        @Schema(type = "date", example = "2023-07-10T15:30:00")
        private LocalDateTime paymentDateTime; // 결제일시

        @Schema(type = "string", example = "KakaoPay")
        private String paymentMethod; // 결제수단

        @Schema(type = "number", example = "50000")
        private int paymentAmount; // 예약금액

        @Schema(type = "number", example = "5000")
        private int serviceFee; // 수수료

        @Schema(type = "number", example = "10000")
        private int couponDiscount; // 쿠폰할인

        @Schema(type = "number", example = "45000")
        private int totalPaymentAmount; // 총결제금액

        @Schema(type = "string", format = "date", example = "2023-07-14")
        private String cancellationDeadline; // 예약취소가능날짜
    }

}
