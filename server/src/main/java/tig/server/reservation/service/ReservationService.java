package tig.server.reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tig.server.club.domain.Club;
import tig.server.club.mapper.ClubMapper;
import tig.server.club.service.ClubService;
import tig.server.member.domain.Member;
import tig.server.member.mapper.MemberMapper;
import tig.server.member.service.MemberService;
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

    private final MemberService memberService;
    private final ClubService clubService;

    private final MemberMapper memberMapper;
    private final ClubMapper clubMapper;

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
    public ReservationDTO.Response createReservation(Long memberId, Long clubId, ReservationDTO.Request reservationRequest) {
        Member member = memberMapper.responseToEntity(memberService.getMemberById(memberId));
        Club club = clubMapper.responseToEntity(clubService.getClubById(clubId));

        Reservation reservation = reservationMapper.requestToEntity(reservationRequest);
        reservation.setMember(member);
        reservation.setClub(club);

        reservation = reservationRepository.save(reservation);
        ReservationDTO.Response response = reservationMapper.entityToResponse(reservation);
        return response;
    }



}
