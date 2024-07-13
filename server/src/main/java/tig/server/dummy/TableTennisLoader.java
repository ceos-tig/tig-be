package tig.server.dummy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tig.server.club.domain.Club;
import tig.server.club.repository.ClubRepository;
import tig.server.enums.Category;
import tig.server.enums.Type;

@Component
public class TableTennisLoader implements CommandLineRunner {

    @Autowired
    private ClubRepository clubRepository;

    @Override
    public void run(String... args) throws Exception {

        Club club1 = Club.builder()
                .clubName("세종탁구장 마포점")
                .address("서울 마포구 삼개로5길 4-3 지층")
                .avgRating(0f)
                .price(5000)
                .phoneNumber("02-3272-0079")
                .snsLink("-")
                .businessHours("무인 운영")
                .category(Category.TABLE_TENNIS)
                .type(Type.TIME)
                .latitude(0f)
                .longitude(0f)
                .build();

        Club club2 = Club.builder()
                .clubName("나루탁구클럽")
                .address("서울 마포구 삼개로 21 근신제2별관")
                .avgRating(0f)
                .price(10000)
                .phoneNumber("0507-1353-7975")
                .snsLink("-")
                .businessHours("12:00 - 22:30")
                .category(Category.TABLE_TENNIS)
                .type(Type.TIME)
                .latitude(0f)
                .longitude(0f)
                .build();

        Club club3 = Club.builder()
                .clubName("마포사랑탁구클럽")
                .address("서울 마포구 신수로 56 유안타증권빌딩 지하1층")
                .avgRating(0f)
                .price(15000)
                .phoneNumber("02-703-6476")
                .snsLink("-")
                .businessHours("12:00 - 22:30")
                .category(Category.TABLE_TENNIS)
                .type(Type.TIME)
                .latitude(0f)
                .longitude(0f)
                .build();

        Club club4 = Club.builder()
                .clubName("마포탁구클럽")
                .address("서울 마포구 만리재옛길 32 지하1층 마포탁구클럽")
                .avgRating(0f)
                .price(15000)
                .phoneNumber("0507-1418-6808")
                .snsLink("https://www.instagram.com/mapottclub/")
                .businessHours("10:00 - 23:00")
                .category(Category.TABLE_TENNIS)
                .type(Type.TIME)
                .latitude(0f)
                .longitude(0f)
                .build();

        Club club5 = Club.builder()
                .clubName("성산탁구클럽")
                .address("서울 마포구 모래내로1길 3 한두빌딩 지하1층")
                .avgRating(0f)
                .price(15000)
                .phoneNumber("02-303-6070")
                .snsLink("-")
                .businessHours("10:00 - 23:00")
                .category(Category.TABLE_TENNIS)
                .type(Type.TIME)
                .latitude(0f)
                .longitude(0f)
                .build();

        Club club6 = Club.builder()
                .clubName("신촌 탁구장")
                .address("서울 마포구 신촌로 118-1")
                .avgRating(0f)
                .price(15000)
                .phoneNumber("02-714-0505")
                .snsLink("-")
                .businessHours("09:00 - 23:00")
                .category(Category.TABLE_TENNIS)
                .type(Type.TIME)
                .latitude(0f)
                .longitude(0f)
                .build();

        Club club7 = Club.builder()
                .clubName("미르메탁구클럽")
                .address("서울 서대문구 연희로 227 러브아트빌딩 본관 지하2층")
                .avgRating(0f)
                .price(15000)
                .phoneNumber("010-6320-6379")
                .snsLink("-")
                .businessHours("11:00 - 23:00")
                .category(Category.TABLE_TENNIS)
                .type(Type.TIME)
                .latitude(0f)
                .longitude(0f)
                .build();

        Club club8 = Club.builder()
                .clubName("박진우탁구클럽")
                .address("서울 서대문구 모래내로 347-2 2층")
                .avgRating(0f)
                .price(14000)
                .phoneNumber("0507-1397-2587")
                .snsLink("-")
                .businessHours("09:00 - 23:00")
                .category(Category.TABLE_TENNIS)
                .type(Type.TIME)
                .latitude(0f)
                .longitude(0f)
                .build();

        clubRepository.save(club1);
        clubRepository.save(club2);
        clubRepository.save(club3);
        clubRepository.save(club4);
        clubRepository.save(club5);
        clubRepository.save(club6);
        clubRepository.save(club7);
        clubRepository.save(club8);
    }
}

