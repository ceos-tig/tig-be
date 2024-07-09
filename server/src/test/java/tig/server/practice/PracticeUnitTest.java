package tig.server.practice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PracticeUnitTest {

    private final PracticeService practiceService;

    public PracticeUnitTest(PracticeService practiceService) {
        this.practiceService = practiceService;
    }


    @DisplayName("algo test")
    @Test
    public void algoTest() {

        List<Integer> nums = List.of(1, 2, 3, 4, 5, 6, 7, 8);
        Integer target = 7;

        boolean exist = practiceService.hasSubArraySum(nums, target);

        assertThat(exist).isFalse();
    }


}
