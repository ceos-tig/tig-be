package tig.server.club.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tig.server.amenity.service.AmenityService;
import tig.server.club.domain.Club;
import tig.server.club.dto.*;
import tig.server.club.mapper.ClubMapper;
import tig.server.club.repository.ClubRepository;
import tig.server.config.S3Uploader;
import tig.server.enums.Category;
import tig.server.enums.District;
import tig.server.enums.Facility;
import tig.server.global.exception.BusinessExceptionHandler;
import tig.server.global.code.ErrorCode;
import tig.server.operatinghours.domain.OperatingHours;
import tig.server.operatinghours.dto.OperatingHoursResponse;
import tig.server.operatinghours.repository.OperatingHoursRepository;
import tig.server.price.domain.Price;
import tig.server.price.dto.*;
import tig.server.price.repository.*;
import tig.server.program.domain.Program;
import tig.server.program.dto.ProgramResponse;
import tig.server.program.repository.ProgramRepository;
import tig.server.reservation.domain.Reservation;
import tig.server.reservation.repository.ReservationRepository;
import tig.server.review.domain.Review;
import tig.server.review.dto.ReviewRequest;
import tig.server.wishlist.domain.Wishlist;
import tig.server.wishlist.repository.WishlistRepository;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubService {

    private final ClubRepository clubRepository;
    private final ReservationRepository reservationRepository;
    private final WishlistRepository wishlistRepository;
    private final PriceRepository priceRepository;
    private final OperatingHoursRepository operatingHoursRepository;
    private final ProgramRepository programRepository;
    private final TableTennisPriceRepository tableTennisPriceRepository;
    private final BallingPriceRepository ballingPriceRepository;
    private final BaseballPriceRepository baseballPriceRepository;
    private final BilliardsPriceRepository billiardsPriceRepository;
    private final FootballPriceRepository footballPriceRepository;
    private final GolfPriceRepository golfPriceRepository;
    private final SquashPriceRepository squashPriceRepository;
    private final TennisPriceRepository tennisPriceRepository;

    private final AmenityService amenityService;

    private final ClubMapper clubMapper = ClubMapper.INSTANCE;

    private final S3Uploader s3Uploader;

    private final ObjectProvider<ClubService> serviceProvider;

    public List<ClubResponse> getAllClubs() {
        return clubRepository.findAll().stream()
                .map(club -> {
                    // 클럽과 연결된 프로그램 조회
                    List<Program> programs = programRepository.findByClub_Id(club.getId());

                    // 각 프로그램에 대한 가격 정보 조회
                    List<PriceResponse> priceResponses = programs.stream()
                            .flatMap(program -> priceRepository.findByProgram(program).stream())
                            .map(price -> new PriceResponse(price.getDayOfWeek(), price.getStartTime(), price.getEndTime(), price.getPrice()))
                            .collect(Collectors.toList());

                    // 클럽의 운영 시간 정보 조회
                    List<OperatingHours> operatingHours = operatingHoursRepository.findByClub_Id(club.getId());
                    List<OperatingHoursResponse> operatingHoursResponses = operatingHours.stream()
                            .map(hours -> new OperatingHoursResponse(hours.getDayOfWeek(), hours.getStartTime(), hours.getEndTime()))
                            .collect(Collectors.toList());

                    // 클럽의 프로그램 정보 설정
                    List<ProgramResponse> programResponses = programs.stream()
                            .map(program -> new ProgramResponse(program.getProgramId(), program.getProgramName().name()))
                            .collect(Collectors.toList());

                    // ClubResponse로 변환 후 필드 설정
                    ClubResponse response = clubMapper.entityToResponse(club);
                    response.setPrices(priceResponses);
                    response.setOperatingHours(operatingHoursResponses);
                    response.setPrograms(programResponses);

                    // 평균 평점 계산
                    return calculateAvgRating(response);
                })
                .collect(Collectors.toList());
    }

    public ClubResponse getClubById(Long id) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new BusinessExceptionHandler("club not found", ErrorCode.NOT_FOUND_ERROR));
        List<String> imageUrls = s3Uploader.getImageUrls(club.getImageUrls());
        club.setImageUrls(imageUrls);

        List<Facility> amenities = amenityService.getAmenitiesByClubId(club.getId());
        List<?> priceResponses = getPriceResponsesByCategory(club);  // 가격 정보 조회

        List<OperatingHours> operatingHours = operatingHoursRepository.findByClub_Id(club.getId());
        List<OperatingHoursResponse> operatingHoursResponses = operatingHours.stream()
                .map(hours -> new OperatingHoursResponse(hours.getDayOfWeek(), hours.getStartTime(), hours.getEndTime()))
                .collect(Collectors.toList());

        ClubResponse clubResponse = clubMapper.entityToResponse(club);
        clubResponse.setAmenities(amenities);
        clubResponse.setPresignedImageUrls(imageUrls);
        clubResponse.setPrices(priceResponses);
        clubResponse.setOperatingHours(operatingHoursResponses);

        return calculateAvgRating(clubResponse);
    }

    private List<?> getPriceResponsesByCategory(Club club) {
        switch (club.getCategory()) {
            case TABLE_TENNIS:
                return tableTennisPriceRepository.findByClub(club)
                        .stream()
                        .map(price -> new TableTennisPriceResponse(
                                price.getProgramName(), price.getPrice(), price.getDurationType()))
                        .collect(Collectors.toList());
            case BALLING:
                return ballingPriceRepository.findByClub(club)
                        .stream()
                        .map(price -> new BallingPriceResponse(
                                price.getProgramName(), price.getDayOfWeek(),
                                price.getStartTime(), price.getEndTime(), price.getPrice()))
                        .collect(Collectors.toList());
            case GOLF:
                return golfPriceRepository.findByClub(club)
                        .stream()
                        .map(price -> new GolfPriceResponse(
                                price.getProgramName(), price.getDayOfWeek(),
                                price.getStartTime(), price.getEndTime(), price.getPrice()))
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
                                price.getProgramType(), price.getDuration(), price.getPrice()))
                        .collect(Collectors.toList());
            case TENNIS:
                return tennisPriceRepository.findByClub(club)
                        .stream()
                        .map(price -> new TennisPriceResponse(
                                price.getProgramType(), price.getDayOfWeek(), price.getDuration(), price.getPrice()))
                        .collect(Collectors.toList());
            case SQUASH:
                return squashPriceRepository.findByClub(club)
                        .stream()
                        .map(price -> new SquashPriceResponse(
                                price.getProgramName(), price.getDurationInMonths(), price.getPrice()))
                        .collect(Collectors.toList());
            default:
                throw new BusinessExceptionHandler("Invalid program type", ErrorCode.NOT_VALID_ERROR);
        }
    }

    public ClubResponse getClubByIdForLoginUser(Long memberId, Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new BusinessExceptionHandler("club not found", ErrorCode.NOT_FOUND_ERROR));
        Optional<Wishlist> wishlist = wishlistRepository.findByMemberIdAndClubId(memberId, clubId);

        List<String> imageUrls = club.getImageUrls();
        List<Facility> amenities = amenityService.getAmenitiesByClubId(club.getId());
        List<?> priceResponses = getPriceResponsesByCategory(club);  // 가격 정보 조회

        List<OperatingHours> operatingHours = operatingHoursRepository.findByClub_Id(club.getId());
        List<OperatingHoursResponse> operatingHoursResponses = operatingHours.stream()
                .map(hours -> new OperatingHoursResponse(hours.getDayOfWeek(), hours.getStartTime(), hours.getEndTime()))
                .collect(Collectors.toList());

        ClubResponse clubResponse = clubMapper.entityToResponse(club);
        clubResponse.setAmenities(amenities);
        clubResponse.setPresignedImageUrls(imageUrls);
        clubResponse.setPrices(priceResponses);
        clubResponse.setOperatingHours(operatingHoursResponses);

        clubResponse.setIsHeart(wishlist.isPresent());  // 좋아요 여부 설정

        return calculateAvgRating(clubResponse);
    }



    private ClubResponse calculateAvgRating(ClubResponse clubResponse) {
        float avgRating = 0f;
        if (clubResponse.getRatingCount() == null) {
            clubResponse.setRatingCount(0);
        }
        if (clubResponse.getRatingSum() == null) {
            clubResponse.setRatingSum(0f);
        }
        if (clubResponse.getRatingCount() != 0) {
            avgRating = (float) clubResponse.getRatingSum() / clubResponse.getRatingCount();
            // round at 1 decimal place
            avgRating = Math.round(avgRating * 10) / 10.0f;
        }
        clubResponse.setAvgRating(avgRating);
        return clubResponse;
    }

    @Transactional
    public Club reflectNewReview(ReviewRequest reviewRequest) {
        Long reservationId = reviewRequest.getReservationId();
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessExceptionHandler("reservation not found",ErrorCode.NOT_FOUND_ERROR));

        Club club = clubRepository.findById(reservation.getClub().getId())
                .orElseThrow(() -> new BusinessExceptionHandler("club not found",ErrorCode.NOT_FOUND_ERROR));

        Float rating = reviewRequest.getRating();

        club.accumulateRatingSum(rating);
        club.increaseRatingCount(rating);

        clubRepository.save(club);

        return club;
    }

    @Transactional
    public Club reflectModifiedReview(ReviewRequest reviewRequest, Review existingReview) {
        Long reservationId = reviewRequest.getReservationId();
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessExceptionHandler("reservation not found",ErrorCode.NOT_FOUND_ERROR));

        Club club = clubRepository.findById(reservation.getClub().getId())
                .orElseThrow(() -> new BusinessExceptionHandler("club not found",ErrorCode.NOT_FOUND_ERROR));


        Float existingRating = existingReview.getRating();
        club.reduceRatingSum(existingRating);

        Float newRating = reviewRequest.getRating();
        club.accumulateRatingSum(newRating);

        clubRepository.save(club);

        return club;
    }

    public HomeResponse getHomeClubs(HomeRequest homeRequest) {
        Float requestLatitude = homeRequest.getLatitude();
        Float requestLongitude = homeRequest.getLongitude();

        ClubService service = serviceProvider.getObject();

        List<ClubResponse> nearestClubs = service.optimizedParallelFindNearestClubs(requestLatitude, requestLongitude, 5).stream()
                .peek(clubResponse -> clubResponse.setPresignedImageUrls(clubResponse.getImageUrls()))
                .collect(Collectors.toList());

        List<ClubResponse> popularClubs = service.getPopularClubs().stream()
                .peek(clubResponse -> clubResponse.setPresignedImageUrls(clubResponse.getImageUrls()))
                .collect(Collectors.toList());

        List<ClubResponse> recommendedClubs = service.getRecommendedClubs(10).stream()
                .peek(clubResponse -> clubResponse.setPresignedImageUrls(clubResponse.getImageUrls()))
                .collect(Collectors.toList());

        Map<Category, List<CategoryClubResponse>> nearestClubsByCategory = service.findNearestClubsByCategory(requestLatitude, requestLongitude, 10);

        nearestClubsByCategory.forEach((category, categoryClubResponses) ->
                categoryClubResponses.forEach(categoryClubResponse ->
                        categoryClubResponse.setPresignedImageUrls(categoryClubResponse.getImageUrls())
                )
        );

        return HomeResponse.builder()
                .nearestClubs(nearestClubs)
                .popularClubs(popularClubs)
                .recommendedClubs(recommendedClubs)
                .nearestClubsByCategory(nearestClubsByCategory)
                .build();
    }

    public List<Club> findNearestClubs(double requestLatitude, double requestLongitude, Integer count) {
        List<Club> allClubs = clubRepository.findAll();

        return allClubs.stream()
                .sorted(Comparator.comparingDouble(
                    club -> distance(requestLatitude, requestLongitude, club.getLatitude(), club.getLongitude()
                )))
                .limit(count)
                .collect(Collectors.toList());
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        // Haversine formula to calculate the distance between two points on the Earth
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // convert to kilometers

        return distance;
    }

    public List<Club> optimizedFindNearestClubsFour(float requestLatitude, float requestLongitude, int count) {
        List<Club> allClubs = clubRepository.findAll();

        // Create a priority queue to maintain the nearest clubs
        PriorityQueue<ClubDistance> nearestClubs = new PriorityQueue<>(count, Comparator.comparingDouble(ClubDistance::getDistance));

        allClubs.stream()
                .map(club -> new ClubDistance(club, distance(requestLatitude, requestLongitude, club.getLatitude(), club.getLongitude())))
                .forEach(clubDistance -> {
                    nearestClubs.offer(clubDistance);
                    if (nearestClubs.size() > count) {
                        nearestClubs.poll(); // Remove the club with the largest distance if the queue exceeds the count
                    }
                });

        return nearestClubs.stream()
                .map(ClubDistance::getClub)
                .collect(Collectors.toList());
    }

    private float distance(float lat1, float lon1, float lat2, float lon2) {
        // Haversine formula to calculate the distance between two points on the Earth
        final float R = 6371; // Radius of the earth in km
        float latDistance = (float) Math.toRadians(lat2 - lat1);
        float lonDistance = (float) Math.toRadians(lon2 - lon1);
        float a = (float) (Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2));
        float c = (float) (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
        return R * c; // convert to kilometers
    }

    public List<Club> optimizedFindNearestClubsFive(float requestLatitude, float requestLongitude, int count) {
        List<Club> allClubs = clubRepository.findAll();

        // Precompute cosine of request latitude
        float requestLatitudeRad = (float) Math.toRadians(requestLatitude);
        float cosRequestLatitude = (float) Math.cos(requestLatitudeRad);

        // Create a priority queue to maintain the nearest clubs
        PriorityQueue<ClubDistance> nearestClubs = new PriorityQueue<>(count, Comparator.comparingDouble(ClubDistance::getDistance));

        allClubs.stream()
                .map(club -> new ClubDistance(club, distance(requestLatitude, requestLongitude, club.getLatitude(), club.getLongitude(), cosRequestLatitude)))
                .forEach(clubDistance -> {
                    nearestClubs.offer(clubDistance);
                    if (nearestClubs.size() > count) {
                        nearestClubs.poll(); // Remove the club with the largest distance if the queue exceeds the count
                    }
                });

        return nearestClubs.stream()
                .map(ClubDistance::getClub)
                .collect(Collectors.toList());
    }


    public List<Club> parallelFindNearestClubs(float requestLatitude, float requestLongitude, int count) {
        List<Club> allClubs = clubRepository.findAll();

        // Precompute cosine of request latitude
        float requestLatitudeRad = (float) Math.toRadians(requestLatitude);
        float cosRequestLatitude = (float) Math.cos(requestLatitudeRad);

        // Use ConcurrentSkipListSet to maintain the nearest clubs in a thread-safe manner
        ConcurrentSkipListSet<ClubDistance> nearestClubs = new ConcurrentSkipListSet<>(Comparator.comparingDouble(ClubDistance::getDistance));

        allClubs.parallelStream()
                .map(club -> new ClubDistance(club, distance(requestLatitude, requestLongitude, club.getLatitude(), club.getLongitude(), cosRequestLatitude)))
                .forEach(clubDistance -> {
                    nearestClubs.add(clubDistance);
                    if (nearestClubs.size() > count) {
                        nearestClubs.pollLast(); // Remove the club with the largest distance if the set exceeds the count
                    }
                });

        return nearestClubs.stream()
                .limit(count)
                .map(ClubDistance::getClub)
                .collect(Collectors.toList());
    }

    public List<ClubResponse> optimizedParallelFindNearestClubs(float requestLatitude, float requestLongitude, int count) {
        List<Club> allClubs = clubRepository.findAll();

        // Precompute cosine of request latitude
        float requestLatitudeRad = (float) Math.toRadians(requestLatitude);
        float cosRequestLatitude = (float) Math.cos(requestLatitudeRad);

        // Use ConcurrentSkipListSet to maintain the nearest clubs in a thread-safe manner
        ConcurrentSkipListSet<ClubDistance> nearestClubs = allClubs.stream()
                .filter(club -> club.getLatitude() != null && club.getLongitude() != null) // Filter out clubs with null latitude or longitude
                .filter(club -> club.getImageUrls() != null && !club.getImageUrls().isEmpty()) // Filter out clubs with null or empty imageUrls
                .map(club -> new ClubDistance(club, distance(requestLatitude, requestLongitude, club.getLatitude(), club.getLongitude(), cosRequestLatitude)))
                .collect(Collectors.toCollection(() -> new ConcurrentSkipListSet<>(Comparator.comparingDouble(ClubDistance::getDistance))));

        // Limit to the desired number of nearest clubs
        return nearestClubs.stream()
                .limit(count)
                .map(clubDistance -> {
                    Club club = clubDistance.getClub();

                    // 클럽과 연결된 프로그램 조회
                    List<Program> programs = programRepository.findByClub_Id(club.getId());

                    // 각 프로그램에 대한 가격 정보 조회
                    List<PriceResponse> priceResponses = programs.stream()
                            .flatMap(program -> priceRepository.findByProgram(program).stream())  // 각 프로그램에 대한 가격 조회
                            .map(price -> new PriceResponse(price.getDayOfWeek(), price.getStartTime(), price.getEndTime(), price.getPrice()))
                            .toList();

                    System.out.println(priceResponses.stream()
                            .map(PriceResponse::getPrice)
                            .toList().toString());

                    // 클럽의 운영 시간 정보 조회
                    List<OperatingHours> operatingHours = operatingHoursRepository.findByClub_Id(club.getId());

                    // ClubResponse로 변환하여 각 필드 설정
                    ClubResponse response = clubMapper.entityToResponse(club);
                    response.setPrices(priceResponses);

                    response.setOperatingHours(operatingHours.stream()
                            .map(hours -> new OperatingHoursResponse(hours.getDayOfWeek(), hours.getStartTime(), hours.getEndTime()))
                            .collect(Collectors.toList()));

                    // 클럽의 프로그램 정보 설정
                    List<ProgramResponse> programResponses = programs.stream()
                            .map(program -> new ProgramResponse(program.getProgramId(), program.getProgramName().name()))
                            .collect(Collectors.toList());

                    response.setPrograms(programResponses);

                    return response;
                })
                .collect(Collectors.toList());
    }


    private float distance(float lat1, float lon1, float lat2, float lon2, float cosRequestLatitude) {
        // Haversine formula to calculate the distance between two points on the Earth
        final float R = 6371; // Radius of the earth in km
        float latDistance = (float) Math.toRadians(lat2 - lat1);
        float lonDistance = (float) Math.toRadians(lon2 - lon1);
        float a = (float) (Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + cosRequestLatitude * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2));
        float c = (float) (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
        return R * c; // convert to kilometers
    }

    public Map<Category, List<CategoryClubResponse>> findNearestClubsByCategory(float requestLatitude, float requestLongitude, int count) {
        List<Club> allClubs = clubRepository.findAll();

        // Precompute cosine of request latitude
        float requestLatitudeRad = (float) Math.toRadians(requestLatitude);
        float cosRequestLatitude = (float) Math.cos(requestLatitudeRad);

        // Group clubs by category
        Map<Category, List<Club>> clubsByCategory = allClubs.stream()
                .filter(club -> club.getLatitude() != null && club.getLongitude() != null) // Filter out clubs with null latitude or longitude
                .collect(Collectors.groupingBy(Club::getCategory));

        // Create a map to hold the nearest clubs for each category
        Map<Category, List<CategoryClubResponse>> nearestClubsByCategory = new HashMap<>();

        // Find the nearest clubs for each category
        for (Map.Entry<Category, List<Club>> entry : clubsByCategory.entrySet()) {
            Category category = entry.getKey();
            List<Club> clubs = entry.getValue();

            List<ClubDistance> sortedClubs = clubs.stream()
                    .map(club -> new ClubDistance(club, distance(requestLatitude, requestLongitude, club.getLatitude(), club.getLongitude(), cosRequestLatitude)))
                    .sorted(Comparator.comparingDouble(ClubDistance::getDistance))
                    .collect(Collectors.toList());

            // Limit to the desired number of nearest clubs and map to CategoryClubResponse
            List<CategoryClubResponse> nearestClubsList = sortedClubs.stream()
                    .limit(count)
                    .map(clubDistance -> toCategoryClubResponse(clubDistance.getClub()))
                    .collect(Collectors.toList());

            // Add the nearest clubs to the result map
            nearestClubsByCategory.put(category, nearestClubsList);
        }

        return nearestClubsByCategory;
    }

    // Helper method to convert Club to CategoryClubResponse
    private CategoryClubResponse toCategoryClubResponse(Club club) {
        return new CategoryClubResponse(
                s3Uploader.getPresignedUrls(club.getId(), club.getImageUrls()),
                club.getImageUrls(),
                club.getCategory().name(),
                club.getId(),
                club.getClubName(),
                club.getAddress()
        );
    }

    public List<ClubResponse> getPopularClubs() {
        return clubRepository.findTop5ByOrderByRatingCountDesc().stream()
                .map(club -> {
                    List<Program> programs = programRepository.findByClub_Id(club.getId());
                    List<Price> prices = programs.stream()
                            .flatMap(program -> priceRepository.findByProgram(program).stream())
                            .toList();
                    List<OperatingHours> operatingHours = operatingHoursRepository.findByClub_Id(club.getId());

                    // DTO로 변환
                    List<PriceResponse> priceResponses = prices.stream()
                            .map(price -> new PriceResponse(price.getDayOfWeek(), price.getStartTime(), price.getEndTime(), price.getPrice()))
                            .collect(Collectors.toList());

                    List<OperatingHoursResponse> operatingHoursResponses = operatingHours.stream()
                            .map(hours -> new OperatingHoursResponse(hours.getDayOfWeek(), hours.getStartTime(), hours.getEndTime()))
                            .collect(Collectors.toList());

                    List<ProgramResponse> programResponses = programs.stream()
                            .map(program -> new ProgramResponse(program.getProgramId(), program.getProgramName().name()))
                            .collect(Collectors.toList());

                    // ClubResponse로 변환 및 설정
                    ClubResponse clubResponse = clubMapper.entityToResponse(club);
                    clubResponse.setPrices(priceResponses.isEmpty() ? Collections.emptyList() : priceResponses);
                    clubResponse.setOperatingHours(operatingHoursResponses.isEmpty() ? Collections.emptyList() : operatingHoursResponses);
                    clubResponse.setPrograms(programResponses.isEmpty() ? Collections.emptyList() : programResponses);

                    return clubResponse;
                })
                .collect(Collectors.toList());
    }

    public List<ClubResponse> getRecommendedClubs(Integer count) {
        List<Club> allClubs = clubRepository.findAll();
        int size = allClubs.size();

        // If the count is greater than or equal to the size of the list, return the whole list
        if (count >= size) {
            return allClubs.stream()
                    .map(club -> {
                        // 클럽과 연결된 프로그램, 가격, 운영 시간 정보를 조회
                        List<Program> programs = programRepository.findByClub_Id(club.getId());
                        List<Price> prices = programs.stream()
                                .flatMap(program -> priceRepository.findByProgram(program).stream())
                                .toList();

                        List<PriceResponse> priceResponses = prices.stream()
                                .map(price -> new PriceResponse(price.getDayOfWeek(), price.getStartTime(), price.getEndTime(), price.getPrice()))
                                .collect(Collectors.toList());

                        List<OperatingHours> operatingHours = operatingHoursRepository.findByClub_Id(club.getId());
                        List<OperatingHoursResponse> operatingHoursResponses = operatingHours.stream()
                                .map(hours -> new OperatingHoursResponse(hours.getDayOfWeek(), hours.getStartTime(), hours.getEndTime()))
                                .collect(Collectors.toList());

                        List<ProgramResponse> programResponses = programs.stream()
                                .map(program -> new ProgramResponse(program.getProgramId(), program.getProgramName().name()))
                                .collect(Collectors.toList());

                        // ClubResponse로 변환 후 필드 설정
                        ClubResponse response = clubMapper.entityToResponse(club);
                        response.setPrices(priceResponses);
                        response.setOperatingHours(operatingHoursResponses);
                        response.setPrograms(programResponses);

                        return response;
                    })
                    //.filter(clubResponse -> clubResponse.getImageUrls() != null && !clubResponse.getImageUrls().isEmpty()) // Filter out clubs with null or empty imageUrls
                    .collect(Collectors.toList());
        }

        // Reservoir sampling algorithm
        List<ClubResponse> reservoir = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            Club club = allClubs.get(i);

            // 클럽과 연결된 프로그램, 가격, 운영 시간 정보를 조회
            List<Program> programs = programRepository.findByClub_Id(club.getId());
            List<PriceResponse> priceResponses = programs.stream()
                    .flatMap(program -> priceRepository.findByProgram(program).stream())
                    .map(price -> new PriceResponse(price.getDayOfWeek(), price.getStartTime(), price.getEndTime(), price.getPrice()))
                    .collect(Collectors.toList());

            List<OperatingHours> operatingHours = operatingHoursRepository.findByClub_Id(club.getId());
            List<OperatingHoursResponse> operatingHoursResponses = operatingHours.stream()
                    .map(hours -> new OperatingHoursResponse(hours.getDayOfWeek(), hours.getStartTime(), hours.getEndTime()))
                    .collect(Collectors.toList());

            List<ProgramResponse> programResponses = programs.stream()
                    .map(program -> new ProgramResponse(program.getProgramId(), program.getProgramName().name()))
                    .collect(Collectors.toList());

            // ClubResponse로 변환 후 필드 설정
            ClubResponse response = clubMapper.entityToResponse(club);
            response.setPrices(priceResponses);
            response.setOperatingHours(operatingHoursResponses);
            response.setPrograms(programResponses);

            reservoir.add(response);
        }

        for (int i = count; i < size; i++) {
            int j = ThreadLocalRandom.current().nextInt(i + 1);
            if (j < count) {
                Club club = allClubs.get(i);

                // 클럽과 연결된 프로그램, 가격, 운영 시간 정보를 조회
                List<Program> programs = programRepository.findByClub_Id(club.getId());
                List<PriceResponse> priceResponses = programs.stream()
                        .flatMap(program -> priceRepository.findByProgram(program).stream())
                        .map(price -> new PriceResponse(price.getDayOfWeek(), price.getStartTime(), price.getEndTime(), price.getPrice()))
                        .collect(Collectors.toList());

                List<OperatingHours> operatingHours = operatingHoursRepository.findByClub_Id(club.getId());
                List<OperatingHoursResponse> operatingHoursResponses = operatingHours.stream()
                        .map(hours -> new OperatingHoursResponse(hours.getDayOfWeek(), hours.getStartTime(), hours.getEndTime()))
                        .collect(Collectors.toList());

                List<ProgramResponse> programResponses = programs.stream()
                        .map(program -> new ProgramResponse(program.getProgramId(), program.getProgramName().name()))
                        .collect(Collectors.toList());

                // ClubResponse로 변환 후 필드 설정
                ClubResponse response = clubMapper.entityToResponse(club);
                response.setPrices(priceResponses);
                response.setOperatingHours(operatingHoursResponses);
                response.setPrograms(programResponses);

                reservoir.set(j, response);
            }
        }

        return reservoir;
    }

    private static class ClubDistance {
        private final Club club;
        private final double distance;

        public ClubDistance(Club club, double distance) {
            this.club = club;
            this.distance = distance;
        }

        public Club getClub() {
            return club;
        }

        public double getDistance() {
            return distance;
        }
    }

    public NearestResponse getNearestDistrict(NearestRequest nearestRequest) {
        double latitude = nearestRequest.getLatitude();
        double longitude = nearestRequest.getLongitude();

        District nearestDistrict = null;
        double minDistance = Double.MAX_VALUE;

        for (District district : District.values()) {
            double distance = distance(latitude, longitude, district.getLatitude(), district.getLongitude());
            if (distance < minDistance) {
                minDistance = distance;
                nearestDistrict = district;
            }
        }
        String result = nearestDistrict != null ? nearestDistrict.getKoreanName() : null;
        return new NearestResponse(result);
    }

}


