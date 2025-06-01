package com.example.greenprojectB.dto;

import lombok.*;

@Getter
@Setter
@ToString
public class Stats {
    double min = Double.MAX_VALUE;
    double max = Double.MIN_VALUE;
    double sum = 0;
    int count = 0;

    public void accept(double val) {
        if (val < min) min = val;
        if (val > max) max = val;
        sum += val;
        count++;
    }

    double mean() {
        return count == 0 ? 0 : sum / count;
    }
}
