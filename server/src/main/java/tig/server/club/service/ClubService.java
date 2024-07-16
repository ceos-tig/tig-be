package tig.server.club.service;
import lombok.RequiredArgsConstructor;
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
import tig.server.reservation.domain.Reservation;
import tig.server.reservation.repository.ReservationRepository;
import tig.server.review.domain.Review;
import tig.server.review.dto.ReviewRequest;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final ReservationRepository reservationRepository;

    private final ClubMapper clubMapper = ClubMapper.INSTANCE;

    private final S3Uploader s3Uploader;

    public List<ClubResponse> getAllClubs() {
        return clubRepository.findAll().stream()
                .map(clubMapper::entityToResponse)
                .map(this::calculateAvgRating)
                .collect(Collectors.toList());
    }

    public ClubResponse getClubById(Long id) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("club not found"));
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
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        Club club = clubRepository.findById(reservation.getClub().getId())
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

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
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        Club club = clubRepository.findById(reservation.getClub().getId())
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));


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

        List<ClubResponse> nearstClubs = findNearestClubs(requestLatitude, requestLongitude, 5).stream()
                .map(clubMapper::entityToResponse)
                .collect(Collectors.toList());

        List<ClubResponse> popularClubs = clubRepository.findTop5ByOrderByRatingCountDesc().stream()
                .map(clubMapper::entityToResponse)
                .collect(Collectors.toList());

        List<ClubResponse> recommendedClubs = getRecommendedClubs(5);

        return HomeResponse.builder()
                .nearestClubs(nearstClubs)
                .popularClubs(popularClubs)
                .recommendedClubs(recommendedClubs)
                .build();
    }

    public List<Club> findNearestClubs(double requestLatitude, double requestLongitude, Integer count) {
        List<Club> allClubs = clubRepository.findAll();
        return allClubs.stream()
                .sorted(Comparator.comparingDouble(club -> distance(requestLatitude, requestLongitude, club.getLatitude(), club.getLongitude())))
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

    public List<ClubResponse> getRecommendedClubs(Integer count) {
        List<ClubResponse> recommendedClubs = clubRepository.findAll().stream()
                .map(clubMapper::entityToResponse)
                .collect(Collectors.toList());

        // Shuffle the list to randomize the order
        Collections.shuffle(recommendedClubs);

        // Take the first 5 elements from the shuffled list
        return recommendedClubs.stream()
                .limit(count)
                .collect(Collectors.toList());
    }



}
