package org.foi.mpc.usecases.reports.statisticsReport.view;

import org.foi.mpc.usecases.combotechnique.view.models.ComboTechniqueViewModel;
import org.foi.mpc.usecases.combotechnique.view.models.ComboTechniqueViewModelBuilder;
import org.foi.mpc.usecases.reports.statisticsReport.StatisticsReportInputBoundary;
import org.foi.mpc.usecases.reports.statisticsReport.StatisticsReportOutputBoundary;
import org.foi.mpc.usecases.reports.statisticsReport.models.StatisticsReportRequestModel;
import org.foi.mpc.usecases.reports.statisticsReport.view.model.StatisticsReportViewModel;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class StatisticsReportControlerTest {

    ComboTechniqueViewModel techniqueViewModel;
    ComboTechniqueViewModelBuilder techniqueViewModelBuilder;

    StatisticsReportOutputBoundary presenter;
    StatisticsReportViewModel viewModel;
    StatisticsReportInputBoundarySpy useCaseSpy;
    StatisticsReportControler controller;

    @Before
    public void setUp() throws Exception {
        viewModel = new StatisticsReportViewModel();
        viewModel.setWorkingDirPath("workingDir");
        viewModel.setSourceDirPath("sourceDir");
        viewModel.setSelectedInputDirPath("assignementDir");
        viewModel.setInputDirDepth(3);
        List<String> tools = new ArrayList<>();
        tools.add("Tool1");
        tools.add("Tool2");
        viewModel.setSelectedTools(tools);

        techniqueViewModelBuilder = new ComboTechniqueViewModelBuilder();
        techniqueViewModel = techniqueViewModelBuilder
                .withSelectedTechnique("Technique1")
                .withSelectedTechnique("Technique2").build();

        controller = new StatisticsReportControler(viewModel,presenter,techniqueViewModel);
        useCaseSpy = new StatisticsReportInputBoundarySpy();
        controller.setUseCase(useCaseSpy);
    }

    @Test
    public void controllerSetsViewDefaultThreshold(){
        assertEquals(StatisticsReportRequestModel.ThresholdType.calculatedPrecentageBased.toString(),viewModel.getThresholdType());
        assertEquals(3.0,viewModel.getThresholdValue(),0.1);
    }

    @Test
    public void presenterIsPassedToLoadProcessedTools() {
        controller.loadProcessedTools();
        assertSame(controller.getPresenter(), useCaseSpy.presenter);
        assertTrue(useCaseSpy.getAvailableToolsWasCalled);
        assertEquals(viewModel.getWorkingDirPath(),useCaseSpy.avalibleToolsWorkingDir.getPath());
    }

    @Test
    public void controllerPassesCorrectInfoOnLoadDirList(){
        viewModel.setSourceDirPath("sourceDirPath");
        viewModel.setInputDirDepth(3);

        controller.loadDirList();

        assertEquals(3,useCaseSpy.inputDirDepth);
        assertEquals(new File(viewModel.getSourceDirPath()),useCaseSpy.inputDir);
        assertSame(presenter,useCaseSpy.presenter);
    }

    @Test
    public void presenterAndRequestModelArePassedToTheUseCaseOnGenerateReport() {
        controller.generateReport();

        assertEquals(viewModel.getSelectedTools(), useCaseSpy.requestModel.tools);
        assertEquals(techniqueViewModel.getSelectedTechniques(), useCaseSpy.requestModel.techniques);
        assertEquals(viewModel.getWorkingDirPath(), useCaseSpy.requestModel.workingDir.getPath());
        assertEquals(viewModel.getSelectedInputDirPath(), useCaseSpy.requestModel.assignementDir.getPath());
        assertEquals(viewModel.getSourceDirPath(), useCaseSpy.requestModel.sourceDir.getPath());
        assertEquals(viewModel.getInputDirDepth(), useCaseSpy.requestModel.inputDirDepth);
        assertEquals(viewModel.getThresholdValue(), useCaseSpy.requestModel.thresholdValue,0.1);
        assertEquals(viewModel.getThresholdType(), useCaseSpy.requestModel.thresholdType.name());
        assertSame(controller.getPresenter(), useCaseSpy.presenter);

        viewModel.setThresholdType(StatisticsReportRequestModel.ThresholdType.calculatedPrecentageBased.name());
        controller.generateReport();
        assertEquals(viewModel.getThresholdType(), useCaseSpy.requestModel.thresholdType.name());

        viewModel.setThresholdType(StatisticsReportRequestModel.ThresholdType.fixedPrecentageBased.name());
        controller.generateReport();
        assertEquals(viewModel.getThresholdType(), useCaseSpy.requestModel.thresholdType.name());

        viewModel.setThresholdType(StatisticsReportRequestModel.ThresholdType.topNBased.name());
        controller.generateReport();
        assertEquals(viewModel.getThresholdType(), useCaseSpy.requestModel.thresholdType.name());

        viewModel.setThresholdType(StatisticsReportRequestModel.ThresholdType.plagMatchesBased.name());
        controller.generateReport();
        assertEquals(viewModel.getThresholdType(), useCaseSpy.requestModel.thresholdType.name());
    }

    private class StatisticsReportInputBoundarySpy implements StatisticsReportInputBoundary {
        public StatisticsReportOutputBoundary presenter;
        public boolean getAvailableToolsWasCalled;
        public File avalibleToolsWorkingDir;
        public int inputDirDepth;
        public File inputDir;
        public StatisticsReportRequestModel requestModel;


        @Override
        public void getAvailableTools(StatisticsReportOutputBoundary outputBoundary, File workingDir) {
            getAvailableToolsWasCalled = true;
            presenter = outputBoundary;
            avalibleToolsWorkingDir = workingDir;
        }

        @Override
        public void loadDirectoryList(File file, int inputDirDepth, StatisticsReportOutputBoundary presenter) {
            this.inputDir = file;
            this.inputDirDepth = inputDirDepth;
            this.presenter = presenter;
        }

        @Override
        public void generateReport(StatisticsReportRequestModel requestModel, StatisticsReportOutputBoundary presenter) {
            this.requestModel = requestModel;
            this.presenter = presenter;
        }
    }



}