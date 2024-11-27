package tig.server.feedback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FeedbackResponseDto {
    private String memberName;
    private String message;
}
