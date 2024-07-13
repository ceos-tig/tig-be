package tig.server.reservation.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import tig.server.reservation.domain.Reservation;
import tig.server.reservation.dto.ReservationRequest;
import tig.server.reservation.dto.ReservationResponse;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    tig.server.reservation.mapper.ReservationMapper INSTANCE = Mappers.getMapper(tig.server.reservation.mapper.ReservationMapper.class);

    @Mapping(target = "id", ignore = true)
    Reservation requestToEntity(ReservationRequest reservationRequest);

    ReservationResponse entityToResponse(Reservation reservation);

    Reservation responseToEntity(ReservationResponse reservationResponse);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Reservation updateFromRequest(ReservationRequest reservationRequest, @MappingTarget Reservation reservation);
}