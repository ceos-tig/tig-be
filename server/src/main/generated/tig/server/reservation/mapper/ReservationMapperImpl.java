package tig.server.reservation.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tig.server.reservation.domain.Reservation;
import tig.server.reservation.dto.ReservationRequest;
import tig.server.reservation.dto.ReservationResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-13T17:58:20+0900",
    comments = "version: 1.5.1.Final, compiler: javac, environment: Java 17.0.5 (Oracle Corporation)"
)
@Component
public class ReservationMapperImpl implements ReservationMapper {

    @Override
    public Reservation requestToEntity(ReservationRequest reservationRequest) {
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
    public ReservationResponse entityToResponse(Reservation reservation) {
        if ( reservation == null ) {
            return null;
        }

        ReservationResponse.ReservationResponseBuilder reservationResponse = ReservationResponse.builder();

        reservationResponse.adultCount( reservation.getAdultCount() );
        reservationResponse.teenagerCount( reservation.getTeenagerCount() );
        reservationResponse.kidsCount( reservation.getKidsCount() );
        reservationResponse.date( reservation.getDate() );
        reservationResponse.startTime( reservation.getStartTime() );
        reservationResponse.endTime( reservation.getEndTime() );
        reservationResponse.price( reservation.getPrice() );
        reservationResponse.status( reservation.getStatus() );

        return reservationResponse.build();
    }

    @Override
    public Reservation responseToEntity(ReservationResponse reservationResponse) {
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
    public Reservation updateFromRequest(ReservationRequest reservationRequest, Reservation reservation) {
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
