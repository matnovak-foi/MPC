package org.foi.mpc.usecases.reports.statisticsReport;

import org.foi.mpc.usecases.reports.statisticsReport.models.StatisticsReportRequestModel;

import java.io.File;

public interface StatisticsReportInputBoundary {
    void getAvailableTools(StatisticsReportOutputBoundary outputBoundary, File workingDir);
    void loadDirectoryList(File file, int inputDirDepth, StatisticsReportOutputBoundary presenter);
    void generateReport(StatisticsReportRequestModel requestModel, StatisticsReportOutputBoundary presenter);
}
