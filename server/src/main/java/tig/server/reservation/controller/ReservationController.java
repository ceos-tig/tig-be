package tig.server.reservation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tig.server.reservation.dto.ReservationDTO;
import tig.server.reservation.service.ReservationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservation")
@Tag(name = "reservation", description = "예약 API")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("")
    @Operation(summary = "전체 예약 내역 조회")
    public ResponseEntity<List<ReservationDTO.Response>> getAllReservations() {
        List<ReservationDTO.Response> reservationResponses = reservationService.getAllReservations();
        return ResponseEntity.ok(reservationResponses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 예약 내역 조회")
    public ResponseEntity<ReservationDTO.Response> getReservationById(@PathVariable Long id) {
        ReservationDTO.Response reservationResponse = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservationResponse);
    }
}
