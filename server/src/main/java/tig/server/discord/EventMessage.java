package tig.server.discord;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum EventMessage {
    SIGN_UP_EVENT("TIG 서비스에 회원가입 이벤트가 발생했습니다. 🎉"),
    RESERVATION_APPLICATION("예약 신청이 발생했습니다."),
    RESERVATION_CANCEL("예약이 취소 되었습니다.");

    private final String message;
}
