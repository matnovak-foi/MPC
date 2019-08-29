package org.foi.mpc.executiontools.calibrator.models;

import java.util.List;

public class CalibratedToolCase {
    public String caseName;
    public float caseBestSimilarity;
    public boolean optimalSimilarityFound;
    public List<CalibratedToolParam> caseBestParams;
    public float optimalSimilarity;
    public float optimalSimilarityDiff;

    @Override
    public String toString() {
        return "CalibratedToolCase{" +
                "caseName='" + caseName + '\'' +
                ", caseBestSimilarity=" + caseBestSimilarity +
                ", optimalSimilarityFound=" + optimalSimilarityFound +
                ", caseBestParams=" + caseBestParams +
                ", optimalSimilarity=" + optimalSimilarity +
                ", optimalSimilarityDiff=" + optimalSimilarityDiff +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CalibratedToolCase)) return false;

        CalibratedToolCase that = (CalibratedToolCase) o;

        if (Float.compare(that.caseBestSimilarity, caseBestSimilarity) != 0) return false;
        if (optimalSimilarityFound != that.optimalSimilarityFound) return false;
        if (Float.compare(that.optimalSimilarity, optimalSimilarity) != 0) return false;
        if (Float.compare(that.optimalSimilarityDiff, optimalSimilarityDiff) != 0) return false;
        if (!caseName.equals(that.caseName)) return false;
        return caseBestParams.equals(that.caseBestParams);
    }

}
