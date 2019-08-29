package org.foi.mpc.usecases.reports.statisticsReport;

import clover.org.apache.commons.collections.map.ListOrderedMap;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.apache.commons.math3.stat.StatUtils;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.mpc.MPCContext;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagJavaAdapter;
import org.foi.mpc.executiontools.prepareTools.SubmissionFilesUnifier;
import org.foi.mpc.executiontools.techniques.NoTechniqueOriginal;
import org.foi.mpc.phases.PhaseFactory;
import org.foi.mpc.phases.readerphase.MPCMatchReaderPhase;
import org.foi.mpc.usecases.UseCaseResponseErrorMessages;
import org.foi.mpc.usecases.reports.DirectoryPreparerSpy;
import org.foi.mpc.usecases.reports.avalibleTools.models.AvailableToolsResponseModel;
import org.foi.mpc.usecases.reports.statisticsReport.models.StatisticsReportRequestModel;
import org.foi.mpc.usecases.reports.statisticsReport.models.StatisticsReportResponseModel;
import org.foi.mpc.usecases.reports.statisticsReport.models.StatisticsReportTableRow;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.*;

@RunWith(HierarchicalContextRunner.class)
public class StatisticsReportUseCaseTest {
    StatisticsReportUseCase useCase;
    StatisticsyReportOutputBoundarySpy presenterSpy;
    File testWorkingDir;
    MPCMatchReaderPhase mpcMatchReaderPhase;

    @Before
    public void setUp()  {
        mpcMatchReaderPhase = (MPCMatchReaderPhase) new PhaseFactory(MPCContext.MATCHES_DIR).createMPCMatchReadPhase();
        presenterSpy = new StatisticsyReportOutputBoundarySpy();
        useCase = new StatisticsReportUseCase(mpcMatchReaderPhase);

        testWorkingDir = new File("testWorkingDir");

        testWorkingDir.mkdirs();
    }

    @After
    public void tearDown() throws Exception {
        DirectoryFileUtility.deleteDirectoryTree(testWorkingDir);
    }

    @Test
    public void isSummaryReportInputBoudary() {
        assertTrue(useCase instanceof StatisticsReportInputBoundary);
    }

    @Test
    public void useCaseReturnsErrorIfWorkingDirDoesNotExist(){
        useCase.getAvailableTools(presenterSpy, null);
        assertEquals(UseCaseResponseErrorMessages.invalidWorkingDir,presenterSpy.availableTTResponseModel.errorMessage);
        assertEquals(new ArrayList<>(), presenterSpy.availableTTResponseModel.tools);

        File invalidWorkignDir = new File("invalidWorkingDir");
        useCase.getAvailableTools(presenterSpy, invalidWorkignDir);
        assertEquals(UseCaseResponseErrorMessages.invalidWorkingDir,presenterSpy.availableTTResponseModel.errorMessage);
        assertEquals(new ArrayList<>(), presenterSpy.availableTTResponseModel.tools);
    }

    @Test
    public void useCaseReturnsErrorIfDetectionDirDoesNotExist(){
        File detectionDir = new File(testWorkingDir +File.separator+MPCContext.DETECTION_DIR);
        detectionDir.getParentFile().mkdirs();
        useCase.getAvailableTools(presenterSpy, detectionDir.getParentFile());
        assertEquals(UseCaseResponseErrorMessages.invalidDetectionDir,presenterSpy.availableTTResponseModel.errorMessage);
        assertEquals(new ArrayList<>(), presenterSpy.availableTTResponseModel.tools);
    }

    @Test
    public void useCaseReturnsAvailableToolsAndTechniquesOnGetAvailableTools(){
        File detectionDir = new File(testWorkingDir +File.separator+MPCContext.DETECTION_DIR);
        detectionDir.mkdirs();
        File tool1 = new File(detectionDir+File.separator+"tool1");
        File tool2 = new File(detectionDir+File.separator+"tool2");
        File tool3 = new File(detectionDir+File.separator+"tool3");
        tool1.mkdirs();
        tool2.mkdirs();
        tool3.mkdirs();

        AvailableToolsResponseModel availableTTResponseModel = new AvailableToolsResponseModel();
        availableTTResponseModel.tools = new ArrayList<>();
        availableTTResponseModel.tools.add(tool1.getName());
        availableTTResponseModel.tools.add(tool3.getName());
        availableTTResponseModel.tools.add(tool2.getName());
        availableTTResponseModel.errorMessage = "";

        useCase = new StatisticsReportUseCase(mpcMatchReaderPhase);
        useCase.getAvailableTools(presenterSpy, detectionDir.getParentFile());

        assertEquals(availableTTResponseModel,presenterSpy.availableTTResponseModel);
    }

    @Test
    public void useCaseRetrunsListOfDirsForGivenInputDir() throws IOException {
        Map<String,File> inputDirs = new TreeMap<>();
        File inputDir = new File(testWorkingDir+File.separator+"NWTIS"+File.separator+"2016-2017"+File.separator+"DZ1");
        inputDir.mkdirs();
        inputDirs.put("NWTIS"+File.separator+"2016-2017"+File.separator+"DZ1",inputDir);
        inputDir = new File(testWorkingDir+File.separator+"NWTIS"+File.separator+"2017-2018"+File.separator+"DZ1");
        inputDir.mkdirs();
        inputDirs.put("NWTIS"+File.separator+"2017-2018"+File.separator+"DZ1",inputDir);
        inputDir = new File(testWorkingDir+File.separator+"NWTIS"+File.separator+"2017-2018"+File.separator+"DZ2");
        inputDir.mkdirs();
        inputDirs.put("NWTIS"+File.separator+"2017-2018"+File.separator+"DZ2",inputDir);

        useCase.loadDirectoryList(testWorkingDir,3,presenterSpy);

        assertEquals(inputDirs,presenterSpy.dirList);
    }

    @Test
    public void useCaseCallsDirectoryPreparerWithCorrectInputDirDepthAndDir(){
        DirectoryPreparerSpy directoryPreparerSpy = new DirectoryPreparerSpy();
        useCase.setDirectoryPreparer(directoryPreparerSpy);

        File selectedInputDir = new File("inputDirs");
        useCase.loadDirectoryList(selectedInputDir,3,presenterSpy);

        assertEquals(3,directoryPreparerSpy.inputDirDepth);
        assertEquals(selectedInputDir,directoryPreparerSpy.selectedInputDir);
    }

    public class GenerateReport {
        StatisticsReportRequestModel requestModel;
        String assignementDirPath = "NWTiS"+File.separator+"2017-2018"+File.separator+"DZ1";
        @Before
        public void setUp()  {
            requestModel = new StatisticsReportRequestModel();
            requestModel.workingDir = new File("testInputData"+File.separator+"statisticsReportWorkingDir");
            requestModel.inputDirDepth = 3;
            requestModel.assignementDir = new File("A1");
            requestModel.sourceDir = testWorkingDir;
            requestModel.assignementDir = new File(testWorkingDir+File.separator+assignementDirPath);
            requestModel.tools = new ArrayList<>();
            requestModel.tools.add(JPlagJavaAdapter.NAME);
            requestModel.techniques = new ArrayList<>();
            requestModel.techniques.add(NoTechniqueOriginal.NAME);
            requestModel.thresholdType = StatisticsReportRequestModel.ThresholdType.calculatedPrecentageBased;
            requestModel.thresholdValue = 3;
        }

        @Test
        public void givenInvalidWorkingDirWriteResponse() {
            requestModel.workingDir = new File("invalid");

            useCase.generateReport(requestModel, presenterSpy);

            Assert.assertEquals(UseCaseResponseErrorMessages.invalidWorkingDir, presenterSpy.responseModel.errorMessages);
            assertNotNull(presenterSpy.responseModel.reportTable);
        }

        @Test
        public void givenInvalidSourceDirWriteResponse() {
            requestModel.sourceDir = new File("invalid");

            useCase.generateReport(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.invalidSourceDir, presenterSpy.responseModel.errorMessages);
            assertNotNull(presenterSpy.responseModel.reportTable);
        }

        @Test
        public void withNoToolSelected() {
            requestModel.tools = new ArrayList<>();

            useCase.generateReport(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.noToolIsSelected, presenterSpy.responseModel.errorMessages);
            assertNotNull(presenterSpy.responseModel.reportTable);
        }

        @Test
        public void withNoTechniqueSelected() {
            requestModel.techniques = new ArrayList<>();

            useCase.generateReport(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.noTechniqueIsSelected, presenterSpy.responseModel.errorMessages);
            assertNotNull(presenterSpy.responseModel.reportTable);
        }

        @Test
        public void withInvalidInputDirDepth() {
            requestModel.inputDirDepth = -1;

            useCase.generateReport(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.inputDirDepth, presenterSpy.responseModel.errorMessages);
            assertNotNull(presenterSpy.responseModel.reportTable);
        }

        @Test
        public void withSourceDirPathAndSelectedDirNotMatchig() {
            requestModel.sourceDir = requestModel.workingDir;

            useCase.generateReport(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.inputDirMismatch, presenterSpy.responseModel.errorMessages);
            assertNotNull(presenterSpy.responseModel.reportTable);
        }

        @Test
        public void analysisDirDoesNotExist() throws IOException {
            requestModel.workingDir = testWorkingDir;

            useCase.generateReport(requestModel, presenterSpy);
            Assert.assertEquals(UseCaseResponseErrorMessages.invalidAnalysisDir+":"+requestModel.workingDir+File.separator+MPCContext.ANALYSIS_DIR+File.separator+assignementDirPath, presenterSpy.responseModel.errorMessages);
        }

        @Test
        public void matchesDirForTheSelectedToolAndTechniqueDoesNotExist() throws IOException {
            requestModel.tools.add("wrongTool");
            useCase.generateReport(requestModel, presenterSpy);
            Assert.assertEquals(UseCaseResponseErrorMessages.invalidMatchesDir+":"
                            +createMatchesDir("wrongTool",requestModel.techniques.get(0)),
                    presenterSpy.responseModel.errorMessages);

            requestModel.tools.remove("wrongTool");
            requestModel.techniques.add("wrongTechnique");
            useCase.generateReport(requestModel, presenterSpy);
            Assert.assertEquals(UseCaseResponseErrorMessages.invalidMatchesDir+":"
                            +createMatchesDir(requestModel.tools.get(0),"wrongTechnique"),
                    presenterSpy.responseModel.errorMessages);
        }

        @Test
        public void generateReportWithToolAndTechniqueInfo(){
            useCase.generateReport(requestModel, presenterSpy);

            assertEquals(1,useCase.getProcessedMatchesDirs());
            assertEquals("",presenterSpy.responseModel.errorMessages);
            assertEquals(requestModel.assignementDir,presenterSpy.responseModel.assignementDir);
            assertEquals(14,presenterSpy.responseModel.plagiarizedMatches);

            assertReportTableRow(requestModel.tools.get(0),requestModel.techniques.get(0),presenterSpy.responseModel.reportTable.get(0));
            //assertReportTableRow(requestModel.tools.get(1),requestModel.techniques.get(0),presenterSpy.responseModel.reportTable.get(1));
            requestModel.tools.add("Tool2");
            requestModel.tools.remove(JPlagJavaAdapter.NAME);
            useCase.generateReport(requestModel, presenterSpy);
            assertEquals(1,useCase.getProcessedMatchesDirs());
            assertEquals(1,presenterSpy.responseModel.reportTable.size());

            requestModel.tools.add(JPlagJavaAdapter.NAME);
            useCase.generateReport(requestModel, presenterSpy);
            assertEquals(2,useCase.getProcessedMatchesDirs());
            assertEquals(2,presenterSpy.responseModel.reportTable.size());
            assertReportTableRow(requestModel.tools.get(1),requestModel.techniques.get(0),presenterSpy.responseModel.reportTable.get(1));

        }

        protected void assertReportTableRow(String tool, String technique, StatisticsReportTableRow tableRow) {
            System.out.println(tool+"-"+technique);
            assertEquals(tool, tableRow.tool);
            assertEquals(technique, tableRow.technique);
            assertEquals(createMatchesDir(tool, technique), tableRow.matchesDir.getPath());
            //TODO assert all elements as array without order checking
            assertEquals(77, tableRow.matches.size());
            // assertEquals(10,presenterSpy.responseModel.reportTable.get(0).nonPlagiarizedMatches);
            // assertEquals(10,presenterSpy.responseModel.reportTable.get(0).numberOfMatches);
            assertEquals(5.253984, tableRow.descStat.q1,0.01);
            assertEquals(15.80041, tableRow.descStat.q3,0.01);
            assertEquals(17.16720, tableRow.descStat.mean,0.01);
            assertEquals(8.171206, tableRow.descStat.median,0.01);
            assertEquals(10.54643, tableRow.descStat.IRQ,0.01);
            assertEquals(24.89070, tableRow.descStat.STD,0.01);
            assertEquals(2.32, tableRow.descStat.min,0.01);
            assertEquals(100, tableRow.descStat.max,0.01);

            assertEquals(6, tableRow.indicatedMatches);
            assertEquals(1, tableRow.falsePositives);
            assertEquals(9, tableRow.falseNegatives);
            assertEquals(77,tableRow.includedMatches);
            assertEquals((4*(4-1))/2,tableRow.numberOfMatches);
            assertEquals(tableRow.descStat.median+tableRow.descStat.IRQ*3, tableRow.threshold,0.01);
            assertEquals(presenterSpy.responseModel.plagiarizedMatches- tableRow.truePositives, tableRow.falseNegatives);
            assertEquals(5, tableRow.truePositives);

            assertEquals(tableRow.truePositives/(double) tableRow.indicatedMatches, tableRow.precision,0.01);
            assertEquals(tableRow.truePositives/(double) presenterSpy.responseModel.plagiarizedMatches, tableRow.recall,0.01);
            double p = tableRow.precision;
            double r = tableRow.recall;
            assertEquals((2*(p*r))/(p+r), tableRow.F1,0.01);

            //  assertEquals(10,presenterSpy.responseModel.reportTable.get(0).trueNegatives);
        }

        @Test
        public void generateReportWithMultipleToolsAndTechniquesInfo(){
            requestModel.tools.add("Tool2");
            requestModel.techniques.add("Technique2");

            useCase.generateReport(requestModel, presenterSpy);

            assertEquals("",presenterSpy.responseModel.errorMessages);
            assertEquals(requestModel.assignementDir,presenterSpy.responseModel.assignementDir);
            assertEquals(requestModel.tools.get(0),presenterSpy.responseModel.reportTable.get(0).tool);
            assertEquals(requestModel.techniques.get(0),presenterSpy.responseModel.reportTable.get(0).technique);
            assertEquals(createMatchesDir(requestModel.tools.get(0),requestModel.techniques.get(0)),presenterSpy.responseModel.reportTable.get(0).matchesDir.getPath());
            assertEquals(requestModel.tools.get(0),presenterSpy.responseModel.reportTable.get(1).tool);
            assertEquals(requestModel.techniques.get(1),presenterSpy.responseModel.reportTable.get(1).technique);
            assertEquals(createMatchesDir(requestModel.tools.get(0),requestModel.techniques.get(1)),presenterSpy.responseModel.reportTable.get(1).matchesDir.getPath());
            assertEquals(requestModel.tools.get(1),presenterSpy.responseModel.reportTable.get(2).tool);
            assertEquals(requestModel.techniques.get(0),presenterSpy.responseModel.reportTable.get(2).technique);
            assertEquals(createMatchesDir(requestModel.tools.get(1),requestModel.techniques.get(0)),presenterSpy.responseModel.reportTable.get(2).matchesDir.getPath());
            assertEquals(requestModel.tools.get(1),presenterSpy.responseModel.reportTable.get(3).tool);
            assertEquals(requestModel.techniques.get(1),presenterSpy.responseModel.reportTable.get(3).technique);
            assertEquals(createMatchesDir(requestModel.tools.get(1),requestModel.techniques.get(1)),presenterSpy.responseModel.reportTable.get(3).matchesDir.getPath());

        }

        @Test
        public void generateReportWithThresholdBasedOnCalculatedPrecentage() {
            requestModel.thresholdType = StatisticsReportRequestModel.ThresholdType.calculatedPrecentageBased;
            requestModel.thresholdValue = 3;
            useCase.generateReport(requestModel, presenterSpy);
            StatisticsReportTableRow tableRow = presenterSpy.responseModel.reportTable.get(0);
            assertEquals(tableRow.descStat.median+tableRow.descStat.IRQ*requestModel.thresholdValue , tableRow.threshold,0.01);
            assertEquals(6, tableRow.indicatedMatches);
            assertEquals(1, tableRow.falsePositives);
            assertEquals(9, tableRow.falseNegatives);
            assertEquals(5, tableRow.truePositives);

            requestModel.thresholdValue = 2.5;
            useCase.generateReport(requestModel, presenterSpy);
            tableRow = presenterSpy.responseModel.reportTable.get(0);
            assertEquals(tableRow.descStat.median+tableRow.descStat.IRQ*requestModel.thresholdValue , tableRow.threshold,0.01);
            assertEquals(9, tableRow.indicatedMatches);
            assertEquals(4, tableRow.falsePositives);
            assertEquals(9, tableRow.falseNegatives);
            assertEquals(5, tableRow.truePositives);

            requestModel.thresholdValue = 2;
            useCase.generateReport(requestModel, presenterSpy);
            tableRow = presenterSpy.responseModel.reportTable.get(0);
            assertEquals(tableRow.descStat.median+tableRow.descStat.IRQ*requestModel.thresholdValue , tableRow.threshold,0.01);
            assertEquals(11, tableRow.indicatedMatches);
            assertEquals(6, tableRow.falsePositives);
            assertEquals(9, tableRow.falseNegatives);
            assertEquals(5, tableRow.truePositives);
        }

        @Test
        public void generateReportWithThresholdBasedOnFixedPrecentage() {
            requestModel.thresholdType = StatisticsReportRequestModel.ThresholdType.fixedPrecentageBased;
            requestModel.thresholdValue = 99;
            useCase.generateReport(requestModel, presenterSpy);
            StatisticsReportTableRow tableRow = presenterSpy.responseModel.reportTable.get(0);
            assertEquals( 99, tableRow.threshold,0.01);
            assertEquals(5, tableRow.indicatedMatches);
            assertEquals(0, tableRow.falsePositives);
            assertEquals(9, tableRow.falseNegatives);
            assertEquals(5, tableRow.truePositives);

            requestModel.thresholdValue = 90;
            useCase.generateReport(requestModel, presenterSpy);
            tableRow = presenterSpy.responseModel.reportTable.get(0);
            assertEquals( 90, tableRow.threshold,0.01);
            assertEquals(6, tableRow.indicatedMatches);
            assertEquals(1, tableRow.falsePositives);
            assertEquals(9, tableRow.falseNegatives);
            assertEquals(5, tableRow.truePositives);

            requestModel.thresholdValue = 20;
            useCase.generateReport(requestModel, presenterSpy);
            tableRow = presenterSpy.responseModel.reportTable.get(0);
            assertEquals(20, tableRow.threshold,0.01);
            assertEquals(11, tableRow.indicatedMatches);
            assertEquals(6, tableRow.falsePositives);
            assertEquals(9, tableRow.falseNegatives);
            assertEquals(5, tableRow.truePositives);
        }

        @Test
        public void generateReportWithThresholdBasedOnTopNMatches() {
            requestModel.thresholdType = StatisticsReportRequestModel.ThresholdType.topNBased;
            requestModel.thresholdValue = 2;
            useCase.generateReport(requestModel, presenterSpy);
            StatisticsReportTableRow tableRow = presenterSpy.responseModel.reportTable.get(0);
            assertEquals(2, tableRow.threshold,0.01);
            assertEquals(2, tableRow.indicatedMatches);
            assertEquals(0, tableRow.falsePositives);
            assertEquals(12, tableRow.falseNegatives);
            assertEquals(2, tableRow.truePositives);

            requestModel.thresholdValue = 5;
            useCase.generateReport(requestModel, presenterSpy);
            tableRow = presenterSpy.responseModel.reportTable.get(0);
            assertEquals(5 , tableRow.threshold,0.01);
            assertEquals(5, tableRow.indicatedMatches);
            assertEquals(0, tableRow.falsePositives);
            assertEquals(9, tableRow.falseNegatives);
            assertEquals(5, tableRow.truePositives);

            requestModel.thresholdValue = 10;
            useCase.generateReport(requestModel, presenterSpy);
            tableRow = presenterSpy.responseModel.reportTable.get(0);
            assertEquals(10, tableRow.threshold,0.01);
            assertEquals(10, tableRow.indicatedMatches);
            assertEquals(5, tableRow.falsePositives);
            assertEquals(9, tableRow.falseNegatives);
            assertEquals(5, tableRow.truePositives);
        }

        @Test
        public void generateReportWithThresholdBasedOnCPlagiarizedMatches() {
            requestModel.thresholdType = StatisticsReportRequestModel.ThresholdType.plagMatchesBased;
            requestModel.thresholdValue = -1;
            useCase.generateReport(requestModel, presenterSpy);
            StatisticsReportTableRow tableRow = presenterSpy.responseModel.reportTable.get(0);
            assertEquals(14, tableRow.threshold,0.01);
            assertEquals(14, tableRow.indicatedMatches);
            assertEquals(9, tableRow.falsePositives);
            assertEquals(9, tableRow.falseNegatives);
            assertEquals(5, tableRow.truePositives);
        }

        String createMatchesDir(String tool, String technique){
            return requestModel.workingDir+File.separator
                    +MPCContext.DETECTION_DIR+File.separator
                    +tool+File.separator
                    +technique+File.separator
                    +SubmissionFilesUnifier.NAME+File.separator
                    +assignementDirPath+File.separator+MPCContext.MATCHES_DIR;
        }
    }

    private class StatisticsyReportOutputBoundarySpy implements StatisticsReportOutputBoundary {

        public AvailableToolsResponseModel availableTTResponseModel;
        public Map<String,File> dirList;
        public StatisticsReportResponseModel responseModel;

        @Override
        public void presentAvailableTools(AvailableToolsResponseModel responseModel) {
            availableTTResponseModel = responseModel;
        }

        @Override
        public void presentDirList(Map<String,File> dirsToPorces) {
            dirList = dirsToPorces;
        }

        @Override
        public void presentStatististicsReport(StatisticsReportResponseModel responseModel) {
            this.responseModel = responseModel;
        }
    }
}