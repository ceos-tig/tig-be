package tig.server.openai.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class OpenAIRequestDto {
    private String model;
    private List<Message> messages;

    public OpenAIRequestDto(String model, String systemContent, String userContent) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("system", systemContent));
        this.messages.add(new Message("user", userContent));
    }
}
