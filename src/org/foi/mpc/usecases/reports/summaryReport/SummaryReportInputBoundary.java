package org.foi.mpc.usecases.reports.summaryReport;

import org.foi.mpc.usecases.reports.summaryReport.models.SummaryReportRequestModel;
import org.foi.mpc.usecases.reports.summaryReport.models.UpdatePlagInfoRequestModel;
import org.foi.mpc.usecases.reports.summaryReport.view.SummaryReportPresenter;

import java.io.File;

public interface SummaryReportInputBoundary {

    void getAvailableTools(SummaryReportOutputBoundary outputBoundary);
    void generateReport(SummaryReportRequestModel requestModel, SummaryReportOutputBoundary outputBoundary);
    void loadDirectoryList(File inputDir, int inputDirDepth, SummaryReportOutputBoundary presenterSpy);
    void updatePlagInfo(UpdatePlagInfoRequestModel requestModel, SummaryReportOutputBoundary presenterSpy);

    void getAvailableTools(SummaryReportOutputBoundary presenter, File file);
}
