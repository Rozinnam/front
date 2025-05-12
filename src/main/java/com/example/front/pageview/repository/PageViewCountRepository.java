package com.example.front.pageview.repository;

import com.example.front.pageview.dto.response.PageViewGetResponse;
import com.example.front.pageview.entity.PageView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PageViewCountRepository extends JpaRepository<PageView, PageView.Pk> {
    List<PageViewGetResponse> findByPk_ViewDate(LocalDate date);
}
