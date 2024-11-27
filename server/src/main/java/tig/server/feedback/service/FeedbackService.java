package tig.server.feedback.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tig.server.feedback.domain.Feedback;
import tig.server.feedback.dto.FeedbackResponseDto;
import tig.server.feedback.repository.FeedbackRepository;
import tig.server.member.domain.Member;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;

    public List<FeedbackResponseDto> getAllFeedback() {
        List<Feedback> feedbackList = feedbackRepository.findAll();
        return feedbackList.stream()
                .map(feedback -> new FeedbackResponseDto(
                        feedback.getMember().getName(),
                        feedback.getMessage()
                ))
                .collect(Collectors.toList());
    }
}
