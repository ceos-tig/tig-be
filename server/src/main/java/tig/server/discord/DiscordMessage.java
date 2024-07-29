package tig.server.discord;

import tig.server.reservation.dto.ReservationResponse;

public record DiscordMessage(
        String content
) {
    public static DiscordMessage createApplicationMessage(String message, ReservationResponse reservationResponse) {
        String memberName = reservationResponse.getUserName();
        String clubName = reservationResponse.getClubName();

        return new DiscordMessage("\n--------------------------------------------\n"
                + "| " + message + "\n"
                + "| " + memberName + " 님이 " + clubName + " 업체를 예약했습니다.\n"
                + "| " + "Reservation ID: " + reservationResponse.getReservationId() + "\n"
                + "| " + "예약 날짜: " + reservationResponse.getDate() + "\n"
                + "| " + "예약 시간: " + reservationResponse.getStartTime() + " - " + reservationResponse.getEndTime() + "\n"
                + "| " + "결제 ID: " + reservationResponse.getPaymentId() + "\n"
                + "--------------------------------------------\n");
    }

    public static DiscordMessage createCancelMessage(String message, ReservationResponse reservationResponse) {
        String memberName = reservationResponse.getUserName();
        String clubName = reservationResponse.getClubName();

        return new DiscordMessage("\n--------------------------------------------\n"
                + "| " + message + "\n"
                + "| " + memberName + " 님이 " + clubName + " 업체예약을 취소했습니다.\n"
                + "| " + "Reservation ID: " + reservationResponse.getReservationId() + "\n"
                + "| " + "예약 날짜: " + reservationResponse.getDate() + "\n"
                + "| " + "예약 시간: " + reservationResponse.getStartTime() + " - " + reservationResponse.getEndTime() + "\n"
                + "| " + "결제 ID: " + reservationResponse.getPaymentId() + "\n"
                + "--------------------------------------------\n");
    }

    public static DiscordMessage createJoinMessage(String message) {
        return new DiscordMessage(message);
    }
}
