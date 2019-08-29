package org.foi.mpc.usecases.reports.summaryReport.view;

import clover.org.apache.commons.collections.map.ListOrderedMap;
import org.foi.mpc.MPCContext;
import org.foi.mpc.executiontools.factories.SimilarityDetectionToolFactory;
import org.foi.mpc.usecases.reports.avalibleTools.models.AvailableToolsResponseModel;
import org.foi.mpc.usecases.reports.statisticsReport.models.SimilarityDescriptiveStatistics;
import org.foi.mpc.usecases.reports.summaryReport.SummaryReportOutputBoundary;
import org.foi.mpc.usecases.reports.summaryReport.models.SummaryReportResponseModel;
import org.foi.mpc.usecases.reports.summaryReport.models.UpdatePlagInfoResponseModel;
import org.foi.mpc.usecases.reports.summaryReport.view.models.PresentableReportSummary;
import org.foi.mpc.usecases.reports.summaryReport.view.models.PresentableReportSummaryTableRow;
import org.foi.mpc.usecases.reports.summaryReport.view.models.SummaryReportViewModel;
import org.foi.mpc.usecases.reports.view.models.SelectOption;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SummaryReportPresenterTest {

    SummaryReportPresenter presenter;
    SummaryReportViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new SummaryReportViewModel();
        viewModel.setSelectedInputDirPath("selectedInputDirPath");
        presenter = new SummaryReportPresenter(viewModel);
    }

    @Test
    public void isPPTestReportOutputBoundary() {
        assertTrue(presenter instanceof SummaryReportOutputBoundary);
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
        SummaryReportResponseModel responseModel = new SummaryReportResponseModel();
        responseModel.errorMessages = "someMessage";

        presenter.presentReport(responseModel);

        assertEquals(responseModel.errorMessages, viewModel.getErrorMessage());
    }

    @Test
    public void presentsDirectoryList(){
        Map<String,File> inputDirs = new ListOrderedMap();
        List<SelectOption> expectedDirs = new ArrayList<>();

        String asignPath1 = "NWTIS"+File.separator+"2016-2017"+File.separator+"DZ1";
        File inputDir = new File("inputDirs"+File.separator+asignPath1);
        inputDirs.put(asignPath1,inputDir);
        expectedDirs.add(new SelectOption(asignPath1,inputDir.getPath()));

        String asignPath2 = "NWTIS"+File.separator+"2017-2018"+File.separator+"DZ1";
        inputDir = new File("inputDirs"+File.separator+asignPath2);
        inputDirs.put(asignPath2,inputDir);
        expectedDirs.add(new SelectOption(asignPath2,inputDir.getPath()));

        String asignPath3 = "NWTIS"+File.separator+"2017-2018"+File.separator+"DZ2";
        inputDir = new File("inputDirs"+File.separator+asignPath3);
        inputDirs.put(asignPath3,inputDir);
        expectedDirs.add(new SelectOption(asignPath3,inputDir.getPath()));

        presenter.presentDirList(inputDirs);

        assertEquals(expectedDirs,viewModel.getInputDirList());
    }

    @Test
    public void translatesResponseModelToViewModel() {
        viewModel.setSelectedPair(new PresentableReportSummaryTableRow());
        SummaryReportResponseModel responseModel = new SummaryReportResponseModel();
        responseModel.toolName = "Tool1";
        responseModel.techniqueName = "Technique1";
        responseModel.matchesDirPath = new File("matchesDirPath");
        responseModel.errorMessages = "";
        responseModel.similarityStatistics = new SimilarityDescriptiveStatistics();
        responseModel.similarityStatistics.min = 20;
        responseModel.similarityStatistics.max = 100;
        responseModel.similarityStatistics.q1 = 25;
        responseModel.similarityStatistics.q3 = 75;
        responseModel.similarityStatistics.percentile99 = 99;
        responseModel.similarityStatistics.mean = 45;
        responseModel.similarityStatistics.median = 50;
        responseModel.similarityStatistics.mode = new double[2];
        responseModel.similarityStatistics.mode[0] = 1;
        responseModel.similarityStatistics.mode[1] = 2;
        responseModel.similarityStatistics.IRQ = 22;
        responseModel.similarityStatistics.STD = 25;

        List<Double> expectedModes = new ArrayList<>();
        expectedModes.add(responseModel.similarityStatistics.mode[0]);
        expectedModes.add(responseModel.similarityStatistics.mode[1]);

        responseModel.reportTable = new ArrayList<>();
        SummaryReportResponseModel.SummaryReportTableRow students = new SummaryReportResponseModel.SummaryReportTableRow();
        students.studentA = "StudentA";
        students.studentB = "StudentB";
        students.similarity = (float) 100.0005;
        students.similarityA = (float) 99.5123;
        students.similarityB = (float) 99.1523;
        students.processed = true;
        students.plagiarized = true;
        students.calculatedSimilarity = (float) 107.0005;
        students.calculatedSimilarityA = (float) 96.5123;
        students.calculatedSimilarityB = (float) 94.1523;
        students.fileAName = "fileAName";
        students.fileBName = "fileBName";
        responseModel.reportTable.add(students);

        presenter.presentReport(responseModel);

        List<PresentableReportSummaryTableRow> reportTable = viewModel.getSummaryReport().getReportTable();
        assertEquals(responseModel.toolName, viewModel.getSummaryReport().getResultToolName());
        assertEquals(responseModel.techniqueName, viewModel.getSummaryReport().getResultTechniqueName());
        assertEquals(viewModel.getSelectedInputDirPath(), viewModel.getSummaryReport().getSelectedInputDirPath());
        assertEquals(responseModel.matchesDirPath.getPath(), viewModel.getSummaryReport().getMatchesDirPath());
        assertEquals(students.studentA, reportTable.get(0).getStudentA());
        assertEquals(students.studentB, reportTable.get(0).getStudentB());
        assertEquals(students.plagiarized, reportTable.get(0).getPlagiarized());
        assertEquals(students.processed, reportTable.get(0).getProcessed());
        assertEquals(students.similarity, reportTable.get(0).getSimilarity(),0.1);
        assertEquals(students.similarityA, reportTable.get(0).getSimilarityA(),0.1);
        assertEquals(students.similarityB, reportTable.get(0).getSimilarityB(),0.1);
        assertEquals(students.calculatedSimilarity, reportTable.get(0).getCalculatedSimilarity(),0.1);
        assertEquals(students.calculatedSimilarityA, reportTable.get(0).getCalculatedSimilarityA(),0.1);
        assertEquals(students.calculatedSimilarityB, reportTable.get(0).getCalculatedSimilarityB(),0.1);
        assertEquals(students.fileAName, reportTable.get(0).getFileA());
        assertEquals(students.fileBName, reportTable.get(0).getFileB());
        assertEquals(responseModel.similarityStatistics.max,viewModel.getSummaryReport().getMax(),0.1);
        assertEquals(responseModel.similarityStatistics.min,viewModel.getSummaryReport().getMin(),0.1);
        assertEquals(responseModel.similarityStatistics.mean,viewModel.getSummaryReport().getMean(),0.1);
        assertEquals(responseModel.similarityStatistics.median,viewModel.getSummaryReport().getMedian(),0.1);
        assertEquals(responseModel.similarityStatistics.q1,viewModel.getSummaryReport().getQ1(),0.1);
        assertEquals(responseModel.similarityStatistics.q3,viewModel.getSummaryReport().getQ3(),0.1);
        assertEquals(responseModel.similarityStatistics.percentile99,viewModel.getSummaryReport().getPercentile99(),0.1);
        assertEquals(responseModel.similarityStatistics.IRQ,viewModel.getSummaryReport().getIRQ(),0.1);
        assertEquals(responseModel.similarityStatistics.median+responseModel.similarityStatistics.IRQ *3,viewModel.getSummaryReport().getMedianPlus3IRQ(),0.1);
        assertEquals(responseModel.similarityStatistics.median+responseModel.similarityStatistics.IRQ *2.5,viewModel.getSummaryReport().getMedianPlus2p5IRQ(),0.1);
        assertEquals(responseModel.similarityStatistics.median+responseModel.similarityStatistics.IRQ *2,viewModel.getSummaryReport().getMedianPlus2IRQ(),0.1);
        assertEquals(responseModel.similarityStatistics.STD,viewModel.getSummaryReport().getSTD(),0.1);
        assertEquals(expectedModes,viewModel.getSummaryReport().getMode());

        assertEquals("", viewModel.getErrorMessage());
        assertNull(viewModel.getSelectedPair());
    }

    @Test
    public void updatesPlagInfoForExistingPair(){
        PresentableReportSummaryTableRow summaryTableRow = new PresentableReportSummaryTableRow();
        summaryTableRow.setFileA("fileA");
        summaryTableRow.setFileB("fileB");
        summaryTableRow.setPlagiarized(false);
        summaryTableRow.setProcessed(false);
        summaryTableRow.setPlagiarizedDisabled(false);
        summaryTableRow.setProcessedDisabled(false);
        List<PresentableReportSummaryTableRow> summaryTable = new ArrayList<>();
        summaryTable.add(summaryTableRow);
        PresentableReportSummary reportSummary = new PresentableReportSummary();
        reportSummary.setReportTable(summaryTable);
        viewModel.setSummaryReport(reportSummary);

        UpdatePlagInfoResponseModel responseModel = new UpdatePlagInfoResponseModel();
        responseModel.fileAname = "fileA";
        responseModel.fileBname = "fileB";
        responseModel.processed = true;
        responseModel.plagiarized = true;
        responseModel.processedDisabled = true;
        responseModel.plagiarizedDisabled = true;
        responseModel.errorMessages = "";

        presenter.presentUpdatedSummaryReportRowPlagInfo(responseModel);

        assertTrue(viewModel.getSummaryReport().getReportTable().get(0).getProcessed());
        assertTrue(viewModel.getSummaryReport().getReportTable().get(0).getPlagiarized());
        assertTrue(viewModel.getSummaryReport().getReportTable().get(0).getPlagiarizedDisabled());
        assertTrue(viewModel.getSummaryReport().getReportTable().get(0).getProcessedDisabled());
        assertEquals(responseModel.errorMessages,viewModel.getErrorMessage());
        assertSame(viewModel.getSummaryReport().getReportTable().get(0),viewModel.getSelectedPair());
    }

    @Test
    public void showsErrorForNonExistingPair(){
        UpdatePlagInfoResponseModel responseModel = new UpdatePlagInfoResponseModel();
        responseModel.fileAname = "fileA";
        responseModel.fileBname = "fileB";
        responseModel.errorMessages = "";

        presenter.presentUpdatedSummaryReportRowPlagInfo(responseModel);
        assertEquals("No such row presenter can not update view!",viewModel.getErrorMessage());

        PresentableReportSummary reportSummary = new PresentableReportSummary();
        viewModel.setSummaryReport(reportSummary);
        presenter.presentUpdatedSummaryReportRowPlagInfo(responseModel);
        assertEquals("No such row presenter can not update view!",viewModel.getErrorMessage());

        List<PresentableReportSummaryTableRow> summaryTable = new ArrayList<>();
        reportSummary.setReportTable(summaryTable);
        viewModel.setSummaryReport(reportSummary);
        presenter.presentUpdatedSummaryReportRowPlagInfo(responseModel);
        assertEquals("No such row presenter can not update view!",viewModel.getErrorMessage());

        PresentableReportSummaryTableRow summaryTableRow = new PresentableReportSummaryTableRow();
        summaryTableRow.setFileA("fileC.java");
        summaryTableRow.setFileB("fileD.java");
        summaryTable.add(summaryTableRow);
        reportSummary.setReportTable(summaryTable);
        viewModel.setSummaryReport(reportSummary);
        presenter.presentUpdatedSummaryReportRowPlagInfo(responseModel);
        assertEquals("No such row presenter can not update view!",viewModel.getErrorMessage());

        responseModel.errorMessages = "error";
        presenter.presentUpdatedSummaryReportRowPlagInfo(responseModel);
        assertEquals(responseModel.errorMessages,viewModel.getErrorMessage());
    }
}