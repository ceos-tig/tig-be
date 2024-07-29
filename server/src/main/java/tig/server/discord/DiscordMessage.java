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
                + "| " + "결제 ID: " + reservationResponse.getPaymentId() + "\n"
                + "--------------------------------------------\n");
    }

    public static DiscordMessage createJoinMessage(String message) {
        return new DiscordMessage(message);
    }

    private static String changeToDateFormat(String beforeDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse(beforeDate); // 기존 string을 date 클래스로 변환
        return dateFormat.format(date); // 변환한 값의 format 변경
    }

    private static String changeToTimeFormat(String beforeTime) throws ParseException {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Date time = timeFormat.parse(beforeTime); // 기존 string을 date 클래스로 변환
        return timeFormat.format(time); // 변환한 값의 format 변경
    }

    // return endtime when reservationResponse.getType is "TIME". if not, it return reservationResponse.getGameCount
    private static String getEndTime(ReservationResponse reservationResponse) throws ParseException {
        if (reservationResponse.getType().equals(Type.TIME)) {
            return "예약 종료 시간: " + changeToTimeFormat(reservationResponse.getEndTime());
        }
        return "게임 수: " + reservationResponse.getGameCount().toString();
    }
}
