package tig.server.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tig.server.review.domain.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
