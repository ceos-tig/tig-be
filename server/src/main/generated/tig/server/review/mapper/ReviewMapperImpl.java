package tig.server.review.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tig.server.review.domain.Review;
import tig.server.review.dto.ReviewDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-09T18:55:39+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.5 (Oracle Corporation)"
)
@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public Review requestToEntity(ReviewDTO.Request reviewRequest) {
        if ( reviewRequest == null ) {
            return null;
        }

        Review.ReviewBuilder review = Review.builder();

        review.rating( reviewRequest.getRating() );
        review.contents( reviewRequest.getContents() );

        return review.build();
    }

    @Override
    public ReviewDTO.Response entityToResponse(Review review) {
        if ( review == null ) {
            return null;
        }

        ReviewDTO.Response.ResponseBuilder response = ReviewDTO.Response.builder();

        response.rating( review.getRating() );
        response.contents( review.getContents() );

        return response.build();
    }

    @Override
    public Review responseToEntity(ReviewDTO.Response reviewResponse) {
        if ( reviewResponse == null ) {
            return null;
        }

        Review.ReviewBuilder review = Review.builder();

        review.rating( reviewResponse.getRating() );
        review.contents( reviewResponse.getContents() );

        return review.build();
    }

    @Override
    public Review updateFromRequest(ReviewDTO.Request reviewRequest, Review review) {
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
}
