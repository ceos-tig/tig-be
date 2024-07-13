package tig.server.review.dto;

import lombok.*;
import tig.server.dummy.DummyDto;
import tig.server.reservation.dto.ReservationResponse;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewWithReservationDTO {

    private ReviewDTO.Response review;
    private ReservationResponse reservation;
}