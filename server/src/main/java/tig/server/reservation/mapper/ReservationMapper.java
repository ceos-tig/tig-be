package tig.server.reservation.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import tig.server.reservation.domain.Reservation;
import tig.server.reservation.dto.ReservationDTO;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    tig.server.reservation.mapper.ReservationMapper INSTANCE = Mappers.getMapper(tig.server.reservation.mapper.ReservationMapper.class);

    @Mapping(target = "id", ignore = true)
    Reservation requestToEntity(ReservationDTO.Request reservationRequest);

    ReservationDTO.Response entityToResponse(Reservation reservation);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Reservation updateFromRequest(ReservationDTO.Request reservationRequest, @MappingTarget Reservation reservation);
}