package tig.server.practice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PracticeService {
    public boolean hasSubArraySum(List<Integer> nums, int target) {
        Map<Integer, Integer> prefixSumMap = new HashMap<>();
        int currentSum = 0;
        prefixSumMap.put(0, -1);

        for (int i = 0; i < nums.size(); i++) {
            currentSum += nums.get(i);

            if (prefixSumMap.containsKey(currentSum - target)) {
                return true;
            }

            prefixSumMap.put(currentSum, i);
        }
        return false;
    }


}
