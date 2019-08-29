package org.foi.mpc.phases.executionphases;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.foi.common.MPCexcpetions;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.file.TextFileUtility;
import org.foi.mpc.phases.executionphases.spies.ExecutionToolSpy;
import org.foi.mpc.phases.executionphases.spies.ExecutionToolWithFileSpy;
import org.foi.common.filesystem.directory.DirectoryCreator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.foi.common.filesystem.directory.DirectoryCreator.assertDirHasFilesCount;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

@RunWith(HierarchicalContextRunner.class)
public class DirectoryToolIteratorTest {

    protected File sourceDirectory;
    protected File workingDirectory;
    protected DirectoryToolIterator dirToolIterator;
    protected List<File> dirsToProcess;
    protected List<ExecutionTool> executionTools;

    @Before
    public void setUp() {    
        workingDirectory = new File("workingdir");
        workingDirectory.mkdir();
        sourceDirectory = new File("sourcedir");
        sourceDirectory.mkdir();

        dirsToProcess = new ArrayList<>();
        executionTools = new ArrayList<>();

        dirToolIterator = new DirectoryToolIterator(workingDirectory,sourceDirectory);
    }
    
    @After
    public void tearDown() throws IOException{
        DirectoryFileUtility.deleteDirectoryTree(sourceDirectory);
        DirectoryFileUtility.deleteDirectoryTree(workingDirectory);
    }

    public class withFixedDirsAndNoRealCopy {

        protected int numberOfToolsAndElements;

        @Before
        public void setUp() {
            dirToolIterator = new DirectoryToolIteratorFakeWithNoRealDirCopy(workingDirectory, sourceDirectory);
        }

        @Test(expected = MPCexcpetions.IsNullException.class)
        public void workingDirListIsNull() {
            dirToolIterator.setDirsToProcess(null);
            dirToolIterator.iterateToolsIndependent(executionTools);
        }

        @Test(expected = MPCexcpetions.IsNullException.class)
        public void executeWithListOfToolsIsNull() {
            dirToolIterator.iterateToolsIndependent(null);
        }

        @Test
        public void workingDirListIsEmpty() {
            dirToolIterator.setDirsToProcess(dirsToProcess);
            dirToolIterator.iterateToolsIndependent(executionTools);
            assertEquals(0, dirToolIterator.getProcessedDirsCount());
        }

        @Test
        public void executionToolListIsEmpty() {
            dirsToProcess = new ArrayList<>();
            dirsToProcess.add(new File("toolDir"));
            dirToolIterator.setDirsToProcess(dirsToProcess);
            dirToolIterator.iterateToolsIndependent(executionTools);
            assertEquals(0, dirToolIterator.getProcessedDirsCount());
        }

        @Test
        public void processListWithMultipleElementsOnOneTool() {
            ExecutionTool tSpy = new ExecutionToolSpy();
            executionTools.add(tSpy);
            int numbreOfIterations = 20;
            for (int dirNumber = 1; dirNumber <= numbreOfIterations; dirNumber++) {
                dirsToProcess.add(new File("toolDir" + dirNumber));
                dirToolIterator.setDirsToProcess(dirsToProcess);
                dirToolIterator.iterateToolsIndependent(executionTools);
                int expectedNumbrerOfProcessedDirs = dirNumber;
                int expectedNumbrerOfToolRunned = dirNumber;

                assertEquals(expectedNumbrerOfProcessedDirs, dirToolIterator.getProcessedDirsCount());
                ExecutionToolSpy toolSpy = (ExecutionToolSpy) executionTools.get(0);
                assertToolWasRunNTimes(toolSpy, expectedNumbrerOfToolRunned);
            }
        }

        @Test
        public void runWasCalledOnTwoDiferentToolsForEveryDirectory() {
            ExecutionTool tSpy1 = new ExecutionToolSpy();
            executionTools.add(tSpy1);
            ExecutionTool tSpy2 = new ExecutionToolSpy();
            executionTools.add(tSpy2);
            int numberOfElements = 100;
            for (int i = 1; i <= numberOfElements; i++) {
                dirsToProcess.add(new File("toolDir" + i));
                dirToolIterator.setDirsToProcess(dirsToProcess);
                dirToolIterator.iterateToolsIndependent(executionTools);
                ExecutionToolSpy toolSpy1 = (ExecutionToolSpy) tSpy1;
                ExecutionToolSpy toolSpy2 = (ExecutionToolSpy) tSpy2;
                assertToolWasRunNTimes(toolSpy1, i);
                assertToolWasRunNTimes(toolSpy2, i);
            }
        }

        @Test
        public void runWasDoneByMultipleToolsAndMultipleDirs() {
            numberOfToolsAndElements = 20;
            for (int i = 1; i <= numberOfToolsAndElements; i++) {
                dirsToProcess.add(new File("dirToProcessDir" + i));
                executionTools.add(new ExecutionToolSpy());
            }
            dirToolIterator.setDirsToProcess(dirsToProcess);
            dirToolIterator.iterateToolsIndependent(executionTools);

            int numberOfProcessedDirs = numberOfToolsAndElements * numberOfToolsAndElements;

            assertEquals(numberOfProcessedDirs, dirToolIterator.getProcessedDirsCount());
            for (ExecutionTool etSpy : executionTools) {
                assertToolWasRunNTimes((ExecutionToolSpy) etSpy, numberOfToolsAndElements);
            }
        }

        @Test
        public void sourceDirsForVariousToolsAreOk() {
            executionTools.add(new ExecutionToolSpy());
            executionTools.add(new ExecutionToolSpy());
            executionTools.add(new ExecutionToolSpy());
            dirsToProcess.add(new File(sourceDirectory + File.separator + "toolDir"));
            dirToolIterator.setDirsToProcess(dirsToProcess);
            dirToolIterator.iterateToolsIndependent(executionTools);
            for (ExecutionTool tool : executionTools) {
                ExecutionToolSpy toolSpy = (ExecutionToolSpy) tool;
                Path correctSourceDir = Paths.get(workingDirectory.getPath(), tool.getName(), "toolDir");
                assertEquals(correctSourceDir.toFile(), toolSpy.getSoruceDir());
            }
        }

        @Test
        public void ifToolHasExecutedOnDatasetThenSkipIt(){
            ExecutionToolSpy spy = new ExecutionToolSpy();
            executionTools.add(spy);
            File processsedToolDir = new File(workingDirectory + File.separator + spy.getName() + File.separator + "toolDir"+ File.separator + "procesedFilesAndDirs");
            processsedToolDir.mkdirs();
            dirsToProcess.add(new File(sourceDirectory + File.separator + "toolDir"));
            dirToolIterator.setDirsToProcess(dirsToProcess);
            dirToolIterator.iterateToolsIndependent(executionTools);
            for (ExecutionTool tool : executionTools) {
                ExecutionToolSpy toolSpy = (ExecutionToolSpy) tool;
                assertNull(toolSpy.getSoruceDir());
            }
            assertEquals(1,dirToolIterator.getProcessedDirsCount());
            assertThat(dirToolIterator.getProcessedDirs(),hasSize(1));
            assertEquals(processsedToolDir.getParentFile(),dirToolIterator.getProcessedDirs().get(0));
        }

        @Test
        public void ifToolDirExistsButNotExecutedWorkNormaly(){
            ExecutionToolSpy spy = new ExecutionToolSpy();
            executionTools.add(spy);
            File unprocessedToolDir = new File(workingDirectory + File.separator + spy.getName() + File.separator + "toolDir");
            unprocessedToolDir.mkdirs();
            dirsToProcess.add(new File(sourceDirectory + File.separator + "toolDir"));
            dirToolIterator.setDirsToProcess(dirsToProcess);
            dirToolIterator.iterateToolsIndependent(executionTools);
            for (ExecutionTool tool : executionTools) {
                ExecutionToolSpy toolSpy = (ExecutionToolSpy) tool;
                Path correctSourceDir = Paths.get(workingDirectory.getPath(), tool.getName(), "toolDir");
                assertEquals(correctSourceDir.toFile(), toolSpy.getSoruceDir());
            }
        }
    }

    public class withRealFilesCopy {

        @Test(expected = DirectoryFileUtility.DirCopyException.class)
        public void wrongDirToProcessCopyFailed() {
            ExecutionTool tSpy = new ExecutionToolSpy();
            executionTools.add(tSpy);
            dirsToProcess.add(new File("nonExistingDir"));
            dirToolIterator.setDirsToProcess(dirsToProcess);
            dirToolIterator.iterateToolsIndependent(executionTools);
        }

        @Test
        public void actualCopyOfSourceDirWasPerformedParalel() throws InterruptedException, IOException {
            ExecutionTool tSpy1 = new ExecutionToolSpy();
            ExecutionTool tSpy2 = new ExecutionToolSpy();
            executionTools.add(tSpy1);
            executionTools.add(tSpy2);
            DirectoryCreator dc = new DirectoryCreator();
            dc.createTestDirStructure(sourceDirectory, DirectoryCreator.DirTypes.ppTechnique);
            dirToolIterator.setDirsToProcess(dc.getCreatedAsignementDirs());
            dirToolIterator.iterateToolsIndependent(executionTools);

            Path toolTargeteDir1 = Paths.get(workingDirectory.getPath(), tSpy1.getName());
            Path toolTargeteDir2 = Paths.get(workingDirectory.getPath(), tSpy2.getName());

            assertDirHasFilesCount(2, workingDirectory);
            dc.assertThatLastCreatedDirStructureIsOkInTargetDir(toolTargeteDir1.toFile());
            dc.assertThatLastCreatedDirStructureIsOkInTargetDir(toolTargeteDir2.toFile());
        }

        public class sequentialOrIndividaulToolCalls {

            private File finalTestFile;
            private TextFileUtility tfu = new TextFileUtility(StandardCharsets.UTF_8);
            ExecutionTool pts3;
            File oneDir;

            @Before
            public void setup() throws IOException {
                ExecutionToolWithFileSpy.callOrder = "";
                ExecutionTool pts1 = new ExecutionToolWithFileSpy("1");
                ExecutionTool pts2 = new ExecutionToolWithFileSpy("2");
                pts3 = new ExecutionToolWithFileSpy("3");
                executionTools.add(pts1);
                executionTools.add(pts2);
                executionTools.add(pts3);
                oneDir = new File(sourceDirectory + File.separator + "oneDir");
                oneDir.mkdir();
                File testFile = new File(oneDir.getPath() + File.separator + "testFile.txt");
                tfu.createFileWithText(testFile, "0");
                dirsToProcess.add(oneDir);
                finalTestFile = new File(workingDirectory.getPath()    + File.separator
                                                            + pts3.getName()   + File.separator
                                                            + oneDir.getName() + File.separator
                                                            + testFile.getName());
                dirToolIterator.setDirsToProcess(dirsToProcess);
            }

            @Test
            public void actualCopyOfSourceDirWasPerformedIndividual() throws InterruptedException, IOException {
                dirToolIterator.iterateToolsIndependent(executionTools);
                String testFileContet = tfu.readFileContentToString(finalTestFile);
                assertEquals("123", ExecutionToolWithFileSpy.callOrder);
                assertEquals("03", testFileContet);
            }

            @Test
            public void actualCopyOfSourceDirWasPerformedSequential() throws InterruptedException, IOException {
                dirToolIterator.iterateToolsSequential(executionTools);
                String testFileContet = tfu.readFileContentToString(finalTestFile);
                assertEquals("123", ExecutionToolWithFileSpy.callOrder);
                assertEquals("0123", testFileContet);
            }

            @Test
            public void outputDirInSequentialIsLastToolDirList(){
                dirToolIterator.iterateToolsSequential(executionTools);
                File lastDirPath = new File(workingDirectory.getPath()    + File.separator+ pts3.getName() +File.separator+ oneDir.getName());

                assertEquals(1,dirToolIterator.getProcessedDirs().size());
                assertEquals(lastDirPath.getPath(),dirToolIterator.getProcessedDirs().get(0).getPath());
            }
        }
    }

    private void assertToolWasRunNTimes(ExecutionToolSpy executionToolSpy, int numberOfRuns) {
        assertTrue(executionToolSpy.runMethodWasCalled());
        assertEquals(numberOfRuns, executionToolSpy.wasRunHowManyTimes());
        executionToolSpy.resetRunTimeCounter();
    }
    
    private class DirectoryToolIteratorFakeWithNoRealDirCopy extends DirectoryToolIterator {

        public boolean detectCalled = false;

        public DirectoryToolIteratorFakeWithNoRealDirCopy(File workingDir, File prepareDir) {
            super(workingDir, prepareDir);
        }

        @Override
        protected void createProcessDirCopy(File dirToProces, Path dirToProcessCopy) {

        }
    }
}
