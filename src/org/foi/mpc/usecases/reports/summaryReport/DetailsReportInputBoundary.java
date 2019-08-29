package org.foi.mpc.usecases.reports.summaryReport;

import org.foi.mpc.usecases.reports.summaryReport.models.DetailsReportMatchInfoRequestModel;
import org.foi.mpc.usecases.reports.summaryReport.models.SideBySideCompariosnRequestModel;

public interface DetailsReportInputBoundary {
    void generateDetailInfoForMatch(DetailsReportMatchInfoRequestModel requestModel, DetailsReportOutputBoundary presenter);
    void generateMatchPartSidBySideComparion(SideBySideCompariosnRequestModel requestModel, DetailsReportOutputBoundary presenter);
    void generateFullFileSidBySideComparion(SideBySideCompariosnRequestModel requestModel, DetailsReportOutputBoundary presenter);
}
