package tig.server.openai.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class OpenAIRequestDto {
    private String model;
    private List<Message> messages;
    private int temperature;
    private int maxTokens;
    private int topP;
    private int frequencyPenalty;
    private int presencePenalty;

    public OpenAIRequestDto(String model, String prompt, int temperature, int maxTokens, int topP, int frequencyPenalty, int presencePenalty) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("user",prompt));
        this.temperature = temperature;
        this.maxTokens = maxTokens;
        this.topP=topP;
        this.frequencyPenalty=frequencyPenalty;
        this.presencePenalty = presencePenalty;
    }
}
