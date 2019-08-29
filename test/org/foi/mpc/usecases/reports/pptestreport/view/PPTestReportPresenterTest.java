package org.foi.mpc.usecases.reports.pptestreport.view;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.foi.mpc.MPCContext;
import org.foi.mpc.executiontools.factories.SimilarityDetectionToolFactory;
import org.foi.mpc.usecases.reports.pptestreport.view.models.PPTestReportViewModel;
import org.foi.mpc.usecases.reports.avalibleTools.models.AvailableToolsResponseModel;
import org.foi.mpc.usecases.reports.pptestreport.PPTestReportOutputBoundary;
import org.foi.mpc.usecases.reports.pptestreport.models.PPTestReportResponseModel;
import org.foi.mpc.usecases.reports.pptestreport.view.models.PresentableReportPPTechnique;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(HierarchicalContextRunner.class)
public class PPTestReportPresenterTest {
    PPTestReportPresenter presenter;
    PPTestReportViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new PPTestReportViewModel();
        presenter = new PPTestReportPresenter(viewModel);
    }

    @Test
    public void isPPTestReportOutputBoundary() {
        assertTrue(presenter instanceof PPTestReportOutputBoundary);
    }

    @Test
    public void translatesResponseModelOfAvalibleToolsToViewModel() {
        AvailableToolsResponseModel responseModel = new AvailableToolsResponseModel();
        responseModel.tools = new SimilarityDetectionToolFactory(MPCContext.MATCHES_DIR).getAvailableTools();

        presenter.presentAvailableTools(responseModel);

        assertEquals(responseModel.tools, viewModel.getAvailableTools());
    }

    @Test
    public void ifErrorMessageIsPresentDisplayIt() {
        PPTestReportResponseModel responseModel = new PPTestReportResponseModel();
        responseModel.errorMessages = "someMessage";

        presenter.presentReport(responseModel);

        assertEquals(responseModel.errorMessages, viewModel.getErrorMessage());
    }

    @Test
    public void translatesResponseModelToViewModel() {
        PPTestReportResponseModel responseModel = new PPTestReportResponseModel();
        responseModel.toolName = "Tool1";
        responseModel.usernameA = "usernameA";
        responseModel.usernameB = "usernameB";
        responseModel.errorMessages = "";

        responseModel.ppReportTable = new ArrayList<>();
        PPTestReportResponseModel.PPTestReportTechnique reportTechnique = new PPTestReportResponseModel.PPTestReportTechnique();
        reportTechnique.name = "Technique1";
        reportTechnique.similarity = (float) 100.00;
        reportTechnique.similarityA = (float) 100.00;
        reportTechnique.similarityB = (float) 100.00;
        responseModel.ppReportTable.add(reportTechnique);

        presenter.presentReport(responseModel);

        List<PresentableReportPPTechnique> reportTable = viewModel.getReport().getPpTechniques();
        assertEquals(responseModel.toolName, viewModel.getReport().getToolName());
        assertEquals(responseModel.usernameA, viewModel.getReport().getUsernameA());
        assertEquals(responseModel.usernameB, viewModel.getReport().getUsernameB());
        assertEquals(reportTechnique.name, reportTable.get(0).getName());
        assertEquals(String.valueOf(reportTechnique.similarity), reportTable.get(0).getSimilarity());
        assertEquals(String.valueOf(reportTechnique.similarityA), reportTable.get(0).getSimilarityA());
        assertEquals(String.valueOf(reportTechnique.similarityB), reportTable.get(0).getSimilarityB());
        assertEquals("", viewModel.getErrorMessage());
    }

}
