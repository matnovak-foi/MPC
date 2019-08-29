package org.foi.mpc.usecases.multipleDetecion;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.directory.InMemoryDir;
import org.foi.mpc.MPCContext;
import org.foi.mpc.abstractfactories.FactoryProvider;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagJavaAdapter;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneJavaAdapter;
import org.foi.mpc.executiontools.techniques.CommonCodeRemoveTechnique;
import org.foi.mpc.executiontools.techniques.NoTechniqueOriginal;
import org.foi.mpc.executiontools.techniques.NoTechniqueOriginalTest;
import org.foi.mpc.phases.runner.PhaseRunner;
import org.foi.mpc.phases.runner.PhaseRunnerBuilder;
import org.foi.mpc.phases.runner.PhaseRunnerBuilderDummy;
import org.foi.mpc.phases.runner.PhaseRunnerBuilderStub;
import org.foi.mpc.usecases.UseCaseResponseErrorMessages;
import org.foi.mpc.usecases.multipleDetecion.models.MultipleDetectionRequestModel;
import org.foi.mpc.usecases.multipleDetecion.models.MultipleDetectionResponseModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(HierarchicalContextRunner.class)
public class MultipleDetectionUseCaseTest {
    MultipleDetectionRequestModel requestModel;
    MultipleDetectionUseCase useCase;
    MultipleDetectionPresenterSpy presenterSpy;
    File workingDir = new File("workingDir");

    @Before
    public void setUp() {
        workingDir.mkdir();

        requestModel = new MultipleDetectionRequestModel();
        requestModel.selectedWorkingDir = workingDir;
        requestModel.selectedInputDir = new File("testInputData" + File.separator + "multipleDetectionTest");
        requestModel.selectedTechniques = new ArrayList<>();
        requestModel.selectedTechniques.add(NoTechniqueOriginal.NAME);
        requestModel.selectedTechniques.add(CommonCodeRemoveTechnique.NAME);
        requestModel.selectedTools = new ArrayList<>();
        requestModel.selectedTools.add(JPlagJavaAdapter.NAME);
        requestModel.selectedTools.add(SimGruneJavaAdapter.NAME);
        requestModel.inputDirDepth = 3;

        useCase = new MultipleDetectionUseCase();
        useCase.setUpFactories(new FactoryProvider(MPCContext.MATCHES_DIR));
        presenterSpy = new MultipleDetectionPresenterSpy();
    }

    @After
    public void tearDown() throws IOException {
        DirectoryFileUtility.deleteDirectoryTree(workingDir);
    }

    @Test
    public void givenInvalidWorkingDirWriteResponse() {
        requestModel.selectedWorkingDir = new File("invalid");

        useCase.runMultipleDetecion(requestModel, presenterSpy);

        assertEquals(UseCaseResponseErrorMessages.invalidWorkingDir, presenterSpy.responseModel.errorMessages);
        assertNotNull(presenterSpy.responseModel.outputDirs);
    }


    @Test
    public void givenInvalidSourceDirWriteResponse() {
        requestModel.selectedInputDir = new File("invalid");

        useCase.runMultipleDetecion(requestModel, presenterSpy);

        assertEquals(UseCaseResponseErrorMessages.invalidSourceDir, presenterSpy.responseModel.errorMessages);
        assertNotNull(presenterSpy.responseModel.outputDirs);
    }

    @Test
    public void withNoToolSelected() {
        requestModel.selectedTools = new ArrayList<>();

        useCase.runMultipleDetecion(requestModel, presenterSpy);

        assertEquals(UseCaseResponseErrorMessages.noToolIsSelected, presenterSpy.responseModel.errorMessages);
        assertNotNull(presenterSpy.responseModel.outputDirs);
    }

    @Test
    public void withNoTechniqueSelected() {
        requestModel.selectedTechniques = new ArrayList<>();

        useCase.runMultipleDetecion(requestModel, presenterSpy);

        assertEquals(UseCaseResponseErrorMessages.noTechniqueIsSelected, presenterSpy.responseModel.errorMessages);
        assertNotNull(presenterSpy.responseModel.outputDirs);
    }

    @Test
    public void withInvalidDepthNumber() {
        requestModel.inputDirDepth = -1;

        useCase.runMultipleDetecion(requestModel, presenterSpy);

        assertEquals(UseCaseResponseErrorMessages.inputDirDepth, presenterSpy.responseModel.errorMessages);
        assertNotNull(presenterSpy.responseModel.outputDirs);
    }

    public class withActualPhaseRun {
        PhaseRunnerBuilderStub phaseRunnerBuilderStub;

        @Before
        public void setUp(){
            phaseRunnerBuilderStub = new PhaseRunnerBuilderStub(MPCContext.MATCHES_DIR);
            phaseRunnerBuilderStub.returnSomeMatch = false;
            useCase.setPhaseRunnerBuilder(phaseRunnerBuilderStub);
        }

        @Test
        public void createsCorrectPhaseRunner() {
            useCase.runMultipleDetecion(requestModel, presenterSpy);

            assertTrue(phaseRunnerBuilderStub.phaseRunnerWasRun);
            assertEquals(requestModel.selectedInputDir, phaseRunnerBuilderStub.sourceDir);
            assertEquals(requestModel.selectedWorkingDir, phaseRunnerBuilderStub.workingDir);
            assertEquals(2, phaseRunnerBuilderStub.toolList.size());
            assertEquals(requestModel.selectedTools.get(0), phaseRunnerBuilderStub.toolList.get(0).getName());
            assertEquals(requestModel.selectedTools.get(1), phaseRunnerBuilderStub.toolList.get(1).getName());
            assertEquals(2, phaseRunnerBuilderStub.techniqueList.size());
            assertEquals(requestModel.selectedTechniques.get(0), phaseRunnerBuilderStub.techniqueList.get(0).getName());
            assertEquals(requestModel.selectedTechniques.get(1), phaseRunnerBuilderStub.techniqueList.get(1).getName());
            assertEquals(4, phaseRunnerBuilderStub.getDirsToProcess().size());
        }

        @Test
        public void enablesCorrectPhasesToRun() {
            useCase.runMultipleDetecion(requestModel, presenterSpy);

            assertTrue(phaseRunnerBuilderStub.phaseRunnerWasRun);
            assertTrue(phaseRunnerBuilderStub.runnerSettings.runPreparePhase);
            assertTrue(phaseRunnerBuilderStub.runnerSettings.runPreprocessPhase);
            assertTrue(phaseRunnerBuilderStub.runnerSettings.runDetectionPhase);
            assertFalse(phaseRunnerBuilderStub.runnerSettings.runMPCMatchReadPhase);
        }

        @Test
        public void responseIsOkWhenInputIsOk() {
            phaseRunnerBuilderStub.fakeDetectionOutputDirs.add(new File("fakeOutputDir1"));
            phaseRunnerBuilderStub.fakeDetectionOutputDirs.add(new File("fakeOutputDir2"));
            useCase.runMultipleDetecion(requestModel, presenterSpy);

            assertEquals("", presenterSpy.responseModel.errorMessages);
            assertEquals(phaseRunnerBuilderStub.fakeDetectionOutputDirs,presenterSpy.responseModel.outputDirs);
        }
    }
    public class ExtractsCorrectDirsToProcess {
        InMemoryDir inputDir = new InMemoryDir("inputDir");
        InMemoryDir NWTiS = new InMemoryDir("NWTiS");
        InMemoryDir DZ1 = new InMemoryDir("DZ1");
        InMemoryDir DZ2 = new InMemoryDir("DZ2");
        InMemoryDir DZ3 = new InMemoryDir("DZ3");
        InMemoryDir DZ4 = new InMemoryDir("DZ4");
        InMemoryDir year1 = new InMemoryDir("2010-2011");
        InMemoryDir year2 = new InMemoryDir("2011-2012");

        @Test
        public void worksWithDepth0() {
            List<File> dirsToProcess = useCase.createDirsToPorces(inputDir, 0);

            assertEquals(1, dirsToProcess.size());
            assertEquals(dirsToProcess.get(0).getPath(), "inputDir");
        }

        @Test
        public void worksWithDepth1() {
            inputDir.addFile(DZ1);
            inputDir.addFile(DZ2);

            List<File> dirsToProcess = useCase.createDirsToPorces(inputDir, 1);

            assertEquals(2, dirsToProcess.size());
            assertEquals(dirsToProcess.get(0).getPath(), "DZ1");
            assertEquals(dirsToProcess.get(1).getPath(), "DZ2");
        }

        @Test
        public void worksWithDepth2() {
            year1.addFile(DZ1);
            year1.addFile(DZ2);
            year2.addFile(DZ3);
            year2.addFile(DZ4);
            inputDir.addFile(year1);
            inputDir.addFile(year2);

            List<File> dirsToProcess = useCase.createDirsToPorces(inputDir, 2);

            assertEquals(4, dirsToProcess.size());
            assertEquals(dirsToProcess.get(0).getPath(), "DZ1");
            assertEquals(dirsToProcess.get(1).getPath(), "DZ2");
            assertEquals(dirsToProcess.get(2).getPath(), "DZ3");
            assertEquals(dirsToProcess.get(3).getPath(), "DZ4");
        }

        @Test
        public void worksWithDepth3() {
            year1.addFile(DZ1);
            year1.addFile(DZ2);
            year2.addFile(DZ3);
            year2.addFile(DZ4);
            NWTiS.addFile(year1);
            NWTiS.addFile(year2);
            inputDir.addFile(NWTiS);

            List<File> dirsToProcess = useCase.createDirsToPorces(inputDir, 3);

            assertEquals(4, dirsToProcess.size());
            assertEquals(dirsToProcess.get(0).getPath(), "DZ1");
            assertEquals(dirsToProcess.get(1).getPath(), "DZ2");
            assertEquals(dirsToProcess.get(2).getPath(), "DZ3");
            assertEquals(dirsToProcess.get(3).getPath(), "DZ4");
        }
    }

    private class MultipleDetectionPresenterSpy implements MultipleDetectionOutputBoundary {
        MultipleDetectionResponseModel responseModel;

        @Override
        public void presentResults(MultipleDetectionResponseModel responseModel) {
            this.responseModel = responseModel;
        }

    }
}
