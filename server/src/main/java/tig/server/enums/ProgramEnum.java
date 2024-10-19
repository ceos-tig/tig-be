package tig.server.enums;

import lombok.Getter;

@Getter
public enum ProgramEnum {
    // Golf Programs
    SCREEN_GOLF("스크린 골프"),
    GOLF_PRACTICE("골프 연습장"),
    GOLF_LESSON("골프 레슨"),
    FIELD("필드"),

    // Table Tennis Programs
    SINGLE("단식"),
    DOUBLE("복식"),
    ONEDAY("일일권"),

    // Balling Programs
    NORMAL_BALLING("일반 볼링"),
    ROCK_BALLING("락볼링"),

    // Billiards Programs
    MEDIUM_TABLE("중대"),
    BIG_TABLE("대대"),
    POCKET_BALL("포켓볼"),

    // Football Programs
    FOOTBALL_FIELD("축구장 대여"),
    FOOTBALL_MACHINE("축구 볼머신"),

    // Baseball Programs
    SCREEN_BASEBALL("스크린야구"),
    BASEBALL_PRACTICE("야구연습장"),
    MAJOR_ROOM("메이저룸"),
    MINOR_ROOM("마이너룸"),
    PITCHING("피칭"),
    BATTING("배팅"),

    // Tennis Programs
    TENNIS_COURT("테니스 코트"),
    TENNIS_LESSON("테니스 레슨"),
    TENNISBALL_MACHINE("테니스 볼 머신"),

    // Squash Programs
    SQUASH_PRACTICE("스쿼시 연습장"),
    SQAUSH_LESSON("스쿼시 레슨"),
    ONE_DAY_CLASS("스쿼시 원데이 클래스");

    private final String description;

    ProgramEnum(String description) {
        this.description = description;
    }
}
