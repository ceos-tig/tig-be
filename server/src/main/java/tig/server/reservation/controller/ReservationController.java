package tig.server.reservation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tig.server.annotation.LoginUser;
import tig.server.enums.Status;
import tig.server.global.response.ApiResponse;
import tig.server.member.domain.Member;
import tig.server.reservation.dto.ReservationClubResponse;
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
    @Operation(summary = "admin : 전체 예약 내역 조회")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getAllReservations() {
        List<ReservationResponse> reservationResponses = reservationService.getAllReservations();
        ApiResponse<List<ReservationResponse>> response = ApiResponse.of(200, "successfully retrieved all reservations", reservationResponses);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{reservationId}")
    @Operation(summary = "특정 예약 내역 조회")
    public ResponseEntity<ApiResponse<ReservationResponse>> getReservationById(@PathVariable Long reservationId) {
        ReservationResponse reservationResponse = reservationService.getReservationById(reservationId);
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

    @GetMapping("/all")
    @Operation(summary = "현재 유저의 모든 예약 내역 조회")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getReservationByMemberId(@LoginUser Member member) {
        List<ReservationResponse> reservationResponses = reservationService.getReservationByMemberId(member.getId());
        ApiResponse<List<ReservationResponse>> response = ApiResponse.of(200, "successfully retrieved member's ALL reservation", reservationResponses);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/proceeding")
    @Operation(summary = "현재 유저의 진행중 예약 내역 조회")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getProceedingReservationByMemberId(@LoginUser Member member) {
        List<ReservationResponse> reservationResponses = reservationService.getProceedingReservationByMemberId(member.getId());
        ApiResponse<List<ReservationResponse>> response = ApiResponse.of(200, "successfully retrieved member's PROCEEDING reservation", reservationResponses);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/terminated")
    @Operation(summary = "현재 유저의 종료된 예약 내역 조회")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getTerminatedReservationByMemberId(@LoginUser Member member) {
        List<ReservationResponse> reservationResponses = reservationService.getTerminatedReservationByMemberId(member.getId());
        ApiResponse<List<ReservationResponse>> response = ApiResponse.of(200, "successfully retrieved member's TERMINATED reservation", reservationResponses);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/cancel/{reservationId}")
    @Operation(summary = "특정 예약 취소")
    public ResponseEntity<ApiResponse<Void>> cancelReservation(@PathVariable Long reservationId) {
        reservationService.cancelReservationById(reservationId);
        ApiResponse<Void> response = ApiResponse.of(200, "successfully canceled reservation", null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/canceled")
    @Operation(summary = "optional : 현재 유저의 취소된 예약 조회")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getCanceledReservation(@LoginUser Member member) {
        List<ReservationResponse> responseList = reservationService.getCanceledReservationByMemberId(member.getId());
        ApiResponse<List<ReservationResponse>> response = ApiResponse.of(200, "successfully get canceled reservation", responseList);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tbc")
    @Operation(summary = "admin : 대기 중인 모든 예약 조회")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> checkTbcReservation() {
        List<ReservationResponse> responseList = reservationService.checkTbcReservation();
        ApiResponse<List<ReservationResponse>> response = ApiResponse.of(200, "대기중인 모든 예약 조회 성공", responseList);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/tbc/{reservationId}")
    @Operation(summary = "admin : 대기 중인 특정 예약 TBC로 전환")
    public ResponseEntity<ApiResponse<Void>> tbcReservation(@PathVariable Long reservationId) {
        reservationService.tbcReservationById(reservationId);
        ApiResponse<Void> response = ApiResponse.of(200, "대기중인 특정 예약을 TBC로 전환 성공", null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/confirm/{reservationId}")
    @Operation(summary = "admin : 대기 중인 특정 예약 확정으로 전환")
    public ResponseEntity<ApiResponse<Void>> confirmReservation(@PathVariable Long reservationId) {
        reservationService.confirmReservationById(reservationId);
        ApiResponse<Void> response = ApiResponse.of(200, "대기중인 특정 예약을 확정으로 전환 성공", null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/decline/{reservationId}")
    @Operation(summary = "admin : 대기 중인 특정 예약 거절로 전환")
    public ResponseEntity<ApiResponse<Void>> declineReservation(@PathVariable Long reservationId) {
        reservationService.declineReservationById(reservationId);
        ApiResponse<Void> response = ApiResponse.of(200, "대기 중인 특정 예약 거절로 전환 성공", null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/done/{reservationId}")
    @Operation(summary = "admin : 대기 중인 특정 예약 체험 완료 로 전환")
    public ResponseEntity<ApiResponse<Void>> doneReservation(@PathVariable Long reservationId) {
        reservationService.doneReservationById(reservationId);
        ApiResponse<Void> response = ApiResponse.of(200, "대기 중인 특정 예약 체험 환료로 전환 성공", null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/confirmed")
    @Operation(summary = "admin : 승인된 모든 예약 조회")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> checkConfirmedReservation() {
        List<ReservationResponse> responseList = reservationService.checkConfirmedReservation();
        ApiResponse<List<ReservationResponse>> response = ApiResponse.of(200, "승인된 모든 예약 조회 성공", responseList);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/declined")
    @Operation(summary = "admin : 거절된 모든 예약 조회")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> checkDeclinedReservation() {
        List<ReservationResponse> responseList = reservationService.checkDeclinedReservation();
        ApiResponse<List<ReservationResponse>> response = ApiResponse.of(200, "거절된 모든 예약 조회 성공", responseList);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/club/{clubId}")
    @Operation(summary = "업체 예약 위해 필요한 정보 조회")
    public ResponseEntity<ApiResponse<ReservationClubResponse>> checkClubInfo(@PathVariable Long clubId) {
        ReservationClubResponse responseList = reservationService.checkClubInfo(clubId);
        ApiResponse<ReservationClubResponse> response = ApiResponse.of(200, "업체 예약 관련 정보 조회 성공", responseList);
        return ResponseEntity.ok(response);
    }

}

