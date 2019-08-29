package org.foi.mpc.usecases.reports.summaryReport.models;

import org.foi.mpc.usecases.reports.statisticsReport.models.SimilarityDescriptiveStatistics;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class SummaryReportResponseModel {
    public String errorMessages;
    public String toolName;
    public String techniqueName;
    public ArrayList<SummaryReportTableRow> reportTable;
    public File matchesDirPath;
    public SimilarityDescriptiveStatistics similarityStatistics;

    public static class SummaryReportTableRow {

        public String studentA;
        public String studentB;
        public float similarity;
        public float similarityA;
        public float similarityB;
        public String fileAName;
        public String fileBName;
        public float calculatedSimilarity;
        public float calculatedSimilarityA;
        public float calculatedSimilarityB;
        public boolean processed;
        public boolean plagiarized;

        @Override
        public String toString() {
            return "SummaryReportTable{" +
                    "studentA='" + studentA + '\'' +
                    ", studentB='" + studentB + '\'' +
                    ", similarity=" + similarity +
                    ", similarityA=" + similarityA +
                    ", similarityB=" + similarityB +
                    ", fileAName='" + fileAName + '\'' +
                    ", fileBName='" + fileBName + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SummaryReportTableRow)) return false;
            SummaryReportTableRow that = (SummaryReportTableRow) o;
            return Float.compare(that.similarity, similarity) == 0 &&
                    Float.compare(that.similarityA, similarityA) == 0 &&
                    Float.compare(that.similarityB, similarityB) == 0 &&
                    Objects.equals(studentA, that.studentA) &&
                    Objects.equals(studentB, that.studentB) &&
                    Objects.equals(fileAName, that.fileAName) &&
                    Objects.equals(fileBName, that.fileBName);
        }
    }

    @Override
    public String toString() {
        return "SummaryReportResponseModel{" +
                "errorMessages='" + errorMessages + '\'' +
                ", toolName='" + toolName + '\'' +
                ", techniqueName='" + techniqueName + '\'' +
                ", reportTable=" + reportTable +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SummaryReportResponseModel)) return false;
        SummaryReportResponseModel that = (SummaryReportResponseModel) o;
        return Objects.equals(errorMessages, that.errorMessages) &&
                Objects.equals(toolName, that.toolName) &&
                Objects.equals(techniqueName, that.techniqueName) &&
                Objects.equals(reportTable, that.reportTable);
    }
}
