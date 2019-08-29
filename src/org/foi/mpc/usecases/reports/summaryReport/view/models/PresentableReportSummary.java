package org.foi.mpc.usecases.reports.summaryReport.view.models;

import org.foi.mpc.usecases.reports.view.models.StandradReportDescriptiveStatistics;

import java.util.List;
import java.util.Objects;

public class PresentableReportSummary extends StandradReportDescriptiveStatistics {
    private List<PresentableReportSummaryTableRow> reportTable;

    private String resultToolName;
    private String resultTechniqueName;
    private String selectedInputDirPath;
    private String matchesDirPath;

    public List<PresentableReportSummaryTableRow> getReportTable() {
        return reportTable;
    }

    public void setReportTable(List<PresentableReportSummaryTableRow> report) {
        this.reportTable = report;
    }

    public String getResultToolName() {
        return resultToolName;
    }

    public void setResultToolName(String resultToolName) {
        this.resultToolName = resultToolName;
    }

    public String getResultTechniqueName() {
        return resultTechniqueName;
    }

    public void setResultTechniqueName(String resultTechniqueName) {
        this.resultTechniqueName = resultTechniqueName;
    }

    public void setSelectedInputDirPath(String selectedInputDirPath) {
        this.selectedInputDirPath = selectedInputDirPath;
    }

    public String getSelectedInputDirPath() {
        return selectedInputDirPath;
    }

    public String getMatchesDirPath() {
        return matchesDirPath;
    }

    public void setMatchesDirPath(String matchesDirPath) {
        this.matchesDirPath = matchesDirPath;
    }

    @Override
    public String toString() {
        return "PresentableReportSummary{" +
                "reportTable=" + reportTable +
                ", resultToolName='" + resultToolName + '\'' +
                ", resultTechniqueName='" + resultTechniqueName + '\'' +
                ", selectedInputDirPath='" + selectedInputDirPath + '\'' +
                ", matchesDirPath='" + matchesDirPath + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PresentableReportSummary)) return false;
        PresentableReportSummary that = (PresentableReportSummary) o;
        return Objects.equals(reportTable, that.reportTable) &&
                Objects.equals(resultToolName, that.resultToolName) &&
                Objects.equals(resultTechniqueName, that.resultTechniqueName) &&
                Objects.equals(selectedInputDirPath, that.selectedInputDirPath) &&
                Objects.equals(matchesDirPath, that.matchesDirPath);
    }


}
