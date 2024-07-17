package tig.server.reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tig.server.club.domain.Club;
import tig.server.club.mapper.ClubMapper;
import tig.server.club.service.ClubService;
import tig.server.discord.DiscordMessageProvider;
import tig.server.discord.EventMessage;
import tig.server.enums.Status;
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

import java.util.ArrayList;
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
                .orElseThrow(() -> new RuntimeException("reservation not found"));
        ReservationResponse response = reservationMapper.entityToResponse(reservation);
        Club club = clubMapper.responseToEntity(clubService.getClubById(reservationId));

        response.setType(club.getType());
        response.setBusinessHours(club.getBusinessHours());
        response.setClubName(club.getClubName());
        response.setClubAddress(club.getAddress());

        response.setReservationId(reservation.getId());
        response.setClubId(club.getId());
        response.setType(club.getType());
        response.setBusinessHours(club.getBusinessHours());
        response.setClubName(club.getClubName());


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

        reservation = reservationRepository.save(reservation);

        ReservationResponse response = reservationMapper.entityToResponse(reservation);
        response.setReservationId(reservation.getId());
        response.setMemberId(member.getId());
        response.setClubId(clubId);
        response.setType(club.getType());
        response.setBusinessHours(club.getBusinessHours());
        response.setClubName(club.getClubName());

        // discord-webhook
        String memberName = member.getName();
        String clubName = club.getClubName();

        discordMessageProvider.sendApplicationMessage(EventMessage.RESERVATION_APPLICATION, memberName, clubName);

        return response;
    }

    public List<ReservationResponse> getReservationByMemberId(Long memberId) {
        List<Reservation> reservations = reservationRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BusinessExceptionHandler("reservation not found", ErrorCode.BAD_REQUEST_ERROR));
        return reservations.stream()
                .map(reservationMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    public List<ReservationResponse> getProceedingReservationByMemberId(Long memberId) {
        List<Status> proceedingStatuses = List.of(Status.TBC, Status.CONFIRMED);
        List<Reservation> reservations = reservationRepository.findReservationsByMemberIdAndStatus(memberId, proceedingStatuses);
        return reservations.stream()
                .map(reservationMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    public List<ReservationResponse> getTerminatedReservationByMemberId(Long memberId) {
        List<Status> proceedingStatuses = List.of(Status.DECLINED, Status.DONE, Status.CANCELED, Status.REVIEWED);
        List<Reservation> reservations = reservationRepository.findReservationsByMemberIdAndStatus(memberId, proceedingStatuses);
        return reservations.stream()
                .map(reservationMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    public List<ReservationResponse> getCanceledReservationByMemberId(Long memberId) {
        List<Status> proceedingStatuses = List.of(Status.CANCELED);
        List<Reservation> reservations = reservationRepository.findReservationsByMemberIdAndStatus(memberId, proceedingStatuses);
        return reservations.stream()
                .map(reservationMapper::entityToResponse)
                .collect(Collectors.toList());
    }


    @Transactional
    public void cancelReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("reservation not found"));

        // Define the list of valid statuses
        List<Status> validStatuses = Arrays.asList(Status.TBC, Status.CONFIRMED);

        // Check if the reservation status is not in the list of valid statuses
        if (!validStatuses.contains(reservation.getStatus())) {
            throw new RuntimeException("Cannot cancel a reservation with status " + reservation.getStatus());
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
    public void confirmReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("reservation not found"));

        // Define the list of valid statuses
        List<Status> validStatuses = Arrays.asList(Status.TBC);

        // Check if the reservation status is not in the list of valid statuses
        if (!validStatuses.contains(reservation.getStatus())) {
            throw new RuntimeException("Cannot confirm a reservation with status " + reservation.getStatus());
        }

        reservation.setStatus(Status.CONFIRMED);
        reservationRepository.save(reservation);
    }

    @Transactional
    public void declineReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("reservation not found"));

        // Define the list of valid statuses
        List<Status> validStatuses = Arrays.asList(Status.TBC);

        // Check if the reservation status is not in the list of valid statuses
        if (!validStatuses.contains(reservation.getStatus())) {
            throw new RuntimeException("Cannot decline a reservation with status " + reservation.getStatus());
        }

        reservation.setStatus(Status.DECLINED);
        reservationRepository.save(reservation);
    }

    public boolean checkReservationIsReviewedById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("reservation not found"));

        return reservation.getStatus() == Status.REVIEWED;
    }

}
