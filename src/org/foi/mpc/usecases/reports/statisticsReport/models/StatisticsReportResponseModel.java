package org.foi.mpc.usecases.reports.statisticsReport.models;

import java.io.File;
import java.util.List;

public class StatisticsReportResponseModel {
    public List<StatisticsReportTableRow> reportTable;
    public String errorMessages;
    public File assignementDir;
    public int plagiarizedMatches;
}
