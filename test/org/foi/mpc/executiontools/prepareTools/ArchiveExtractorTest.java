package org.foi.mpc.executiontools.prepareTools;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.mpc.executiontools.factories.PrepareTools;
import org.foi.common.filesystem.directory.InMemoryDir;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static org.foi.common.filesystem.directory.DirectoryCreator.assertDirHasFilesCount;
import static org.junit.Assert.*;

@RunWith(HierarchicalContextRunner.class)
public class ArchiveExtractorTest {
    ArchiveExtractor extractor;
    File testDir;

    @Before
    public void setUp() throws Exception {
        extractor = new ArchiveExtractor();
    }

    @Test
    public void isPreparerToolAndHasCorecctName() {
        assertTrue(extractor instanceof PrepareTools);
        assertEquals(ArchiveExtractor.NAME, extractor.getName());
    }

    @Test(expected = ArchiveExtractor.ExtractException.class)
    public void runWithDirWithNonArchiveFile() throws IOException {
        InMemoryDir nonArchiveDir = new InMemoryDir("dirWithNormalFile");
        nonArchiveDir.addFile(new File("noArchiveFile.java"));
        extractor.runTool(nonArchiveDir);
    }

    @Test
    public void runWithDirGivenAsArchiveFile(){
        File testDir = new InMemoryDir("testDir");
        extractor.extractToArchiveName(testDir);
    }

    public class withRealTestDir {
        @Before
        public void setUp() throws Exception {
            testDir = new File("testDir");
            testDir.mkdir();
        }

        @After
        public void tearDown() throws IOException {
            DirectoryFileUtility.deleteDirectoryTree(testDir);
        }

        @Test
        public void canExtractOneEmptyZipFile() {
            File expectedDir = new File(testDir.getPath() + File.separator + "someZipFile");
            File extractedDir = extractor.extractToArchiveName(new File(testDir.getPath() + File.separator + "someZipFile.zip"));
            assertEquals(expectedDir.getAbsolutePath(), extractedDir.getAbsolutePath());
            assertTrue(extractedDir.exists());
        }

        @Test
        public void canExtractZipFileWithSubdirs() throws IOException {
            String zipFileName = "directoryWithTwoWilesAndSubDirWithTwoFiles";
            File zipFile = setUpZipFile(zipFileName);
            File extractedDir = extractor.extractToArchiveName(zipFile);
            assertTrueArchiveWasExtracted(zipFileName, extractedDir);
            File subdir = new File(extractedDir + File.separator + "directory" + File.separator + "subdir");
            assertTrue(subdir.exists());
            assertDirHasFilesCount(2, subdir);
        }

        @Test
        public void canExtractZipInsideZipFile() throws IOException {
            String zipFileName = "twoFilesAndZipWithFile";
            File zipFile = setUpZipFile(zipFileName);
            File extractedDir = extractor.extractToArchiveName(zipFile);
            assertTrueArchiveWasExtracted(zipFileName, extractedDir);
            assertTrueInsideArchiveIsExtracted(extractedDir, "insideZip");
        }

        @Test
        public void canExtractRarFileWithSubdirs() throws IOException {
            String rarFileName = "rarDirectoryWithTwoWilesAndSubDirWithTwoFiles";
            File rarFile = setUpRarFile(rarFileName);
            File extractedDir = extractor.extractToArchiveName(rarFile);
            assertTrueArchiveWasExtracted(rarFileName, extractedDir);
            File subdir = new File(extractedDir + File.separator + "directory" + File.separator + "subdir");
            assertTrue(subdir.exists());
            assertDirHasFilesCount(2, subdir);
        }

        @Test
        public void canExtractRarInsideZipFileOrRarFile() throws IOException {
            String rarFileName = "rarTwoFilesAndZipRarWithFile";
            File rarFile = setUpRarFile(rarFileName);
            File extractedDir = extractor.extractToArchiveName(rarFile);
            assertTrueArchiveWasExtracted(rarFileName, extractedDir);
            assertTrueInsideArchiveIsExtracted(extractedDir, "insideZip");
            assertTrueInsideArchiveIsExtracted(extractedDir, "insideRar");
        }

        @Test
        public void canExtractZipInsideZipFileOrRarFile() throws IOException {
            String zipFileName = "twoFilesAndZipRarWithFile";
            File zipFile = setUpZipFile(zipFileName);
            File extractedDir = extractor.extractToArchiveName(zipFile);
            assertTrueArchiveWasExtracted(zipFileName, extractedDir);
            assertTrueInsideArchiveIsExtracted(extractedDir, "insideZip");
            assertTrueInsideArchiveIsExtracted(extractedDir, "insideRar");
        }

        @Test
        public void realRarTest() throws IOException {
            String rarFileName = "student14_zadaca_4";
            File rarFile = setUpRarFile(rarFileName);
            File extractedDir = extractor.extractToArchiveName(rarFile);
        }
    }

    private File setUpZipFile(String zipFileName) throws IOException {
        return setUpArchiveFile(zipFileName, "zip");
    }

    private File setUpRarFile(String rarFileName) throws IOException {
        return setUpArchiveFile(rarFileName, "rar");
    }

    private File setUpArchiveFile(String archiveName, String extension) throws IOException {
        File inputArchive = new File("testInputData" + File.separator + "archiveTestFiles" + File.separator + archiveName+"."+extension);
        File workingArchive = new File(testDir.getPath() + File.separator + archiveName+"."+extension);
        CopyOption[] copyOptions = new CopyOption[]{StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING};
        Files.copy(inputArchive.toPath(), workingArchive.toPath(), copyOptions);
        return workingArchive;
    }

    private void assertTrueArchiveWasExtracted(String zipFileName, File extractedDir) {
        File expectedDir = new File(testDir.getPath() + File.separator + zipFileName);
        assertEquals(expectedDir.getAbsolutePath(), extractedDir.getAbsolutePath());
        extractedDir = extractedDir.listFiles()[0];
        assertTrue(extractedDir.exists());
        assertDirHasFilesCount(3, extractedDir);
    }

    private void assertTrueInsideArchiveIsExtracted(File extractedDir, String archiveName) {
        File archiveFile = new File(extractedDir + File.separator + "directory" + File.separator + archiveName);
        assertTrue(archiveFile.exists());
        assertDirHasFilesCount(1, archiveFile);
    }
}