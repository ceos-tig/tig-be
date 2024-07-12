package tig.server.review.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import tig.server.review.domain.Review;
import tig.server.review.dto.ReviewDTO;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    tig.server.review.mapper.ReviewMapper INSTANCE = Mappers.getMapper(tig.server.review.mapper.ReviewMapper.class);

    @Mapping(target = "id", ignore = true)
    Review requestToEntity(ReviewDTO.Request reviewRequest);

    @Mapping(source = "reservation.id", target = "reservationId") // reservationId 타겟팅이 안되어 있었음
    ReviewDTO.Response entityToResponse(Review review);

    Review responseToEntity(ReviewDTO.Response reviewResponse);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Review updateFromRequest(ReviewDTO.Request reviewRequest, @MappingTarget Review review);
}