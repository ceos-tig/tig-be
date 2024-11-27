package tig.server.feedback.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tig.server.feedback.dto.FeedbackResponseDto;
import tig.server.feedback.service.FeedbackService;
import tig.server.global.response.ApiResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Operation(summary = "ADMIN : 사용자 입력 피드백 전체 조회")
    @GetMapping("")
    public ResponseEntity<ApiResponse<List<FeedbackResponseDto>>> getFeedback() {
        List<FeedbackResponseDto> allFeedback = feedbackService.getAllFeedback();
        ApiResponse<List<FeedbackResponseDto>> response = ApiResponse.of(200, "successfully retrieved all feedbacks", allFeedback);
        return ResponseEntity.ok(response);
    }

}
