package org.foi.mpc.executiontools.techniques;

import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.file.TextFileUtility;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.foi.common.filesystem.directory.DirectoryCreator.assertDirHasFilesCount;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PreprocessDirectoryCleanerTest {
    TextFileUtility tfu = new TextFileUtility(StandardCharsets.UTF_8);
    PreprocessDirectoryCleaner cleaner;
    File testDir;
    String techniqueName = "techniqueWorkingDirName";
    File processedFile1;
    File processedFile2;

    @Before
    public void setUp() throws IOException {
        testDir = new File("testdir");
        testDir.mkdir();

        cleaner = new PreprocessDirectoryCleaner(techniqueName);
    }

    @After
    public void tearDown() throws IOException {
        testDir = new File("testdir");
        DirectoryFileUtility.deleteDirectoryTree(testDir);
    }

    private void createOriginalDir() throws IOException {
        File file1 = new File(testDir + File.separator + "file1.java");
        tfu.createFileWithText(file1, "someText1");
        File subDir = new File(testDir + File.separator + "subdir");
        subDir.mkdir();
        File file2 = new File(subDir + File.separator + "file2.java");
        tfu.createFileWithText(file2, "someText2");
    }

    private void createTechniqueDir() throws IOException {
        File techniqueDir = new File(testDir + File.separator + techniqueName);
        techniqueDir.mkdir();
        processedFile1 = new File(techniqueDir + File.separator + "file1.java");
        processedFile2 = new File(techniqueDir + File.separator + "file2.java");
        tfu.createFileWithText(processedFile1, "processedSomeText1");
        tfu.createFileWithText(processedFile2, "processedSomeText2");
    }

    @Test
    public void deleteAndRenameAreOkWithSoruceDirWithNoParent() throws IOException {
        createTechniqueDir();
        createOriginalDir();

        cleaner.clean(testDir);

        assertPreprocessedFilesAreInSourceDirRenamed();
    }

    @Test
    public void deleteAndRenameAreOkWithFilesInSubdir() throws IOException {
        testDir = new File(testDir + File.separator + "sourceWithParent");
        testDir.mkdir();

        createOriginalDir();
        createTechniqueDir();

        cleaner.clean(testDir);

        assertPreprocessedFilesAreInSourceDirRenamed();

        File nullSourceParent = new File(testDir.getParentFile() + File.separator + "null");
        assertFalse(nullSourceParent.exists());
    }

    private void assertPreprocessedFilesAreInSourceDirRenamed() throws IOException {
        File preprocessDir = new File(testDir.getPath() + File.separator + techniqueName);
        assertFalse(preprocessDir.exists());

        assertDirHasFilesCount(2, testDir);
        assertEquals("processedSomeText1",tfu.readFileContentToString(testDir.listFiles()[0]));
        assertEquals("processedSomeText2",tfu.readFileContentToString(testDir.listFiles()[1]));

        File nullParent = new File("null");
        assertFalse(nullParent.exists());
    }
}
