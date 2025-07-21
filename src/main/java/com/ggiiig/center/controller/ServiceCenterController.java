package com.ggiiig.center.controller;

import com.example.front.center.dto.ServiceCenterDto;
import com.example.front.center.service.ServiceCenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/center")
public class ServiceCenterController {

    private final ServiceCenterService serviceCenterService;

    // 카센터 상세정보 REST API
    @GetMapping("/{id}")
    public ResponseEntity<ServiceCenterDto> getServiceCenterDetail(@PathVariable Long id) {
        ServiceCenterDto serviceCenter = serviceCenterService.getServiceCenterById(id);
        return ResponseEntity.ok(serviceCenter);
    }

    // 카센터 리스트 REST API - 정렬 옵션에 따른 조회
    @GetMapping("/list")
    public ResponseEntity<List<ServiceCenterDto>> getServiceCenters(
            @RequestParam(defaultValue = "rating") String sortBy,
            @RequestParam(required = false) Double userLat,
            @RequestParam(required = false) Double userLng) {

        List<ServiceCenterDto> centers;

        if ("distance".equals(sortBy) && userLat != null && userLng != null) {
            // 거리순 정렬은 아직 DTO로 변환되지 않았으므로 평점순으로 대체
            centers = serviceCenterService.getTopServiceCentersByRating();
        } else {
            centers = serviceCenterService.getTopServiceCentersByRating();
        }

        return ResponseEntity.ok(centers);
    }
}
