package org.foi.mpc.usecases.reports.summaryReport.view;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.mpc.usecases.reports.summaryReport.DetailsReportInputBoundary;
import org.foi.mpc.usecases.reports.summaryReport.DetailsReportOutputBoundary;
import org.foi.mpc.usecases.reports.summaryReport.models.DetailsReportMatchInfoRequestModel;
import org.foi.mpc.usecases.reports.summaryReport.models.SideBySideCompariosnRequestModel;
import org.foi.mpc.usecases.reports.summaryReport.view.models.DetailsReportViewModel;
import org.foi.mpc.usecases.reports.summaryReport.view.models.PresentableDetailSimilarityTable;
import org.foi.mpc.usecases.reports.summaryReport.view.models.PresentableDetailsMatchPart;
import org.foi.mpc.usecases.reports.summaryReport.view.models.PresentableReportSummaryTableRow;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(HierarchicalContextRunner.class)
public class DetailsReportControlerTest {
    DetailsReportViewModel viewModel;
    DetailsReportControler controller;
    DetailsReportInputBoundarySpy useCasespy;

    @Before
    public void setUp() throws Exception {
        viewModel = new DetailsReportViewModel();

        PresentableReportSummaryTableRow selectedPair = new PresentableReportSummaryTableRow();
        selectedPair.setStudentA("A");
        selectedPair.setStudentB("B");
        selectedPair.setFileA("fileAName");
        selectedPair.setFileB("fileBName");
        viewModel.setSelectedPair(selectedPair);

        viewModel.setResultTechniqueName("Technique");
        viewModel.setResultToolName("Tool");
        viewModel.setSelectedWorkingDirPath("inputPath");
        viewModel.setMatchesDirPath("matchesDir");
        viewModel.setTechniqueList("Tool1, Tool2, Tool3");
        viewModel.setToolList("Technique1, Technique2, Technique3");
        viewModel.setSelectedSideBySideMarkingType(SideBySideCompariosnRequestModel.SideBySideType.NoMarking.name());

        PresentableDetailSimilarityTable similarityTableRow = new PresentableDetailSimilarityTable();
        similarityTableRow.setMatchesDir("matchesDir2");
        similarityTableRow.setTool(viewModel.getResultToolName());
        similarityTableRow.setTechnique(viewModel.getResultTechniqueName());
        viewModel.setSelectedSimilarity(similarityTableRow);

        PresentableDetailsMatchPart selectedMatchPart = new PresentableDetailsMatchPart();
        selectedMatchPart.setStartLineNumberA(2);
        selectedMatchPart.setEndLineNumberA(5);
        selectedMatchPart.setStartLineNumberB(22);
        selectedMatchPart.setEndLineNumberB(55);
        selectedMatchPart.setMatchesDir("matchesDir3");
        viewModel.setSelectedMatchPart(selectedMatchPart);

        useCasespy = new DetailsReportInputBoundarySpy();
        controller = new DetailsReportControler(viewModel, new DetailsReportPresenter(viewModel), useCasespy);
    }

    @Test
    public void controllerSetupTechniqueAndDetailList() throws IOException {
        viewModel.setTechniqueList(null);
        viewModel.setToolList(null);
        File workingDir = new File("testworkingdir");
        workingDir.mkdirs();
        new File(workingDir.getPath()+File.separator+"preprocess"+File.separator+"T1").mkdirs();
        new File(workingDir.getPath()+File.separator+"detection"+File.separator+"T2").mkdirs();

        viewModel.setSelectedWorkingDirPath(workingDir.getPath());
        controller.initToolAndTechniqueList();

        DirectoryFileUtility.deleteDirectoryTree(workingDir);

        assertEquals("T1",viewModel.getTechniqueList());
        assertEquals("T2",viewModel.getToolList());
    }

    public class generateDetailReport {

        @Test
        public void presenterAndRequestModelArePassedToTheUseCaseOnGenerateReport() {
            controller.generateDetailInfoForMatch();

            assertEquals(viewModel.getResultToolName(), useCasespy.detailsInfoRequestModel.selectedTool);
            assertEquals(viewModel.getResultTechniqueName(), useCasespy.detailsInfoRequestModel.selectedTechnique);
            assertEquals(viewModel.getSelectedWorkingDirPath(), useCasespy.detailsInfoRequestModel.selectedWorkingDirPath.getPath());
            assertEquals(viewModel.getSelectedPair().getStudentA(), useCasespy.detailsInfoRequestModel.selectedStudentA);
            assertEquals(viewModel.getSelectedPair().getStudentB(), useCasespy.detailsInfoRequestModel.selectedStudentB);
            assertEquals(viewModel.getSelectedPair().getFileA(), useCasespy.detailsInfoRequestModel.selectedFileAName);
            assertEquals(viewModel.getSelectedPair().getFileB(), useCasespy.detailsInfoRequestModel.selectedFileBName);
            assertEquals(viewModel.getMatchesDirPath(), useCasespy.detailsInfoRequestModel.selectedMatchesDir.getPath());
            assertEquals("[" + viewModel.getToolList() + "]", useCasespy.detailsInfoRequestModel.toolList.toString());
            assertEquals("[" + viewModel.getTechniqueList() + "]", useCasespy.detailsInfoRequestModel.techniqueList.toString());
            assertSame(controller.getPresenter(), useCasespy.presenter);
        }

        @Test
        public void ifOtherDataIsNotSelectedUseCaseIsCalled() {
            viewModel.setResultToolName(null);
            controller.generateDetailInfoForMatch();

            viewModel.setResultToolName("Tool");
            viewModel.setResultTechniqueName(null);
            controller.generateDetailInfoForMatch();

            viewModel.setResultTechniqueName("Technique");
            viewModel.setSelectedWorkingDirPath(null);
            controller.generateDetailInfoForMatch();

            assertEquals(viewModel.getResultToolName(), useCasespy.detailsInfoRequestModel.selectedTool);
            assertEquals(viewModel.getResultTechniqueName(), useCasespy.detailsInfoRequestModel.selectedTechnique);
            assertEquals(viewModel.getSelectedWorkingDirPath(), useCasespy.detailsInfoRequestModel.selectedWorkingDirPath);
            assertEquals(viewModel.getSelectedPair().getStudentA(), useCasespy.detailsInfoRequestModel.selectedStudentA);
            assertEquals(viewModel.getSelectedPair().getStudentB(), useCasespy.detailsInfoRequestModel.selectedStudentB);
            assertSame(controller.getPresenter(), useCasespy.presenter);
        }
    }

    public class generateSideBySideCompariosnMatchPart {
        @Test
        public void presenterSetsByDefaultViewSideBySideMarkingType() {
            viewModel.setSelectedSideBySideMarkingType(null);
            controller = new DetailsReportControler(viewModel, new DetailsReportPresenter(viewModel), useCasespy);
            assertEquals(SideBySideCompariosnRequestModel.SideBySideType.NoMarking.name(),viewModel.getSelectedSideBySideMarkingType());
        }

        @Test
        public void presenterAndRequestModelArePassedToTheUseCaseOnGenerateSideBySide() {
            controller.generateMatchPartSideBySideComaprisonView();

            assertEquals(viewModel.getSelectedMatchPart().getMatchesDir(), useCasespy.sideBySideRequestModel.matchesDir.getPath());
            assertEquals(viewModel.getSelectedMatchPart().getStartLineNumberA(), useCasespy.sideBySideRequestModel.startLineA);
            assertEquals(viewModel.getSelectedMatchPart().getEndLineNumberA(), useCasespy.sideBySideRequestModel.endLineA);
            assertEquals(viewModel.getSelectedPair().getFileA(), useCasespy.sideBySideRequestModel.studentAfileName);
            assertEquals(viewModel.getSelectedMatchPart().getStartLineNumberB(), useCasespy.sideBySideRequestModel.startLineB);
            assertEquals(viewModel.getSelectedMatchPart().getEndLineNumberB(), useCasespy.sideBySideRequestModel.endLineB);
            assertEquals(viewModel.getSelectedPair().getFileB(), useCasespy.sideBySideRequestModel.studentBfileName);
            assertEquals(viewModel.getSelectedSideBySideMarkingType(),useCasespy.sideBySideRequestModel.sideBySideType.name());
            assertSame(controller.getPresenter(), useCasespy.presenter);

            viewModel.setSelectedSideBySideMarkingType(SideBySideCompariosnRequestModel.SideBySideType.MarkingJYCR.name());
            controller.generateMatchPartSideBySideComaprisonView();
            assertEquals(viewModel.getSelectedSideBySideMarkingType(),useCasespy.sideBySideRequestModel.sideBySideType.name());
            controller.generateMatchPartSideBySideComaprisonView();

            viewModel.setSelectedSideBySideMarkingType(SideBySideCompariosnRequestModel.SideBySideType.MarkingWumpz.name());
            controller.generateMatchPartSideBySideComaprisonView();
            assertEquals(viewModel.getSelectedSideBySideMarkingType(),useCasespy.sideBySideRequestModel.sideBySideType.name());
        }

        @Test
        public void ifNoMatchPartIsSelectedMatchDirProblem(){
            viewModel.setSelectedMatchPart(null);;

            controller.generateMatchPartSideBySideComaprisonView();

            assertSame(controller.getPresenter(), useCasespy.presenter);
            assertNull(useCasespy.sideBySideRequestModel.matchesDir);
        }

        @Test
        public void ifNoMatchPartIsSelectedLineNumbersProblem(){
            viewModel.setSelectedMatchPart(null);

            controller.generateMatchPartSideBySideComaprisonView();

            assertSame(controller.getPresenter(), useCasespy.presenter);
            assertEquals(0,useCasespy.sideBySideRequestModel.startLineA);
            assertEquals(0,useCasespy.sideBySideRequestModel.endLineA);
            assertEquals(0,useCasespy.sideBySideRequestModel.startLineB);
            assertEquals(0,useCasespy.sideBySideRequestModel.endLineB);
        }

        @Test
        public void ifNoSelectedPair(){
            viewModel.setSelectedPair(null);;

            controller.generateMatchPartSideBySideComaprisonView();

            assertSame(controller.getPresenter(), useCasespy.presenter);
            assertNull(useCasespy.sideBySideRequestModel.studentAfileName);
            assertNull(useCasespy.sideBySideRequestModel.studentBfileName);
        }
    }

    public class generateSideBySideCompariosnFullFile {
        @Test
        public void presenterAndRequestModelArePassedToTheUseCaseOnGenerateSideBySide() {
            controller.generateFullFileSideBySideComaprisonView();

            assertEquals(viewModel.getSelectedSimilarity().getMatchesDir(), useCasespy.sideBySideRequestModel.matchesDir.getPath());
            assertEquals(viewModel.getSelectedPair().getFileA(), useCasespy.sideBySideRequestModel.studentAfileName);
            assertEquals(viewModel.getSelectedPair().getFileB(), useCasespy.sideBySideRequestModel.studentBfileName);
            assertSame(controller.getPresenter(), useCasespy.presenter);
        }

        @Test
        public void ifNoSimilarityRowSelectedMatchDirProblem(){
            viewModel.setSelectedSimilarity(null);;

            controller.generateFullFileSideBySideComaprisonView();

            assertSame(controller.getPresenter(), useCasespy.presenter);
            assertNull(useCasespy.sideBySideRequestModel.matchesDir);
        }

        @Test
        public void ifNoSelectedPair(){
            viewModel.setSelectedPair(null);;

            controller.generateFullFileSideBySideComaprisonView();

            assertSame(controller.getPresenter(), useCasespy.presenter);
            assertNull(useCasespy.sideBySideRequestModel.studentAfileName);
            assertNull(useCasespy.sideBySideRequestModel.studentBfileName);
        }
    }

    @Test
    public void ifNoMatchIsSelectedUseCase() {
        viewModel.setSelectedPair(null);
        viewModel.setMatchesDirPath(null);

        controller.generateDetailInfoForMatch();

        assertNull(useCasespy.detailsInfoRequestModel.selectedStudentA);
        assertNull(useCasespy.detailsInfoRequestModel.selectedStudentB);
        assertSame(controller.getPresenter(), useCasespy.presenter);

        controller.generateMatchPartSideBySideComaprisonView();
        assertNull(useCasespy.sideBySideRequestModel.studentAfileName);
        assertNull(useCasespy.sideBySideRequestModel.studentBfileName);
        assertSame(controller.getPresenter(), useCasespy.presenter);
    }

    private class DetailsReportInputBoundarySpy implements DetailsReportInputBoundary {
        public DetailsReportMatchInfoRequestModel detailsInfoRequestModel;
        public SideBySideCompariosnRequestModel sideBySideRequestModel;
        public DetailsReportOutputBoundary presenter;

        @Override
        public void generateDetailInfoForMatch(DetailsReportMatchInfoRequestModel requestModel, DetailsReportOutputBoundary presenter) {
            this.detailsInfoRequestModel = requestModel;
            this.presenter = presenter;
        }

        @Override
        public void generateMatchPartSidBySideComparion(SideBySideCompariosnRequestModel requestModel, DetailsReportOutputBoundary presenter) {
            this.sideBySideRequestModel = requestModel;
            this.presenter = presenter;
        }

        @Override
        public void generateFullFileSidBySideComparion(SideBySideCompariosnRequestModel requestModel, DetailsReportOutputBoundary presenter) {
            this.sideBySideRequestModel = requestModel;
            this.presenter = presenter;
        }
    }
}