package tig.server.feedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tig.server.feedback.domain.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback,Long> {

}
