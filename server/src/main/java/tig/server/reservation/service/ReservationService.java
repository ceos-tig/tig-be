package tig.server.reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tig.server.club.domain.Club;
import tig.server.club.mapper.ClubMapper;
import tig.server.club.service.ClubService;
import tig.server.discord.DiscordMessageProvider;
import tig.server.discord.EventMessage;
import tig.server.enums.Status;
import tig.server.enums.Type;
import tig.server.error.BusinessExceptionHandler;
import tig.server.error.ErrorCode;
import tig.server.member.domain.Member;
import tig.server.member.mapper.MemberMapper;
import tig.server.member.service.MemberService;
import tig.server.reservation.domain.Reservation;
import tig.server.reservation.dto.ReservationRequest;
import tig.server.reservation.dto.ReservationResponse;
import tig.server.reservation.mapper.ReservationMapper;
import tig.server.reservation.repository.ReservationRepository;
import tig.server.review.domain.Review;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    private final MemberService memberService;
    private final ClubService clubService;

    private final MemberMapper memberMapper;
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
        Club club = clubMapper.responseToEntity(clubService.getClubById(reservationId));
        response.setType(club.getType());
        response.setBusinessHours(club.getBusinessHours());
        response.setClubName(club.getClubName());
        response.setClubAddress(club.getAddress());
        response.setMemberName(reservation.getMember().getName());
        response.setReservationId(reservation.getId());
        response.setClubId(club.getId());
        response.setType(club.getType());
        response.setBusinessHours(club.getBusinessHours());
        response.setClubName(club.getClubName());

        // check is review null
        response.setReviewed(reservation.getReview() != null);
        response.setPaymentId(reservation.getPaymentId());
        
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

        // discord-webhook
        String memberName = member.getName();
        String clubName = club.getClubName();

        discordMessageProvider.sendApplicationMessage(EventMessage.RESERVATION_APPLICATION, memberName, clubName);

        return response;
    }

    @Transactional
    public List<ReservationResponse> getReservationByMemberId(Long memberId) {
        List<Reservation> reservations = reservationRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BusinessExceptionHandler("reservation not found", ErrorCode.BAD_REQUEST_ERROR));

        for (Reservation reservation : reservations) {
            try {
                doneReservationById(reservation.getId());
            } catch (BusinessExceptionHandler e) {
                // Handle the exception or log it as needed
                System.out.println("Exception updating reservation: " + e.getMessage());
            }
        }

        return reservations.stream()
                .map(entity -> {
                    ReservationResponse response = reservationMapper.entityToResponse(entity);
                    response.setReviewId(checkReviewed(entity.getReview()));
                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<ReservationResponse> getProceedingReservationByMemberId(Long memberId) {
        List<Status> proceedingStatuses = List.of(Status.TBC, Status.CONFIRMED);
        List<Reservation> reservations = reservationRepository.findReservationsByMemberIdAndStatus(memberId, proceedingStatuses);
        return reservations.stream()
                .map(entity -> ensureNonNullFields(reservationMapper.entityToResponse(entity), entity))
                .collect(Collectors.toList());
    }

    public List<ReservationResponse> getTerminatedReservationByMemberId(Long memberId) {
        List<Status> proceedingStatuses = List.of(Status.DECLINED, Status.DONE, Status.CANCELED, Status.REVIEWED);
        List<Reservation> reservations = reservationRepository.findReservationsByMemberIdAndStatus(memberId, proceedingStatuses);
        return reservations.stream()
                .map(entity -> ensureNonNullFields(reservationMapper.entityToResponse(entity), entity))
                .collect(Collectors.toList());
    }


    public List<ReservationResponse> getCanceledReservationByMemberId(Long memberId) {
        List<Status> proceedingStatuses = List.of(Status.CANCELED);
        List<Reservation> reservations = reservationRepository.findReservationsByMemberIdAndStatus(memberId, proceedingStatuses);
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

        Member member = reservation.getMember();
        Club club = reservation.getClub();

        // discord-webhook
        String memberName = member.getName();
        String clubName = club.getClubName();

        discordMessageProvider.sendCancelMessage(EventMessage.RESERVATION_CANCEL, memberName, clubName);

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
                .orElseThrow(() -> new BusinessExceptionHandler("reservation not found", ErrorCode.NOT_FOUND_ERROR));

        List<Status> validStatuses = Arrays.asList(Status.CONFIRMED);

        if (!validStatuses.contains(reservation.getStatus())) {
            throw new BusinessExceptionHandler("Cannot done a reservation with status " + reservation.getStatus(), ErrorCode.BAD_REQUEST_ERROR);
        }

        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(reservation.getStartTime())) {
            throw new BusinessExceptionHandler("Cannot done a reservation before its start time", ErrorCode.BAD_REQUEST_ERROR);
        }

        reservation.setStatus(Status.DONE);
        reservationRepository.save(reservation);
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
        if (response.getMemberName() == null) {
            response.setMemberName(entity.getMember().getName());
        }
        if (response.getGameCount() == null) {
            response.setGameCount(entity.getGameCount());
        }
        if (response.getPaymentId() == null) {
            response.setPaymentId(entity.getPaymentId());
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
