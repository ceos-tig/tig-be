package tig.server.reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tig.server.reservation.domain.Reservation;
import tig.server.reservation.dto.ReservationDTO;
import tig.server.reservation.mapper.ReservationMapper;
import tig.server.reservation.repository.ReservationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    public List<ReservationDTO.Response> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(reservationMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    public ReservationDTO.Response getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("reservation not found"));
        return reservationMapper.entityToResponse(reservation);
    }

    @Transactional
    public ReservationDTO.Response createReservation(ReservationDTO.Request reservationRequest) {
        Reservation reservation = reservationMapper.requestToEntity(reservationRequest);
        reservation = reservationRepository.save(reservation);
        ReservationDTO.Response response = reservationMapper.entityToResponse(reservation);
        return response;
    }



}
