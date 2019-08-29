package org.foi.mpc.executiontools.techniques;

import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.file.TextFileUtility;
import org.foi.mpc.phases.executionphases.ExecutionTool;
import org.foi.mpc.executiontools.factories.PreprocessingTechnique;
import org.foi.common.filesystem.directory.DirectoryCreator;
import org.foi.common.filesystem.directory.InMemoryDir;
import org.foi.common.filesystem.file.InMemoryFile;
import org.foi.common.filesystem.file.TextCreator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class NoTechniqueOriginalTest {

    NoTechniqueOriginal noTechnique;
    File testDir;

    @Before
    public void setUp(){
         noTechnique = new NoTechniqueOriginal();
         testDir = new File("testDir");
         testDir.mkdir();
    }

    @After
    public void tearDown() throws IOException {
        DirectoryFileUtility.deleteDirectoryTree(testDir);
    }

    @Test
    public void isExecutionPhaseAndHasCorrectGetName() throws IOException {
        assertTrue(noTechnique instanceof ExecutionTool);
        assertTrue(noTechnique instanceof PreprocessingTechnique);
        assertNotNull(noTechnique.getName());
        assertEquals("NoPreprocessing", noTechnique.getName());
    }

    @Test
    public void submissionFilesAreInRootDoNothing() throws IOException {
        DirectoryCreator.createTestDirWithNFiles(testDir,2);

        noTechnique.runTool(testDir);

        assertEquals(2,testDir.listFiles().length);
        assertEquals("testFile1.java",testDir.listFiles()[1].getName());
        assertEquals("testFile2.java",testDir.listFiles()[0].getName());
    }

    @Test
    public void copiesFromSubmissionsFileToRoot() throws IOException {
        TextFileUtility tfu = new TextFileUtility(StandardCharsets.UTF_8);
        File subm1 = new File(testDir.getPath()+File.separator+"subm1");
        File subm2 = new File(testDir.getPath()+File.separator+"subm2");
        File file1 = new File(subm1.getPath() + File.separator + "testFile1.java");
        File file2 = new File(subm2.getPath() + File.separator + "testFile2.java");
        subm1.mkdir();
        subm2.mkdir();
        tfu.createFileWithText(file1, TextCreator.getSourceCodeExample1());
        tfu.createFileWithText(file2, TextCreator.getSourceCodeExample2());

        noTechnique.runTool(testDir);

        assertEquals(2,testDir.listFiles().length);
        assertEquals("testFile1.java",testDir.listFiles()[1].getName());
        assertEquals("testFile2.java",testDir.listFiles()[0].getName());
    }

    @Test(expected = NoTechniqueOriginal.NoTechniqueRunException.class)
    public void subDirHasMoreThanOneFile(){
        InMemoryDir dir = new InMemoryDir("testDir");
        InMemoryDir subDir = new InMemoryDir("subDir");
        InMemoryFile file1 = new InMemoryFile("file1");
        InMemoryFile file2 = new InMemoryFile("file2");
        subDir.addFile(file1);
        subDir.addFile(file2);
        dir.addFile(subDir);

        noTechnique.runPreporcess(dir);
    }
}
