package tig.server.oauth2.memberInfo;

import java.util.Map;
import java.util.Optional;

public class KakaoMemberInfo implements OAuth2MemberInfo {
    private Map<String, Object> attributes;
    private Map<String, Object> kakaoAccountAttributes;
    private Map<String, Object> kakaoAccountEmailAttributes;
    private Map<String, Object> profileAttributes;

    public KakaoMemberInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.kakaoAccountAttributes = (Map<String, Object>) attributes.get("properties");
        this.kakaoAccountEmailAttributes = (Map<String, Object>) attributes.get("kakao_account");
        this.profileAttributes = (Map<String, Object>) attributes.get("profile");
    }


    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getName() {
        return kakaoAccountAttributes.get("nickname").toString();
    }

    @Override
    public String getProfileImage() {
        return kakaoAccountAttributes.get("profile_image").toString();
    }

    @Override
    public String getEmail() {
        return Optional.ofNullable(kakaoAccountEmailAttributes.get("email"))
                .map(Object::toString)
                .orElse(null); // 이메일이 없을 때 기본 값 설정
    }
}
