package org.foi.mpc.usecases.reports.pptestreport;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.foi.mpc.MPCContext;
import org.foi.mpc.TestContext;
import org.foi.mpc.abstractfactories.FactoryProvider;
import org.foi.mpc.matches.models.MPCMatchBuilder;
import org.foi.mpc.executiontools.factories.SimilarityDetectionToolFactory;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.mpc.phases.readerphase.MPCMatchReaderPhaseSpy;
import org.foi.mpc.usecases.UseCaseResponseErrorMessages;
import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.executiontools.spies.PrepareToolSpy;
import org.foi.mpc.executiontools.spies.PreprocessingTechniqueSpy;
import org.foi.mpc.executiontools.spies.SimilarityDetectionToolSpy;
import org.foi.mpc.usecases.reports.DirectoryPreparerSpy;
import org.foi.mpc.usecases.reports.avalibleTools.models.AvailableToolsResponseModel;
import org.foi.mpc.usecases.reports.pptestreport.models.PPTestReportRequestModel;
import org.foi.mpc.usecases.reports.pptestreport.models.PPTestReportResponseModel;
import org.junit.*;
import org.junit.runner.RunWith;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(HierarchicalContextRunner.class)
public class PPTestReportUseCaseTest {
    PPTestReportUseCase useCase;
    PPTestReportOutputBoundarySpy presenterSpy;

    @Before
    public void setUp(){
        presenterSpy = new PPTestReportOutputBoundarySpy();
    }

    @Test
    public void isPPTestReportInputBoudary() {
        assertTrue(new PPTestReportUseCase(new FactoryProvider(MPCContext.MATCHES_DIR), MPCContext.MATCHES_DIR) instanceof PPTestReportInputBoundary);
    }

    @Test
    public void useCaseReturnsAvailableToolsOnGetAvailableTools(){
        AvailableToolsResponseModel availableTTResponseModel = new AvailableToolsResponseModel();
        availableTTResponseModel.tools = new SimilarityDetectionToolFactory(MPCContext.MATCHES_DIR).getAvailableTools();

        useCase = new PPTestReportUseCase(new FactoryProvider(MPCContext.MATCHES_DIR), MPCContext.MATCHES_DIR);
        useCase.getAvailableTools(presenterSpy);

        assertEquals(availableTTResponseModel,presenterSpy.availableTTResponseModel);
    }



    public class generateReportUseCaseTest {
        TestContext testContext;

        SimilarityDetectionToolSpy toolSpy;
        PreprocessingTechniqueSpy techniqueSpy;
        PreprocessingTechniqueSpy techniqueSpy2;
        PrepareToolSpy prepareToolSpy;
        MPCMatchReaderPhaseSpy matchReaderPhaseSpy;

        PPTestReportRequestModel requestModel;
        File workingDir = new File("workingDir");

        @Before
        public void setUp() {
            testContext = new TestContext();
            toolSpy = testContext.getToolSpy();
            techniqueSpy = new PreprocessingTechniqueSpy();
            techniqueSpy2 = new PreprocessingTechniqueSpy();
            testContext.setTechniqueSpy(techniqueSpy);
            testContext.setTechniqueSpy(techniqueSpy2);

            requestModel = new PPTestReportRequestModel();
            requestModel.selectedTechniques = new ArrayList<>();
            requestModel.selectedInputDir = new File("testInputData" + File.separator + "ppTestFile");
            requestModel.selectedWorking = workingDir;
            requestModel.selectedTools =  new ArrayList<>();
            requestModel.selectedTools.add(toolSpy.getName());

            prepareToolSpy = testContext.getPrepareToolSpy();
            matchReaderPhaseSpy = testContext.matchReaderPhaseSpy;
            MPCMatch match = new MPCMatchBuilder().with100Similarity()
                    .withMatchesDirForTechnique(techniqueSpy.getName())
                    .build();
            MPCMatch match2 = new MPCMatchBuilder().with100Similarity()
                    .withMatchesDirForTechnique(techniqueSpy2.getName())
                    .build();
            matchReaderPhaseSpy.mpcMatches.add(match);
            matchReaderPhaseSpy.mpcMatches.add(match2);

            useCase = new PPTestReportUseCase(testContext.factoryProvider, MPCContext.MATCHES_DIR);

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
            assertNotNull(presenterSpy.reportResponseModel.ppReportTable);
        }

        @Test
        public void givenInvalidSourceDirWriteResponse() {
            requestModel.selectedInputDir = new File("invalid");

            useCase.generateReport(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.invalidSourceDir, presenterSpy.reportResponseModel.errorMessages);
            assertNotNull(presenterSpy.reportResponseModel.ppReportTable);
        }

        @Test
        public void withNoToolSelected() {
            requestModel.selectedTools = new ArrayList<>();

            useCase.generateReport(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.noToolIsSelected, presenterSpy.reportResponseModel.errorMessages);
            assertNotNull(presenterSpy.reportResponseModel.ppReportTable);
        }

        @Test
        public void withMultipleToolsSelected(){
            requestModel.selectedTools.add(toolSpy.getName());
            SimilarityDetectionToolSpy toolSpy2 = new SimilarityDetectionToolSpy();
            requestModel.selectedTechniques.add(toolSpy2.getName());

            useCase.generateReport(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.toManyToolsSelected, presenterSpy.reportResponseModel.errorMessages);
            assertNotNull(presenterSpy.reportResponseModel.ppReportTable);
        }

        @Test
        public void withNoTechniqueSelected() {
            requestModel.selectedTechniques = new ArrayList<>();

            useCase.generateReport(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.noTechniqueIsSelected, presenterSpy.reportResponseModel.errorMessages);
            assertNotNull(presenterSpy.reportResponseModel.ppReportTable);
        }

        @Test
        public void responseModelIsPassedToThePresenter() {
            requestModel.selectedTechniques.add(techniqueSpy.getName());

            useCase.generateReport(requestModel, presenterSpy);

            PPTestReportResponseModel responseModel = presenterSpy.reportResponseModel;
            assertEquals(requestModel.selectedTools.get(0), responseModel.toolName);
        }

        @Test
        public void useCaseCreatesCorrectToolInResponse() {
            requestModel.selectedTechniques.add(techniqueSpy.getName());

            useCase.generateReport(requestModel, presenterSpy);

            assertEquals(toolSpy.getName(), presenterSpy.reportResponseModel.toolName);
        }

        @Test
        public void useCaseCreatesPreparer() {
            requestModel.selectedTechniques.add(techniqueSpy.getName());

            useCase.generateReport(requestModel, presenterSpy);

            assertTrue(testContext.testPhaseFactorySpy.defaultPreparePhaseIsUsed);
        }

        @Test
        public void useCaseCreatesCorrectTechniquesInResponse() {
            requestModel.selectedTechniques.add(techniqueSpy.getName());
            requestModel.selectedTechniques.add(techniqueSpy2.getName());

            useCase.generateReport(requestModel, presenterSpy);

            assertThat(presenterSpy.reportResponseModel.ppReportTable, hasSize(2));
            assertEquals(techniqueSpy.getName(), presenterSpy.reportResponseModel.ppReportTable.get(0).name);
            assertEquals(techniqueSpy2.getName(), presenterSpy.reportResponseModel.ppReportTable.get(1).name);
        }

        @Test
        public void useCaseReturnsErrorBecouseMatchDirNameIsDifferentFromAnyTechnique() {
            matchReaderPhaseSpy.numberOfMatches = 2;
            requestModel.selectedTechniques.add(techniqueSpy2.getName());

            useCase.generateReport(requestModel, presenterSpy);

            assertThat(presenterSpy.reportResponseModel.ppReportTable, hasSize(2));
            assertEquals(UseCaseResponseErrorMessages.noSuchTechnique, presenterSpy.reportResponseModel.ppReportTable.get(0).name);
        }

        @Test
        public void useCaseRuns_PrepareTools_SelectedTool_Techniques_CorrectNumberOfTimes() {
            requestModel.selectedTechniques.add(techniqueSpy.getName());
            requestModel.selectedTechniques.add(techniqueSpy2.getName());

            useCase.generateReport(requestModel, presenterSpy);

            assertTrue(techniqueSpy.runMethodWasCalled());
            assertTrue(toolSpy.runMethodWasCalled());
            assertTrue(prepareToolSpy.runMethodWasCalled());
            assertEquals(1, techniqueSpy.wasRunHowManyTimes());
            assertEquals(1, techniqueSpy2.wasRunHowManyTimes());
            assertEquals(2, toolSpy.wasRunHowManyTimes());
        }

        @Test
        public void useCaseCreatesCorrectInfoForTechniques() {
            MPCMatch match = matchReaderPhaseSpy.mpcMatches.get(0);
            matchReaderPhaseSpy.numberOfMatches = 2;
            requestModel.selectedTechniques.add(techniqueSpy.getName());
            requestModel.selectedTechniques.add(techniqueSpy2.getName());

            useCase.generateReport(requestModel, presenterSpy);

            assertEquals(DirectoryFileUtility.getFileNameWithoutExtension(match.fileAName), presenterSpy.reportResponseModel.usernameA);
            assertEquals(DirectoryFileUtility.getFileNameWithoutExtension(match.fileBName), presenterSpy.reportResponseModel.usernameB);
            assertEquals(2, presenterSpy.reportResponseModel.ppReportTable.size());
            assertEquals(match.similarityB, presenterSpy.reportResponseModel.ppReportTable.get(0).similarityB, 0.001);
            assertEquals(match.similarity, presenterSpy.reportResponseModel.ppReportTable.get(0).similarity, 0.001);
        }

        @Test
        public void useCaseWorksOkWithTechniquesContainingOtherTechniqueName() {
            techniqueSpy.setName("SomeName");
            techniqueSpy2.setName("SomeNameOther");
            PreprocessingTechniqueSpy techniqueSpy3 = new PreprocessingTechniqueSpy();
            techniqueSpy3.setName("OtherSomeName");
            testContext.setTechniqueSpy(techniqueSpy);
            testContext.setTechniqueSpy(techniqueSpy2);
            testContext.setTechniqueSpy(techniqueSpy3);

            matchReaderPhaseSpy.mpcMatches = new ArrayList<>();
            matchReaderPhaseSpy.mpcMatches.add(new MPCMatchBuilder().withMatchesDirForTechnique(techniqueSpy.getName()).build());
            matchReaderPhaseSpy.mpcMatches.add(new MPCMatchBuilder().withMatchesDirForTechnique(techniqueSpy2.getName()).build());
            matchReaderPhaseSpy.mpcMatches.add(new MPCMatchBuilder().withMatchesDirForTechnique(techniqueSpy3.getName()).build());
            matchReaderPhaseSpy.mpcMatches.add(new MPCMatchBuilder().withMatchesDirForTechnique("dir" + File.separator + techniqueSpy3.getName()).build());


            requestModel.selectedTechniques.add(techniqueSpy.getName());
            requestModel.selectedTechniques.add(techniqueSpy2.getName());
            requestModel.selectedTechniques.add(techniqueSpy3.getName());

            useCase.generateReport(requestModel, presenterSpy);

            assertEquals("SomeName", presenterSpy.reportResponseModel.ppReportTable.get(0).name);
            assertEquals("SomeNameOther", presenterSpy.reportResponseModel.ppReportTable.get(1).name);
            assertEquals("OtherSomeName", presenterSpy.reportResponseModel.ppReportTable.get(2).name);
            assertEquals("OtherSomeName", presenterSpy.reportResponseModel.ppReportTable.get(3).name);
        }

        @Test
        public void readerFoundMatchForTechniqueThatIsNotRequested() {
            matchReaderPhaseSpy.mpcMatches.add(new MPCMatchBuilder().withMatchesDirForTechnique("wrongTechniqueName").build());
            requestModel.selectedTechniques.add(techniqueSpy.getName());

            useCase.generateReport(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.noSuchTechnique, presenterSpy.reportResponseModel.ppReportTable.get(2).name);
        }

        @Test
        public void useCaseCallsDirectoryPreparerWithCorrectInputDirDepthAndDir(){
            requestModel.selectedTechniques.add(techniqueSpy.getName());

            DirectoryPreparerSpy directoryPreparerSpy = new DirectoryPreparerSpy();
            useCase.setDirectoryPreparer(directoryPreparerSpy);
            useCase.generateReport(requestModel,presenterSpy);

            assertEquals(0,directoryPreparerSpy.inputDirDepth);
            assertEquals(requestModel.selectedInputDir,directoryPreparerSpy.selectedInputDir);
        }
    }

    private class PPTestReportOutputBoundarySpy implements PPTestReportOutputBoundary {
        PPTestReportResponseModel reportResponseModel;
        AvailableToolsResponseModel availableTTResponseModel;


        @Override
        public void presentReport(PPTestReportResponseModel responseModel) {
            this.reportResponseModel = responseModel;
        }

        @Override
        public void presentAvailableTools(AvailableToolsResponseModel responseModel) {
            this.availableTTResponseModel = responseModel;
        }


    }
}
