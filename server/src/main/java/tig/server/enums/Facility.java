package tig.server.enums;

import lombok.Getter;

@Getter
public enum Facility {
    WIRELESS_INTERNET("무선 인터넷"),
    TOILET_CLASSIFICATION("남/녀 화장실 구분"),
    GROUP_AVAILABILITY("단체 이용 가능"),
    WAITING_SPACE("대기 공간"),
    PET_ALLOWED("반려동물 동반"),
    PARKING_AVAILABLE("주차 가능"),
    VISIT_SERVICE("방문접수/출장"),
    DELIVERY_AVAILABLE("포장/배달 가능"),
    PAID_PARKING("주차 가능 유료"),
    DRESSING_ROOM("탈의실"),
    WATER_PURIFIER("정수기"),
    PERSONAL_LOCKER("개인 락커"),
    REST_FACILITY("오락 시설"),
    KIDS_FACILITY("유아 시설"),
    WHEELCHAIR_ACCESSIBLE("장애인 휠체어 이용 가능"),
    ENTRANCE_WHEELCHAIR_ACCESSIBLE("출입구 휠체어 이용 가능"),
    SEAT_WHEELCHAIR_ACCESSIBLE("좌석 휠체어 가능"),
    DISABLED_PARKING("장애인 주차 구역");

    private final String description;

    Facility(String description) {
        this.description = description;
    }
}
