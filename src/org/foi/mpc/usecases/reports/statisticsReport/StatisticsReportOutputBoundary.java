package org.foi.mpc.usecases.reports.statisticsReport;

import org.foi.mpc.usecases.reports.avalibleTools.models.AvailableToolsResponseModel;
import org.foi.mpc.usecases.reports.statisticsReport.models.StatisticsReportResponseModel;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface StatisticsReportOutputBoundary {
    public void presentAvailableTools(AvailableToolsResponseModel responseModel);
    void presentDirList(Map<String,File> dirList);
    void presentStatististicsReport(StatisticsReportResponseModel responseModel);
}
