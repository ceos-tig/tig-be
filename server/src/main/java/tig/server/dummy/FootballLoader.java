package tig.server.dummy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tig.server.club.domain.Club;
import tig.server.club.repository.ClubRepository;
import tig.server.enums.Category;
import tig.server.enums.Type;

@Component
public class FootballLoader implements CommandLineRunner {
    @Autowired
    private ClubRepository clubRepository;

    @Override
    public void run(String... args) throws Exception {

        Club club1 = Club.builder()
                .clubName("올리브크리에이티브")
                .address("서울 마포구 월드컵로 240")
                .ratingSum(20.0f)
                .ratingCount(10)
                .price(15000)
                .phoneNumber("010-4569-9024")
                .snsLink("-")
                .businessHours("09:00 - 19:00")
                .category(Category.FOOTBALL)
                .type(Type.TIME)
                .latitude(0f)
                .longitude(0f)
                .build();

        Club club2 = Club.builder()
                .clubName("연희 풋볼존 트레이닝존")
                .address("서울시 서대문구 연희로132 지층")
                .ratingSum(20.0f)
                .ratingCount(10)
                .price(30000)
                .phoneNumber("0507-1304-9865")
                .snsLink("-")
                .businessHours("무인 운영")
                .category(Category.FOOTBALL)
                .type(Type.TIME)
                .latitude(0f)
                .longitude(0f)
                .build();

        Club club3 = Club.builder()
                .clubName("김병지축구클럽 마포점")
                .address("서울 마포구 신수로 107 신촌삼익아파트 상가동 지하1층")
                .ratingSum(20.0f)
                .ratingCount(10)
                .price(100000)
                .phoneNumber("1670-7609")
                .snsLink("-")
                .businessHours("00:00 - 24:00")
                .category(Category.FOOTBALL)
                .type(Type.TIME)
                .latitude(0f)
                .longitude(0f)
                .build();

        Club club4 = Club.builder()
                .clubName("마포키즈아레나 3호점 트레이닝존 A면")
                .address("서울시 마포구 마포대로 231푸르지오 101동상가 지하1층")
                .ratingSum(20.0f)
                .ratingCount(10)
                .price(35000)
                .phoneNumber("0507-1488-3482")
                .snsLink("-")
                .businessHours("11:00 - 21:00")
                .category(Category.FOOTBALL)
                .type(Type.TIME)
                .latitude(0f)
                .longitude(0f)
                .build();

        clubRepository.save(club1);
        clubRepository.save(club2);
        clubRepository.save(club3);
        clubRepository.save(club4);
    }
}
