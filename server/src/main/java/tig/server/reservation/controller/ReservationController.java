package tig.server.reservation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tig.server.annotation.LoginUser;
import tig.server.enums.Status;
import tig.server.error.ApiResponse;
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
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getAllReservations() {
        List<ReservationResponse> reservationResponses = reservationService.getAllReservations();
        ApiResponse<List<ReservationResponse>> response = ApiResponse.of(200, "successfully retrieved all reservations", reservationResponses);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 예약 내역 조회")
    public ResponseEntity<ApiResponse<ReservationResponse>> getReservationById(@PathVariable Long id) {
        ReservationResponse reservationResponse = reservationService.getReservationById(id);
        ApiResponse<ReservationResponse> response = ApiResponse.of(200, "successfully retrieved reservation by id", reservationResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{clubId}")
    @Operation(summary = "예약")
    public ResponseEntity<ApiResponse<ReservationResponse>> createReservation(
            @LoginUser Member member,
            @PathVariable Long clubId,
            @RequestBody ReservationRequest reservationRequest
    ) {
        ReservationResponse createdReservation = reservationService.createReservation(member, clubId, reservationRequest);
        createdReservation.setStatus(Status.TBC);
        ApiResponse<ReservationResponse> response = ApiResponse.of(200, "successfully reserved", createdReservation);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("all/{memberId}")
    @Operation(summary = "특정 유저의 모든 예약 내역 조회")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getReservationByMemberId(@PathVariable Long memberId) {
        List<ReservationResponse> reservationResponses = reservationService.getReservationByMemberId(memberId);
        ApiResponse<List<ReservationResponse>> response = ApiResponse.of(200, "successfully retrieved member's ALL reservation", reservationResponses);
        return ResponseEntity.ok(response);
    }

    @GetMapping("proceeding/{memberId}")
    @Operation(summary = "특정 유저의 진행중 예약 내역 조회")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getProceedingReservationByMemberId(@PathVariable Long memberId) {
        List<ReservationResponse> reservationResponses = reservationService.getProceedingReservationByMemberId(memberId);
        ApiResponse<List<ReservationResponse>> response = ApiResponse.of(200, "successfully retrieved member's PROCEEDING reservation", reservationResponses);
        return ResponseEntity.ok(response);
    }

    @GetMapping("terminated/{memberId}")
    @Operation(summary = "특정 유저의 종료된 예약 내역 조회")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getTerminatedReservationByMemberId(@PathVariable Long memberId) {
        List<ReservationResponse> reservationResponses = reservationService.getTerminatedReservationByMemberId(memberId);
        ApiResponse<List<ReservationResponse>> response = ApiResponse.of(200, "successfully retrieved member's TERMINATED reservation", reservationResponses);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/cancel/{id}")
    @Operation(summary = "특정 예약 취소")
    public ResponseEntity<ApiResponse<Void>> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservationById(id);
        ApiResponse<Void> response = ApiResponse.of(200, "successfully canceled reservation", null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/confirm/{id}")
    @Operation(summary = "admin : 대기 중인 특정 예약 확정으로 전환")
    public ResponseEntity<ApiResponse<Void>> confirmReservation(@PathVariable Long id) {
        reservationService.confirmReservationById(id);
        ApiResponse<Void> response = ApiResponse.of(200, "대기중인 특정 예약을 확정으로 전환 성공", null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/decline/{id}")
    @Operation(summary = "admin : 대기 중인 특정 예약 거절로 전환")
    public ResponseEntity<ApiResponse<Void>> declineReservation(@PathVariable Long id) {
        reservationService.declineReservationById(id);
        ApiResponse<Void> response = ApiResponse.of(200, "대기 중인 특정 예약 거절로 전환 성공", null);
        return ResponseEntity.ok(response);
    }


}

