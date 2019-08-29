package org.foi.mpc.usecases.toolCalibration.models;

public class CaseSimilarityBaseTool {
    public String caseName;
    public float similarity;

    @Override
    public String toString() {
        return "CaseSimilarityBaseTool{" +
                "caseName='" + caseName + '\'' +
                ", similarity=" + similarity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CaseSimilarityBaseTool)) return false;

        CaseSimilarityBaseTool that = (CaseSimilarityBaseTool) o;

        if (Float.compare(that.similarity, similarity) != 0) return false;
        return caseName.equals(that.caseName);
    }
}
