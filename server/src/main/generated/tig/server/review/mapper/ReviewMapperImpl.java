package tig.server.review.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tig.server.reservation.domain.Reservation;
import tig.server.review.domain.Review;
import tig.server.review.dto.ReviewRequest;
import tig.server.review.dto.ReviewResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-13T17:58:20+0900",
    comments = "version: 1.5.1.Final, compiler: javac, environment: Java 17.0.5 (Oracle Corporation)"
)
@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public Review requestToEntity(ReviewRequest reviewRequest) {
        if ( reviewRequest == null ) {
            return null;
        }

        Review.ReviewBuilder review = Review.builder();

        review.rating( reviewRequest.getRating() );
        review.contents( reviewRequest.getContents() );

        return review.build();
    }

    @Override
    public ReviewResponse entityToResponse(Review review) {
        if ( review == null ) {
            return null;
        }

        ReviewResponse.ReviewResponseBuilder reviewResponse = ReviewResponse.builder();

        reviewResponse.reservationId( reviewReservationId( review ) );
        reviewResponse.rating( review.getRating() );
        reviewResponse.contents( review.getContents() );

        return reviewResponse.build();
    }

    @Override
    public Review responseToEntity(ReviewResponse reviewResponse) {
        if ( reviewResponse == null ) {
            return null;
        }

        Review.ReviewBuilder review = Review.builder();

        review.rating( reviewResponse.getRating() );
        review.contents( reviewResponse.getContents() );

        return review.build();
    }

    @Override
    public Review updateFromRequest(ReviewRequest reviewRequest, Review review) {
        if ( reviewRequest == null ) {
            return review;
        }

        if ( reviewRequest.getRating() != null ) {
            review.setRating( reviewRequest.getRating() );
        }
        if ( reviewRequest.getContents() != null ) {
            review.setContents( reviewRequest.getContents() );
        }

        return review;
    }

    private Long reviewReservationId(Review review) {
        if ( review == null ) {
            return null;
        }
        Reservation reservation = review.getReservation();
        if ( reservation == null ) {
            return null;
        }
        Long id = reservation.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
