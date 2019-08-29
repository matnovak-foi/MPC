package org.foi.mpc.phases.executionphases;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.foi.mpc.MPCContext;
import org.foi.common.MPCexcpetions;
import org.foi.common.filesystem.directory.DirectoryFileUtility;

import static org.foi.mpc.phases.executionphases.ExecutionToolAssertHelper.assertToolWasRunNTimes;

import static org.foi.common.filesystem.directory.DirectoryCreator.assertDirHasFilesCount;
import static org.foi.common.filesystem.directory.DirectoryCreator.assertDirectoryIsEmpty;

import org.foi.mpc.phases.PhaseFactory;
import org.foi.mpc.executiontools.spies.PreprocessingTechniqueSpy;
import org.foi.mpc.executiontools.spies.SimilarityDetectionToolSpy;
import org.foi.mpc.phases.executionphases.spies.ExecutionToolSpy;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(HierarchicalContextRunner.class)
public class IndependentExecutionPhaseTest {
    
    List<ExecutionTool> tools;
    protected File sourceDirectory;
    protected File workingDirectory;
    protected ExecutionPhase similarityDetector;
    protected List<File> dirsToProcess;
    private PhaseFactory phaseFactory = new PhaseFactory(MPCContext.MATCHES_DIR);

    @Before
    public void setUp() {
        workingDirectory = new File("workingdir");
        workingDirectory.mkdir();
        sourceDirectory = new File("sourceDir");
        sourceDirectory.mkdir();
        similarityDetector = phaseFactory.createSimilarityDetector(workingDirectory, sourceDirectory);
        dirsToProcess = new ArrayList<>();
        tools = new ArrayList<>();
        similarityDetector.setExecutionTools(tools);
        similarityDetector.setDirsToProcess(dirsToProcess);
    }

    @After
    public void tearDown() throws IOException {
        DirectoryFileUtility.deleteDirectoryTree(sourceDirectory);
        DirectoryFileUtility.deleteDirectoryTree(workingDirectory);
    }

    public class withFixedDirsAndNoRealCopy {

        @Test
        public void inputDirectoryIsEmpty_workingDirHasEmptyPreprocessDir() {
            similarityDetector.runPhase();
            File preporcessDir = new File(workingDirectory + File.separator + "detection");
            assertTrue(preporcessDir.exists());
            assertDirectoryIsEmpty(preporcessDir);
        }

        @Test(expected = MPCexcpetions.IsNullException.class)
        public void workingDirListIsNull() {
            similarityDetector.setDirsToProcess(null);
            similarityDetector.runPhase();
        }

        @Test(expected = MPCexcpetions.IsNullException.class)
        public void executeWithListOfToolsIsNull() {
            similarityDetector.setExecutionTools(null);
            similarityDetector.runPhase();
        }

        @Test
        public void workingDirListIsEmpty() {
            similarityDetector.runPhase();
        }

        @Test
        public void executionToolListIsEmpty() {
            dirsToProcess.add(new File("toolDir"));
            similarityDetector.runPhase();
        }

        @Test
        public void runWasDoneByMultipleToolsAndMultipleDirs() {
            int numberOfToolsAndElements = 4;
            for (int i = 1; i <= numberOfToolsAndElements; i++) {
                File newDir = new File(sourceDirectory+File.separator+"dirToProcessDir" + i);
                newDir.mkdir();
                dirsToProcess.add(newDir);
                tools.add(new SimilarityDetectionToolSpy());
            }
            similarityDetector.setExecutionTools(tools);
            similarityDetector.setDirsToProcess(dirsToProcess);
            similarityDetector.runPhase();

            for (ExecutionTool ptSpy : tools) {
                assertToolWasRunNTimes((ExecutionToolSpy) ptSpy, numberOfToolsAndElements);
            }
        }
    }

    public class withPreprocessor {
        protected ExecutionPhase asignementPreprocesser;
        List<ExecutionTool> techniques;

        @Before
        public void setUp() {
            asignementPreprocesser = phaseFactory.createAssignmentsPreprocessor(workingDirectory, sourceDirectory);
            techniques = new ArrayList<>();
            asignementPreprocesser.setExecutionTools(techniques);
        }

        @Test
        public void runWasDoneByMultipleToolsAndMultipleDirs() {
            int numberOfToolsAndElements = 4;
            for (int i = 1; i <= numberOfToolsAndElements; i++) {
                File newDir = new File(sourceDirectory+File.separator+"dirToProcessDir" + i);
                newDir.mkdir();
                dirsToProcess.add(newDir);
                techniques.add(new PreprocessingTechniqueSpy());
            }
            asignementPreprocesser.setExecutionTools(techniques);
            asignementPreprocesser.setDirsToProcess(dirsToProcess);
            asignementPreprocesser.runPhase();

            assertDirHasFilesCount(numberOfToolsAndElements, new File(workingDirectory+File.separator+"preprocess"));
            for (ExecutionTool ptSpy : techniques) {
                assertDirHasFilesCount(numberOfToolsAndElements, new File(workingDirectory+File.separator+"preprocess"+File.separator+ptSpy.getName()));
                assertToolWasRunNTimes((ExecutionToolSpy) ptSpy, numberOfToolsAndElements);
            }
        }
    }
}
