package org.foi.mpc.usecases.reports.pptestreport.view.models;

public class PresentableReportPPTechnique {
    private String name;
    private String similarity;
    private String similarityA;
    private String similarityB;

    public void setName(String name) {
        this.name = name;
    }

    public void setSimilarity(String similarity) {
        this.similarity = similarity;
    }

    public void setSimilarityA(String similarityA) {
        this.similarityA = similarityA;
    }

    public void setSimilarityB(String similarityB) {
        this.similarityB = similarityB;
    }

    public String getName() {
        return name;
    }

    public String getSimilarity() {
        return similarity;
    }

    public String getSimilarityA() {
        return similarityA;
    }

    public String getSimilarityB() {
        return similarityB;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PresentableReportPPTechnique)) return false;

        PresentableReportPPTechnique that = (PresentableReportPPTechnique) o;

        return name.equals(that.name)
                && similarity.equals(that.similarity)
                && similarityA.equals(that.similarityA)
                && similarityB.equals(that.similarityB);
    }

    @Override
    public String toString() {
        return "PresentableReportPPTechnique{" +
                "name='" + name + '\'' +
                ", similarity='" + similarity + '\'' +
                ", similarityA='" + similarityA + '\'' +
                ", similarityB='" + similarityB + '\'' +
                '}';
    }
}
