package com.ggiiig.center.service;

import com.example.front.center.dto.ServiceCenterDto;
import com.example.front.center.entity.Review;
import com.example.front.center.entity.ServiceCenter;
import com.example.front.center.repository.ServiceCenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceCenterService {

    private final ServiceCenterRepository serviceCenterRepository;

    // 더미 데이터 생성
    private List<ServiceCenter> createDummyData() {
        // 리뷰 더미 데이터
        List<Review> reviews1 = Arrays.asList(
            Review.builder().customerName("김철수").rating(5.0).comment("정말 친절하고 꼼꼼하게 수리해주셨어요. 가격도 합리적이고 추천합니다!").createdAt(LocalDateTime.now().minusDays(5)).build(),
            Review.builder().customerName("이영희").rating(4.5).comment("예약제로 운영해서 대기시간이 없어서 좋았습니다. 설명도 자세히 해주시네요.").createdAt(LocalDateTime.now().minusDays(3)).build(),
            Review.builder().customerName("박민수").rating(5.0).comment("사고차 수리 맡겼는데 완전 새차처럼 나왔어요. 기술력이 정말 좋습니다.").createdAt(LocalDateTime.now().minusDays(1)).build()
        );

        List<Review> reviews2 = Arrays.asList(
            Review.builder().customerName("최유진").rating(4.5).comment("빠른 수리로 당일에 차를 찾을 수 있었어요. 급한 일이 있을 때 도움이 됩니다.").createdAt(LocalDateTime.now().minusDays(4)).build(),
            Review.builder().customerName("정한국").rating(4.8).comment("직원분들이 매우 친절하고 서비스가 좋습니다. 재방문 의사 있어요.").createdAt(LocalDateTime.now().minusDays(2)).build()
        );

        List<Review> reviews3 = Arrays.asList(
            Review.builder().customerName("강미래").rating(4.7).comment("견적을 투명하게 알려주셔서 믿음이 갔습니다. 작업도 깔끔하게 해주셨어요.").createdAt(LocalDateTime.now().minusDays(6)).build(),
            Review.builder().customerName("윤성호").rating(4.6).comment("가격 대비 만족도가 높습니다. 정기점검도 여기서 받으려고 해요.").createdAt(LocalDateTime.now().minusDays(1)).build()
        );

        List<Review> reviews4 = Arrays.asList(
            Review.builder().customerName("임소영").rating(4.3).comment("시설이 깔끔하고 현대적이에요. 진단 장비가 좋아서 정확한 점검이 가능합니다.").createdAt(LocalDateTime.now().minusDays(3)).build(),
            Review.builder().customerName("홍길동").rating(4.7).comment("친절한 상담과 합리적인 가격이 마음에 듭니다. 추천해요!").createdAt(LocalDateTime.now().minusDays(1)).build()
        );

        return Arrays.asList(
            ServiceCenter.builder()
                .id(1L)
                .name("프리미엄 카서비스")
                .address("서울시 강남구 테헤란로 123")
                .phoneNumber("02-1234-5678")
                .latitude(37.5665)
                .longitude(126.9780)
                .rating(4.8)
                .description("20년 경력의 숙련된 정비사들이 최고의 서비스를 제공합니다. 수입차 전문 정비와 보험 수리를 전문으로 합니다.")
                .operatingHours("평일 09:00-18:00, 토요일 09:00-15:00")
                .reviews(reviews1)
                .build(),
            ServiceCenter.builder()
                .id(2L)
                .name("신속정비 카센터")
                .address("서울시 서초구 서초대로 456")
                .phoneNumber("02-2345-6789")
                .latitude(37.4943)
                .longitude(127.0292)
                .rating(4.6)
                .description("빠르고 정확한 정비 서비스로 고객 만족도 최고! 일반 정비부터 사고 수리까지 원스톱 서비스 제공.")
                .operatingHours("평일 08:00-19:00, 토요일 08:00-17:00")
                .reviews(reviews2)
                .build(),
            ServiceCenter.builder()
                .id(3L)
                .name("믿음직한 오토케어")
                .address("서울시 송파구 올림픽로 789")
                .phoneNumber("02-3456-7890")
                .latitude(37.5147)
                .longitude(127.1060)
                .rating(4.7)
                .description("정직한 가격과 투명한 견적으로 고객의 신뢰를 얻고 있는 카센터입니다. 각종 브랜드 차량 정비 가능.")
                .operatingHours("평일 09:00-18:30, 토요일 09:00-16:00")
                .reviews(reviews3)
                .build(),
            ServiceCenter.builder()
                .id(4L)
                .name("스마트 카케어")
                .address("서울시 마포구 월드컵로 321")
                .phoneNumber("02-4567-8901")
                .latitude(37.5669)
                .longitude(126.9019)
                .rating(4.5)
                .description("최신 장비와 기술력으로 정밀한 진단과 수리를 제공합니다. 친절한 서비스가 자랑입니다.")
                .operatingHours("평일 08:30-19:00, 토요일 09:00-15:30")
                .reviews(reviews4)
                .build()
        );
    }

    // 평점순으로 상위 4개 카센터 조회 (더미 데이터를 DTO로 반환)
    public List<ServiceCenterDto> getTopServiceCentersByRating() {
        return Arrays.asList(
            ServiceCenterDto.builder()
                .id(1L)
                .name("프리미엄 카서비스")
                .address("서울시 강남구 테헤란로 123")
                .phoneNumber("02-1234-5678")
                .rating(4.8)
                .description("20년 경력의 숙련된 정비사들이 최고의 서비스를 제공합니다.")
                .operatingHours("평일 09:00-18:00, 토요일 09:00-15:00")
                .build(),
            ServiceCenterDto.builder()
                .id(2L)
                .name("믿음직한 오토케어")
                .address("서울시 송파구 올림픽로 789")
                .phoneNumber("02-3456-7890")
                .rating(4.7)
                .description("정직한 가격과 투명한 견적으로 고객의 신뢰를 얻고 있는 카센터입니다.")
                .operatingHours("평일 09:00-18:30, 토요일 09:00-16:00")
                .build(),
            ServiceCenterDto.builder()
                .id(3L)
                .name("신속정비 카센터")
                .address("서울시 서초구 서초대로 456")
                .phoneNumber("02-2345-6789")
                .rating(4.6)
                .description("빠르고 정확한 정비 서비스로 고객 만족도 최고!")
                .operatingHours("평일 08:00-19:00, 토요일 08:00-17:00")
                .build(),
            ServiceCenterDto.builder()
                .id(4L)
                .name("스마트 카케어")
                .address("서울시 마포구 월드컵로 321")
                .phoneNumber("02-4567-8901")
                .rating(4.5)
                .description("최신 장비와 기술력으로 정밀한 진단과 수리를 제공합니다.")
                .operatingHours("평일 08:30-19:00, 토요일 09:00-15:30")
                .build()
        );
    }

    // 거리순으로 상위 4개 카센터 조회 (사용자 위치 기준)
    public List<ServiceCenter> getTopServiceCentersByDistance(double userLat, double userLng) {
        return createDummyData().stream()
                .sorted(Comparator.comparingDouble(center -> center.calculateDistance(userLat, userLng)))
                .limit(4)
                .collect(Collectors.toList());
    }

    // 특정 카센터 상세 정보 조회 (DTO로 반환)
    public ServiceCenterDto getServiceCenterById(Long id) {
        // 더미 데이터에서 해당 ID의 카센터 찾기
        return getTopServiceCentersByRating().stream()
                .filter(center -> center.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("카센터를 찾을 수 없습니다."));
    }

    // 모든 카센터 조회 (정렬 옵션에 따라)
    public List<ServiceCenter> getAllServiceCenters(String sortBy, Double userLat, Double userLng) {
        List<ServiceCenter> centers = serviceCenterRepository.findAllServiceCenters();

        if ("distance".equals(sortBy) && userLat != null && userLng != null) {
            return centers.stream()
                    .sorted(Comparator.comparingDouble(center -> center.calculateDistance(userLat, userLng)))
                    .collect(Collectors.toList());
        } else {
            return centers.stream()
                    .sorted(Comparator.comparingDouble(ServiceCenter::getRating).reversed())
                    .collect(Collectors.toList());
        }
    }
}
