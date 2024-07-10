package tig.server.review.dto;

import lombok.*;
import tig.server.reservation.dto.ReservationDTO;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewWithReservationDTO {

    private ReviewDTO.Response review;
    private ReservationDTO.Response reservation;
}