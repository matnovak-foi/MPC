package org.foi.mpc.usecases.reports.summaryReport.view.models;

import org.foi.mpc.usecases.reports.view.models.StandardReportInputListData;

public class SummaryReportViewModel extends StandardReportInputListData {
    private PresentableReportSummary summaryReport;
    private PresentableReportSummaryTableRow selectedPair;

    public PresentableReportSummary getSummaryReport() {
        return summaryReport;
    }

    public void setSummaryReport(PresentableReportSummary summaryReport) {
        this.summaryReport = summaryReport;
    }

    public void setSelectedPair(PresentableReportSummaryTableRow selectedPair) {
        this.selectedPair = selectedPair;
    }

    public PresentableReportSummaryTableRow getSelectedPair() {
        return selectedPair;
    }

    @Override
    public String toString() {
        return "SummaryReportViewModel{" +
                "summaryReport=" + summaryReport +
                ", selectedPair=" + selectedPair +
                ", inputDirDepth=" + inputDirDepth +
                ", inputDirList=" + inputDirList +
                ", selectedInputDirPath='" + selectedInputDirPath + '\'' +
                ", availableTools=" + availableTools +
                ", selectedTools=" + selectedTools +
                ", sourceDirPath='" + sourceDirPath + '\'' +
                ", workingDirPath='" + workingDirPath + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", disabledLoadProcessedToolsAndTechniques=" + disabledLoadProcessedToolsAndTechniques +
                ", disabledLoadToolsAndTechniquesFromFile=" + disabledLoadToolsAndTechniquesFromFile +
                '}';
    }
}
