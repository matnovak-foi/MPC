package org.foi.mpc.usecases.reports.summaryReport.view.models;

public class PresentableDetailsMatchPart {
    private int startLineNumberA;
    private int startLineNumberB;
    private int endLineNumberA;
    private int endLineNumberB;
    private double similarity;
    private double similarityA;
    private double similarityB;
    private int lineCountA;
    private int lineCountB;
    private String matchesDir;

    public int getStartLineNumberA() {
        return startLineNumberA;
    }

    public void setStartLineNumberA(int startLineNumberA) {
        this.startLineNumberA = startLineNumberA;
    }

    public int getStartLineNumberB() {
        return startLineNumberB;
    }

    public void setStartLineNumberB(int startLineNumberB) {
        this.startLineNumberB = startLineNumberB;
    }

    public int getEndLineNumberA() {
        return endLineNumberA;
    }

    public void setEndLineNumberA(int endLineNumberA) {
        this.endLineNumberA = endLineNumberA;
    }

    public int getEndLineNumberB() {
        return endLineNumberB;
    }

    public void setEndLineNumberB(int endLineNumberB) {
        this.endLineNumberB = endLineNumberB;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    public double getSimilarityA() {
        return similarityA;
    }

    public void setSimilarityA(double similarityA) {
        this.similarityA = similarityA;
    }

    public double getSimilarityB() {
        return similarityB;
    }

    public void setSimilarityB(double similarityB) {
        this.similarityB = similarityB;
    }

    public int getLineCountA() {
        return lineCountA;
    }

    public void setLineCountA(int lineCountA) {
        this.lineCountA = lineCountA;
    }

    public int getLineCountB() {
        return lineCountB;
    }

    public void setLineCountB(int lineCountB) {
        this.lineCountB = lineCountB;
    }

    @Override
    public String toString() {
        return "PresentableDetailsMatchPart{" +
                "startLineNumberA=" + startLineNumberA +
                ", startLineNumberB=" + startLineNumberB +
                ", endLineNumberA=" + endLineNumberA +
                ", endLineNumberB=" + endLineNumberB +
                ", similarity=" + similarity +
                ", similarityA=" + similarityA +
                ", similarityB=" + similarityB +
                ", lineCountA=" + lineCountA +
                ", lineCountB=" + lineCountB +
                '}';
    }

    public String getMatchesDir() {
        return matchesDir;
    }

    public void setMatchesDir(String matchesDir) {
        this.matchesDir = matchesDir;
    }
}
