package tig.server.review.dto;

import lombok.*;
import tig.server.reservation.dto.ReservationResponse;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewWithReservationDTO {

    private ReviewResponse review;
    private ReservationResponse reservation;
}