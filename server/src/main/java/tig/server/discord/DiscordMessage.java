package tig.server.discord;

import tig.server.enums.Type;
import tig.server.reservation.dto.ReservationResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public record DiscordMessage(
        String content
) {
    public static DiscordMessage createApplicationMessage(String message, ReservationResponse reservationResponse) throws ParseException {
        String memberName = reservationResponse.getUserName();
        String clubName = reservationResponse.getClubName();

        return new DiscordMessage("\n--------------------------------------------\n"
                + "| " + message + "\n"
                + "| " + memberName + " 님이 " + clubName + " 업체를 예약했습니다.\n"
                + "| " + "Reservation ID: " + reservationResponse.getReservationId() + "\n"
                + "| " + "예약 날짜: " + changeToDateFormat(reservationResponse.getStartTime()) + "\n"
                + "| " + "예약 시작 시간: " + changeToTimeFormat(reservationResponse.getStartTime()) + "\n"
                + "| " + getEndTime(reservationResponse) + "\n"
                + "| " + "성인: " + reservationResponse.getAdultCount() + "명, 청소년: " + reservationResponse.getTeenagerCount() + "명, 어린이: " + reservationResponse.getKidsCount() + "명\n"
                + "| " + "메세지: " + reservationResponse.getMessage() + "\n"
                + "| " + "결제 ID: " + reservationResponse.getPaymentId() + "\n"
                + "--------------------------------------------\n");
    }

    public static DiscordMessage createCancelMessage(String message, ReservationResponse reservationResponse) throws ParseException {
        String memberName = reservationResponse.getUserName();
        String clubName = reservationResponse.getClubName();

        return new DiscordMessage("\n--------------------------------------------\n"
                + "| " + message + "\n"
                + "| " + memberName + " 님이 " + clubName + " 업체예약을 취소했습니다.\n"
                + "| " + "Reservation ID: " + reservationResponse.getReservationId() + "\n"
                + "| " + "예약 날짜: " + changeToDateFormat(reservationResponse.getStartTime()) + "\n"
                + "| " + "예약 시작 시간: " + changeToTimeFormat(reservationResponse.getStartTime()) + "\n"
                + "| " + getEndTime(reservationResponse) + "\n"
                + "| " + "성인: " + reservationResponse.getAdultCount() + "명, 청소년: " + reservationResponse.getTeenagerCount() + "명, 어린이: " + reservationResponse.getKidsCount() + "명\n"
                + "| " + "메세지: " + reservationResponse.getMessage() + "\n"
                + "| " + "결제 ID: " + reservationResponse.getPaymentId() + "\n"
                + "--------------------------------------------\n");
    }

    public static DiscordMessage createJoinMessage(String message) {
        return new DiscordMessage(message);
    }

    private static String changeToDateFormat(String beforeDateTime) {
        // Split the input at the 'T' character to get the date part
        String[] parts = beforeDateTime.split("T");
        String datePart = parts[0]; // yyyy-MM-dd
        return datePart;
    }

    private static String changeToTimeFormat(String beforeDateTime) {
        // Split the input at the 'T' character to get the time part
        String[] parts = beforeDateTime.split("T");
        String timePart = parts[1]; // HH:mm:ss
        return timePart;
    }

    // Return endtime when reservationResponse.getType is "TIME". if not, return reservationResponse.getGameCount
    private static String getEndTime(ReservationResponse reservationResponse) {
        if (reservationResponse.getType().equals(Type.TIME)) {
            return "예약 종료 시간: " + changeToTimeFormat(reservationResponse.getEndTime());
        }
        return "게임 수: " + reservationResponse.getGameCount().toString();
    }

}
