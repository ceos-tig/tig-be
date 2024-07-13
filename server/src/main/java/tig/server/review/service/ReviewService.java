package tig.server.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tig.server.club.domain.Club;
import tig.server.club.repository.ClubRepository;
import tig.server.club.service.ClubService;
import tig.server.error.BusinessExceptionHandler;
import tig.server.error.ErrorCode;
import tig.server.member.service.MemberService;
import tig.server.reservation.domain.Reservation;
import tig.server.reservation.dto.ReservationResponse;
import tig.server.reservation.mapper.ReservationMapper;
import tig.server.reservation.repository.ReservationRepository;
import tig.server.reservation.service.ReservationService;
import tig.server.review.domain.Review;
import tig.server.review.dto.ReviewRequest;
import tig.server.review.dto.ReviewResponse;
import tig.server.review.dto.ReviewWithReservationDTO;
import tig.server.review.mapper.ReviewMapper;
import tig.server.review.repository.ReviewRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final ClubRepository clubRepository;

    private final MemberService memberService;
    private final ReservationService reservationService;
    private final ClubService clubService;

    private final ReviewMapper reviewMapper = ReviewMapper.INSTANCE;
    private final ReservationMapper reservationMapper = ReservationMapper.INSTANCE;

    @Transactional
    public ReviewWithReservationDTO createReview(Long memberId, Long reservationId, ReviewRequest reviewRequest) {
        try {
            memberService.getMemberById(memberId);// 있는 멤버인지 확인
            Reservation reservation = reservationRepository.findById(reservationId)
                    .orElseThrow(() -> new BusinessExceptionHandler("no reservation found", ErrorCode.NOT_FOUND_ERROR));

            Review review = reviewMapper.requestToEntity(reviewRequest);
            review.setReservation(reservation);

            Club club = clubService.reflectNewReview(reviewRequest);
            reservation.setClub(club);

            reviewRepository.save(review);
            reservationRepository.save(reservation);

            return ReviewWithReservationDTO.builder()
                    .review(reviewMapper.entityToResponse(review))
                    .reservation(reservationMapper.entityToResponse(reservation))
                    .build();

        } catch (Exception e) {
            throw new BusinessExceptionHandler("리뷰 작성 중 에러 : " + e.getMessage(), ErrorCode.IO_ERROR);
        }
    }

    @Transactional
    public void modifyReview(Long reviewId, ReviewRequest reviewRequest) {
        try {
            Review existingReview = reviewRepository.findById(reviewId)
                    .orElseThrow(() -> new BusinessExceptionHandler("review not found", ErrorCode.NOT_FOUND_ERROR));

            Review updatedReview = reviewMapper.updateFromRequest(reviewRequest, existingReview);

            updatedReview.setRating(reviewRequest.getRating());
            updatedReview.setContents(reviewRequest.getContents());

            Club club = clubService.reflectModifiedReview(reviewRequest, existingReview);
            Reservation reservation = updatedReview.getReservation();

            updatedReview.setReservation(reservation);
            reservation.setClub(club);

            reviewRepository.save(updatedReview);
            reservationRepository.save(reservation);


        } catch (Exception e) {
            throw new BusinessExceptionHandler("리뷰 수정 중 에러 : " + e.getMessage(), ErrorCode.IO_ERROR);
        }
    }

    public ReviewWithReservationDTO getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new BusinessExceptionHandler("review not found", ErrorCode.BAD_REQUEST_ERROR));
        ReviewResponse reviewResponse = reviewMapper.entityToResponse(review);
        ReservationResponse reservationResponse = reservationService.getReservationById(review.getReservation().getId());

        return ReviewWithReservationDTO.builder()
                .review(reviewResponse)
                .reservation(reservationResponse)
                .build();
    }

    public List<ReviewResponse> getReviewsByClubId(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("club not found"));

        List<Reservation> reservations = club.getReservations();

        return reservations.stream()
                .map(Reservation::getReview)
                .filter(Objects::nonNull)
                .map(reviewMapper::entityToResponse)
                .collect(Collectors.toList());
    }



    @Transactional
    public void deleteReview(Long reviewId) {
        try {
            Review review = reviewRepository.findById(reviewId)
                    .orElseThrow(() -> new BusinessExceptionHandler("review not found", ErrorCode.NOT_FOUND_ERROR));

            List<Reservation> reservations = reservationRepository.findByReview(review);
            for (Reservation reservation : reservations) {
                reservation.setReview(null);
                reservationRepository.save(reservation);
            }

            reviewRepository.delete(review);
        } catch (Exception e) {
            throw new BusinessExceptionHandler("리뷰 삭제 중 에러 : " + e.getMessage(), ErrorCode.IO_ERROR);
        }
    }
}
