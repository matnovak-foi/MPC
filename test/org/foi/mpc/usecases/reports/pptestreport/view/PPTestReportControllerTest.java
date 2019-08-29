package org.foi.mpc.usecases.reports.pptestreport.view;

import org.foi.mpc.usecases.combotechnique.view.models.ComboTechniqueViewModel;
import org.foi.mpc.usecases.combotechnique.view.models.ComboTechniqueViewModelBuilder;
import org.foi.mpc.usecases.reports.pptestreport.view.models.PPTestReportViewModel;
import org.foi.mpc.usecases.reports.pptestreport.PPTestReportInputBoundary;
import org.foi.mpc.usecases.reports.pptestreport.PPTestReportOutputBoundary;
import org.foi.mpc.usecases.reports.pptestreport.models.PPTestReportRequestModel;
import org.foi.mpc.usecases.reports.pptestreport.view.models.PPTestReportViewModelBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PPTestReportControllerTest {
    ComboTechniqueViewModel techniqueViewModel;
    ComboTechniqueViewModelBuilder techniqueViewModelBuilder;

    PPTestReportPresenter presenter;
    PPTestReportViewModel viewModel;
    PPTestReportViewModelBuilder viewModelBuilder;
    PPTestReportInputBoundarySpy useCaseSpy;
    PPTestReportController controller;

    @Before
    public void setUp() {

        viewModelBuilder = new PPTestReportViewModelBuilder();
        viewModel = viewModelBuilder
                .withSourceDir("sourceDir")
                .withWorkingDir("workingDir")
                .withTool("Tool")
                .build();

        techniqueViewModelBuilder = new ComboTechniqueViewModelBuilder();
        techniqueViewModel = techniqueViewModelBuilder
                .withSelectedTechnique("Technique1")
                .withSelectedTechnique("Technique2").build();

        presenter = new PPTestReportPresenter(viewModel);
        useCaseSpy = new PPTestReportInputBoundarySpy();

        controller = new PPTestReportController(presenter, viewModel, techniqueViewModel);
        controller.setTestReportUseCase(useCaseSpy);
    }

    @Test
    public void presenterAndRequestModelArePassedToTheUseCaseOnGenerateReport() {
        controller.generateReport();

        assertEquals(viewModel.getSelectedTools(), useCaseSpy.requestModel.selectedTools);
        assertEquals(viewModel.getSourceDirPath(), useCaseSpy.requestModel.selectedInputDir.getPath());
        assertSame(controller.getPresenter(), useCaseSpy.presenter);
    }

    @Test
    public void presenterIsPassedToTheUseCaseOnCreationCallingGetAvailable() {
        assertSame(controller.getPresenter(), useCaseSpy.presenter);
        assertTrue(useCaseSpy.getAvailableToolsWasCalled);
    }

    @Test
    public void requestModelIsCreatedCorrectlyOnGenerateReport() {
        controller.setTestReportUseCase(useCaseSpy);
        controller.generateReport();

        PPTestReportRequestModel requestModel = useCaseSpy.requestModel;
        assertEquals(viewModel.getSelectedTools(), requestModel.selectedTools);
        assertEquals(viewModel.getSourceDirPath(), requestModel.selectedInputDir.getPath());
        assertEquals(viewModel.getWorkingDirPath(), requestModel.selectedWorking.getPath());
        assertEquals(techniqueViewModel.getSelectedTechniques().get(0), requestModel.selectedTechniques.get(0));
        assertEquals(techniqueViewModel.getSelectedTechniques().get(1), requestModel.selectedTechniques.get(1));
    }

    @Test
    public void controllerCanPassRequestModelWithNoTechnique() {
        techniqueViewModel = new ComboTechniqueViewModelBuilder().build();

        controller = new PPTestReportController(presenter, viewModel, techniqueViewModel);
        controller.setTestReportUseCase(useCaseSpy);
        controller.generateReport();

        PPTestReportRequestModel requestModel = useCaseSpy.requestModel;
        assertEquals(new ArrayList<>(), requestModel.selectedTechniques);
    }

    @Test
    public void controllerCanPassRequestModelWithNoComboTechnique() {
        controller.setTestReportUseCase(useCaseSpy);
        controller.generateReport();

        PPTestReportRequestModel requestModel = useCaseSpy.requestModel;
        assertEquals(techniqueViewModel.getSelectedTechniques(), requestModel.selectedTechniques);
    }

    @Test
    public void controllerCanPassRequestModelWithComboTechniquesToUseCase() {
        techniqueViewModel = techniqueViewModelBuilder.withSelectedComboTechnique("Combo1")
                .withSelectedComboTechnique("Combo2").build();

        controller.setTestReportUseCase(useCaseSpy);
        controller.generateReport();

        PPTestReportRequestModel requestModel = useCaseSpy.requestModel;

        List<String> expected = new ArrayList<>();
        expected.addAll(techniqueViewModel.getSelectedTechniques());
        expected.addAll(techniqueViewModel.getSelectedComboTechniques());
        assertEquals(expected, requestModel.selectedTechniques);
    }

    private class PPTestReportInputBoundarySpy implements PPTestReportInputBoundary {
        public PPTestReportRequestModel requestModel;
        public PPTestReportOutputBoundary presenter;
        public boolean getAvailableToolsWasCalled = false;

        @Override
        public void generateReport(PPTestReportRequestModel requestModel, PPTestReportOutputBoundary presenter) {
            this.requestModel = requestModel;
            this.presenter = presenter;
        }

        @Override
        public void getAvailableTools(PPTestReportOutputBoundary outputBoundary) {
            this.presenter = outputBoundary;
            getAvailableToolsWasCalled = true;
        }
    }
}
