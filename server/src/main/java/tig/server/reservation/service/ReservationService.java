package tig.server.reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tig.server.club.domain.Club;
import tig.server.club.mapper.ClubMapper;
import tig.server.club.repository.ClubRepository;
import tig.server.club.service.ClubService;
import tig.server.discord.DiscordMessageProvider;
import tig.server.discord.EventMessage;
import tig.server.enums.Status;
import tig.server.enums.Type;
import tig.server.global.exception.BusinessExceptionHandler;
import tig.server.global.code.ErrorCode;
import tig.server.member.domain.Member;
import tig.server.member.mapper.MemberMapper;
import tig.server.member.service.MemberService;
import tig.server.payment.dto.PaymentResponseDto;
import tig.server.payment.service.PaymentService;
import tig.server.reservation.domain.Reservation;
import tig.server.reservation.dto.ReservationClubResponse;
import tig.server.reservation.dto.ReservationRequest;
import tig.server.reservation.dto.ReservationResponse;
import tig.server.reservation.mapper.ReservationMapper;
import tig.server.reservation.repository.ReservationRepository;
import tig.server.review.domain.Review;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    private final ClubRepository clubRepository;
    private final ClubService clubService;
    private final PaymentService paymentService;

    private final ClubMapper clubMapper;

    private final DiscordMessageProvider discordMessageProvider;

    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(reservationMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    public ReservationResponse getReservationById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessExceptionHandler("reservation not found",ErrorCode.NOT_FOUND_ERROR));
        ReservationResponse response = reservationMapper.entityToResponse(reservation);

        // get response from portone
        PaymentResponseDto paymentResponseDto = paymentService.getPaymentResponse(response.getPaymentId()).block();
        String provider = paymentResponseDto.getMethod().getProvider();
        String updatedAt = paymentResponseDto.getUpdatedAt();

        Club club = reservation.getClub();
        response.setType(club.getType());
        response.setBusinessHours(club.getBusinessHours());
        response.setClubName(club.getClubName());
        response.setClubAddress(club.getAddress());
        response.setReservationId(reservation.getId());
        response.setClubId(club.getId());
        response.setType(club.getType());
        response.setBusinessHours(club.getBusinessHours());
        response.setClubName(club.getClubName());
        response.setProvider(provider);
        response.setUpdatedAt(updatedAt);

        // check is review null
        response.setReviewed(reservation.getReview() != null);
        response.setPaymentId(reservation.getPaymentId());

        response.setReviewId(checkReviewed(reservation.getReview()));
        response.setMemberId(reservation.getMember().getId());
        response.setClubPhoneNumber(club.getPhoneNumber());
        
        return response;
    }

    @Transactional
    public ReservationResponse createReservation(Member member, Long clubId, ReservationRequest reservationRequest) {
        System.out.println(member.getId());

        Club club = clubMapper.responseToEntity(clubService.getClubById(clubId));
        System.out.println(club.getId());

        Reservation reservation = reservationMapper.requestToEntity(reservationRequest);
        reservation.setMember(member);
        reservation.setClub(club);

        if (club.getType() == Type.GAME) {
            reservationRequest.setEndTime("2000-01-01T00:00:00");
        }

        LocalDateTime date = LocalDateTime.parse(reservationRequest.getDate());
        LocalDateTime startTime = LocalDateTime.parse(reservationRequest.getStartTime());
        LocalDateTime endTime = LocalDateTime.parse(reservationRequest.getEndTime());

        reservation.setDate(date);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);

        reservation = reservationRepository.save(reservation);

        ReservationResponse response = reservationMapper.entityToResponse(reservation);
        response.setReservationId(reservation.getId());
        response.setMemberId(member.getId());
        response.setClubId(clubId);
        response.setType(club.getType());
        response.setBusinessHours(club.getBusinessHours());
        response.setClubName(club.getClubName());
        response.setPaymentId(reservation.getPaymentId());
        response.setGameCount(reservation.getGameCount());
        response.setReviewId(checkReviewed(reservation.getReview()));

        // discord-webhook
        discordMessageProvider.sendApplicationMessage(EventMessage.RESERVATION_APPLICATION, response);

        return response;
    }

    public List<ReservationResponse> getReservationByMemberId(Long memberId) {
        List<Reservation> reservations = reservationRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BusinessExceptionHandler("reservation not found", ErrorCode.NOT_FOUND_ERROR));

        return reservations.stream()
                .map(entity -> {
                    ReservationResponse response = ensureNonNullFields(reservationMapper.entityToResponse(entity), entity);
                    doneReservation(response, entity);
                    response.setReviewId(checkReviewed(entity.getReview()));
                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<ReservationResponse> getProceedingReservationByMemberId(Long memberId) {
        List<Status> proceedingStatuses = List.of(Status.TBC, Status.CONFIRMED);
        List<Reservation> reservations = reservationRepository.findReservationsByMemberIdAndStatus(memberId, proceedingStatuses)
                .orElseThrow(() -> new BusinessExceptionHandler("can not find reservation PROCEEDING", ErrorCode.NOT_FOUND_ERROR));
        return reservations.stream()
                .map(entity -> ensureNonNullFields(reservationMapper.entityToResponse(entity), entity))
                .collect(Collectors.toList());
    }

    public List<ReservationResponse> getTerminatedReservationByMemberId(Long memberId) {
        List<Status> proceedingStatuses = List.of(Status.DECLINED, Status.DONE, Status.CANCELED, Status.REVIEWED);
        List<Reservation> reservations = reservationRepository.findReservationsByMemberIdAndStatus(memberId, proceedingStatuses)
                .orElseThrow(() -> new BusinessExceptionHandler("can not find reservation TERMINATED", ErrorCode.NOT_FOUND_ERROR));
        return reservations.stream()
                .map(entity -> ensureNonNullFields(reservationMapper.entityToResponse(entity), entity))
                .collect(Collectors.toList());
    }


    public List<ReservationResponse> getCanceledReservationByMemberId(Long memberId) {
        List<Status> proceedingStatuses = List.of(Status.CANCELED);
        List<Reservation> reservations = reservationRepository.findReservationsByMemberIdAndStatus(memberId, proceedingStatuses)
                .orElseThrow(() -> new BusinessExceptionHandler("can not find reservation CANCELED", ErrorCode.NOT_FOUND_ERROR));
        return reservations.stream()
                .map(entity -> ensureNonNullFields(reservationMapper.entityToResponse(entity), entity))
                .collect(Collectors.toList());
    }


    @Transactional
    public void cancelReservationById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessExceptionHandler("reservation not found",ErrorCode.NOT_FOUND_ERROR));

        // Define the list of valid statuses
        List<Status> validStatuses = Arrays.asList(Status.TBC, Status.CONFIRMED);

        // Check if the reservation status is not in the list of valid statuses
        if (!validStatuses.contains(reservation.getStatus())) {
            throw new BusinessExceptionHandler("Cannot cancel a reservation with status " + reservation.getStatus(),ErrorCode.BAD_REQUEST_ERROR);
        }

        reservation.setStatus(Status.CANCELED);

        ReservationResponse response = reservationMapper.entityToResponse(reservation);

        // discord-webhook
        discordMessageProvider.sendCancelMessage(EventMessage.RESERVATION_CANCEL, response);

        reservationRepository.save(reservation);
    }

    public List<ReservationResponse> checkTbcReservation() {
        List<Reservation> reservations = reservationRepository.findByStatus(Status.TBC)
                .orElseThrow(() -> new BusinessExceptionHandler("can not find TBC reservation", ErrorCode.NOT_FOUND_ERROR));
        return reservations.stream()
                .map(entity -> ensureNonNullFields(reservationMapper.entityToResponse(entity), entity))
                .collect(Collectors.toList());
    }

    public List<ReservationResponse> checkConfirmedReservation() {
        List<Reservation> reservations = reservationRepository.findByStatus(Status.CONFIRMED)
                .orElseThrow(() -> new BusinessExceptionHandler("can not find Confirmed reservation", ErrorCode.NOT_FOUND_ERROR));
        return reservations.stream()
                .map(entity -> ensureNonNullFields(reservationMapper.entityToResponse(entity), entity))
                .collect(Collectors.toList());
    }

    public List<ReservationResponse> checkDeclinedReservation() {
        List<Reservation> reservations = reservationRepository.findByStatus(Status.DECLINED)
                .orElseThrow(() -> new BusinessExceptionHandler("can not find Declined reservation", ErrorCode.NOT_FOUND_ERROR));
        return reservations.stream()
                .map(entity -> ensureNonNullFields(reservationMapper.entityToResponse(entity), entity))
                .collect(Collectors.toList());
    }

    @Transactional
    public void tbcReservationById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessExceptionHandler("reservation not found",ErrorCode.NOT_FOUND_ERROR));

        reservation.setStatus(Status.TBC);
        reservationRepository.save(reservation);
    }

    @Transactional
    public void confirmReservationById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessExceptionHandler("reservation not found",ErrorCode.NOT_FOUND_ERROR));

        // Define the list of valid statuses
        List<Status> validStatuses = Arrays.asList(Status.TBC);

        // Check if the reservation status is not in the list of valid statuses
        if (!validStatuses.contains(reservation.getStatus())) {
            throw new BusinessExceptionHandler("Cannot confirm a reservation with status " + reservation.getStatus(),ErrorCode.BAD_REQUEST_ERROR);
        }

        reservation.setStatus(Status.CONFIRMED);
        reservationRepository.save(reservation);
    }

    @Transactional
    public void declineReservationById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessExceptionHandler("reservation not found",ErrorCode.NOT_FOUND_ERROR));

        // Define the list of valid statuses
        List<Status> validStatuses = Arrays.asList(Status.TBC);

        // Check if the reservation status is not in the list of valid statuses
        if (!validStatuses.contains(reservation.getStatus())) {
            throw new BusinessExceptionHandler("Cannot decline a reservation with status " + reservation.getStatus(),ErrorCode.BAD_REQUEST_ERROR);
        }

        reservation.setStatus(Status.DECLINED);
        reservationRepository.save(reservation);
    }

    @Transactional
    public void doneReservationById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessExceptionHandler("reservation not found",ErrorCode.NOT_FOUND_ERROR));

        // Define the list of valid statuses
        List<Status> validStatuses = Arrays.asList(Status.CONFIRMED);

        // Check if the reservation status is not in the list of valid statuses
        if (!validStatuses.contains(reservation.getStatus())) {
            throw new BusinessExceptionHandler("Cannot done a reservation with status " + reservation.getStatus(),ErrorCode.BAD_REQUEST_ERROR);
        }

        reservation.setStatus(Status.DONE);
        reservationRepository.save(reservation);
    }
    
    private ReservationResponse doneReservation(ReservationResponse reservationResponse, Reservation reservation) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(reservation.getStartTime()) && reservation.getStatus() == Status.CONFIRMED) {
            reservation.setStatus(Status.DONE);
            reservationResponse.setStatus(Status.DONE);

            reservationRepository.save(reservation);
        }
        return reservationResponse;
    }

    @Transactional
    public void reviewReservationById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessExceptionHandler("reservation not found",ErrorCode.NOT_FOUND_ERROR));

        List<Status> validStatuses = Arrays.asList(Status.DONE);

        if(!validStatuses.contains(reservation.getStatus())) {
            throw new BusinessExceptionHandler("Cannot review a reservation with status " + reservation.getStatus(), ErrorCode.BAD_REQUEST_ERROR);
        }

        reservation.setStatus(Status.REVIEWED);
        reservationRepository.save(reservation);
    }

    public boolean checkReservationIsReviewedById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessExceptionHandler("reservation not found",ErrorCode.NOT_FOUND_ERROR));

        return reservation.getStatus() == Status.REVIEWED;
    }

    public ReservationClubResponse checkClubInfo(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new BusinessExceptionHandler("club not found", ErrorCode.NOT_FOUND_ERROR));

        return new ReservationClubResponse().builder()
                .clubName(club.getClubName())
                .address(club.getAddress())
                .price(club.getPrice())
                .businessHours(club.getBusinessHours())
                .build();
    }

    private ReservationResponse ensureNonNullFields(ReservationResponse response, Reservation entity) {
        if (response.getMemberId() == null) {
            response.setMemberId(entity.getMember().getId());
        }
        if (response.getClubId() == null) {
            response.setClubId(entity.getClub().getId());
        }
        if (response.getType() == null) {
            response.setType(entity.getClub().getType());
        }
        if (response.getBusinessHours() == null) {
            response.setBusinessHours(entity.getClub().getBusinessHours());
        }
        if (response.getClubName() == null) {
            response.setClubName(entity.getClub().getClubName());
        }
        if (response.getClubAddress() == null) {
            response.setClubAddress(entity.getClub().getAddress());
        }
        if (response.getReservationId() == null) {
            response.setReservationId(entity.getId());
        }
        if (response.getUserName() == null) {
            response.setUserName(entity.getUserName());
        }
        if (response.getGameCount() == null) {
            response.setGameCount(entity.getGameCount());
        }
        if (response.getPaymentId() == null) {
            response.setPaymentId(entity.getPaymentId());
        }
        if (response.getClubPhoneNumber() == null) {
            response.setClubPhoneNumber(entity.getClub().getPhoneNumber());
        }
        if (response.getPhoneNumber() == null) {
            response.setPhoneNumber(entity.getPhoneNumber());
        }
        return response;
    }

    private Long checkReviewed(Review review) {
        if (review == null) {
            return 0L;
        }
        return review.getId();
    }

}
