package org.foi.mpc.usecases.reports.summaryReport.view;

import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.mpc.MPCContext;
import org.foi.mpc.abstractfactories.FactoryProvider;
import org.foi.mpc.usecases.combotechnique.view.models.ComboTechniqueViewModel;
import org.foi.mpc.usecases.combotechnique.view.models.ComboTechniqueViewModelBuilder;
import org.foi.mpc.usecases.reports.summaryReport.SummaryReportInputBoundary;
import org.foi.mpc.usecases.reports.summaryReport.SummaryReportOutputBoundary;
import org.foi.mpc.usecases.reports.summaryReport.SummaryReportUseCase;
import org.foi.mpc.usecases.reports.summaryReport.models.SummaryReportRequestModel;
import org.foi.mpc.usecases.reports.summaryReport.models.UpdatePlagInfoRequestModel;
import org.foi.mpc.usecases.reports.summaryReport.view.models.PresentableReportSummaryTableRow;
import org.foi.mpc.usecases.reports.summaryReport.view.models.SummaryReportViewModel;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SummaryReportControlerTest {

    ComboTechniqueViewModel techniqueViewModel;
    ComboTechniqueViewModelBuilder techniqueViewModelBuilder;

    SummaryReportPresenter presenter;
    SummaryReportViewModel viewModel;
    SummaryReportInputBoundarySpy useCaseSpy;
    SummaryReportControler controller;

    @Before
    public void setUp() throws Exception {
        viewModel = new SummaryReportViewModel();
        viewModel.setSelectedInputDirPath("summaryReportSourceDir");
        viewModel.setWorkingDirPath("summaryWorkingDir");
        viewModel.setSourceDirPath("summaryReportSourceDir");
        viewModel.setInputDirDepth(3);
        List<String> tools = new ArrayList<>();
        tools.add("Tool");
        viewModel.setSelectedTools(tools);

        techniqueViewModelBuilder = new ComboTechniqueViewModelBuilder();
        techniqueViewModel = techniqueViewModelBuilder
                .withSelectedTechnique("Technique1")
                .withSelectedTechnique("Technique2").build();

        presenter = new SummaryReportPresenter(viewModel);
        useCaseSpy = new SummaryReportInputBoundarySpy();
        controller = new SummaryReportControler(viewModel, presenter, techniqueViewModel);
        controller.setSummaryReportUseCase(useCaseSpy);
    }

    @Test
    public void presenterAndRequestModelArePassedToTheUseCaseOnGenerateReport() {
        controller.generateReport();

        assertEquals(viewModel.getSelectedTools(), useCaseSpy.requestModel.selectedTools);
        assertEquals(viewModel.getSelectedInputDirPath(), useCaseSpy.requestModel.selectedInputDir.getPath());
        assertSame(controller.getPresenter(), useCaseSpy.presenter);
    }

    @Test
    public void presenterIsPassedToTheUseCaseOnCreationCallingGetAvailable() {
        assertSame(controller.getPresenter(), useCaseSpy.presenter);
        assertTrue(useCaseSpy.getAvailableToolsWasCalled);
    }

    @Test
    public void requestModelIsCreatedCorrectlyOnGenerateReport() {
        controller.generateReport();

        SummaryReportRequestModel requestModel = useCaseSpy.requestModel;
        assertEquals(viewModel.getInputDirDepth(), requestModel.selectedInputDirDepth);
        assertEquals(viewModel.getSelectedTools(), requestModel.selectedTools);
        assertEquals(viewModel.getSelectedInputDirPath(), requestModel.selectedInputDir.getPath());
        assertEquals(viewModel.getSourceDirPath(), requestModel.sourceDirPath.getPath());
        assertEquals(viewModel.getWorkingDirPath(), requestModel.selectedWorking.getPath());
        assertEquals(techniqueViewModel.getSelectedTechniques().get(0), requestModel.selectedTechniques.get(0));
        assertEquals(techniqueViewModel.getSelectedTechniques().get(1), requestModel.selectedTechniques.get(1));
    }

    @Test
    public void controllerCanPassRequestModelWithNoTechnique() {
        techniqueViewModel = new ComboTechniqueViewModelBuilder().build();

        controller = new SummaryReportControler(viewModel, presenter, techniqueViewModel);
        controller.setSummaryReportUseCase(useCaseSpy);
        controller.generateReport();

        SummaryReportRequestModel requestModel = useCaseSpy.requestModel;
        assertEquals(new ArrayList<>(), requestModel.selectedTechniques);
    }

    @Test
    public void controllerCanPassRequestModelWithNoComboTechnique() {
        controller.generateReport();

        SummaryReportRequestModel requestModel = useCaseSpy.requestModel;
        assertEquals(techniqueViewModel.getSelectedTechniques(), requestModel.selectedTechniques);
    }

    @Test
    public void controllerCanPassRequestModelWithComboTechniquesToUseCase() {
        techniqueViewModel = techniqueViewModelBuilder.withSelectedComboTechnique("Combo1")
                .withSelectedComboTechnique("Combo2").build();

        controller.generateReport();

        SummaryReportRequestModel requestModel = useCaseSpy.requestModel;

        List<String> expected = new ArrayList<>();
        expected.addAll(techniqueViewModel.getSelectedTechniques());
        expected.addAll(techniqueViewModel.getSelectedComboTechniques());
        assertEquals(expected, requestModel.selectedTechniques);
    }

    @Test
    public void controllerPassesCorrectInfoOnLoadDirList(){
        viewModel.setSourceDirPath("sourceDirPath");
        viewModel.setInputDirDepth(3);

        controller.loadDirList();

        assertEquals(3,useCaseSpy.inputDirDepth);
        assertEquals(new File(viewModel.getSourceDirPath()),useCaseSpy.inputDir);
        assertEquals(presenter,useCaseSpy.presenter);
    }

    @Test
    public void controllerClearsTechniquesNotInWorkingDir() throws IOException {
        List<String> techniques = new ArrayList<>();
        techniques.add("T1");
        techniques.add("T2");
        viewModel.setAvailableTools(techniques);

        File workingDir = new File("testworkingdir");
        workingDir.mkdirs();
        new File(workingDir.getPath()+File.separator+MPCContext.DETECTION_DIR +File.separator+"T2").mkdirs();

        viewModel.setWorkingDirPath(workingDir.getPath());
        controller.setSummaryReportUseCase(new SummaryReportUseCase(new FactoryProvider(MPCContext.MATCHES_DIR)));
        controller.clearNonProcessedTools();

        techniques = new ArrayList<>();
        techniques.add("T2");

        DirectoryFileUtility.deleteDirectoryTree(workingDir);

        assertEquals("",viewModel.getErrorMessage());
        assertEquals(techniques,viewModel.getAvailableTools());
    }

    @Test
    public void controllerPassesRequestModelToUseCaseOnUpdatePlagInfo() {
        PresentableReportSummaryTableRow summaryTableRow = new PresentableReportSummaryTableRow();
        summaryTableRow.setPlagiarized(true);
        summaryTableRow.setProcessed(false);
        summaryTableRow.setFileA("fileA");
        summaryTableRow.setFileB("fileB");
        viewModel.setSelectedPair(summaryTableRow);

        controller.updatePlagInfo();

        UpdatePlagInfoRequestModel requestModel = useCaseSpy.updatePlagInfoRequestModel;
        assertSame(presenter,useCaseSpy.presenter);
        assertEquals(viewModel.getSelectedPair().getFileA(), requestModel.fileAname);
        assertEquals(viewModel.getSelectedPair().getFileB(), requestModel.fileBname);
        assertEquals(viewModel.getSelectedInputDirPath(), requestModel.selectedInputDirPath.getPath());
        assertEquals(viewModel.getSourceDirPath(), requestModel.sourceDirPath.getPath());
        assertEquals(viewModel.getWorkingDirPath(), requestModel.workingDir.getPath());
        assertEquals(viewModel.getSelectedPair().getPlagiarized(), requestModel.plagiarized);
        assertEquals(viewModel.getSelectedPair().getProcessed(), requestModel.processed);
    }

    @Test
    public void controllerPassesRequestModelToUseCaseWithNoSelectedPair() {
        controller.updatePlagInfo();

        UpdatePlagInfoRequestModel requestModel = useCaseSpy.updatePlagInfoRequestModel;
        assertSame(presenter,useCaseSpy.presenter);

        assertNull(requestModel.fileAname);
        assertNull(requestModel.fileBname);
        assertEquals(viewModel.getSelectedInputDirPath(), requestModel.selectedInputDirPath.getPath());
        assertEquals(viewModel.getSourceDirPath(), requestModel.sourceDirPath.getPath());
        assertEquals(viewModel.getWorkingDirPath(), requestModel.workingDir.getPath());
        assertFalse(requestModel.plagiarized);
        assertFalse(requestModel.processed);
    }

    private class SummaryReportInputBoundarySpy implements SummaryReportInputBoundary {
        public SummaryReportRequestModel requestModel;
        public UpdatePlagInfoRequestModel updatePlagInfoRequestModel;
        public SummaryReportOutputBoundary presenter;
        public boolean getAvailableToolsWasCalled = false;
        public File inputDir;
        public int inputDirDepth;

        @Override
        public void getAvailableTools(SummaryReportOutputBoundary outputBoundary) {
            this.getAvailableToolsWasCalled = true;
            this.presenter = outputBoundary;
        }

        @Override
        public void generateReport(SummaryReportRequestModel requestModel, SummaryReportOutputBoundary outputBoundary) {
            this.presenter = outputBoundary;
            this.requestModel = requestModel;
        }

        @Override
        public void loadDirectoryList(File inputDir, int inputDirDepth, SummaryReportOutputBoundary outputBoundary) {
            this.inputDir = inputDir;
            this.inputDirDepth = inputDirDepth;
            this.presenter = outputBoundary;
        }

        @Override
        public void updatePlagInfo(UpdatePlagInfoRequestModel requestModel, SummaryReportOutputBoundary outputBoundary) {
            this.updatePlagInfoRequestModel = requestModel;
            this.presenter = outputBoundary;
        }

        @Override
        public void getAvailableTools(SummaryReportOutputBoundary presenter, File file) {

        }
    }
}