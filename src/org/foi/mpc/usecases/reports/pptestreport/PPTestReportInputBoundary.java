package org.foi.mpc.usecases.reports.pptestreport;

import org.foi.mpc.usecases.reports.pptestreport.models.PPTestReportRequestModel;

public interface PPTestReportInputBoundary {
    public void getAvailableTools(PPTestReportOutputBoundary outputBoundary);
    public void generateReport(PPTestReportRequestModel requestModel, PPTestReportOutputBoundary outputBoundary);
}
