package com.example.front.pageview.scheduler;

import com.example.front.pageview.entity.PageType;
import com.example.front.pageview.entity.PageView;
import com.example.front.pageview.repository.PageViewCountRepository;
import com.example.front.pageview.service.PageViewCountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PageViewCountScheduler {
    private final PageViewCountService pageViewCountService;
    private final PageViewCountRepository pageViewCountRepository;

    @Scheduled(cron = "0 25 14 * * *")
    @Transactional
    public void syncViewCountToDB() {
        try {
            log.info("페이지 조회수 동기화 작업 시작");
            int processedCount = 0;
            int failedCount = 0;

            Map<PageType, Long> viewCounts = pageViewCountService.getAllViewCounts();
            LocalDate date = LocalDate.now().minusDays(1);
            log.info("처리할 페이지 타입 수: {}, 기준일자: {}", viewCounts.size(), date);

            for (Map.Entry<PageType, Long> entry : viewCounts.entrySet()) {
                PageType pageType = entry.getKey();
                Long count = entry.getValue();

                try {
                    PageView.Pk pk = new PageView.Pk(date, pageType);
                    Optional<PageView> existing = pageViewCountRepository.findById(pk);

                    if (existing.isPresent()) {
                        PageView pageView = existing.get();
                        pageView.updateViewCount(count);
                        pageViewCountRepository.save(pageView);
                    } else {
                        pageViewCountRepository.save(new PageView(pk, count));
                    }

//                    pageViewCountService.deleteViewCount(pageType);
                    processedCount++;
                    log.debug("페이지 타입 {} 처리 완료: {} 조회수", pageType, count);
                } catch (Exception e) {
                    failedCount++;
                    log.error("페이지 타입 {} 처리 중 오류 발생: {}", pageType, e.getMessage(), e);
                }
            }
            log.info("페이지 조회수 동기화 작업 완료. 성공: {}, 실패: {}", processedCount, failedCount);
        } catch (Exception e) {
            log.error("페이지 조회수 동기화 작업 중 오류 발생", e);
            throw e;
        }
    }
}
