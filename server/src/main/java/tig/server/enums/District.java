package tig.server.enums;

public enum District {
    GANGNAM("강남구", 127.0495556, 37.514575),
    GANGDONG("강동구", 127.1258639, 37.52736667),
    GANGBUK("강북구", 127.0277194, 37.63695556),
    GANGSEO("강서구", 126.851675, 37.54815556),
    GWANAK("관악구", 126.9538444, 37.47538611),
    GWANGJIN("광진구", 127.0845333, 37.53573889),
    GURO("구로구", 126.8895972, 37.49265),
    GEUMCHEON("금천구", 126.9041972, 37.44910833),
    NOWON("노원구", 127.0583889, 37.65146111),
    DOBONG("도봉구", 127.0495222, 37.66583333),
    DONGDAEMUN("동대문구", 127.0421417, 37.571625),
    DONGJAK("동작구", 126.941575, 37.50965556),
    MAPO("마포구", 126.9105306, 37.56070556),
    SEODAEMUN("서대문구", 126.9388972, 37.57636667),
    SEOCHO("서초구", 127.0348111, 37.48078611),
    SEONGDONG("성동구", 127.039, 37.56061111),
    SEONGBUK("성북구", 127.0203333, 37.58638333),
    SONGPA("송파구", 127.1079306, 37.51175556),
    YANGCHEON("양천구", 126.8687083, 37.51423056),
    YEONGDEUNGPO("영등포구", 126.8983417, 37.52361111),
    YONGSAN("용산구", 126.9675222, 37.53609444),
    EUNPYEONG("은평구", 126.9312417, 37.59996944),
    JONGNO("종로구", 126.9816417, 37.57037778),
    JUNG("중구", 126.9996417, 37.56100278),
    JUNGRANG("중랑구", 127.0947778, 37.60380556);

    private final String koreanName;
    private final double latitude;
    private final double longitude;

    District(String koreanName, double latitude, double longitude) {
        this.koreanName = koreanName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getKoreanName() {
        return koreanName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}