package org.foi.mpc.usecases.reports.summaryReport.view.models;

import java.util.Collection;
import java.util.List;

public class PresentableDetailSimilarityTable {
    private String tool;
    private String technique;
    private float similarity;
    private float similarityA;
    private float similarityB;
    private float calculatedSimilarity;
    private float calculatedSimilarityA;
    private float calculatedSimilarityB;
    private List<PresentableDetailsMatchPart> matchParts;
    private int totalLineCountA;
    private int totalLineCountB;
    private String matchesDir;

    public String getTool() {
        return tool;
    }

    public void setTool(String tool) {
        this.tool = tool;
    }

    public String getTechnique() {
        return technique;
    }

    public void setTechnique(String technique) {
        this.technique = technique;
    }

    public float getSimilarity() {
        return similarity;
    }

    public void setSimilarity(float similarity) {
        this.similarity = similarity;
    }

    public float getSimilarityA() {
        return similarityA;
    }

    public void setSimilarityA(float similarityA) {
        this.similarityA = similarityA;
    }

    public float getSimilarityB() {
        return similarityB;
    }

    public void setSimilarityB(float similarityB) {
        this.similarityB = similarityB;
    }

    public float getCalculatedSimilarity() {
        return calculatedSimilarity;
    }

    public void setCalculatedSimilarity(float calculatedSimilarity) {
        this.calculatedSimilarity = calculatedSimilarity;
    }

    public float getCalculatedSimilarityA() {
        return calculatedSimilarityA;
    }

    public void setCalculatedSimilarityA(float calculatedSimilarityA) {
        this.calculatedSimilarityA = calculatedSimilarityA;
    }

    public float getCalculatedSimilarityB() {
        return calculatedSimilarityB;
    }

    public void setCalculatedSimilarityB(float calculatedSimilarityB) {
        this.calculatedSimilarityB = calculatedSimilarityB;
    }

    public List<PresentableDetailsMatchPart> getMatchParts() {
        return matchParts;
    }

    public void setMatchParts(List<PresentableDetailsMatchPart> matchParts) {
        this.matchParts = matchParts;
    }

    public int getTotalLineCountA() {
        return totalLineCountA;
    }

    public void setTotalLineCountA(int totalLineCountA) {
        this.totalLineCountA = totalLineCountA;
    }

    public int getTotalLineCountB() {
        return totalLineCountB;
    }

    public void setTotalLineCountB(int totalLineCountB) {
        this.totalLineCountB = totalLineCountB;
    }

    public String getMatchesDir() {
        return matchesDir;
    }

    public void setMatchesDir(String matchesDir) {
        this.matchesDir = matchesDir;
    }
}
