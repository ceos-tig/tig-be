package tig.server.dummy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tig.server.club.domain.Club;
import tig.server.club.repository.ClubRepository;
import tig.server.member.domain.Member;
import tig.server.member.repository.MemberRepository;
import tig.server.reservation.domain.Reservation;
import tig.server.reservation.repository.ReservationRepository;
import tig.server.review.domain.Review;
import tig.server.review.dto.ReviewRequest;
import tig.server.review.dto.ReviewResponse;
import tig.server.review.mapper.ReviewMapper;
import tig.server.review.repository.ReviewRepository;
import tig.server.review.service.ReviewService;
import tig.server.wishlist.domain.Wishlist;
import tig.server.wishlist.repository.WishlistRepository;
import tig.server.enums.Category;
import tig.server.enums.Type;
import tig.server.enums.MemberRoleEnum;
import tig.server.enums.Status;

import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ReviewService reviewService;

    @Override
    public void run(String... args) throws Exception {
        // Create Clubs
        List<String> services1 = Arrays.asList("Service1", "Service2");
        List<String> services2 = Arrays.asList("ServiceA", "ServiceB");

        List<String> clubImages1 = Arrays.asList("club1_1.jpg", "club1_2.jpg");
        List<String> clubImages2 = Arrays.asList("club2_1.jpg", "club2_2.jpg");

        Club club1 = Club.builder()
                .clubName("Club One")
                .address("123 Street, City")
                .ratingCount(10)
                .ratingSum(20)
                .price(100)
                .phoneNumber("123-456-7890")
                .snsLink("http://clubone.com")
                .businessHours("9 AM - 5 PM")
                .category(Category.FOOTBALL)
                .type(Type.TIME)
                .latitude(37.7749f)
                .longitude(-122.4194f)
                .services(services1)
                .imageUrls(clubImages1)
                .build();

        Club club2 = Club.builder()
                .clubName("Club Two")
                .address("456 Avenue, City")
                .ratingCount(10)
                .ratingSum(20)
                .price(200)
                .phoneNumber("987-654-3210")
                .snsLink("http://clubtwo.com")
                .businessHours("10 AM - 6 PM")
                .category(Category.BALLING)
                .type(Type.GAME)
                .latitude(34.0522f)
                .longitude(-118.2437f)
                .services(services2)
                .imageUrls(clubImages2)
                .build();

        clubRepository.save(club1);
        clubRepository.save(club2);

        // Create Members
        Member member1 = Member.builder()
                .name("Alice")
                .email("alice@example.com")
                .phoneNumber("111-222-3333")
                .uniqueId("alice123")
                .profileImage("profile1.jpg")
                .refreshToken("refreshToken1")
                .memberRoleEnum(MemberRoleEnum.USER)
                .build();

        Member member2 = Member.builder()
                .name("Bob")
                .email("bob@example.com")
                .phoneNumber("444-555-6666")
                .uniqueId("bob456")
                .profileImage("profile2.jpg")
                .refreshToken("refreshToken2")
                .memberRoleEnum(MemberRoleEnum.ADMIN)
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);

        // Create Reservations
        Reservation reservation1 = Reservation.builder()
                .adultCount(2)
                .teenagerCount(1)
                .kidsCount(0)
                .date("2024-07-24")
                .startTime("10:00")
                .endTime("12:00")
                .price(150)
                .status(Status.CONFIRMED)
                .member(member2)
                .club(club1)
                .build();

        Reservation reservation2 = Reservation.builder()
                .adultCount(3)
                .teenagerCount(0)
                .kidsCount(2)
                .date("2024-08-24")
                .startTime("14:00")
                .endTime("16:00")
                .price(250)
                .status(Status.TBC)
                .member(member1)
                .club(club2)
                .build();

        Reservation reservation3 = Reservation.builder()
                .adultCount(1)
                .teenagerCount(1)
                .kidsCount(1)
                .date("2024-08-25")
                .startTime("14:00")
                .endTime("16:00")
                .price(250)
                .status(Status.DECLINED)
                .member(member1)
                .club(club1)
                .build();

        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);
        reservationRepository.save(reservation3);

        member1.setReservations(Arrays.asList(reservation2, reservation3));
        member2.setReservations(Arrays.asList(reservation1));

        memberRepository.save(member1);
        memberRepository.save(member2);

        // Create Reviews
        Review review1 = Review.builder()
                .reservation(reservation1)
                .rating(4)
                .contents("Great experience at Club One!")
                .build();

        Review review2 = Review.builder()
                .reservation(reservation2)
                .rating(5)
                .contents("Excellent service at Club Two!")
                .build();

        Review review3 = Review.builder()
                .reservation(reservation3)
                .rating(3)
                .contents("Not Bad experience at Club One!")
                .build();


        reservation1.setReview(review1);
        reservation2.setReview(review2);
        reservation3.setReview(review3);

        // review1 to request
        reviewService.createReview(member1.getId(), reservation1.getId(), ReviewRequest.builder()
                .rating(4)
                .contents("Great experience at Club One!")
                .build());

        reviewService.createReview(member2.getId(), reservation2.getId(), ReviewRequest.builder()
                .rating(5)
                .contents("Excellent service at Club Two!")
                .build());

        reviewService.createReview(member1.getId(), reservation3.getId(), ReviewRequest.builder()
                .rating(3)
                .contents("Not Bad experience at Club One!")
                .build());

        reviewRepository.save(review1);
        reviewRepository.save(review2);
        reviewRepository.save(review3);

        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);
        reservationRepository.save(reservation3);

        // Create Wishlists
        Wishlist wishlist1 = Wishlist.builder()
                .member(member1)
                .club(club1)
                .build();

        Wishlist wishlist2 = Wishlist.builder()
                .member(member1)
                .club(club2)
                .build();


        Wishlist wishlist3 = Wishlist.builder()
                .member(member2)
                .club(club1)
                .build();


        wishlistRepository.save(wishlist1);
        wishlistRepository.save(wishlist2);
        wishlistRepository.save(wishlist3);

        System.out.println("Sample data loaded.");
    }
}
