package com.example.front.util;

import com.example.front.file.dto.response.ResponseDto;
import com.example.front.part.domain.CarPart;

public class CarRepairCostCalculator {
    private CarRepairCostCalculator() {
    }

    private static final int BASIC_PAINT_COST = 300000;
    private static final double UNIT = 100000.0;
    private static final String NEW_LINE = "\n";
    private static final String HTML_NEW_LINE = "<br>";

    public static String calculate(ResponseDto responseDto, CarPart carPart) {
        String rawMessage = generateMessage(responseDto, carPart);
        return convertNewLineToBrTags(rawMessage);
    }

    private static String generateMessage(ResponseDto responseDto, CarPart carPart) {
        if (isReplacementRecommended(responseDto, carPart)) {
            return "교체 추천" + NEW_LINE + "비용 : " + carPart.getReplacementCost() + "원";
        }

        int repairCost = roundToUnit(calculateRepairCost(responseDto, carPart));
        int replacementCost = carPart.getReplacementCost();
        double percent = calculateRepairPercent(repairCost, replacementCost);

        if (repairCost > replacementCost) {
            return "교체 추천" + NEW_LINE + "비용 : " + replacementCost + "원";
        } else if (percent >= 45 && percent < 55) {
            return "교체 비용 : " + replacementCost + NEW_LINE + "판금 및 도색 비용 : " + repairCost + "원";
        } else {
            return "판금 및 도색 추천" + NEW_LINE + "비용 : " + repairCost + "원";
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

    private static int calculateSheetMetalCost(ResponseDto responseDto, CarPart carpart) {
        float breakageAmount = responseDto.getBreakage();
        float crushedAmount = responseDto.getCrushed();

        if (breakageAmount < 20 && crushedAmount < 20) {
            return 0;
        } else if ((breakageAmount + crushedAmount > 600) || breakageAmount > 300 || crushedAmount > 400) {
            return carpart.getReplacementCost() - calculatePaintCost(responseDto.getScratch(), carpart);
        } else if (breakageAmount >= 20 && crushedAmount >= 20) {
            return (int) (breakageAmount + crushedAmount) / 6 * 10000;
        } else if (breakageAmount >= 20 && crushedAmount < 20) {
            return (int) (breakageAmount / 3 * 10000);
        } else if (breakageAmount < 20 && crushedAmount >= 20) {
            if (responseDto.getScratch() >= 50) {
                return (int) crushedAmount / 3 * 20000;
            }

            return 50000;
        }

        return 0;
    }

    private static boolean isReplacementRecommended(ResponseDto responseDto, CarPart carPart) {
        return carPart.isReplacementOnly() || responseDto.getSeperated() >= 50;
    }

    private static int roundToUnit(double cost) {
        return (int) (Math.round(cost / UNIT) * UNIT);
    }

    private static double calculateRepairPercent(int repairCost, int replacementCost) {
        return (repairCost * 100.0) / replacementCost;
    }

    private static int calculatePaintCost(float scratchAmount, CarPart carPart) {
        if (scratchAmount < 20) {
            return 0;
        } else if (scratchAmount >= 20 && scratchAmount < 60) {
            return (int) (scratchAmount * 5000);
        }

        switch (carPart) {
            case HOOD:
                return BASIC_PAINT_COST + 100000;
            case ROOF:
                return BASIC_PAINT_COST + 200000;
            case FRONT_DOOR, REAR_DOOR:
                return BASIC_PAINT_COST + 50000;
            default:
                return BASIC_PAINT_COST;
        }
    }

    private static String convertNewLineToBrTags(String message) {
        return message.replace(NEW_LINE, HTML_NEW_LINE);
    }
}
