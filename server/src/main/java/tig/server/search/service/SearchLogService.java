package tig.server.search.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tig.server.global.code.ErrorCode;
import tig.server.global.exception.BusinessExceptionHandler;
import tig.server.member.domain.Member;
import tig.server.member.repository.MemberRepository;
import tig.server.search.domain.SearchLog;
import tig.server.search.dto.SearchLogDto;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchLogService {

    private final MemberRepository memberRepository;
    private final RedisTemplate<String, SearchLog> redisTemplate;

    public void saveRecentSearchLog(Long memberId, SearchLogDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessExceptionHandler("member not found", ErrorCode.NOT_FOUND_ERROR));

        String now = request.getCreatedAt();
        String key = "SearchLog:" + member.getUniqueId();
        SearchLog value = SearchLog.builder()
                .name(request.getName())
                .createdAt(now)
                .build();

        // 기존에 같은 이름의 검색 기록이 있는지 확인하고 제거
        List<SearchLog> logs = redisTemplate.opsForList().range(key, 0, -1);
        for (SearchLog log : logs) {
            if (log.getName().equals(request.getName())) {
                redisTemplate.opsForList().remove(key, 1, log);
            }
        }

        Long size = redisTemplate.opsForList().size(key);
        if (size == 10) {
            // rightPop을 통해 가장 오래된 데이터 삭제
            redisTemplate.opsForList().rightPop(key);
        }

        redisTemplate.opsForList().leftPush(key, value);
    }

    public List<SearchLogDto> findRecentSearchLogs(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessExceptionHandler("member not found",ErrorCode.NOT_FOUND_ERROR));

        String key = "SearchLog:" + member.getUniqueId();
        List<SearchLog> logs = redisTemplate.opsForList().
                range(key, 0, 10);

        List<SearchLogDto> searchLogDtoList = new ArrayList<>();
        for (SearchLog log : logs) {
            SearchLogDto searchLogDto = SearchLogDto.fromLog(log.getName(), log.getCreatedAt());
            searchLogDtoList.add(searchLogDto);
        }
        return searchLogDtoList;
    }

    public void deleteSearchLog(Long memberId, String target) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessExceptionHandler("member not found", ErrorCode.NOT_FOUND_ERROR));

        String key = "SearchLog:" + member.getUniqueId();
        List<SearchLog> logs = redisTemplate.opsForList().range(key, 0, -1);
        for (SearchLog log : logs) {
            if (log.getName().equals(target)) {
                redisTemplate.opsForList().remove(key, 1, log);
                break; // 검색 기록을 하나만 삭제
            }
        }
    }

    public void deleteAllSearchLogs(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessExceptionHandler("member not found", ErrorCode.NOT_FOUND_ERROR));

        String key = "SearchLog:" + member.getUniqueId();
        redisTemplate.delete(key);
    }
}
