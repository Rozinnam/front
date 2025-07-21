package com.ggiiig.util;

import lombok.Getter;

import java.text.DecimalFormat;

@Getter
public class PriceRange {
    private final int max;
    private final int min;

    public PriceRange(int amount) {
        this.max = calculateMax(amount);
        this.min = calculateMin(amount);
    }

    private static int calculateMax(int amount) {
        double discounted = amount * 1.10;
        return (int)(Math.round(discounted / 10000.0) * 10000);
    }

    private static int calculateMin(int amount) {
        double discounted = amount * 0.9;
        return (int)(Math.round(discounted / 10000.0) * 10000);
    }

    public String printRange() {
        return addThousandsSeparator(this.min) + " ~ " + addThousandsSeparator(this.max);
    }

    public String addThousandsSeparator(int amount) {
        return new DecimalFormat("#,###").format(amount);
    }
}
