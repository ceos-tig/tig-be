package tig.server.reservation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tig.server.club.domain.Club;
import tig.server.club.mapper.ClubMapper;
import tig.server.club.repository.ClubRepository;
import tig.server.club.service.ClubService;
import tig.server.coupon.domain.Coupon;
import tig.server.coupon.repository.CouponRepository;
import tig.server.discord.DiscordMessageProvider;
import tig.server.discord.EventMessage;
import tig.server.enums.PackageCategory;
import tig.server.enums.Status;
import tig.server.enums.Type;
import tig.server.global.exception.BusinessExceptionHandler;
import tig.server.global.code.ErrorCode;
import tig.server.member.domain.Member;
import tig.server.operatinghours.domain.OperatingHours;
import tig.server.operatinghours.dto.OperatingHoursResponse;
import tig.server.operatinghours.repository.OperatingHoursRepository;
import tig.server.packageSet.domain.PackageSet;
import tig.server.packageSet.repository.PackageSetRepository;
import tig.server.payment.dto.PaymentResponseDto;
import tig.server.payment.service.PaymentService;
import tig.server.price.dto.*;
import tig.server.price.repository.*;
import tig.server.reservation.PackageReservationRequest;
import tig.server.reservation.domain.PackageReservation;
import tig.server.reservation.domain.Reservation;
import tig.server.reservation.dto.*;
import tig.server.packageSet.dto.PackagePriceDto;
import tig.server.review.repository.PackageSetReviewRepository;
import tig.server.reservation.mapper.ReservationMapper;
import tig.server.reservation.repository.PackageReservationRepository;
import tig.server.reservation.repository.ReservationRepository;
import tig.server.review.domain.Review;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    private final ClubRepository clubRepository;
    private final TableTennisPriceRepository tableTennisPriceRepository;
    private final BallingPriceRepository ballingPriceRepository;
    private final BaseballPriceRepository baseballPriceRepository;
    private final BilliardsPriceRepository billiardsPriceRepository;
    private final FootballPriceRepository footballPriceRepository;
    private final GolfPriceRepository golfPriceRepository;
    private final SquashPriceRepository squashPriceRepository;
    private final TennisPriceRepository tennisPriceRepository;
    private final OperatingHoursRepository operatingHoursRepository;

    private final ClubService clubService;
    private final PaymentService paymentService;

    private final ClubMapper clubMapper;
    private final ObjectMapper objectMapper;

    private final DiscordMessageProvider discordMessageProvider;
    private final CouponRepository couponRepository;
    private final GolfClubReservationService golfClubReservationService;
    private final PensionReservationService pensionReservationService;
    private final BusReservationService busReservationService;
    private final BuffetReservationService buffetReservationService;
    private final LunchBoxReservationService lunchBoxReservationService;
    private final UniformReservationService uniformReservationService;
    private final PackageSetRepository packageSetRepository;
    private final PackageReservationRepository packageReservationRepository;
    private final PackageSetReviewRepository packageSetReviewRepository;

    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(reservationMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    public ReservationResponse getReservationById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessExceptionHandler("reservation not found",ErrorCode.NOT_FOUND_ERROR));
        ReservationResponse response = reservationMapper.entityToResponse(reservation);

        String updatedAt = null;
        String provider = null;
        if (reservation.getPrice() > 0) { // 0원 초과 결제 (정상 가격 결제)
            // get response from portone
            PaymentResponseDto paymentResponseDto = paymentService.getPaymentResponse(response.getPaymentId()).block();
            provider = paymentResponseDto.getMethod().getProvider();
            updatedAt = paymentResponseDto.getUpdatedAt();
        } else { // 0원 이하 결제
            provider = reservation.getProvider();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            updatedAt = reservation.getUpdatedAt()
                    .atZone(ZoneId.of("Asia/Seoul"))
                    .format(formatter);
        }


        // Convert updatedAt to Korean time
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(updatedAt, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            ZonedDateTime koreanDateTime = localDateTime.atZone(ZoneId.of("Asia/Seoul"));
            updatedAt = koreanDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        } catch (DateTimeParseException e) {
            // Handle parsing exception if necessary
            e.printStackTrace();
        }

        Club club = reservation.getClub();
        response.setType(club.getType());
        response.setClubName(club.getClubName());
        response.setClubAddress(club.getAddress());
        response.setReservationId(reservation.getId());
        response.setClubId(club.getId());
        response.setType(club.getType());
        response.setClubName(club.getClubName());
        response.setProvider(provider);
        response.setUpdatedAt(updatedAt);
        response.setImageUrls(club.getImageUrls());

        // check is review null
        response.setReviewed(reservation.getReview() != null);
        response.setPaymentId(reservation.getPaymentId());

        response.setReviewId(checkReviewed(reservation.getReview()));
        response.setMemberId(reservation.getMember().getId());
        response.setClubPhoneNumber(club.getPhoneNumber());
        
        return response;
    }

    @Transactional
    public ReservationResponse createReservation(Member member, Long clubId, ReservationRequest reservationRequest) throws ParseException {
        Coupon usedCoupon = null;
        if(reservationRequest.getCouponId() != -1){ // 쿠폰 사용했다면
            usedCoupon = couponRepository.findById(reservationRequest.getCouponId())
                    .orElseThrow(() -> new BusinessExceptionHandler("coupon not found", ErrorCode.NOT_FOUND_ERROR));

            usedCoupon.markAsDeleted();
        }

        Club club = clubMapper.responseToEntity(clubService.getClubById(clubId));

        Reservation reservation = reservationMapper.requestToEntity(reservationRequest);

        reservation.setMember(member);
        reservation.setClub(club);

        if (usedCoupon != null) {
            reservation.addCouponDiscountPrice(usedCoupon.getDiscount());
            reservation.injectCoupon(usedCoupon);
        } else {
            reservation.addCouponDiscountPrice(0);
        }

        if (club.getType() == Type.GAME) {
            reservationRequest.setEndTime("2000-01-01T00:00:00");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        try {
            // Trim any potential whitespace
            String dateString = reservationRequest.getDate().trim();
            String startTimeString = reservationRequest.getStartTime().trim();
            String endTimeString = reservationRequest.getEndTime().trim();

            // Parse the input strings using the custom formatter
            LocalDateTime date = LocalDateTime.parse(dateString, formatter);
            LocalDateTime startTime = LocalDateTime.parse(startTimeString, formatter);
            LocalDateTime endTime = LocalDateTime.parse(endTimeString, formatter);

            // Set the parsed LocalDateTime objects to the reservation
            reservation.setDate(date);
            reservation.setStartTime(startTime);
            reservation.setEndTime(endTime);

            // Save the reservation
            reservation = reservationRepository.save(reservation);
        } catch (DateTimeParseException e) {
            // Handle the parsing error
            System.err.println("Unparseable date: " + e.getMessage());
            e.printStackTrace();
        }


        ReservationResponse response = reservationMapper.entityToResponse(reservation);
        response.setReservationId(reservation.getId());
        response.setMemberId(member.getId());
        response.setClubId(clubId);
        response.setType(club.getType());
        response.setClubName(club.getClubName());
        response.setPaymentId(reservation.getPaymentId());
        response.setGameCount(reservation.getGameCount());
        response.setReviewId(checkReviewed(reservation.getReview()));
        response.setGameDescription(reservation.getGameDescription());

        // discord-webhook
        discordMessageProvider.sendApplicationMessage(EventMessage.RESERVATION_APPLICATION, response);

        return response;
    }

    @Transactional
    public List<ReservationResponse> getReservationByMemberId(Long memberId) {
        List<Reservation> reservations = reservationRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BusinessExceptionHandler("reservation not found", ErrorCode.NOT_FOUND_ERROR));

        return reservations.stream()
                .map(entity -> {
                    ReservationResponse response = ensureNonNullFields(reservationMapper.entityToResponse(entity), entity);
                    doneReservation(response, entity);
                    response.setReviewId(checkReviewed(entity.getReview()));
                    response.setImageUrls(entity.getClub().getImageUrls());
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
    public void cancelReservationById(Long reservationId, Long couponId) throws ParseException {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessExceptionHandler("reservation not found",ErrorCode.NOT_FOUND_ERROR));

        Coupon coupon = null;
        if (couponId != null) { // 쿠폰을 사용했다면 "쿠폰 사용 취소"를 위해 검색
            coupon = couponRepository.findByIdIgnoringSoftDelete(couponId)
                    .orElseThrow(() -> new BusinessExceptionHandler("coupon not found", ErrorCode.NOT_FOUND_ERROR));
            reservation.minusCouponDiscountPrice(coupon.getDiscount());
            coupon.restore();
            reservation.cancelReservation();
        }
        // Define the list of valid statuses
        List<Status> validStatuses = Arrays.asList(Status.TBC, Status.CONFIRMED);

        // Check if the reservation status is not in the list of valid statuses
        if (!validStatuses.contains(reservation.getStatus())) {
            throw new BusinessExceptionHandler("Cannot cancel a reservation with status " + reservation.getStatus(),ErrorCode.BAD_REQUEST_ERROR);
        }

        reservation.setStatus(Status.CANCELED);

        ReservationResponse response = reservationMapper.entityToResponse(reservation);
        response.setType(reservation.getClub().getType());
        response.setUserName(reservation.getUserName());
        response.setClubName(reservation.getClub().getClubName());
        response.setReservationId(reservationId);

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

    @Scheduled(cron = "0 1,31 * * * *") // 매시 1분과 31분에 실행
    @Transactional
    public void updateReservationsToDone() {
        // 현재 시간 가져오기
        LocalDateTime now = LocalDateTime.now();

        // CONFIRMED 상태이고, 예약 시간이 현재 시간보다 이전인 예약 조회
        List<Reservation> reservationsToUpdate = reservationRepository.findByStatusAndStartTimeBefore(Status.CONFIRMED, now);

        // 상태를 DONE으로 업데이트
        for (Reservation reservation : reservationsToUpdate) {
            reservation.setStatus(Status.DONE);
        }

        reservationRepository.saveAll(reservationsToUpdate); // 일괄 저장
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

        // 스포츠 프로그램별 가격 정보 조회
        List<?> priceResponses = getPriceResponsesByCategory(club);

        // 클럽의 운영 시간 정보 조회
        List<OperatingHours> operatingHours = operatingHoursRepository.findByClub_Id(club.getId());
        List<OperatingHoursResponse> operatingHoursResponses = operatingHours.stream()
                .map(hours -> new OperatingHoursResponse(hours.getDayOfWeek(), hours.getStartTime(), hours.getEndTime()))
                .collect(Collectors.toList());

        // ReservationClubResponse로 반환
        return ReservationClubResponse.builder()
                .clubName(club.getClubName())
                .address(club.getAddress())
                .prices(priceResponses)  // 가격 정보 설정
                .operatingHours(operatingHoursResponses)  // 운영 시간 정보 설정
                .category(club.getCategory())
                .build();
    }

    public ReservationPackageSetResponse checkPackageSetInfo(Long packageSetId) {
        PackageSet packageSet = packageSetRepository.findById(packageSetId)
                .orElseThrow(() -> new BusinessExceptionHandler("package set not found", ErrorCode.NOT_FOUND_ERROR));

        // 패키지 가격 정보 조회
        List<PackagePriceDto> priceResponses = packageSet.getPackagePrices().stream()
                .map(packagePrice -> PackagePriceDto.builder()
                        .id(packagePrice.getId())
                        .optionType(packagePrice.getOptionType())
                        .optionValue(packagePrice.getOptionValue())
                        .price(packagePrice.getPrice())
                        .description(packagePrice.getDescription())
                        .isDefault(packagePrice.getIsDefault())
                        .build())
                .collect(Collectors.toList());

        // 리뷰 통계 정보 조회
        Double averageRating = packageSetReviewRepository.findAverageRatingByPackageSetId(packageSetId);
        Long reviewCount = packageSetReviewRepository.countByPackageSetId(packageSetId);

        // ReservationPackageSetResponse로 반환
        return ReservationPackageSetResponse.builder()
                .packageSetName(packageSet.getName())
                .address(packageSet.getAddress())
                .prices(priceResponses)
                .category(packageSet.getCategory())
                .averageRating(averageRating != null ? averageRating : 0.0)
                .reviewCount(reviewCount != null ? reviewCount : 0L)
                .build();
    }

    private List<?> getPriceResponsesByCategory(Club club) {
        switch (club.getCategory()) {
            case TABLE_TENNIS:
                return tableTennisPriceRepository.findByClub(club)
                        .stream()
                        .map(price -> new TableTennisPriceResponse(
                                price.getProgramName(), price.getPrice(), price.getDuration()))
                        .collect(Collectors.toList());
            case BALLING:
                return ballingPriceRepository.findByClub(club)
                        .stream()
                        .map(price -> new BallingPriceResponse(
                                price.getProgramName(), price.getDayOfWeek(),
                                price.getStartTime(), price.getEndTime(),
                                price.getGameCount(), price.getPrice()))
                        .collect(Collectors.toList());
            case GOLF:
                return golfPriceRepository.findByClub(club)
                        .stream()
                        .map(price -> new GolfPriceResponse(
                                price.getProgramName(), price.getDayOfWeek(),
                                price.getStartTime(), price.getEndTime(), price.getPrice(),
                                price.getHoles(), price.getDuration()))
                        .collect(Collectors.toList());
            case BILLIARDS:
                return billiardsPriceRepository.findByClub(club)
                        .stream()
                        .map(price -> new BilliardsPriceResponse(
                                price.getProgramName(), price.getDuration(), price.getPrice()))
                        .collect(Collectors.toList());
            case FOOTBALL:
                return footballPriceRepository.findByClub(club)
                        .stream()
                        .map(price -> new FootballPriceResponse(
                                price.getProgramName(), price.getDuration(), price.getPrice()))
                        .collect(Collectors.toList());
            case BASEBALL:
                return baseballPriceRepository.findByClub(club)
                        .stream()
                        .map(price -> new BaseballPriceResponse(
                                price.getProgramName(),
                                price.getInning(),
                                price.getDuration(),
                                price.getStartTime(),
                                price.getEndTime(),
                                price.getPrice()))
                        .collect(Collectors.toList());
            case TENNIS:
                return tennisPriceRepository.findByClub(club)
                        .stream()
                        .map(price -> new TennisPriceResponse(
                                price.getProgramName(), price.getDayOfWeek(),
                                price.getDuration(),
                                price.getPrice(),
                                price.getCountPerWeek()))
                        .collect(Collectors.toList());
            case SQUASH:
                return squashPriceRepository.findByClub(club)
                        .stream()
                        .map(price -> new SquashPriceResponse(
                                price.getProgramName(),
                                price.getDurationInMonths(),
                                price.getLessonCount(),
                                price.getDuration(),
                                price.getPrice()))
                        .collect(Collectors.toList());
            default:
                throw new BusinessExceptionHandler("Invalid program type", ErrorCode.NOT_VALID_ERROR);
        }
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

    @Transactional
    public PackageReservationResponse createPackageReservation(Member member, tig.server.reservation.dto.PackageReservationRequest request) {
        // 패키지 존재 확인
        PackageSet packageSet = packageSetRepository.findById(request.getPackageSetId())
                .orElseThrow(() -> new BusinessExceptionHandler("패키지를 찾을 수 없습니다.", ErrorCode.NOT_FOUND_ERROR));

        // 요청 데이터 유효성 검증
        validatePackageReservationRequest(request);

        // PackageReservation 생성
        PackageReservation reservation = PackageReservation.builder()
                .member(member)
                .packageSet(packageSet)
                .category(request.getPackageCategory())
                .message(request.getMessage())
                .status(Status.TBC)
                .build();

        PackageReservation savedReservation = packageReservationRepository.save(reservation);

        // 응답 생성
        return buildPackageReservationResponse(savedReservation, request);
    }

    private void validatePackageReservationRequest(tig.server.reservation.dto.PackageReservationRequest request) {
        // 기본 유효성 검증
        if (request.getPackageSetId() == null) {
            throw new BusinessExceptionHandler("패키지 ID는 필수입니다.", ErrorCode.BAD_REQUEST_ERROR);
        }
        
        if (request.getPackageCategory() == null) {
            throw new BusinessExceptionHandler("패키지 카테고리는 필수입니다.", ErrorCode.BAD_REQUEST_ERROR);
        }

        if (!request.isValidForCategory()) {
            throw new BusinessExceptionHandler("패키지 카테고리에 맞는 옵션이 필요합니다.", ErrorCode.BAD_REQUEST_ERROR);
        }

        if (request.getTotalParticipants() <= 0) {
            throw new BusinessExceptionHandler("총 참여 인원은 1명 이상이어야 합니다.", ErrorCode.BAD_REQUEST_ERROR);
        }

        // 날짜 검증 (날짜가 제공된 경우)
        if (request.getDate() != null) {
            try {
                LocalDate reservationDate = LocalDate.parse(request.getDate());
                if (reservationDate.isBefore(LocalDate.now())) {
                    throw new BusinessExceptionHandler("과거 날짜로는 예약할 수 없습니다.", ErrorCode.BAD_REQUEST_ERROR);
                }
            } catch (Exception e) {
                throw new BusinessExceptionHandler("올바른 날짜 형식을 입력해주세요. (YYYY-MM-DD)", ErrorCode.BAD_REQUEST_ERROR);
            }
        }

        // 카테고리별 추가 검증
        validateByCategory(request);
    }

    private void validateByCategory(tig.server.reservation.dto.PackageReservationRequest request) {
        switch (request.getPackageCategory()) {
            case GOLF_COURSE:
                if (request.getTotalParticipants() > 4) {
                    throw new BusinessExceptionHandler("골프장 예약은 최대 4명까지 가능합니다.", ErrorCode.BAD_REQUEST_ERROR);
                }
                break;
            case BUS:
                String busType = request.getBusType();
                if (busType != null && busType.contains("24인승") && request.getTotalParticipants() > 24) {
                    throw new BusinessExceptionHandler("24인승 버스는 최대 24명까지 탑승 가능합니다.", ErrorCode.BAD_REQUEST_ERROR);
                }
                if (busType != null && busType.contains("45인승") && request.getTotalParticipants() > 45) {
                    throw new BusinessExceptionHandler("45인승 버스는 최대 45명까지 탑승 가능합니다.", ErrorCode.BAD_REQUEST_ERROR);
                }
                break;
            case GROUP_UNIFORM:
                if (request.getUniformSize() == null) {
                    throw new BusinessExceptionHandler("단체복 사이즈를 선택해주세요.", ErrorCode.BAD_REQUEST_ERROR);
                }
                break;
            case CATERING:
            case PENSION:
            case LUNCH_BOX:
                // 기본 검증만 수행
                break;
        }
    }

    private PackageReservationResponse buildPackageReservationResponse(PackageReservation savedReservation, tig.server.reservation.dto.PackageReservationRequest request) {
        PackageReservationResponse response = PackageReservationResponse.from(savedReservation);
        
        // 요청 정보를 응답에 추가
        response.setDate(request.getDate());
        response.setStartTime(request.getStartTime());
        response.setEndTime(request.getEndTime());
        response.setAdultCount(request.getAdultCount());
        response.setTeenagerCount(request.getTeenagerCount());
        response.setKidsCount(request.getKidsCount());
        response.setUserName(request.getUserName());
        response.setPhoneNumber(request.getPhoneNumber());
        response.setTotalPrice(request.getTotalPrice());
        response.setPackageOptions(request.getPackageOptions());
        
        return response;
    }

}
