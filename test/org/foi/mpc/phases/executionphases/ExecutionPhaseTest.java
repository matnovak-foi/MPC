package org.foi.mpc.phases.executionphases;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.foi.common.MPCexcpetions;
import org.foi.common.filesystem.directory.DirectoryFileUtility;

import static org.foi.common.filesystem.directory.DirectoryFileUtility.deleteDirectoryTree;
import static org.foi.common.filesystem.directory.DirectoryCreator.assertDirectoryIsEmpty;

import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class ExecutionPhaseTest {

    File workingDir;
    File sourceDir;
    ExecutionPhase phase;
    protected List<File> dirsToProcess;

    @Before
    public void setUp() {
        workingDir = new File("workingDir");
        sourceDir = new File(workingDir + File.separator + "sourceDir");

        workingDir.mkdir();
        sourceDir.mkdir();
    }

    @After
    public void tearDown() throws IOException {
        deleteDirectoryTree(workingDir);
        deleteDirectoryTree(sourceDir);
    }

    @Test(expected = MPCexcpetions.IsNullException.class)
    public void workingDirIsNull() {
        phase = new ExecutionPhaseStub(null, sourceDir, "phaseDirName");
    }

    @Test(expected = MPCexcpetions.IsNullException.class)
    public void sourceDirIsNull() {
        phase = new ExecutionPhaseStub(workingDir, null, "phaseDirName");
    }

    @Test(expected = DirectoryFileUtility.DirDoesNotExistException.class)
    public void workingDirDoesNotExist() {
        phase = new ExecutionPhaseStub(new File("nonExistingFile"), sourceDir, "phaseDirName");
    }

    @Test(expected = DirectoryFileUtility.DirDoesNotExistException.class)
    public void sourceDirDoesNotExist() {
        phase = new ExecutionPhaseStub(workingDir, new File("nonExistingFile"), "phaseDirName");
    }

    @Test
    public void inputDirectoryIsEmpty_workingDirHasEmptyPhaseDir() {
        phase = new ExecutionPhaseStub(workingDir, sourceDir, "phaseDirName");
        phase.runPhase();
        File phaseDir = new File(workingDir + File.separator + "phaseDirName");
        assertTrue(phaseDir.exists());
        assertDirectoryIsEmpty(phaseDir);
    }

    @Test
    public void workingDirOfPhaseIsNotEmptyNoError(){
        File phaseDir = new File(workingDir+File.separator+"phaseDirName");
        phaseDir.mkdir();
        phase = new ExecutionPhaseStub(workingDir,sourceDir,"phaseDirName");
    }

    private class ExecutionPhaseStub extends ExecutionPhase {

        public ExecutionPhaseStub(File workingDir, File sourceDir, String phaseSubDirName) {
            super(workingDir, sourceDir, phaseSubDirName);
        }

        @Override
        public void runPhase() {
        }
    }
}
