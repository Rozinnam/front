package com.example.front.pageview.repository;

import com.example.front.pageview.entity.PageView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageViewCountRepository extends JpaRepository<PageView, PageView.Pk> {
}
