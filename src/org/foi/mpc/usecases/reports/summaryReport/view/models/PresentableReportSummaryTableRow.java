package org.foi.mpc.usecases.reports.summaryReport.view.models;

import java.util.Objects;

public class PresentableReportSummaryTableRow {

    private String studentA;
    private String studentB;
    private float similarity;
    private float similarityA;
    private float similarityB;
    private String fileA;
    private String fileB;
    private float calculatedSimilarity;
    private float calculatedSimilarityA;
    private float calculatedSimilarityB;
    private boolean plagiarized;
    private boolean processed;
    private Boolean processedDisabled = true;
    private Boolean plagiarizedDisabled = true;

    public String getStudentA() {
        return studentA;
    }

    public void setStudentA(String studentA) {
        this.studentA = studentA;
    }

    public String getStudentB() {
        return studentB;
    }

    public void setStudentB(String studentB) {
        this.studentB = studentB;
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

    public String getFileA() {
        return fileA;
    }

    public void setFileA(String fileA) {
        this.fileA = fileA;
    }

    public String getFileB() {
        return fileB;
    }

    public void setFileB(String fileB) {
        this.fileB = fileB;
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

    public boolean getPlagiarized() {
        return plagiarized;
    }

    public void setPlagiarized(boolean plagiarized) {
        this.plagiarized = plagiarized;
    }

    public boolean getProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public void setProcessedDisabled(Boolean processedDisabled) {
        this.processedDisabled = processedDisabled;
    }

    public Boolean getProcessedDisabled() {
        return processedDisabled;
    }

    public Boolean getPlagiarizedDisabled() {
        return plagiarizedDisabled;
    }

    public void setPlagiarizedDisabled(Boolean plagiarizedDisabled) {
        this.plagiarizedDisabled = plagiarizedDisabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PresentableReportSummaryTableRow)) return false;
        PresentableReportSummaryTableRow that = (PresentableReportSummaryTableRow) o;
        return (Objects.equals(studentA, that.studentA) &&
                Objects.equals(studentB, that.studentB)) ||
                (Objects.equals(fileA, that.fileA) &&
                Objects.equals(fileB, that.fileB));
    }

    @Override
    public String toString() {
        return "PresentableReportSummaryTable{" +
                "studentA='" + studentA + '\'' +
                ", studentB='" + studentB + '\'' +
                ", similarity=" + similarity +
                ", similarityA=" + similarityA +
                ", similarityB=" + similarityB +
                ", fileA='" + fileA + '\'' +
                ", fileB='" + fileB + '\'' +
                ", calculatedSimilarity=" + calculatedSimilarity +
                ", calculatedSimilarityA=" + calculatedSimilarityA +
                ", calculatedSimilarityB=" + calculatedSimilarityB +
                ", plagiarized=" + plagiarized +
                ", processed=" + processed +
                ", processedDisabled=" + processedDisabled +
                '}';
    }


}
