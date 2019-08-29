package org.foi.mpc.usecases.reports.summaryReport;

import org.foi.mpc.usecases.reports.summaryReport.models.DetailsReportMatchInfoResponseModel;
import org.foi.mpc.usecases.reports.summaryReport.models.SideBySideComparisonResponseModel;

public interface DetailsReportOutputBoundary {
    void updateDetailsMatchInfo(DetailsReportMatchInfoResponseModel responseModel);
    void updateDetailsSideBySideComparison(SideBySideComparisonResponseModel responseModel);
}
