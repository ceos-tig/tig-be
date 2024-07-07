package tig.server.reservation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tig.server.enums.Status;
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

    @PostMapping("/{memberId}/{clubId}")
    @Operation(summary = "예약")
    public ResponseEntity<ReservationDTO.Response> createReservation(
            @PathVariable Long memberId,
            @PathVariable Long clubId,
            @RequestBody ReservationDTO.Request reservationRequest
    ) {
        ReservationDTO.Response createdReservation = reservationService.createReservation(memberId, clubId, reservationRequest);
        createdReservation.setStatus(Status.TBC);
        return ResponseEntity.status(201).body(createdReservation);
    }

    @GetMapping("all/{memberId}")
    @Operation(summary = "특정 유저의 모든 예약 내역 조회")
    public ResponseEntity<List<ReservationDTO.Response>> getReservationByMemberId(@PathVariable Long memberId) {
        List<ReservationDTO.Response> reservationResponses = reservationService.getReservationByMemberId(memberId);
        return ResponseEntity.ok(reservationResponses);
    }

    @GetMapping("proceeding/{memberId}")
    @Operation(summary = "특정 유저의 진행중 예약 내역 조회")
    public ResponseEntity<List<ReservationDTO.Response>> getProceedingReservationByMemberId(@PathVariable Long memberId) {
        List<ReservationDTO.Response> reservationResponses = reservationService.getProceedingReservationByMemberId(memberId);
        return ResponseEntity.ok(reservationResponses);
    }

    @GetMapping("terminated/{memberId}")
    @Operation(summary = "특정 유저의 종료된 예약 내역 조회")
    public ResponseEntity<List<ReservationDTO.Response>> getTerminatedReservationByMemberId(@PathVariable Long memberId) {
        List<ReservationDTO.Response> reservationResponses = reservationService.getTerminatedReservationByMemberId(memberId);
        return ResponseEntity.ok(reservationResponses);
    }


    @PostMapping("/cancel/{id}")
    @Operation(summary = "특정 예약 취소")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservationById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/confirm/{id}")
    @Operation(summary = "admin : 대기 중인 특정 예약 확정으로 전환")
    public ResponseEntity<Void> confirmReservation(@PathVariable Long id) {
        reservationService.confirmReservationById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/decline/{id}")
    @Operation(summary = "admin : 대기 중인 특정 예약 거절로 전환")
    public ResponseEntity<Void> declineReservation(@PathVariable Long id) {
        reservationService.declineReservationById(id);
        return ResponseEntity.noContent().build();
    }


}

