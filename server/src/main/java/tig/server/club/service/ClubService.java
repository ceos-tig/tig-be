package tig.server.club.service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tig.server.club.domain.Club;
import tig.server.club.dto.ClubRequest;
import tig.server.club.dto.ClubResponse;
import tig.server.club.dto.HomeRequest;
import tig.server.club.dto.HomeResponse;
import tig.server.club.mapper.ClubMapper;
import tig.server.club.repository.ClubRepository;
import tig.server.config.S3Uploader;
import tig.server.global.exception.BusinessExceptionHandler;
import tig.server.global.code.ErrorCode;
import tig.server.reservation.domain.Reservation;
import tig.server.reservation.repository.ReservationRepository;
import tig.server.review.domain.Review;
import tig.server.review.dto.ReviewRequest;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final ReservationRepository reservationRepository;

    private final ClubMapper clubMapper = ClubMapper.INSTANCE;

    private final S3Uploader s3Uploader;

    private final ObjectProvider<ClubService> serviceProvider;

    public List<ClubResponse> getAllClubs() {
        return clubRepository.findAll().stream()
                .map(clubMapper::entityToResponse)
                .map(this::calculateAvgRating)
                .collect(Collectors.toList());
    }

    public ClubResponse getClubById(Long id) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new BusinessExceptionHandler("club not found", ErrorCode.NOT_FOUND_ERROR));
        List<String> CloudFrontImageUrl = s3Uploader.getImageUrls(club.getImageUrls());
        club.setImageUrls(CloudFrontImageUrl);
        ClubResponse clubResponse = clubMapper.entityToResponse(club);
        return calculateAvgRating(clubResponse);
    }


    @Transactional
    public ClubResponse createClub(ClubRequest clubRequest) {
        //Change image name to be unique
        clubRequest.setImageUrls(clubRequest.getImageUrls().stream()
                .map(s3Uploader::getUniqueFilename)
                .collect(Collectors.toList()));

        //Upload image to s3
        List<String> presignedUrlList = s3Uploader.uploadFileList(clubRequest.getImageUrls());

        //save club, set presigned url and cloudfront url to response
        Club club = clubMapper.requestToEntity(clubRequest);
        club = clubRepository.save(club);
        ClubResponse response = clubMapper.entityToResponse(club);
        response.setPresignedImageUrls(presignedUrlList);

        response.setImageUrls(club.getImageUrls().stream()
                .map(s3Uploader::getImageUrl)
                .collect(Collectors.toList()));

        return response;
    }

    private ClubResponse calculateAvgRating(ClubResponse clubResponse) {
        if (clubResponse.getRatingCount() != 0) {
            float avgRating = (float) clubResponse.getRatingSum() / clubResponse.getRatingCount();
            clubResponse.setAvgRating(avgRating);
        }
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

        List<ClubResponse> nearestClubs = service.findNearestClubs(requestLatitude, requestLongitude, 5).stream()
                .map(clubMapper::entityToResponse)
                .collect(Collectors.toList());

        List<ClubResponse> popularClubs = service.getPopularClubs();

        List<ClubResponse> recommendedClubs = service.getRecommendedClubs(5);

        return HomeResponse.builder()
                .nearestClubs(nearestClubs)
                .popularClubs(popularClubs)
                .recommendedClubs(recommendedClubs)
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

    public List<Club> optimizedParallelFindNearestClubs(float requestLatitude, float requestLongitude, int count) {
        List<Club> allClubs = clubRepository.findAll();

        // Precompute cosine of request latitude
        float requestLatitudeRad = (float) Math.toRadians(requestLatitude);
        float cosRequestLatitude = (float) Math.cos(requestLatitudeRad);

        // Use ConcurrentSkipListSet to maintain the nearest clubs in a thread-safe manner
        ConcurrentSkipListSet<ClubDistance> nearestClubs = allClubs.parallelStream()
                .map(club -> new ClubDistance(club, distance(requestLatitude, requestLongitude, club.getLatitude(), club.getLongitude(), cosRequestLatitude)))
                .collect(Collectors.toCollection(() -> new ConcurrentSkipListSet<>(Comparator.comparingDouble(ClubDistance::getDistance))));

        // Limit to the desired number of nearest clubs
        return nearestClubs.stream()
                .limit(count)
                .map(ClubDistance::getClub)
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


    public List<ClubResponse> getPopularClubs() {
        return clubRepository.findTop5ByOrderByRatingCountDesc().stream()
                .map(clubMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    public List<ClubResponse> getRecommendedClubs(Integer count) {
        List<Club> allClubs = clubRepository.findAll();
        int size = allClubs.size();

        // If the count is greater than or equal to the size of the list, return the whole list
        if (count >= size) {
            return allClubs.stream()
                    .map(clubMapper::entityToResponse)
                    .collect(Collectors.toList());
        }

        // Reservoir sampling algorithm
        List<ClubResponse> reservoir = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            reservoir.add(clubMapper.entityToResponse(allClubs.get(i)));
        }

        for (int i = count; i < size; i++) {
            int j = ThreadLocalRandom.current().nextInt(i + 1);
            if (j < count) {
                reservoir.set(j, clubMapper.entityToResponse(allClubs.get(i)));
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

}


