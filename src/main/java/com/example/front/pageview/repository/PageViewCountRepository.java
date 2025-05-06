package com.example.front.pageview.repository;

import com.example.front.pageview.entity.PageType;
import com.example.front.pageview.entity.PageView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface PageViewCountRepository extends JpaRepository<PageView, PageView.Pk> {
    void saveOrUpdate(LocalDate date, PageType pageType, Long viewCount);
}
