package org.foi.mpc.usecases.reports.summaryReport;

import org.foi.mpc.usecases.reports.avalibleTools.models.AvailableToolsResponseModel;
import org.foi.mpc.usecases.reports.summaryReport.models.SummaryReportResponseModel;
import org.foi.mpc.usecases.reports.summaryReport.models.UpdatePlagInfoResponseModel;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface SummaryReportOutputBoundary {
    void presentAvailableTools(AvailableToolsResponseModel responseModel);
    void presentReport(SummaryReportResponseModel responseModel);
    void presentDirList(Map<String,File> dirList);
    void presentUpdatedSummaryReportRowPlagInfo(UpdatePlagInfoResponseModel responseModel);
}
