package tig.server.openai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import tig.server.openai.dto.OpenAIRequestDto;
import tig.server.openai.dto.OpenAIResposneDto;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OpenAIService {
    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;
    private final RestTemplate restTemplate;

    public OpenAIResposneDto reviewSummary(String prompt) {
        String systemContent = "You are an AI model that summarizes user reviews in one sentence.";
        OpenAIRequestDto request = new OpenAIRequestDto(model,systemContent,prompt);
        OpenAIResposneDto response = restTemplate.postForObject(apiUrl, request, OpenAIResposneDto.class);
        System.out.println("AI 요약 결과 : " + response.getChoices().get(0).getMessage().getContent());
        return response;
    }
}
