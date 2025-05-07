package com.example.front.pageview.scheduler;

import com.example.front.pageview.entity.PageType;
import com.example.front.pageview.entity.PageView;
import com.example.front.pageview.repository.PageViewCountRepository;
import com.example.front.pageview.service.PageViewCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;

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

        for (Map.Entry<PageType, Long> entry : viewCounts.entrySet()) {
            PageType pageType = entry.getKey();
            Long count = entry.getValue();

            PageView pageView = new PageView(new PageView.Pk(date, pageType), count);

            pageViewCountRepository.save(pageView);
            pageViewCountService.deleteViewCount(pageType);
        }
    }
}
