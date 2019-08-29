package org.foi.common.filesystem.directory;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import org.foi.common.MPCexcpetions;
import org.foi.common.filesystem.directory.DirectoryFileUtility.DirDoesNotExistException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

@RunWith(HierarchicalContextRunner.class)
public class DirectoryFileUtilityTest {

    private File rootSourceDirectory;

    @Rule
    public ExpectedException exceptionThrown = ExpectedException.none();

    @Before
    public void setUp() {
        rootSourceDirectory = new File("rootSourceDirectory");
        rootSourceDirectory.mkdir();
    }

    @After
    public void tearDown() throws IOException {
        DirectoryFileUtility.deleteDirectoryTree(rootSourceDirectory);
    }

    @Test
    public void canInstantiate() {
        DirectoryFileUtility fu = new DirectoryFileUtility();
    }

    @Test(expected = MPCexcpetions.IsNullException.class)
    public void checkIfIsNullDirectoryEmpty() {
        DirectoryFileUtility.isEmptyDirectory(null);
    }

    @Test(expected = MPCexcpetions.IsNullException.class)
    public void checkIfIsNullDirectoryNotEmpty() {
        DirectoryFileUtility.isNotEmptyDirectory(null);
    }

    @Test
    public void checkIfIsNonExistingDirectoryEmpty() {
        assertTrue(DirectoryFileUtility.isEmptyDirectory(new File("nonExistingDirectory")));
        assertFalse(DirectoryFileUtility.isNotEmptyDirectory(new File("nonExistingDirectory")));
    }

    @Test
    public void checkIfIsEmptyDirectoryDirectoryEmpty() {
        assertTrue(DirectoryFileUtility.isEmptyDirectory(rootSourceDirectory));
        assertFalse(DirectoryFileUtility.isNotEmptyDirectory(rootSourceDirectory));
    }

    @Test
    public void checkIfIsNonEmptyDirectoryEmpty() {
        createSubDirectories(1);
        assertFalse(DirectoryFileUtility.isEmptyDirectory(rootSourceDirectory));
        assertTrue(DirectoryFileUtility.isNotEmptyDirectory(rootSourceDirectory));
    }

    @Test(expected = MPCexcpetions.IsNullException.class)
    public void deleteNullDirectory() throws IOException {
        DirectoryFileUtility.deleteDirectoryTree(null);
    }

    @Test
    public void deleteNonExistingDirectory() throws IOException {
        DirectoryFileUtility.deleteDirectoryTree(new File("nonExistingDirectory"));
    }

    @Test
    public void deleteEmptyDirectory() throws IOException {
        DirectoryFileUtility.deleteDirectoryTree(rootSourceDirectory);
        assertThat(rootSourceDirectory.exists(), is(false));
    }

    @Test
    public void deleteDirectoryWithSubDirs() throws IOException {
        createSubDirectories(10);
        DirectoryFileUtility.deleteDirectoryTree(rootSourceDirectory);
        assertThat(rootSourceDirectory.exists(), is(false));
    }

    @Test
    public void deleteDirectoryWithNestedSubDirs() throws IOException {
        createNestedDirectories(10);
        DirectoryFileUtility.deleteDirectoryTree(rootSourceDirectory);
        assertThat(rootSourceDirectory.exists(), is(false));
    }

    @Test
    public void deleteDirectoryWitNestedSubDirsAndSubDirs() throws IOException {
        createNestedDirectories(10);
        createSubDirectories(10);

        DirectoryFileUtility.deleteDirectoryTree(rootSourceDirectory);
        assertThat(rootSourceDirectory.exists(), is(false));
    }

    @Test
    public void deleteDirectoryWithFiles() throws IOException, InterruptedException {
        Path testFile1 = Paths.get(rootSourceDirectory.getAbsolutePath(), "testFile.txt");
        Path testFile2 = Paths.get(rootSourceDirectory.getAbsolutePath(), "testFile2.txt");
        Files.createFile(testFile1, new FileAttribute[]{});
        Files.createFile(testFile2, new FileAttribute[]{});

        DirectoryFileUtility.deleteDirectoryTree(rootSourceDirectory);
        assertThat(rootSourceDirectory.exists(), is(false));
    }

    public class withOutputDirectory {

        private File rootTargetDirectory;

        @Before
        public void setUp() {
            rootTargetDirectory = new File("rootTargetDirectory");
            rootTargetDirectory.mkdir();
        }

        @After
        public void tearDown() throws IOException {
            DirectoryFileUtility.deleteDirectoryTree(rootTargetDirectory);
        }

        @Test(expected = MPCexcpetions.IsNullException.class)
        public void copyDirWithNullFromDir() throws IOException {
            new DirectoryFileUtility().copyDirectoryTree(null,rootSourceDirectory);
        }
        
        @Test(expected = MPCexcpetions.IsNullException.class)
        public void copyDirWithNullToDir() throws IOException {
            new DirectoryFileUtility().copyDirectoryTree(rootTargetDirectory,null);
        }

        @Test(expected = DirDoesNotExistException.class)
        public void copyNonExistingDirectory() throws IOException {
            new DirectoryFileUtility().copyDirectoryTree(new File("nonExistingDirectory"), rootTargetDirectory);
        }

        @Test(expected = MPCexcpetions.IsNullException.class)
        public void copyNullDirectory() throws IOException {
            new DirectoryFileUtility().copyDirectoryTree(null, rootTargetDirectory);
        }

        @Test(expected = DirDoesNotExistException.class)
        public void copyWhenDestinationDirectoryDoesNotExist() throws IOException {
            new DirectoryFileUtility().copyDirectoryTree(rootSourceDirectory, new File("nonExistingDirectory"));
        }

        @Test(expected = MPCexcpetions.IsNullException.class)
        public void copyWhenDestinationDirectoryIsNull() throws IOException {
            new DirectoryFileUtility().copyDirectoryTree(rootSourceDirectory, null);
        }

        @Test
        public void copyEmptyDirectory() throws IOException {
            new DirectoryFileUtility().copyDirectoryTree(rootSourceDirectory, rootTargetDirectory);

            assertTrue(DirectoryFileUtility.isEmptyDirectory(rootTargetDirectory));
        }

        @Test
        public void copyDirectoryWithNestedDirs() throws IOException, InterruptedException {
            createNestedDirectories(10);

            new DirectoryFileUtility().copyDirectoryTree(rootSourceDirectory, rootTargetDirectory);

            assertTrue(DirectoryFileUtility.isNotEmptyDirectory(rootTargetDirectory));
            assertThatNumberOfDirChildsIs(rootTargetDirectory, 1);

            File destNestedSubDir = rootTargetDirectory.listFiles()[0];
            assertThatNumberOfDirChildsIs(destNestedSubDir, 1);
        }

        @Test
        public void copyDirectoryWithSubDirs() throws IOException, InterruptedException {
            int numberOfSubDirectories = 10;
            createSubDirectories(numberOfSubDirectories);

            new DirectoryFileUtility().copyDirectoryTree(rootSourceDirectory, rootTargetDirectory);

            assertTrue(DirectoryFileUtility.isNotEmptyDirectory(rootTargetDirectory));
            assertThatNumberOfDirChildsIs(rootTargetDirectory, numberOfSubDirectories);

            DirectoryFileUtility.deleteDirectoryTree(rootTargetDirectory);
        }

        private void assertThatNumberOfDirChildsIs(File directory, int numberOfChilds) {
            assertTrue(directory.list().length == numberOfChilds);
        }
    }

    private void createSubDirectories(int numberOfDirectories) {
        for (int i = 0; i < numberOfDirectories; i++) {
            File subDir = new File(rootSourceDirectory + File.separator + "subDir" + i);
            subDir.mkdir();
        }
    }

    private void createNestedDirectories(int numberOfDirectories) {
        String nestedDirPath = rootSourceDirectory.getPath();
        for (int i = 0; i < numberOfDirectories; i++) {
            nestedDirPath = nestedDirPath + File.separator + "nestedDir" + i;
            File nestedDir = new File(nestedDirPath);
            nestedDir.mkdir();
        }
    }
}
