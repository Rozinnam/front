package com.ggiiig.center.repository;

import com.example.front.center.entity.ServiceCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServiceCenterRepository extends JpaRepository<ServiceCenter, Long> {

    // 평점순으로 상위 4개 조회
    List<ServiceCenter> findTop4ByOrderByRatingDesc();

    // 모든 카센터를 평점순으로 조회
    List<ServiceCenter> findAllByOrderByRatingDesc();

    // 특정 위치 기준 거리순으로 상위 4개 조회 (JPA에서는 직접 거리 계산이 어려우므로 서비스에서 처리)
    @Query("SELECT sc FROM ServiceCenter sc")
    List<ServiceCenter> findAllServiceCenters();
}
