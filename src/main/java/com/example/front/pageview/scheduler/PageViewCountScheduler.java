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

@Slf4j
@Service
@RequiredArgsConstructor
public class PageViewCountScheduler {
    private final PageViewCountService pageViewCountService;
    private final PageViewCountRepository pageViewCountRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void syncViewCountToDB() {
        Map<PageType, Long> viewCounts = pageViewCountService.getAllViewCounts();
        LocalDate date = LocalDate.now().minusDays(1);
        log.info("Redis -> DB 스케쥴러 시작. 날짜 : {}\n", date);

        for (Map.Entry<PageType, Long> entry : viewCounts.entrySet()) {
            PageType pageType = entry.getKey();
            Long count = entry.getValue();

            PageView pageView = new PageView(new PageView.Pk(date, pageType), count);

            pageViewCountRepository.save(pageView);
            log.info("DB에 적재. 날짜 : {}, 페이지 타입 : {}, 조회수 : {}\n",
                    pageView.getPk().getViewDate(), pageView.getPk().getPageType(), pageView.getViewCount());

            pageViewCountService.deleteViewCount(pageType);
            log.info("Redis에서 삭제. 날짜 : {}, 페이지 타입 : {}, 조회수 : {}\n",
                    pageView.getPk().getViewDate(), pageView.getPk().getPageType(), pageView.getViewCount());
        }
    }
}
