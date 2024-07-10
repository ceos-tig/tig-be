package tig.server.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tig.server.club.service.ClubService;
import tig.server.error.BusinessExceptionHandler;
import tig.server.error.ErrorCode;
import tig.server.member.dto.MemberDTO;
import tig.server.member.service.MemberService;
import tig.server.reservation.domain.Reservation;
import tig.server.reservation.dto.ReservationDTO;
import tig.server.reservation.mapper.ReservationMapper;
import tig.server.reservation.repository.ReservationRepository;
import tig.server.reservation.service.ReservationService;
import tig.server.review.domain.Review;
import tig.server.review.dto.ReviewDTO;
import tig.server.review.dto.ReviewWithReservationDTO;
import tig.server.review.mapper.ReviewMapper;
import tig.server.review.repository.ReviewRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;

    private final MemberService memberService;
    private final ReservationService reservationService;

    private final ReviewMapper reviewMapper = ReviewMapper.INSTANCE;
    private final ReservationMapper reservationMapper = ReservationMapper.INSTANCE;

    @Transactional
    public void createReview(Long memberId, Long reservationId, ReviewDTO.Request request) {
        try {
            memberService.getMemberById(memberId);// 있는 멤버인지 확인
            Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new BusinessExceptionHandler("no reservation found", ErrorCode.NOT_FOUND_ERROR));
//            ReservationDTO.Response reservationReseponse = reservationService.getReservationById(reservationId);// 있는 예약 내역인지 확인
//
//            Reservation reservation = reservationMapper.responseToEntity(reservationReseponse);

            Review review = reviewMapper.requestToEntity(request);
            review.setReservation(reservation);

            reviewRepository.save(review);
        } catch (Exception e) {
            throw new BusinessExceptionHandler("리뷰 작성 중 에러 : " + e.getMessage(), ErrorCode.IO_ERROR);
        }
    }

    public ReviewWithReservationDTO getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new BusinessExceptionHandler("review not found", ErrorCode.BAD_REQUEST_ERROR));
        ReviewDTO.Response reviewResponse = reviewMapper.entityToResponse(review);
        ReservationDTO.Response reservationResponse = reservationService.getReservationById(review.getReservation().getId());

        return ReviewWithReservationDTO.builder()
                .review(reviewResponse)
                .reservation(reservationResponse)
                .build();
    }
}
