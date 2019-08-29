package org.foi.mpc.usecases.reports.pptestreport.view.models;

import org.foi.mpc.usecases.reports.view.models.StandardReportInputData;

public class PPTestReportViewModel extends StandardReportInputData {
    private PresentableReport report;

    public PPTestReportViewModel() {
        super();
    }

    public PresentableReport getReport() {
        return report;
    }

    public void setReport(PresentableReport report) {
        this.report = report;
    }

    @Override
    public String toString() {
        return "PPTestReportViewModel{" +
                "report=" + report +
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
