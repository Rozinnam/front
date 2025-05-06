package com.example.front.pageview.service;

import com.example.front.pageview.entity.PageType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PageViewCountService {
    private final RedisTemplate<String, String> redisTemplate;

    public void incrementViewCount(PageType pageType) {
        String key = "view:" + pageType.getValue();
        redisTemplate.opsForValue().increment(key);
    }

    public long getViewCount(PageType pageType) {
        String key = "view:" + pageType.getValue();
        String value = redisTemplate.opsForValue().get(key);

        return value == null ? 0 : Long.parseLong(value);
    }

    public Map<PageType, Long> getAllViewCounts() {
        Map<PageType, Long> result = new HashMap<>();
        for (PageType pageType : PageType.values()) {
            result.put(pageType, getViewCount(pageType));
        }

        return result;
    }

    public void deleteViewCount(PageType pageType) {
        String key = "view:" + pageType.getValue();
        redisTemplate.delete(key);
    }
}
