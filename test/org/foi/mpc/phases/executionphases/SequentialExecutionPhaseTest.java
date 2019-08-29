package org.foi.mpc.phases.executionphases;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.foi.mpc.MPCContext;
import org.foi.common.MPCexcpetions;
import org.foi.mpc.executiontools.factories.PrepareTools;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.file.TextFileUtility;
import org.foi.mpc.phases.PhaseFactory;
import org.foi.mpc.executiontools.spies.PrepareToolSpy;
import org.foi.mpc.executiontools.spies.PrepareToolWithFileSpy;
import org.foi.mpc.phases.executionphases.spies.ExecutionToolSpy;
import org.foi.common.filesystem.directory.DirectoryCreator;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.foi.mpc.phases.executionphases.ExecutionToolAssertHelper.assertToolWasRunNTimes;
import static org.foi.common.filesystem.directory.DirectoryCreator.assertDirHasFilesCount;
import static org.foi.common.filesystem.directory.DirectoryCreator.assertDirectoryIsEmpty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(HierarchicalContextRunner.class)
public class SequentialExecutionPhaseTest {

    private ExecutionPhase assignPreparer;
    private File inputDirectory;
    private File workingDirectory;
    private List<ExecutionTool> prepareTools;
    protected List<File> dirsToProcess;
    private PhaseFactory phaseFactory = new PhaseFactory(MPCContext.MATCHES_DIR);

    @Rule
    public ExpectedException exceptionThrown = ExpectedException.none();

    @Before
    public void setUp() {
        workingDirectory = new File("workingdir");
        workingDirectory.mkdir();
        inputDirectory = new File("testInputDirectory");
        inputDirectory.mkdir();

        assignPreparer = phaseFactory.createAssignmentPreparer(workingDirectory, inputDirectory);
        dirsToProcess = new ArrayList<>();
        prepareTools = new ArrayList<>();
        assignPreparer.setExecutionTools(prepareTools);
        assignPreparer.setDirsToProcess(dirsToProcess);
    }

    @After
    public void tearDown() throws IOException {
        DirectoryFileUtility.deleteDirectoryTree(inputDirectory);
        DirectoryFileUtility.deleteDirectoryTree(workingDirectory);
    }


    @Test
    public void inputDirectoryIsEmpty_workingDirHasEmptyPreprocessDir() {
        assignPreparer.runPhase();
        File preporcessDir = new File(workingDirectory + File.separator + "prepared");
        assertTrue(preporcessDir.exists());
        assertDirectoryIsEmpty(preporcessDir);
    }

    @Test(expected = MPCexcpetions.IsNullException.class)
    public void workingDirListIsNull() {
        assignPreparer.setDirsToProcess(null);
        assignPreparer.runPhase();
    }

    @Test(expected = MPCexcpetions.IsNullException.class)
    public void executeWithListOfToolsIsNull() {
        assignPreparer.setExecutionTools(null);
        assignPreparer.runPhase();
    }

    @Test
    public void workingDirListIsEmpty() {
        assignPreparer.runPhase();
    }

    @Test
    public void executionToolListIsEmpty() {
        dirsToProcess.add(new File("toolDir"));
        assignPreparer.runPhase();
    }

    @Test
    public void runWasDoneByMultipleToolsAndMultipleDirs() {
        int numberOfToolsAndElements = 4;
        for (int i = 1; i <= numberOfToolsAndElements; i++) {
            File newDir = new File(inputDirectory + File.separator + "dirToProcessDir" + i);
            newDir.mkdir();
            dirsToProcess.add(newDir);
            prepareTools.add(new PrepareToolSpy());
        }
        assignPreparer.setExecutionTools(prepareTools);
        assignPreparer.setDirsToProcess(dirsToProcess);
        assignPreparer.runPhase();

        assertDirHasFilesCount(numberOfToolsAndElements, new File(workingDirectory + File.separator + "prepared"));
        for (ExecutionTool ptSpy : prepareTools) {
            assertToolWasRunNTimes((ExecutionToolSpy) ptSpy, numberOfToolsAndElements);
            assertDirHasFilesCount(numberOfToolsAndElements, new File(workingDirectory + File.separator + "prepared" + File.separator + ptSpy.getName()));
        }
    }


    @Test
    public void copiesInputDirectoryWithMultipleCoursesYearsAndAssignments() throws InterruptedException, IOException {
        DirectoryCreator dc = new DirectoryCreator();
        dc.createTestDirStructure(inputDirectory, DirectoryCreator.DirTypes.course);
        prepareTools.add(new PrepareToolSpy());
        assignPreparer.setExecutionTools(prepareTools);
        assignPreparer.setDirsToProcess(dc.getCreatedAsignementDirs());
        assignPreparer.runPhase();
        dc.assertThatLastCreatedDirStructureIsOkInTargetDir(new File(workingDirectory + File.separator + "prepared" + File.separator + prepareTools.get(0).getName()));
    }

    @Test
    public void outputDirOfFirstIsInputDirOfSecond() throws IOException {
        PrepareToolWithFileSpy.callOrder = "";
        PrepareTools pts1 = new PrepareToolWithFileSpy("1");
        PrepareTools pts2 = new PrepareToolWithFileSpy("2");
        PrepareTools pts3 = new PrepareToolWithFileSpy("3");
        prepareTools.add(pts1);
        prepareTools.add(pts2);
        prepareTools.add(pts3);
        File oneDir = new File(inputDirectory + File.separator + "oneDir");
        oneDir.mkdir();
        File testFile = new File(oneDir.getPath() + File.separator + "testFile.txt");
        TextFileUtility tfu = new TextFileUtility(StandardCharsets.UTF_8);
        tfu.createFileWithText(testFile, "0");
        dirsToProcess.add(oneDir);
        File finalTestFile = new File(workingDirectory.getPath() + File.separator
                + "prepared" + File.separator
                + pts3.getName() + File.separator
                + oneDir.getName() + File.separator
                + testFile.getName());

        assignPreparer.setExecutionTools(prepareTools);
        assignPreparer.setDirsToProcess(dirsToProcess);
        assignPreparer.runPhase();

        String testFileContent = tfu.readFileContentToString(finalTestFile);
        assertEquals("123", PrepareToolWithFileSpy.callOrder);
        assertEquals("0123", testFileContent);
    }

}
