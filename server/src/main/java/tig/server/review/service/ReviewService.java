package tig.server.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tig.server.club.domain.Club;
import tig.server.club.repository.ClubRepository;
import tig.server.club.service.ClubService;
import tig.server.global.exception.BusinessExceptionHandler;
import tig.server.global.code.ErrorCode;
import tig.server.member.service.MemberService;
import tig.server.openai.service.OpenAIService;
import tig.server.reservation.domain.Reservation;
import tig.server.reservation.dto.ReservationResponse;
import tig.server.reservation.mapper.ReservationMapper;
import tig.server.reservation.repository.ReservationRepository;
import tig.server.reservation.service.ReservationService;
import tig.server.review.domain.Review;
import tig.server.review.dto.ReviewRequest;
import tig.server.review.dto.ReviewResponse;
import tig.server.review.dto.ReviewWithSummaryResponseDto;
import tig.server.review.dto.ReviewWithReservationDTO;
import tig.server.review.mapper.ReviewMapper;
import tig.server.review.repository.ReviewRepository;

import java.util.Comparator;
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
    private final OpenAIService openAIService;

    private final ReviewMapper reviewMapper = ReviewMapper.INSTANCE;
    private final ReservationMapper reservationMapper = ReservationMapper.INSTANCE;

    @Transactional
    public ReviewWithReservationDTO createReview(Long memberId, Long reservationId, ReviewRequest reviewRequest) {
        try {
            memberService.getMemberById(memberId);// 있는 멤버인지 확인
            Reservation reservation = reservationRepository.findById(reservationId)
                    .orElseThrow(() -> new BusinessExceptionHandler("no reservation found", ErrorCode.NOT_FOUND_ERROR));

            Review review = reviewMapper.requestToEntity(reviewRequest);
            reservationService.reviewReservationById(reservationId);

            review.setReservation(reservation);
            reservation.setReview(review);

            Club club = clubService.reflectNewReview(reviewRequest);
            reservation.setClub(club);

            reviewRepository.save(review);
            reservationRepository.save(reservation);

            // reservation.getClub().getImageUrls() 값을 ReservationResponse에 설정
            ReservationResponse reservationResponse = reservationMapper.entityToResponse(reservation);
            reservationResponse.setImageUrls(reservation.getClub().getImageUrls());

            return ReviewWithReservationDTO.builder()
                    .review(reviewMapper.entityToResponse(review))
                    .reservation(reservationResponse)
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

    public ReviewWithSummaryResponseDto getReviewsByClubId(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new BusinessExceptionHandler("club not found", ErrorCode.NOT_FOUND_ERROR));

        List<Reservation> reservations = club.getReservations();

        // 리뷰가 존재하는 예약만 필터링하고 최신 50개의 리뷰를 가져오기
        List<Review> latestReviews = reservations.stream()
                .map(Reservation::getReview)
                .filter(Objects::nonNull)  // Review가 null이 아닌 것만 필터링
                .filter(review -> review.getContents() != null)  // Review의 내용이 null이 아닌 것만 필터링
                .sorted(Comparator.comparing(review -> review.getReservation().getStartTime(), Comparator.reverseOrder()))  // 최신 순으로 정렬
                .limit(50)  // 최신 50개 리뷰만 추출
                .toList();

        String aiSummary="";
        StringBuilder prompt = new StringBuilder(); // StringBuilder 객체 초기화

        // 최신 50개의 리뷰 중 3개 이상일 때 AI 요약
        if (latestReviews.size() >= 3) {
            latestReviews.forEach(review -> prompt.append(review.getContents()).append(" "));
            aiSummary = openAIService.reviewSummary(prompt.toString()).getChoices().get(0).getMessage().getContent();
        } else {
            aiSummary = "리뷰를 요약하기 위해서는 최소한 3개 이상의 리뷰가 필요해요.";
        }

        // 최신 50개의 리뷰에 해당하는 응답 생성
        List<ReviewResponse> responses = reservations.stream()
                .filter(reservation -> Objects.nonNull(reservation.getReview()))
                .sorted(Comparator.comparing(Reservation::getStartTime, Comparator.reverseOrder())) // 최신 순으로 정렬
                .limit(50)  // 최신 50개의 리뷰만 추출
                .map(reservation -> {
                    Review review = reservation.getReview();
                    return ReviewResponse.builder()
                            .reservationId(reservation.getId())
                            .rating(review.getRating())
                            .contents(review.getContents())
                            .userName(reservation.getMember().getName())
                            .adultCount(reservation.getAdultCount())
                            .teenagerCount(reservation.getTeenagerCount())
                            .kidsCount(reservation.getKidsCount())
                            .startTime(reservation.getStartTime().toString())
                            .build();
                })
                .collect(Collectors.toList());

        return ReviewWithSummaryResponseDto.from(responses, aiSummary);
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
