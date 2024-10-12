package tig.server.reservation.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tig.server.reservation.domain.Reservation;
import tig.server.reservation.dto.ReservationRequest;
import tig.server.reservation.dto.ReservationResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-12T23:12:44+0900",
    comments = "version: 1.5.1.Final, compiler: javac, environment: Java 17.0.7 (Oracle Corporation)"
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
        reservation.price( reservationRequest.getPrice() );
        reservation.gameCount( reservationRequest.getGameCount() );
        if ( reservationRequest.getDate() != null ) {
            reservation.date( LocalDateTime.parse( reservationRequest.getDate() ) );
        }
        if ( reservationRequest.getStartTime() != null ) {
            reservation.startTime( LocalDateTime.parse( reservationRequest.getStartTime() ) );
        }
        if ( reservationRequest.getEndTime() != null ) {
            reservation.endTime( LocalDateTime.parse( reservationRequest.getEndTime() ) );
        }
        reservation.status( reservationRequest.getStatus() );
        reservation.paymentId( reservationRequest.getPaymentId() );
        reservation.message( reservationRequest.getMessage() );
        reservation.userName( reservationRequest.getUserName() );
        reservation.phoneNumber( reservationRequest.getPhoneNumber() );
        reservation.programEnum( reservationRequest.getProgramEnum() );

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
        if ( reservation.getDate() != null ) {
            reservationResponse.date( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( reservation.getDate() ) );
        }
        if ( reservation.getStartTime() != null ) {
            reservationResponse.startTime( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( reservation.getStartTime() ) );
        }
        if ( reservation.getEndTime() != null ) {
            reservationResponse.endTime( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( reservation.getEndTime() ) );
        }
        reservationResponse.gameCount( reservation.getGameCount() );
        reservationResponse.price( reservation.getPrice() );
        reservationResponse.status( reservation.getStatus() );
        reservationResponse.phoneNumber( reservation.getPhoneNumber() );
        reservationResponse.userName( reservation.getUserName() );
        reservationResponse.paymentId( reservation.getPaymentId() );
        if ( reservation.getUpdatedAt() != null ) {
            reservationResponse.updatedAt( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( reservation.getUpdatedAt() ) );
        }
        reservationResponse.message( reservation.getMessage() );
        reservationResponse.programEnum( reservation.getProgramEnum() );

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
        reservation.price( reservationResponse.getPrice() );
        reservation.gameCount( reservationResponse.getGameCount() );
        if ( reservationResponse.getDate() != null ) {
            reservation.date( LocalDateTime.parse( reservationResponse.getDate() ) );
        }
        if ( reservationResponse.getStartTime() != null ) {
            reservation.startTime( LocalDateTime.parse( reservationResponse.getStartTime() ) );
        }
        if ( reservationResponse.getEndTime() != null ) {
            reservation.endTime( LocalDateTime.parse( reservationResponse.getEndTime() ) );
        }
        reservation.status( reservationResponse.getStatus() );
        reservation.paymentId( reservationResponse.getPaymentId() );
        reservation.message( reservationResponse.getMessage() );
        reservation.userName( reservationResponse.getUserName() );
        reservation.phoneNumber( reservationResponse.getPhoneNumber() );
        reservation.programEnum( reservationResponse.getProgramEnum() );

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
        if ( reservationRequest.getPrice() != null ) {
            reservation.setPrice( reservationRequest.getPrice() );
        }
        if ( reservationRequest.getGameCount() != null ) {
            reservation.setGameCount( reservationRequest.getGameCount() );
        }
        if ( reservationRequest.getDate() != null ) {
            reservation.setDate( LocalDateTime.parse( reservationRequest.getDate() ) );
        }
        if ( reservationRequest.getStartTime() != null ) {
            reservation.setStartTime( LocalDateTime.parse( reservationRequest.getStartTime() ) );
        }
        if ( reservationRequest.getEndTime() != null ) {
            reservation.setEndTime( LocalDateTime.parse( reservationRequest.getEndTime() ) );
        }
        if ( reservationRequest.getStatus() != null ) {
            reservation.setStatus( reservationRequest.getStatus() );
        }
        if ( reservationRequest.getPaymentId() != null ) {
            reservation.setPaymentId( reservationRequest.getPaymentId() );
        }
        if ( reservationRequest.getMessage() != null ) {
            reservation.setMessage( reservationRequest.getMessage() );
        }
        if ( reservationRequest.getUserName() != null ) {
            reservation.setUserName( reservationRequest.getUserName() );
        }
        if ( reservationRequest.getPhoneNumber() != null ) {
            reservation.setPhoneNumber( reservationRequest.getPhoneNumber() );
        }
        if ( reservationRequest.getProgramEnum() != null ) {
            reservation.setProgramEnum( reservationRequest.getProgramEnum() );
        }

        return reservation;
    }
}
