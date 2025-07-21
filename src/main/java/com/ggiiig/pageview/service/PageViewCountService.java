package com.ggiiig.pageview.service;

import com.ggiiig.pageview.dto.response.PageViewGetResponse;
import com.ggiiig.pageview.entity.PageType;
import com.ggiiig.pageview.repository.PageViewCountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PageViewCountService {
    private final RedisTemplate<String, String> redisTemplate;
    private final PageViewCountRepository pageViewCountRepository;
    private static final long EXPIRE_SECONDS = 30L * 60L; // 30분

    public void handleViewCount(String clientIP, PageType pageType) {
        if (shouldCountView(clientIP, pageType)) {
            incrementViewCount(pageType);
        }
    }

    private void incrementViewCount(PageType pageType) {
        String key = getPageViewKey(pageType);

        try {
            Boolean isNew = redisTemplate.opsForValue().setIfAbsent(key, "0");
            if (Boolean.TRUE.equals(isNew)) {
                redisTemplate.expire(key, 48, TimeUnit.HOURS);
            }

            redisTemplate.opsForValue().increment(key);
        } catch (Exception e) {
            log.error("조회수 증가시키는 중 오류 발생\n {}", e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public long getViewCount(PageType pageType) {
        String key = getPageViewKey(pageType);
        String value = redisTemplate.opsForValue().get(key);

        return value == null ? 0 : Long.parseLong(value);
    }

    @Transactional(readOnly = true)
    public Map<PageType, Long> getAllViewCounts() {
        Map<PageType, Long> result = new EnumMap<>(PageType.class);
        for (PageType pageType : PageType.values()) {
            result.put(pageType, getViewCount(pageType));
        }

        return result;
    }

    public boolean deleteViewCount(PageType pageType) {
        String key = getPageViewKey(pageType);
        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
            log.error("Redis key : {} 존재하지 않음", key);

            return false;
        }

        Boolean result = redisTemplate.delete(key);

        return Boolean.TRUE.equals(result);
    }

    @Transactional(readOnly = true)
    public List<PageViewGetResponse> getPageViewByDate(LocalDate date) {
        return pageViewCountRepository.findByPk_ViewDate(date);
    }

    private String getPageViewKey(PageType pageType) {
        return "view:" + pageType.getValue();
    }

    private String getClientPageViewKey(String clientIP, PageType pageType) {
        return pageType.getValue() + ":" + clientIP;
    }

    private boolean shouldCountView(String clientIP, PageType pageType) {
        String key = getClientPageViewKey(clientIP, pageType);

        try {
            Boolean exists = redisTemplate.hasKey(key);
            if (Boolean.TRUE.equals(exists)) {
                return false;
            }

            // 키가 없으면 조회수 증가 대상, 30분 TTL 설정
            redisTemplate.opsForValue().set(key, "1", Duration.ofSeconds(EXPIRE_SECONDS));
            return true;
        } catch (Exception e) {
            log.error("클라이언트 조회 이력 확인 중 오류 발생\n {}", e.getMessage());

            //Redis 오류 발생 시 조회수 즈악 허용 (보수적 접근)
            return true;
        }

    }
}
