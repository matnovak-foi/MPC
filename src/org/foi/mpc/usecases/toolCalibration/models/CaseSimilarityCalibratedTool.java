package org.foi.mpc.usecases.toolCalibration.models;

import java.util.List;

public class CaseSimilarityCalibratedTool {
    public String caseName;
    public float bestSimilarity;
    public List<ToolParam> calibratedToolParams;
    public float optimalSimilarityDiff;
    public float optimalSimilarity;

    @Override
    public String toString() {
        return "CaseSimilarityCalibratedTool{" +
                "caseName='" + caseName + '\'' +
                ", bestSimilarity=" + bestSimilarity +
                ", calibratedToolParams=" + calibratedToolParams +
                ", optimalSimilarityDiff=" + optimalSimilarityDiff +
                ", optimalSimilarity=" + optimalSimilarity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CaseSimilarityCalibratedTool)) return false;

        CaseSimilarityCalibratedTool that = (CaseSimilarityCalibratedTool) o;

        if (Float.compare(that.bestSimilarity, bestSimilarity) != 0) return false;
        if (Float.compare(that.optimalSimilarityDiff, optimalSimilarityDiff) != 0) return false;
        if (Float.compare(that.optimalSimilarity, optimalSimilarity) != 0) return false;
        if (!caseName.equals(that.caseName)) return false;
        return calibratedToolParams.equals(that.calibratedToolParams);
    }
}
