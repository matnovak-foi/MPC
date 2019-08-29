package org.foi.mpc.executiontools.prepareTools;

import org.foi.common.MPCexcpetions;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.directory.InMemoryDir;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class AssignmentDirectoryIteratorTest {

    PrepareToolIteratorSpy prepareTool = new PrepareToolIteratorSpy();

    @Test(expected = MPCexcpetions.IsNullException.class)
    public void runWithNullDir() {
        prepareTool.runTool(null);
    }

    @Test(expected = DirectoryFileUtility.DirDoesNotExistException.class)
    public void runWithInvalidDir() {
        prepareTool.runTool(new File("invalidDir"));
    }

    @Test
    public void runWithEmptyDir() {
        prepareTool.runTool(new InMemoryDir("emptyDir"));
    }


    @Test
    public void canRunPrepareToolOnAllElementsInDir() throws IOException {
        InMemoryDir testDir = new InMemoryDir("testDir");
        testDir.addFile(new InMemoryDir("dir1"));
        testDir.addFile(new InMemoryDir("dir2"));
        testDir.addFile(new File("file.rar"));
        prepareTool.runTool(testDir);
        assertEquals(3, prepareTool.wasRunCount);
    }

    private class PrepareToolIteratorSpy extends AssignementDirectoryIterator {
        protected int wasRunCount = 0;

        @Override
        public void runPrepareTool(File dirToProcess) {
            wasRunCount++;
        }

        @Override
        public String getName() {
            return null;
        }
    }

}