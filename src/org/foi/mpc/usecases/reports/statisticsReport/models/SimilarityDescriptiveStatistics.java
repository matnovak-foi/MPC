package org.foi.mpc.usecases.reports.statisticsReport.models;

import java.util.Arrays;

public class SimilarityDescriptiveStatistics {
    public double mean;
    public double[] mode;
    public double median;
    public double q1;
    public double q3;
    public double min;
    public double max;
    public double percentile99;
    public double STD;
    public double IRQ;

    @Override
    public String toString() {
        return "SimilarityDescriptiveStatistics{" +
                "mean=" + mean +
                ", mode=" + Arrays.toString(mode) +
                ", median=" + median +
                ", q1=" + q1 +
                ", q3=" + q3 +
                ", min=" + min +
                ", max=" + max +
                ", percentile99=" + percentile99 +
                ", STD=" + STD +
                ", IRQ=" + IRQ +
                '}';
    }
}
