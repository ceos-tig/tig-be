package tig.server.reservation.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tig.server.reservation.domain.Reservation;
import tig.server.reservation.dto.ReservationDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-10T13:44:03+0900",
    comments = "version: 1.5.1.Final, compiler: javac, environment: Java 17.0.7 (Oracle Corporation)"
)
@Component
public class ReservationMapperImpl implements ReservationMapper {

    @Override
    public Reservation requestToEntity(ReservationDTO.Request reservationRequest) {
        if ( reservationRequest == null ) {
            return null;
        }

        Reservation.ReservationBuilder reservation = Reservation.builder();

        reservation.adultCount( reservationRequest.getAdultCount() );
        reservation.teenagerCount( reservationRequest.getTeenagerCount() );
        reservation.kidsCount( reservationRequest.getKidsCount() );
        reservation.date( reservationRequest.getDate() );
        reservation.startTime( reservationRequest.getStartTime() );
        reservation.endTime( reservationRequest.getEndTime() );
        reservation.price( reservationRequest.getPrice() );
        reservation.status( reservationRequest.getStatus() );

        return reservation.build();
    }

    @Override
    public ReservationDTO.Response entityToResponse(Reservation reservation) {
        if ( reservation == null ) {
            return null;
        }

        ReservationDTO.Response.ResponseBuilder response = ReservationDTO.Response.builder();

        response.adultCount( reservation.getAdultCount() );
        response.teenagerCount( reservation.getTeenagerCount() );
        response.kidsCount( reservation.getKidsCount() );
        response.date( reservation.getDate() );
        response.startTime( reservation.getStartTime() );
        response.endTime( reservation.getEndTime() );
        response.price( reservation.getPrice() );
        response.status( reservation.getStatus() );

        return response.build();
    }

    @Override
    public Reservation responseToEntity(ReservationDTO.Response reservationResponse) {
        if ( reservationResponse == null ) {
            return null;
        }

        Reservation.ReservationBuilder reservation = Reservation.builder();

        reservation.adultCount( reservationResponse.getAdultCount() );
        reservation.teenagerCount( reservationResponse.getTeenagerCount() );
        reservation.kidsCount( reservationResponse.getKidsCount() );
        reservation.date( reservationResponse.getDate() );
        reservation.startTime( reservationResponse.getStartTime() );
        reservation.endTime( reservationResponse.getEndTime() );
        reservation.price( reservationResponse.getPrice() );
        reservation.status( reservationResponse.getStatus() );

        return reservation.build();
    }

    @Override
    public Reservation updateFromRequest(ReservationDTO.Request reservationRequest, Reservation reservation) {
        if ( reservationRequest == null ) {
            return reservation;
        }

        if ( reservationRequest.getAdultCount() != null ) {
            reservation.setAdultCount( reservationRequest.getAdultCount() );
        }
        if ( reservationRequest.getTeenagerCount() != null ) {
            reservation.setTeenagerCount( reservationRequest.getTeenagerCount() );
        }
        if ( reservationRequest.getKidsCount() != null ) {
            reservation.setKidsCount( reservationRequest.getKidsCount() );
        }
        if ( reservationRequest.getDate() != null ) {
            reservation.setDate( reservationRequest.getDate() );
        }
        if ( reservationRequest.getStartTime() != null ) {
            reservation.setStartTime( reservationRequest.getStartTime() );
        }
        if ( reservationRequest.getEndTime() != null ) {
            reservation.setEndTime( reservationRequest.getEndTime() );
        }
        if ( reservationRequest.getPrice() != null ) {
            reservation.setPrice( reservationRequest.getPrice() );
        }
        if ( reservationRequest.getStatus() != null ) {
            reservation.setStatus( reservationRequest.getStatus() );
        }

        return reservation;
    }
}
