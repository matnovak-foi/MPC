package org.foi.mpc.usecases.reports.statisticsReport.view.model;

import org.foi.mpc.usecases.reports.view.models.StandardReportInputData;
import org.foi.mpc.usecases.reports.view.models.StandardReportInputListData;
import org.foi.mpc.usecases.reports.view.models.StandradReportDescriptiveStatistics;

import java.io.File;
import java.util.List;

public class StatisticsReportViewModel extends StandardReportInputListData {
    private String assigmentDirPath;
    private int plagiarizedMatches;
    private List<PresentableStatisticsReportTableRow> reportTable;
    private PresentableStatisticsReportTableRow selectedReportTableRow;
    private String thresholdType;
    private double thresholdValue;

    public List<PresentableStatisticsReportTableRow> getReportTable() {
        return reportTable;
    }

    public void setReportTable(List<PresentableStatisticsReportTableRow> reportTable) {
        this.reportTable = reportTable;
    }

    public void setAssigmentDirPath(String assigmentDirPath) {
        this.assigmentDirPath = assigmentDirPath;
    }

    public String getAssigmentDirPath() {
        return assigmentDirPath;
    }

    public int getPlagiarizedMatches() {
        return plagiarizedMatches;
    }

    public void setPlagiarizedMatches(int plagiarizedMatches) {
        this.plagiarizedMatches = plagiarizedMatches;
    }

    public void setSelectedReportTableRow(PresentableStatisticsReportTableRow selectedReportTableRow) {
        this.selectedReportTableRow = selectedReportTableRow;
    }

    public PresentableStatisticsReportTableRow getSelectedReportTableRow() {
        return selectedReportTableRow;
    }

    public String getThresholdType() {
        return thresholdType;
    }

    public void setThresholdType(String thresholdType) {
        this.thresholdType = thresholdType;
    }

    public double getThresholdValue() {
        return thresholdValue;
    }

    public void setThresholdValue(double thresholdValue) {
        this.thresholdValue = thresholdValue;
    }

    public static class PresentableStatisticsReportTableRow extends StandradReportDescriptiveStatistics {
        private double precision;
        private double recall;
        private double F1;
        private int truePositives;
        private int falseNegatives;
        private int falsePositives;
        private int indicatedMatches;
        private String technique;
        private String tool;
        private String matchesDir;
        private double treshold;
        private int includedMatches;
        private int numberOfMatches;

        public double getPrecision() {
            return precision;
        }

        public void setPrecision(double precision) {
            this.precision = precision;
        }

        public double getRecall() {
            return recall;
        }

        public void setRecall(double recall) {
            this.recall = recall;
        }

        public double getF1() {
            return F1;
        }

        public void setF1(double f1) {
            F1 = f1;
        }

        public int getTruePositives() {
            return truePositives;
        }

        public void setTruePositives(int truePositives) {
            this.truePositives = truePositives;
        }

        public int getFalseNegatives() {
            return falseNegatives;
        }

        public void setFalseNegatives(int falseNegatives) {
            this.falseNegatives = falseNegatives;
        }

        public int getFalsePositives() {
            return falsePositives;
        }

        public void setFalsePositives(int falsePositives) {
            this.falsePositives = falsePositives;
        }

        public int getIndicatedMatches() {
            return indicatedMatches;
        }

        public void setIndicatedMatches(int indicatedMatches) {
            this.indicatedMatches = indicatedMatches;
        }

        public String getTechnique() {
            return technique;
        }

        public void setTechnique(String technique) {
            this.technique = technique;
        }

        public String getTool() {
            return tool;
        }

        public void setTool(String tool) {
            this.tool = tool;
        }


        public String getMatchesDir() {
            return matchesDir;
        }

        public void setMatchesDir(String matchesDir) {
            this.matchesDir = matchesDir;
        }

        public double getTreshold() {
            return treshold;
        }

        public void setTreshold(double treshold) {
            this.treshold = treshold;
        }

        public int getIncludedMatches() {
            return includedMatches;
        }

        public void setIncludedMatches(int includedMatches) {
            this.includedMatches = includedMatches;
        }

        public int getNumberOfMatches() {
            return numberOfMatches;
        }

        public void setNumberOfMatches(int numberOfMatches) {
            this.numberOfMatches = numberOfMatches;
        }
    }
}
