package tig.server.reservation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tig.server.annotation.LoginUser;
import tig.server.enums.Status;
import tig.server.member.domain.Member;
import tig.server.reservation.dto.ReservationRequest;
import tig.server.reservation.dto.ReservationResponse;
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
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        List<ReservationResponse> reservationResponses = reservationService.getAllReservations();
        return ResponseEntity.ok(reservationResponses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 예약 내역 조회")
    public ResponseEntity<ReservationResponse> getReservationById(@PathVariable Long id) {
        ReservationResponse reservationResponse = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservationResponse);
    }

    @PostMapping("/{clubId}")
    @Operation(summary = "예약")
    public ResponseEntity<ReservationResponse> createReservation(
            @LoginUser Member member,
            @PathVariable Long clubId,
            @RequestBody ReservationRequest reservationRequest
    ) {
        ReservationResponse createdReservation = reservationService.createReservation(member, clubId, reservationRequest);
        createdReservation.setStatus(Status.TBC);
        return ResponseEntity.status(201).body(createdReservation);
    }

    @GetMapping("all/{memberId}")
    @Operation(summary = "특정 유저의 모든 예약 내역 조회")
    public ResponseEntity<List<ReservationResponse>> getReservationByMemberId(@PathVariable Long memberId) {
        List<ReservationResponse> reservationResponses = reservationService.getReservationByMemberId(memberId);
        return ResponseEntity.ok(reservationResponses);
    }

    @GetMapping("proceeding/{memberId}")
    @Operation(summary = "특정 유저의 진행중 예약 내역 조회")
    public ResponseEntity<List<ReservationResponse>> getProceedingReservationByMemberId(@PathVariable Long memberId) {
        List<ReservationResponse> reservationResponses = reservationService.getProceedingReservationByMemberId(memberId);
        return ResponseEntity.ok(reservationResponses);
    }

    @GetMapping("terminated/{memberId}")
    @Operation(summary = "특정 유저의 종료된 예약 내역 조회")
    public ResponseEntity<List<ReservationResponse>> getTerminatedReservationByMemberId(@PathVariable Long memberId) {
        List<ReservationResponse> reservationResponses = reservationService.getTerminatedReservationByMemberId(memberId);
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

