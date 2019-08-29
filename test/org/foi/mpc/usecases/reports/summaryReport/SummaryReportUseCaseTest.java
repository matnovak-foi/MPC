package org.foi.mpc.usecases.reports.summaryReport;

import clover.org.apache.commons.collections.map.ListOrderedMap;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.file.TextFileUtility;
import org.foi.mpc.MPCContext;
import org.foi.mpc.TestContext;
import org.foi.mpc.abstractfactories.FactoryProvider;
import org.foi.mpc.executiontools.factories.SimilarityDetectionToolFactory;
import org.foi.mpc.executiontools.spies.PrepareToolSpy;
import org.foi.mpc.executiontools.spies.PreprocessingTechniqueSpy;
import org.foi.mpc.executiontools.spies.SimilarityDetectionToolSpy;
import org.foi.mpc.matches.PlagMatchesReader;
import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.matches.models.MPCMatchBuilder;
import org.foi.mpc.phases.readerphase.MPCMatchReaderPhaseSpy;
import org.foi.mpc.usecases.UseCaseResponseErrorMessages;
import org.foi.mpc.usecases.reports.DirectoryPreparerSpy;
import org.foi.mpc.usecases.reports.avalibleTools.models.AvailableToolsResponseModel;
import org.foi.mpc.usecases.reports.summaryReport.models.SummaryReportRequestModel;
import org.foi.mpc.usecases.reports.summaryReport.models.SummaryReportResponseModel;
import org.foi.mpc.usecases.reports.summaryReport.models.UpdatePlagInfoRequestModel;
import org.foi.mpc.usecases.reports.summaryReport.models.UpdatePlagInfoResponseModel;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;

@RunWith(HierarchicalContextRunner.class)
public class SummaryReportUseCaseTest {
    SummaryReportUseCase useCase;
    SummaryReportOutputBoundarySpy presenterSpy;

    @Before
    public void setUp()  {
        presenterSpy = new SummaryReportOutputBoundarySpy();
    }

    @Test
    public void isSummaryReportInputBoudary() {
        assertTrue(new SummaryReportUseCase(new FactoryProvider(MPCContext.MATCHES_DIR)) instanceof SummaryReportInputBoundary);
    }

    @Test
    public void useCaseReturnsAvailableToolsOnGetAvailableTools(){
        AvailableToolsResponseModel availableTTResponseModel = new AvailableToolsResponseModel();
        availableTTResponseModel.tools = new SimilarityDetectionToolFactory(MPCContext.MATCHES_DIR).getAvailableTools();

        useCase = new SummaryReportUseCase(new FactoryProvider(MPCContext.MATCHES_DIR));
        useCase.getAvailableTools(presenterSpy);

        assertEquals(availableTTResponseModel,presenterSpy.availableTTResponseModel);
    }

    @Test
    public void useCaseRetrunsListOfDirsForGivenInputDir() throws IOException {
        Map<String,File> inputDirs = new TreeMap<>();
        File inputDir = new File("inputDirs"+File.separator+"NWTIS"+File.separator+"2016-2017"+File.separator+"DZ1");
        inputDir.mkdirs();
        inputDirs.put("NWTIS"+File.separator+"2016-2017"+File.separator+"DZ1",inputDir);
        inputDir = new File("inputDirs"+File.separator+"NWTIS"+File.separator+"2017-2018"+File.separator+"DZ1");
        inputDir.mkdirs();
        inputDirs.put("NWTIS"+File.separator+"2017-2018"+File.separator+"DZ1",inputDir);
        inputDir = new File("inputDirs"+File.separator+"NWTIS"+File.separator+"2017-2018"+File.separator+"DZ2");
        inputDir.mkdirs();
        inputDirs.put("NWTIS"+File.separator+"2017-2018"+File.separator+"DZ2",inputDir);

        useCase = new SummaryReportUseCase(new FactoryProvider(MPCContext.MATCHES_DIR));
        useCase.loadDirectoryList(new File("inputDirs"),3,presenterSpy);

        DirectoryFileUtility.deleteDirectoryTree(new File("inputDirs"));
        assertEquals(inputDirs,presenterSpy.dirList);
    }

    @Test
    public void useCaseCallsDirectoryPreparerWithCorrectInputDirDepthAndDir(){
        DirectoryPreparerSpy directoryPreparerSpy = new DirectoryPreparerSpy();
        useCase = new SummaryReportUseCase(new FactoryProvider(MPCContext.MATCHES_DIR));
        useCase.setDirectoryPreparer(directoryPreparerSpy);

        File selectedInputDir = new File("inputDirs");
        useCase.loadDirectoryList(selectedInputDir,3,presenterSpy);

        assertEquals(3,directoryPreparerSpy.inputDirDepth);
        assertEquals(selectedInputDir,directoryPreparerSpy.selectedInputDir);
    }

    public class generateReportUseCaseTest {
        TestContext testContext;

        SimilarityDetectionToolSpy toolSpy;
        PreprocessingTechniqueSpy techniqueSpy;
        PrepareToolSpy prepareToolSpy;
        MPCMatchReaderPhaseSpy matchReaderPhaseSpy;

        SummaryReportRequestModel requestModel;
        File workingDir = new File("workingDir");

        @Before
        public void setUp() {

            testContext = new TestContext();
            toolSpy = testContext.getToolSpy();
            techniqueSpy = new PreprocessingTechniqueSpy();
            testContext.setTechniqueSpy(techniqueSpy);

            requestModel = new SummaryReportRequestModel();
            requestModel.selectedTechniques = new ArrayList<>();
            requestModel.selectedTools = new ArrayList<>();
            requestModel.selectedTools.add(toolSpy.getName());
            requestModel.sourceDirPath = new File("testInputData");
            requestModel.selectedInputDir = new File("testInputData" + File.separator + "ppTestFile");
            requestModel.selectedWorking = workingDir;
            requestModel.selectedInputDirDepth = 0;

            prepareToolSpy = testContext.getPrepareToolSpy();
            matchReaderPhaseSpy = testContext.matchReaderPhaseSpy;
            MPCMatch match = new MPCMatchBuilder().with100Similarity()
                    .withMatchesDirForTechnique(toolSpy.getName()+File.separator+techniqueSpy.getName())
                    .build();

            matchReaderPhaseSpy.mpcMatches.add(match);
            matchReaderPhaseSpy.mpcMatches.add(match);

            useCase = new SummaryReportUseCase(testContext.factoryProvider);

            workingDir.mkdir();
        }

        @After
        public void tearDown() throws IOException {
            DirectoryFileUtility.deleteDirectoryTree(workingDir);
        }

        @Test
        public void givenInvalidWorkingDirWriteResponse() {
            requestModel.selectedWorking = new File("invalid");

            useCase.generateReport(requestModel, presenterSpy);

            Assert.assertEquals(UseCaseResponseErrorMessages.invalidWorkingDir, presenterSpy.reportResponseModel.errorMessages);
            assertNotNull(presenterSpy.reportResponseModel.reportTable);
        }

        @Test
        public void givenInvalidSourceDirWriteResponse() {
            requestModel.selectedInputDir = new File("invalid");

            useCase.generateReport(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.invalidSourceDir, presenterSpy.reportResponseModel.errorMessages);
            assertNotNull(presenterSpy.reportResponseModel.reportTable);
        }

        @Test
        public void withNoToolSelected() {
            requestModel.selectedTools = new ArrayList<>();

            useCase.generateReport(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.noToolIsSelected, presenterSpy.reportResponseModel.errorMessages);
            assertNotNull(presenterSpy.reportResponseModel.reportTable);
        }

        @Test
        public void withNoTechniqueSelected() {
            requestModel.selectedTechniques = new ArrayList<>();

            useCase.generateReport(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.noTechniqueIsSelected, presenterSpy.reportResponseModel.errorMessages);
            assertNotNull(presenterSpy.reportResponseModel.reportTable);
        }

        @Test
        public void withInvalidInputDirDepth() {
            requestModel.selectedInputDirDepth = -1;
            requestModel.selectedTechniques = new ArrayList<>();
            requestModel.selectedTechniques.add(techniqueSpy.getName());

            useCase.generateReport(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.inputDirDepth, presenterSpy.reportResponseModel.errorMessages);
            assertNotNull(presenterSpy.reportResponseModel.reportTable);
        }

        @Test
        public void withSourceDirPathAndSelectedDirNotMatchig() {
            requestModel.selectedTechniques = new ArrayList<>();
            requestModel.selectedTechniques.add(techniqueSpy.getName());
            requestModel.sourceDirPath = new File("wrongSourceDirPath");

            useCase.generateReport(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.inputDirMismatch, presenterSpy.reportResponseModel.errorMessages);
            assertNotNull(presenterSpy.reportResponseModel.reportTable);
        }

        @Test
        public void withMultipleTechniqueSelected(){
            requestModel.selectedTechniques = new ArrayList<>();
            requestModel.selectedTechniques.add(techniqueSpy.getName());
            PreprocessingTechniqueSpy techniqueSpy2 = new PreprocessingTechniqueSpy();
            requestModel.selectedTechniques.add(techniqueSpy2.getName());

            useCase.generateReport(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.toManyTechniquesSelected, presenterSpy.reportResponseModel.errorMessages);
            assertNotNull(presenterSpy.reportResponseModel.reportTable);
        }

        @Test
        public void withMultipleToolsSelected(){
            requestModel.selectedTools.add(toolSpy.getName());
            SimilarityDetectionToolSpy toolSpy2 = new SimilarityDetectionToolSpy();
            requestModel.selectedTechniques.add(toolSpy2.getName());

            useCase.generateReport(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.toManyToolsSelected, presenterSpy.reportResponseModel.errorMessages);
            assertNotNull(presenterSpy.reportResponseModel.reportTable);
        }

        @Test
        public void useCaseCreatesCorrectToolInResponse() {
            requestModel.selectedTechniques.add(techniqueSpy.getName());

            useCase.generateReport(requestModel, presenterSpy);

            assertEquals(toolSpy.getName(), presenterSpy.reportResponseModel.toolName);
        }

        @Test
        public void useCaseCreatesCorrectTechniqueInResponse() {
            requestModel.selectedTechniques.add(techniqueSpy.getName());

            useCase.generateReport(requestModel, presenterSpy);

            assertEquals(techniqueSpy.getName(), presenterSpy.reportResponseModel.techniqueName);
        }

        @Test
        public void useCaseCreatesPreparer() {
            requestModel.selectedTechniques.add(techniqueSpy.getName());

            useCase.generateReport(requestModel, presenterSpy);

            assertTrue(testContext.testPhaseFactorySpy.defaultPreparePhaseIsUsed);
        }

        @Test
        public void useCaseRuns_PrepareTools_SelectedTool_Techniques_CorrectNumberOfTimes() {
            requestModel.selectedTechniques.add(techniqueSpy.getName());

            useCase.generateReport(requestModel, presenterSpy);

            assertTrue(techniqueSpy.runMethodWasCalled());
            assertTrue(toolSpy.runMethodWasCalled());
            assertTrue(prepareToolSpy.runMethodWasCalled());
            assertEquals(1, techniqueSpy.wasRunHowManyTimes());
            assertEquals(1, toolSpy.wasRunHowManyTimes());
        }

        @Test
        public void toolsAreRunOnCorrectInputDirs() throws IOException {
            requestModel.sourceDirPath = new File("inputData");
            requestModel.selectedInputDir = new File("inputData" +File.separator+"NWTiS"+ File.separator + "2012-2013"+File.separator+"DZ1");
            requestModel.selectedInputDir.mkdirs();
            requestModel.selectedInputDirDepth = 3;
            requestModel.selectedTechniques.add(techniqueSpy.getName());

            useCase.generateReport(requestModel, presenterSpy);

            assertTrue(prepareToolSpy.runMethodWasCalled());
            assertEquals(1, prepareToolSpy.wasRunHowManyTimes());
            assertEquals(requestModel.sourceDirPath,testContext.testPhaseFactorySpy.sourceInputDir);
            assertEquals(new File(requestModel.selectedWorking+File.separator+"prepared"+File.separator
                    +prepareToolSpy.getName()+File.separator+"NWTiS"+ File.separator + "2012-2013"
                    +File.separator+"DZ1"),prepareToolSpy.getSoruceDir());

            DirectoryFileUtility.deleteDirectoryTree(requestModel.sourceDirPath);
        }

        @Test
        public void readerFoundMatchForTechniqueThatIsNotRequested() {
            matchReaderPhaseSpy.mpcMatches.add(new MPCMatchBuilder().withMatchesDirForTechnique("wrongTechniqueName").build());
            requestModel.selectedTechniques.add(techniqueSpy.getName());

            useCase.generateReport(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.noSuchTechnique+":wrongTechniqueName"+File.separator+MPCContext.MATCHES_DIR, presenterSpy.reportResponseModel.errorMessages);
        }

        @Test
        public void useCaseCreatesCorrectInfoForStudents() {
            MPCMatch match = matchReaderPhaseSpy.mpcMatches.get(0);

            requestModel.selectedTechniques.add(techniqueSpy.getName());

            useCase.generateReport(requestModel, presenterSpy);

            assertEquals(DirectoryFileUtility.getFileNameWithoutExtension(match.fileAName), presenterSpy.reportResponseModel.reportTable.get(0).studentA);
            assertEquals(DirectoryFileUtility.getFileNameWithoutExtension(match.fileBName), presenterSpy.reportResponseModel.reportTable.get(0).studentB);
            assertEquals(2, presenterSpy.reportResponseModel.reportTable.size());
            assertEquals(match.similarityA, presenterSpy.reportResponseModel.reportTable.get(0).similarityA, 0.001);
            assertEquals(match.similarityB, presenterSpy.reportResponseModel.reportTable.get(0).similarityB, 0.001);
            assertEquals(match.similarity, presenterSpy.reportResponseModel.reportTable.get(0).similarity, 0.001);
            assertEquals(match.calculatedSimilarity, presenterSpy.reportResponseModel.reportTable.get(0).calculatedSimilarity, 0.001);
            assertEquals(match.calculatedSimilarityA, presenterSpy.reportResponseModel.reportTable.get(0).calculatedSimilarityA, 0.001);
            assertEquals(match.calculatedSimilarityB, presenterSpy.reportResponseModel.reportTable.get(0).calculatedSimilarityB, 0.001);
            assertEquals(match.fileAName,presenterSpy.reportResponseModel.reportTable.get(0).fileAName);
            assertEquals(match.fileBName,presenterSpy.reportResponseModel.reportTable.get(0).fileBName);
            assertEquals(match.matchesDir,presenterSpy.reportResponseModel.matchesDirPath);
        }

        @Test
        public void useCaseCreatesProcessedPlagiarizedInfo() throws IOException {
            TextFileUtility textFileUtility = new TextFileUtility(StandardCharsets.UTF_8);
            File dir = new File(workingDir+File.separator+"analysis"+File.separator + "ppTestFile");
            dir.mkdirs();
            File processedFile = new File(dir+File.separator+MPCContext.PROCESSED_MATCH_FILE);
            File plagFile = new File(dir+File.separator+MPCContext.PLAG_MATCH_FILE);

            MPCMatch match = matchReaderPhaseSpy.mpcMatches.get(0);
            textFileUtility.createFileWithText(processedFile, match.fileAName+"|"+match.fileBName);
            textFileUtility.createFileWithText(plagFile, match.fileAName+"|"+match.fileBName);

            requestModel.selectedTechniques.add(techniqueSpy.getName());
            useCase.generateReport(requestModel, presenterSpy);

            assertEquals(2, presenterSpy.reportResponseModel.reportTable.size());
            assertTrue(presenterSpy.reportResponseModel.reportTable.get(0).processed);
            assertTrue(presenterSpy.reportResponseModel.reportTable.get(0).plagiarized);

            plagFile.delete();
            useCase.generateReport(requestModel, presenterSpy);
            assertEquals(2, presenterSpy.reportResponseModel.reportTable.size());
            assertTrue(presenterSpy.reportResponseModel.reportTable.get(0).processed);
            assertFalse(presenterSpy.reportResponseModel.reportTable.get(0).plagiarized);

            processedFile.delete();
            useCase.generateReport(requestModel, presenterSpy);
            assertEquals(2, presenterSpy.reportResponseModel.reportTable.size());
            assertFalse(presenterSpy.reportResponseModel.reportTable.get(0).processed);
            assertFalse(presenterSpy.reportResponseModel.reportTable.get(0).plagiarized);
        }

        @Test
        public void useCaseCallsDirectoryPreparerWithCorrectInputDirDepthAndDir(){
            requestModel.selectedTechniques.add(techniqueSpy.getName());
            requestModel.selectedInputDirDepth = 0;

            DirectoryPreparerSpy directoryPreparerSpy = new DirectoryPreparerSpy();
            useCase.setDirectoryPreparer(directoryPreparerSpy);
            useCase.generateReport(requestModel,presenterSpy);

            assertEquals(requestModel.selectedInputDirDepth,directoryPreparerSpy.inputDirDepth);
            assertEquals(requestModel.selectedInputDir,directoryPreparerSpy.selectedInputDir);
        }

        @Test
        public void useCaseCalulatesStatisticMeasures(){
            MPCMatch match = new MPCMatchBuilder().withSimilarity(20)
                    .withMatchesDirForTechnique(toolSpy.getName()+File.separator+techniqueSpy.getName())
                    .build();
            matchReaderPhaseSpy.mpcMatches.add(match);
            match = new MPCMatchBuilder().withSimilarity(80)
                    .withMatchesDirForTechnique(toolSpy.getName()+File.separator+techniqueSpy.getName())
                    .build();
            matchReaderPhaseSpy.mpcMatches.add(match);
            match = new MPCMatchBuilder().withSimilarity(30)
                    .withMatchesDirForTechnique(toolSpy.getName()+File.separator+techniqueSpy.getName())
                    .build();
            matchReaderPhaseSpy.mpcMatches.add(match);
            match = new MPCMatchBuilder().withSimilarity(90)
                    .withMatchesDirForTechnique(toolSpy.getName()+File.separator+techniqueSpy.getName())
                    .build();
            matchReaderPhaseSpy.mpcMatches.add(match);
            requestModel.selectedTechniques.add(techniqueSpy.getName());

            useCase.generateReport(requestModel, presenterSpy);

            assertEquals(6, presenterSpy.reportResponseModel.reportTable.size());
            assertNotNull(presenterSpy.reportResponseModel.similarityStatistics);
            assertEquals(70,presenterSpy.reportResponseModel.similarityStatistics.mean,0.01);
            assertEquals(1,presenterSpy.reportResponseModel.similarityStatistics.mode.length);
            assertEquals(100,presenterSpy.reportResponseModel.similarityStatistics.mode[0],0.01);
            assertEquals(20,presenterSpy.reportResponseModel.similarityStatistics.min,0.01);
            assertEquals(100,presenterSpy.reportResponseModel.similarityStatistics.max,0.01);
            assertEquals(85,presenterSpy.reportResponseModel.similarityStatistics.median,0.01);
            assertEquals(100,presenterSpy.reportResponseModel.similarityStatistics.q3,0.01);
            assertEquals(100,presenterSpy.reportResponseModel.similarityStatistics.percentile99,0.01);
            assertEquals(72.5,presenterSpy.reportResponseModel.similarityStatistics.IRQ,0.01);
            assertEquals(32.65,presenterSpy.reportResponseModel.similarityStatistics.STD,0.01);

        }
    }

    public class updatePlagInfoUseCaseTest {
        File workingDir = new File("workingDir");
        UpdatePlagInfoRequestModel requestModel = new UpdatePlagInfoRequestModel();

        @Before
        public void setUp() throws Exception {
            requestModel.fileAname = "fileA";
            requestModel.fileBname = "fileB";
            requestModel.workingDir = workingDir;
            requestModel.sourceDirPath = new File("testInputData");
            requestModel.selectedInputDirPath = new File(requestModel.sourceDirPath+File.separator+"ppTestFile");

            workingDir.mkdirs();

            useCase = new SummaryReportUseCase(new FactoryProvider(MPCContext.MATCHES_DIR));
        }

        @After
        public void tearDown() throws IOException {
            DirectoryFileUtility.deleteDirectoryTree(workingDir);
        }

        @Test
        public void analysisDirDoesNotExist(){
            useCase.updatePlagInfo(requestModel, presenterSpy);
            Assert.assertEquals(UseCaseResponseErrorMessages.invalidAnalysisDir+":"+workingDir+File.separator+MPCContext.ANALYSIS_DIR+File.separator+requestModel.selectedInputDirPath.getName(), presenterSpy.updatePlagInfoResponseModel.errorMessages);
        }

        @Test
        public void givenInvalidWorkingDirWriteResponse() {
            requestModel.workingDir = new File("invalid");

            useCase.updatePlagInfo(requestModel, presenterSpy);

            Assert.assertEquals(UseCaseResponseErrorMessages.invalidWorkingDir, presenterSpy.updatePlagInfoResponseModel.errorMessages);
        }

        @Test
        public void givenInvalidSourceDirWriteResponse() {
            requestModel.sourceDirPath = new File("invalid");

            useCase.updatePlagInfo(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.invalidSourceDir, presenterSpy.updatePlagInfoResponseModel.errorMessages);
        }

        @Test
        public void withSourceDirPathAndSelectedDirNotMatchig() {
            requestModel.selectedInputDirPath = new File("wrongSourceDirPath");

            useCase.updatePlagInfo(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.inputDirMismatch, presenterSpy.updatePlagInfoResponseModel.errorMessages);
        }

        @Test
        public void givenInvalidFileAnameWriteResponse() {
            requestModel.fileAname = "";
            useCase.updatePlagInfo(requestModel, presenterSpy);
            Assert.assertEquals(UseCaseResponseErrorMessages.noSelectedaStudentPairMatch, presenterSpy.updatePlagInfoResponseModel.errorMessages);

            requestModel.fileAname = null;
            useCase.updatePlagInfo(requestModel, presenterSpy);
            Assert.assertEquals(UseCaseResponseErrorMessages.noSelectedaStudentPairMatch, presenterSpy.updatePlagInfoResponseModel.errorMessages);
        }

        @Test
        public void givenInvalidFileBnameWriteResponse() {
            requestModel.fileBname = "";
            useCase.updatePlagInfo(requestModel, presenterSpy);
            Assert.assertEquals(UseCaseResponseErrorMessages.noSelectedaStudentPairMatch, presenterSpy.updatePlagInfoResponseModel.errorMessages);

            requestModel.fileBname = null;
            useCase.updatePlagInfo(requestModel, presenterSpy);
            Assert.assertEquals(UseCaseResponseErrorMessages.noSelectedaStudentPairMatch, presenterSpy.updatePlagInfoResponseModel.errorMessages);
        }

        public class sucesfullTestcases {

            public class withPreviousProcessedMatch extends noPreviousData {
                @Override
                public void setUp() throws Exception {
                    super.setUp();
                    plagMatchesReader.addProcessedMatch(requestModel.fileAname,requestModel.fileBname);
                }
            }

            public class withPreviousPPlagMatch extends noPreviousData {
                @Override
                public void setUp() throws Exception {
                    super.setUp();
                    plagMatchesReader.addPlagiarizedMatch(requestModel.fileAname,requestModel.fileBname);
                }
            }

            public class withPreviousProcessedAndPPlagMatch extends noPreviousData {
                @Override
                public void setUp() throws Exception {
                    super.setUp();
                    plagMatchesReader.addProcessedMatch(requestModel.fileAname,requestModel.fileBname);
                    plagMatchesReader.addPlagiarizedMatch(requestModel.fileAname,requestModel.fileBname);
                }
            }

            public class noPreviousData {
                PlagMatchesReader plagMatchesReader;

                @Before
                public void setUp() throws Exception {
                    plagMatchesReader = PlagMatchesReader.createPlagMatchReader(requestModel.sourceDirPath, requestModel.workingDir, requestModel.selectedInputDirPath);
                    plagMatchesReader.getDir().mkdirs();
                }

                @Test
                public void addProcessedMatchPlagiarized() {
                    requestModel.processed = true;
                    requestModel.plagiarized = true;

                    useCase.updatePlagInfo(requestModel, presenterSpy);

                    assertErrorMessageAndFileNames();
                    assertTrue(presenterSpy.updatePlagInfoResponseModel.processed);
                    assertTrue(presenterSpy.updatePlagInfoResponseModel.plagiarized);
                    assertTrue(presenterSpy.updatePlagInfoResponseModel.processedDisabled);
                    assertFalse(presenterSpy.updatePlagInfoResponseModel.plagiarizedDisabled);

                    plagMatchesReader.read();
                    assertTrue(plagMatchesReader.containsPair(requestModel.fileAname, requestModel.fileBname));
                    assertTrue(plagMatchesReader.containsPlagPair(requestModel.fileAname, requestModel.fileBname));
                }

                @Test
                public void addNotProcessedMatchPlagiarized() {
                    requestModel.processed = false;
                    requestModel.plagiarized = true;

                    useCase.updatePlagInfo(requestModel, presenterSpy);

                    assertErrorMessageAndFileNames();
                    assertTrue(presenterSpy.updatePlagInfoResponseModel.processed);
                    assertTrue(presenterSpy.updatePlagInfoResponseModel.plagiarized);
                    assertTrue(presenterSpy.updatePlagInfoResponseModel.processedDisabled);
                    assertFalse(presenterSpy.updatePlagInfoResponseModel.plagiarizedDisabled);

                    plagMatchesReader.read();
                    assertTrue(plagMatchesReader.containsPair(requestModel.fileAname, requestModel.fileBname));
                    assertTrue(plagMatchesReader.containsPlagPair(requestModel.fileAname, requestModel.fileBname));
                }

                @Test
                public void addProcessedMatchNotPlagiarized() {
                    requestModel.processed = true;
                    requestModel.plagiarized = false;

                    useCase.updatePlagInfo(requestModel, presenterSpy);

                    assertErrorMessageAndFileNames();
                    assertTrue(presenterSpy.updatePlagInfoResponseModel.processed);
                    assertFalse(presenterSpy.updatePlagInfoResponseModel.plagiarized);
                    assertFalse(presenterSpy.updatePlagInfoResponseModel.processedDisabled);
                    assertFalse(presenterSpy.updatePlagInfoResponseModel.plagiarizedDisabled);

                    plagMatchesReader.read();
                    assertTrue(plagMatchesReader.containsPair(requestModel.fileAname, requestModel.fileBname));
                    assertFalse(plagMatchesReader.containsPlagPair(requestModel.fileAname, requestModel.fileBname));
                }

                @Test
                public void addNotProcessedMatchNotPlagiarized() {
                    requestModel.processed = false;
                    requestModel.plagiarized = false;

                    useCase.updatePlagInfo(requestModel, presenterSpy);

                    assertErrorMessageAndFileNames();
                    assertFalse(presenterSpy.updatePlagInfoResponseModel.processed);
                    assertFalse(presenterSpy.updatePlagInfoResponseModel.plagiarized);
                    assertFalse(presenterSpy.updatePlagInfoResponseModel.processedDisabled);
                    assertFalse(presenterSpy.updatePlagInfoResponseModel.plagiarizedDisabled);

                    plagMatchesReader.read();
                    assertFalse(plagMatchesReader.containsPair(requestModel.fileAname, requestModel.fileBname));
                    assertFalse(plagMatchesReader.containsPlagPair(requestModel.fileAname, requestModel.fileBname));
                }

                private void assertErrorMessageAndFileNames() {
                    assertEquals("",presenterSpy.updatePlagInfoResponseModel.errorMessages);
                    assertEquals(requestModel.fileAname, presenterSpy.updatePlagInfoResponseModel.fileAname);
                    assertEquals(requestModel.fileBname, presenterSpy.updatePlagInfoResponseModel.fileBname);
                }
            }

        }
    }

    private class SummaryReportOutputBoundarySpy implements SummaryReportOutputBoundary {
        public UpdatePlagInfoResponseModel updatePlagInfoResponseModel;
        public SummaryReportResponseModel reportResponseModel;
        public AvailableToolsResponseModel availableTTResponseModel;
        public Map<String,File> dirList;

        @Override
        public void presentAvailableTools(AvailableToolsResponseModel responseModel) {
            availableTTResponseModel = responseModel;
        }

        @Override
        public void presentReport(SummaryReportResponseModel responseModel) {
            reportResponseModel = responseModel;
        }

        @Override
        public void presentDirList(Map<String,File> dirList) {
            this.dirList = dirList;
        }

        @Override
        public void presentUpdatedSummaryReportRowPlagInfo(UpdatePlagInfoResponseModel responseModel){
            updatePlagInfoResponseModel = responseModel;
        }
    }



}