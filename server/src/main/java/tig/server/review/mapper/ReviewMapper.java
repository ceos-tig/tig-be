package tig.server.review.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import tig.server.review.domain.Review;
import tig.server.review.dto.ReviewRequest;
import tig.server.review.dto.ReviewResponse;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    tig.server.review.mapper.ReviewMapper INSTANCE = Mappers.getMapper(tig.server.review.mapper.ReviewMapper.class);

    @Mapping(target = "id", ignore = true)
    Review requestToEntity(ReviewRequest reviewRequest);

    @Mapping(source = "reservation.id", target = "reservationId") // reservationId 타겟팅이 안되어 있었음
    ReviewResponse entityToResponse(Review review);

    Review responseToEntity(ReviewResponse reviewResponse);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Review updateFromRequest(ReviewRequest reviewRequest, @MappingTarget Review review);
}