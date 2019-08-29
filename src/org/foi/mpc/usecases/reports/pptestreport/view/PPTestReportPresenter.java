package org.foi.mpc.usecases.reports.pptestreport.view;

import org.foi.mpc.usecases.reports.pptestreport.view.models.PPTestReportViewModel;
import org.foi.mpc.usecases.reports.pptestreport.view.models.PresentableReport;
import org.foi.mpc.usecases.reports.avalibleTools.models.AvailableToolsResponseModel;
import org.foi.mpc.usecases.reports.pptestreport.PPTestReportOutputBoundary;
import org.foi.mpc.usecases.reports.pptestreport.models.PPTestReportResponseModel;
import org.foi.mpc.usecases.reports.pptestreport.view.models.PresentableReportPPTechnique;

import java.util.ArrayList;
import java.util.List;

public class PPTestReportPresenter implements PPTestReportOutputBoundary {
    private PPTestReportViewModel viewModel;

    public PPTestReportPresenter(PPTestReportViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentReport(PPTestReportResponseModel responseModel) {
        PresentableReport report = new PresentableReport();
        report.setToolName(responseModel.toolName);
        report.setUsernameA(responseModel.usernameA);
        report.setUsernameB(responseModel.usernameB);
        report.setPpTechniques(prepareReportPPTable(responseModel));

        viewModel.setErrorMessage(responseModel.errorMessages);
        viewModel.setReport(report);
    }

    private List<PresentableReportPPTechnique> prepareReportPPTable(PPTestReportResponseModel responseModel) {
        List<PresentableReportPPTechnique> reportPPTable = new ArrayList<>();

        if(responseModel.ppReportTable != null) {
            for (PPTestReportResponseModel.PPTestReportTechnique technique : responseModel.ppReportTable) {
                PresentableReportPPTechnique reportPPTechnique = new PresentableReportPPTechnique();
                reportPPTechnique.setName(technique.name);
                reportPPTechnique.setSimilarity(String.valueOf(technique.similarity));
                reportPPTechnique.setSimilarityA(String.valueOf(technique.similarityA));
                reportPPTechnique.setSimilarityB(String.valueOf(technique.similarityB));
                reportPPTable.add(reportPPTechnique);
            }
        }

        return reportPPTable;
    }

    @Override
    public void presentAvailableTools(AvailableToolsResponseModel responseModel) {
        viewModel.setAvailableTools(responseModel.tools);
    }
}
