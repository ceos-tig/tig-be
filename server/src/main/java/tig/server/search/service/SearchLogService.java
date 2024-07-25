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
        String key = "SearchLog" + member.getId();
        SearchLog value = SearchLog.builder()
                .name(request.getName())
                .createdAt(now)
                .build();

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

        String key = "SearchLog" + member.getId();
        List<SearchLog> logs = redisTemplate.opsForList().
                range(key, 0, 10);

        List<SearchLogDto> searchLogDtoList = new ArrayList<>();
        for (SearchLog log : logs) {
            SearchLogDto searchLogDto = SearchLogDto.fromLog(log.getName(), log.getCreatedAt());
            searchLogDtoList.add(searchLogDto);
        }
        return searchLogDtoList;
    }
    /*
    public void deleteRecentSearchLog(Long memberId, SearchLogDeleteRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessExceptionHandler("member not found",ErrorCode.NOT_FOUND_ERROR));

        String key = "SearchLog" + member.getId();
        SearchLog value = SearchLog.builder()
                .name(request.getName())
                .createdAt(request.getCreatedAt())
                .build();

        long count = redisTemplate.opsForList().remove(key, 1, value);

        if (count == 0) {
            throw new CustomException(ErrorCode.SEARCH_LOG_NOT_EXIST);
        }
    }
    /*
     */
}
