package tig.server.oauth2.google.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tig.server.oauth2.google.dto.GoogleInfoResponseDto;
import tig.server.oauth2.google.dto.GoogleRequestDto;
import tig.server.oauth2.google.dto.GoogleResponseDto;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GoogleService {

    private final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private final String GOOGLE_MEMBER_INFO_URL = "https://oauth2.googleapis.com/tokeninfo";

    @Value("${google.client_id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${google.client_secret}")
    private String GOOGLE_CLIENT_SECRET;
    

    public GoogleInfoResponseDto getGoogleMemberInfoTest(String code) {
        RestTemplate restTemplate = new RestTemplate();
        GoogleRequestDto googleOAuthRequestParam = GoogleRequestDto
                .builder()
                .clientId(GOOGLE_CLIENT_ID)
                .clientSecret(GOOGLE_CLIENT_SECRET)
                .code(code)
                .redirectUri("https://localhost:3000/login/oauth2/code/google")
                .grantType("authorization_code").build();

        ResponseEntity<GoogleResponseDto> resultEntity = restTemplate.postForEntity(GOOGLE_TOKEN_URL, googleOAuthRequestParam, GoogleResponseDto.class);
        String jwtToken=resultEntity.getBody().getId_token();

        Map<String, String> map = new HashMap<>();
        map.put("id_token",jwtToken);
        ResponseEntity<GoogleInfoResponseDto> memberInfo = restTemplate.postForEntity(GOOGLE_MEMBER_INFO_URL, map, GoogleInfoResponseDto.class);
        return memberInfo.getBody();
    }

    public GoogleInfoResponseDto getGoogleMemberInfoDeploy(String code) {
        RestTemplate restTemplate = new RestTemplate();
        GoogleRequestDto googleOAuthRequestParam = GoogleRequestDto
                .builder()
                .clientId(GOOGLE_CLIENT_ID)
                .clientSecret(GOOGLE_CLIENT_SECRET)
                .code(code)
                .redirectUri("https://tigleisure.com/login/oauth2/code/google")
                .grantType("authorization_code").build();

        ResponseEntity<GoogleResponseDto> resultEntity = restTemplate.postForEntity(GOOGLE_TOKEN_URL, googleOAuthRequestParam, GoogleResponseDto.class);
        String jwtToken=resultEntity.getBody().getId_token();

        Map<String, String> map = new HashMap<>();
        map.put("id_token",jwtToken);
        ResponseEntity<GoogleInfoResponseDto> memberInfo = restTemplate.postForEntity(GOOGLE_MEMBER_INFO_URL, map, GoogleInfoResponseDto.class);
        return memberInfo.getBody();
    }
}
