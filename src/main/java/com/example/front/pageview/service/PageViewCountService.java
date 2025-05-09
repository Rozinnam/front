package com.example.front.pageview.service;

import com.example.front.pageview.dto.response.PageViewGetResponse;
import com.example.front.pageview.entity.PageType;
import com.example.front.pageview.repository.PageViewCountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void incrementViewCount(PageType pageType) {
        String key = getKey(pageType);

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
        String key = getKey(pageType);
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
        String key = getKey(pageType);
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

    private String getKey(PageType pageType) {
        return "view:" + pageType.getValue();
    }
}
