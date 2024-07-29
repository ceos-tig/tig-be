//package tig.server.dummy;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import tig.server.club.domain.Club;
//import tig.server.club.repository.ClubRepository;
//import tig.server.enums.Category;
//import tig.server.enums.Type;
//
//@Component
//public class BallingDataLoader implements CommandLineRunner {
//
//    @Autowired
//    private ClubRepository clubRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//
//        Club club1 = Club.builder()
//                .clubName("클럽스트라이크 볼링장")
//                .address("서울 마포구 마포대로 52 4층")
//                .ratingSum(20.0f)
//                .ratingCount(10)
//                .price(5000)
//                .phoneNumber("010-6283-5645")
//                .snsLink("https://www.instagram.com/clubstrikepub/")
//                .businessHours("12:00 -T 02:00")
//                .category(Category.BALLING)
//                .type(Type.GAME)
//                .latitude(0f)
//                .longitude(0f)
//                .build();
//
//        Club club2 = Club.builder()
//                .clubName("W락볼링장")
//                .address("서울 마포구 양화로 104 지하1층")
//                .ratingSum(20.0f)
//                .ratingCount(10)
//                .price(5000)
//                .phoneNumber("02-336-0928")
//                .snsLink("-")
//                .businessHours("12:00 - 02:00")
//                .category(Category.BALLING)
//                .type(Type.GAME)
//                .latitude(0f)
//                .longitude(0f)
//                .build();
//
//        Club club3 = Club.builder()
//                .clubName("스매싱볼 홍대점")
//                .address("서울 마포구 독막로7길 48")
//                .ratingSum(20.0f)
//                .ratingCount(10)
//                .price(7500)
//                .phoneNumber("02-322-2231")
//                .snsLink("-")
//                .businessHours("15:00 - 01:00")
//                .category(Category.BALLING)
//                .type(Type.GAME)
//                .latitude(0f)
//                .longitude(0f)
//                .build();
//
//        Club club4 = Club.builder()
//                .clubName("태화볼링장")
//                .address("서울 마포구 와우산로 111 태화프라자 지하 1, 2층")
//                .ratingSum(20.0f)
//                .ratingCount(10)
//                .price(10000)
//                .phoneNumber("0507-1329-3398")
//                .snsLink("-")
//                .businessHours("11:00 - 06:00")
//                .category(Category.BALLING)
//                .type(Type.GAME)
//                .latitude(0f)
//                .longitude(0f)
//                .build();
//
//        Club club5 = Club.builder()
//                .clubName("태화볼링장")
//                .address("서울 마포구 와우산로 111 태화프라자 지하 1, 2층")
//                .ratingSum(20.0f)
//                .ratingCount(10)
//                .price(10000)
//                .phoneNumber("0507-1329-3398")
//                .snsLink("-")
//                .businessHours("11:00 - 06:00")
//                .category(Category.BALLING)
//                .type(Type.GAME)
//                .latitude(0f)
//                .longitude(0f)
//                .build();
//
//        clubRepository.save(club1);
//        clubRepository.save(club2);
//        clubRepository.save(club3);
//        clubRepository.save(club4);
//        clubRepository.save(club5);
//    }
//}
