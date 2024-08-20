package tig.server.openai.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import tig.server.openai.dto.OpenAIRequestDto;
import tig.server.openai.dto.OpenAIResposneDto;

@RestController
@RequiredArgsConstructor
public class OpenAIController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;
    private final RestTemplate restTemplate;


    @GetMapping("/summary-review")
    public String chat(@RequestParam("prompt") String prompt){

        OpenAIRequestDto request = new OpenAIRequestDto(model,prompt,1,256,1,2,2);
        OpenAIResposneDto gptResponse = restTemplate.postForObject(apiUrl, request, OpenAIResposneDto.class);
        System.out.println("AI 요약 결과 : " + gptResponse.getChoices().get(0).getMessage().getContent());

        return gptResponse.getChoices().get(0).getMessage().getContent();
    }
}
