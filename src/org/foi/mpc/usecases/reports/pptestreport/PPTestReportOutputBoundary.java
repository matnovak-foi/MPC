package org.foi.mpc.usecases.reports.pptestreport;

import org.foi.mpc.usecases.reports.avalibleTools.models.AvailableToolsResponseModel;
import org.foi.mpc.usecases.reports.pptestreport.models.PPTestReportResponseModel;

public interface PPTestReportOutputBoundary {
    public void presentReport(PPTestReportResponseModel responseModel);
    public void presentAvailableTools(AvailableToolsResponseModel responseModel);
}
