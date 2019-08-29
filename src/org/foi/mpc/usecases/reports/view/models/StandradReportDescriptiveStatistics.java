package org.foi.mpc.usecases.reports.view.models;

import java.util.List;

public abstract class StandradReportDescriptiveStatistics {
    private double mean;
    private List<Double> mode;
    private double median;
    private double q1;
    private double q3;
    private double min;
    private double max;
    private double percentile99;
    private double IRQ;
    private double STD;
    private double medianPlus3IRQ;
    private double medianPlus2p5IRQ;
    private double medianPlus2IRQ;

    public void setPercentile99(double percentile99) {
        this.percentile99 = percentile99;
    }

    public double getPercentile99() {
        return percentile99;
    }

    public double getIRQ() {
        return IRQ;
    }

    public void setIRQ(double irq) {
        this.IRQ = irq;
    }

    public double getSTD() {
        return STD;
    }

    public void setSTD(double std) {
        this.STD = std;
    }

    public double getMedianPlus3IRQ() {
        return medianPlus3IRQ;
    }

    public void setMedianPlus3IRQ(double medianPlus3IRQ) {
        this.medianPlus3IRQ = medianPlus3IRQ;
    }

    public double getMedianPlus2p5IRQ() {
        return medianPlus2p5IRQ;
    }

    public void setMedianPlus2p5IRQ(double medianPlus2p5IRQ) {
        this.medianPlus2p5IRQ = medianPlus2p5IRQ;
    }

    public double getMedianPlus2IRQ() {

        return medianPlus2IRQ;
    }

    public void setMedianPlus2IRQ(double medianPlus2IRQ) {
        this.medianPlus2IRQ = medianPlus2IRQ;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public List<Double> getMode() {
        return mode;
    }

    public void setMode(List<Double> mode) {
        this.mode = mode;
    }

    public double getMedian() {
        return median;
    }

    public void setMedian(double median) {
        this.median = median;
    }

    public double getQ1() {
        return q1;
    }

    public void setQ1(double q1) {
        this.q1 = q1;
    }

    public double getQ3() {
        return q3;
    }

    public void setQ3(double q3) {
        this.q3 = q3;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }
}
