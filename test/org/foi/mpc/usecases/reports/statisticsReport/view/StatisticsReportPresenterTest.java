package org.foi.mpc.usecases.reports.statisticsReport.view;

import clover.org.apache.commons.collections.map.ListOrderedMap;
import org.foi.mpc.MPCContext;
import org.foi.mpc.executiontools.factories.SimilarityDetectionToolFactory;
import org.foi.mpc.usecases.reports.avalibleTools.models.AvailableToolsResponseModel;
import org.foi.mpc.usecases.reports.statisticsReport.StatisticsReportOutputBoundary;
import org.foi.mpc.usecases.reports.statisticsReport.models.SimilarityDescriptiveStatistics;
import org.foi.mpc.usecases.reports.statisticsReport.models.StatisticsReportResponseModel;
import org.foi.mpc.usecases.reports.statisticsReport.models.StatisticsReportTableRow;
import org.foi.mpc.usecases.reports.statisticsReport.view.model.StatisticsReportViewModel;
import org.foi.mpc.usecases.reports.view.models.SelectOption;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class StatisticsReportPresenterTest {
    StatisticsReportPresenter presenter;
    StatisticsReportViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new StatisticsReportViewModel();
        presenter = new StatisticsReportPresenter(viewModel);
    }

    @Test
    public void isPPTestReportOutputBoundary() {
        assertTrue(presenter instanceof StatisticsReportOutputBoundary);
    }

    @Test
    public void translatesResponseModelOfAvalibleToolsToViewModel() {
        AvailableToolsResponseModel responseModel = new AvailableToolsResponseModel();
        responseModel.tools = new SimilarityDetectionToolFactory(MPCContext.MATCHES_DIR).getAvailableTools();
        responseModel.errorMessage = "errorMessage";

        presenter.presentAvailableTools(responseModel);

        assertEquals(responseModel.tools, viewModel.getAvailableTools());
        assertEquals(responseModel.errorMessage,viewModel.getErrorMessage());
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
    public void translatesResponseModelOGenerateReportWithErrorMessages() {
        StatisticsReportResponseModel responseModel = new StatisticsReportResponseModel();
        responseModel.errorMessages = "error";
        responseModel.assignementDir = null;

        presenter.presentStatististicsReport(responseModel);

        assertEquals(responseModel.errorMessages,viewModel.getErrorMessage());
        assertEquals(new ArrayList<>(), viewModel.getReportTable());
        assertEquals( 0,responseModel.plagiarizedMatches);
        assertNull(viewModel.getAssigmentDirPath());
    }

    @Test
    public void translatesResponseModelOGenerateReport() {
        StatisticsReportResponseModel responseModel = new StatisticsReportResponseModel();
        responseModel.reportTable = new ArrayList<>();
        responseModel.plagiarizedMatches = 10;
        responseModel.errorMessages = "";
        responseModel.assignementDir = new File("assignemntDir");

        StatisticsReportTableRow tableRow = new StatisticsReportTableRow();
        tableRow.F1 = 0.9;
        tableRow.recall = 0.8;
        tableRow.precision = 0.7;
        tableRow.falseNegatives = 9;
        tableRow.falsePositives = 8;
        tableRow.truePositives = 7;
        tableRow.indicatedMatches = 6;
        tableRow.threshold = 10;
        tableRow.numberOfMatches = 100;
        tableRow.includedMatches = 200;

        tableRow.technique = "Technique1";
        tableRow.tool =  "Tool1";
        tableRow.matchesDir = new File("MatchesDir");
       /* StatisticsReportTableRow.StatMatch statMatch = new StatisticsReportTableRow.StatMatch();
        statMatch.fileAName = "FileA";
        statMatch.fileBName = "FileB";
        statMatch.similarity = 0.6;
        tableRow.matches = new ArrayList<>();
        tableRow.matches.add(statMatch);
        tableRow.matches.add(statMatch);*/

        tableRow.descStat = new SimilarityDescriptiveStatistics();
        tableRow.descStat.median = 22;
        tableRow.descStat.IRQ = 21;
        tableRow.descStat.q3 = 20;
        tableRow.descStat.q1 = 19;
        tableRow.descStat.mean = 18;
        tableRow.descStat.STD = 17;
        tableRow.descStat.max = 16;
        tableRow.descStat.min =15;
        tableRow.descStat.percentile99 = 14;

        tableRow.descStat.mode = new double[2];
        tableRow.descStat.mode[0] = 13;
        tableRow.descStat.mode[1] = 12;
        List<Double> expectedModes = new ArrayList<>();
        expectedModes.add(tableRow.descStat.mode[0]);
        expectedModes.add(tableRow.descStat.mode[1]);

        responseModel.reportTable.add(tableRow);
        responseModel.reportTable.add(tableRow);

        presenter.presentStatististicsReport(responseModel);

        assertEquals(responseModel.errorMessages,viewModel.getErrorMessage());
        assertEquals(responseModel.assignementDir.getPath(), viewModel.getAssigmentDirPath());
        assertEquals(responseModel.plagiarizedMatches,viewModel.getPlagiarizedMatches());
        assertEquals(2,viewModel.getReportTable().size());
        assertStatisticsReportTableRow(responseModel.reportTable.get(0),viewModel.getReportTable().get(0));
        assertEquals(expectedModes, viewModel.getReportTable().get(0).getMode());

        responseModel.reportTable = new ArrayList<>();
        responseModel.reportTable.add(tableRow);
        presenter.presentStatististicsReport(responseModel);
        assertEquals(1,viewModel.getReportTable().size());
    }

    private void assertStatisticsReportTableRow(StatisticsReportTableRow tableRow, StatisticsReportViewModel.PresentableStatisticsReportTableRow presentableTableRow){
        assertEquals(tableRow.technique, presentableTableRow.getTechnique());
        assertEquals(tableRow.tool, presentableTableRow.getTool());
        assertEquals(tableRow.matchesDir.getPath(), presentableTableRow.getMatchesDir());

        assertEquals(tableRow.truePositives, presentableTableRow.getTruePositives());
        assertEquals(tableRow.falseNegatives, presentableTableRow.getFalseNegatives());
        assertEquals(tableRow.falsePositives, presentableTableRow.getFalsePositives());
        assertEquals(tableRow.indicatedMatches, presentableTableRow.getIndicatedMatches());
        assertEquals(tableRow.numberOfMatches, presentableTableRow.getNumberOfMatches());
        assertEquals(tableRow.includedMatches, presentableTableRow.getIncludedMatches());

        assertEquals(tableRow.threshold, presentableTableRow.getTreshold(),0.1);

        assertEquals(tableRow.recall, presentableTableRow.getRecall(),0.01);
        assertEquals(tableRow.precision, presentableTableRow.getPrecision(),0.01);
        assertEquals(tableRow.F1, presentableTableRow.getF1(),0.01);

        assertEquals(tableRow.descStat.IRQ, presentableTableRow.getIRQ(),0.01);
        assertEquals(tableRow.descStat.median, presentableTableRow.getMedian(),0.01);
        assertEquals(tableRow.descStat.q1, presentableTableRow.getQ1(),0.01);
        assertEquals(tableRow.descStat.q3, presentableTableRow.getQ3(),0.01);
        assertEquals(tableRow.descStat.mean, presentableTableRow.getMean(),0.01);
        assertEquals(tableRow.descStat.max, presentableTableRow.getMax(),0.01);
        assertEquals(tableRow.descStat.min, presentableTableRow.getMin(),0.01);
        assertEquals(tableRow.descStat.percentile99, presentableTableRow.getPercentile99(),0.01);

        assertEquals(-1, presentableTableRow.getMedianPlus2IRQ(),0.01);
        assertEquals(-1, presentableTableRow.getMedianPlus2p5IRQ(),0.01);
        assertEquals(-1, presentableTableRow.getMedianPlus3IRQ(),0.01);
    }
}