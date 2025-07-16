package com.ggiiig.util;

import com.ggiiig.file.dto.response.ResponseDto;
import com.ggiiig.part.domain.CarPart;

import java.text.DecimalFormat;

import static org.springframework.web.util.HtmlUtils.htmlEscape;

public class CarRepairCostCalculator {
    private CarRepairCostCalculator() {
    }

    private static final int BASIC_PAINT_COST = 300000;
    private static final double UNIT = 100000.0;

    // 손상 임계값 상수
    private static final float MINOR_DAMAGE_THRESHOLD = 20.0f;
    private static final float SEVERE_BREAKAGE_THRESHOLD = 300.0f;
    private static final float SEVERE_CRUSHED_THRESHOLD = 400.0f;
    private static final float TOTAL_DAMAGE_THRESHOLD = 600.0f;
    private static final float SIGNIFICANT_SCRATCH_THRESHOLD = 50.0f;
    private static final float MODERATE_SCRATCH_THRESHOLD = 60.0f;

    // 부품별 추가 도색 비용
    private static final int HOOD_ADDITIONAL_COST = 100000;
    private static final int ROOF_ADDITIONAL_COST = 200000;
    private static final int DOOR_ADDITIONAL_COST = 50000;

    private static final String NEW_LINE = "\n";
    private static final String HTML_NEW_LINE = "<br>";

    public static String calculate(ResponseDto responseDto, CarPart carPart) {
        if (responseDto == null || carPart == null) {
            throw new IllegalArgumentException("responseDto 또는 carPart 는 null 이 될 수 없습니다.");
        }

        String rawMessage = generateMessage(responseDto, carPart);
        return convertNewLineToBrTags(rawMessage);
    }

    private static String generateMessage(ResponseDto responseDto, CarPart carPart) {
        if (isReplacementRecommended(responseDto, carPart)) {
            return "교체 추천" + NEW_LINE + "비용 : " + addThousandsSeparator(carPart.getReplacementCost()) + "원";
        }

        int repairCost = roundToUnit(calculateRepairCost(responseDto, carPart));
        int replacementCost = carPart.getReplacementCost();
        double percent = calculateRepairPercent(repairCost, replacementCost);

        // 수리 비용이 교체 비용보다 크면 교체 추천
        if (repairCost > replacementCost) {
            return "교체 추천" + NEW_LINE + "비용 : " + addThousandsSeparator(replacementCost) + "원";
            // 수리 비용이 교체 비용의 45~55% 범위이면 두 옵션 모두 제시
        } else if (percent >= 45 && percent < 55) {
            return "교체 비용 : " + addThousandsSeparator(replacementCost) + NEW_LINE + "판금 및 도색 비용 : "
                    + addThousandsSeparator(repairCost) + "원";
            // 그 외의 경우 수리 추천
        } else {
            return "판금 및 도색 추천" + NEW_LINE + "비용 : " + addThousandsSeparator(repairCost) + "원";
        }
    }

    private static int calculateRepairCost(ResponseDto responseDto, CarPart carPart) {
        if (isReplacementRecommended(responseDto, carPart)) {
            return carPart.getReplacementCost();
        }

        int sheetMetalCost = calculateSheetMetalCost(responseDto, carPart);
        int paintCost = calculatePaintCost(responseDto.getScratch(), carPart);

        return Math.min(carPart.getReplacementCost(), (sheetMetalCost + paintCost));
    }

    private static int calculateSheetMetalCost(ResponseDto responseDto, CarPart carPart) {
        float breakageAmount = responseDto.getBreakage();
        float crushedAmount = responseDto.getCrushed();
        float scratchAmount = responseDto.getScratch();

        // 1. 파손과 찌그러짐이 모두 경미한 경우 → 판금 없음
        if (breakageAmount < MINOR_DAMAGE_THRESHOLD && crushedAmount < MINOR_DAMAGE_THRESHOLD) {
            return 0;
        }

        // 2. 심각한 손상의 경우 → 교체 비용 - 도색 비용
        if ((breakageAmount + crushedAmount > TOTAL_DAMAGE_THRESHOLD) ||
                breakageAmount > SEVERE_BREAKAGE_THRESHOLD ||
                crushedAmount > SEVERE_CRUSHED_THRESHOLD) {
            return carPart.getReplacementCost() - calculatePaintCost(scratchAmount, carPart);
        }

        // 3. 파손과 찌그러짐이 모두 존재하는 경우
        if (breakageAmount >= MINOR_DAMAGE_THRESHOLD && crushedAmount >= MINOR_DAMAGE_THRESHOLD) {
            return (int) ((breakageAmount + crushedAmount) / 6 * 10000);
        }

        // 4. 파손만 있는 경우
        if (breakageAmount >= MINOR_DAMAGE_THRESHOLD) {
            return (int) (breakageAmount / 3 * 10000);
        }

        // 5. 찌그러짐만 있는 경우
        return scratchAmount >= SIGNIFICANT_SCRATCH_THRESHOLD ? (int) (crushedAmount / 3 * 20000) : 50000;
    }

    private static boolean isReplacementRecommended(ResponseDto responseDto, CarPart carPart) {
        return carPart.isReplacementOnly() || responseDto.getSeperated() >= SIGNIFICANT_SCRATCH_THRESHOLD;
    }

    private static int roundToUnit(double cost) {
        return (int) (Math.round(cost / UNIT) * UNIT);
    }

    private static double calculateRepairPercent(int repairCost, int replacementCost) {
        if (replacementCost == 0) {
            return 100.0;
        }

        return (repairCost * 100.0) / replacementCost;
    }

    private static int calculatePaintCost(float scratchAmount, CarPart carPart) {
        // 스크래치가 경미한 경우 도색 비용 없음
        if (scratchAmount < MINOR_DAMAGE_THRESHOLD) {
            return 0;
        }

        // 중간 정도의 스크래치는 면적에 비례한 비용 계산
        if (scratchAmount < MODERATE_SCRATCH_THRESHOLD) {
            return (int) (scratchAmount * 5000);
        }

        // 심각한 스크래치(60 이상)는 부품에 따라 기본 비용 + 추가 비용 적용
        switch (carPart) {
            case HOOD:
                return BASIC_PAINT_COST + HOOD_ADDITIONAL_COST; // 후드는 넓은 면적과 복잡한 도색 작업이 필요
            case ROOF:
                return BASIC_PAINT_COST + ROOF_ADDITIONAL_COST; // 루프는 가장 넓은 면적과 특수 도색 기법 필요
            case FRONT_DOOR, REAR_DOOR:
                return BASIC_PAINT_COST + DOOR_ADDITIONAL_COST; // 도어는 추가 작업 필요
            default:
                return BASIC_PAINT_COST;
        }
    }

    private static String convertNewLineToBrTags(String message) {
        // HTML 엔티티 이스케이프 후, 줄바꿈을 <br> 태그로 변환
        String escaped = htmlEscape(message);

        return escaped.replace(NEW_LINE, HTML_NEW_LINE);
    }

    private static String addThousandsSeparator(int amount) {
        return new DecimalFormat("#,###").format(amount);
    }
}
