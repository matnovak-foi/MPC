package org.foi.common.filesystem.file;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import org.foi.common.MPCexcpetions;
import org.foi.common.filesystem.file.TextFileUtility.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

@RunWith(HierarchicalContextRunner.class)
public class TextFileUtilityTest  {

    String text;
    TextFileUtility fileUtility;
    File rootDir;

    @Rule
    public ExpectedException exceptionThrown = ExpectedException.none();

    @Before
    public void setUp() {
        fileUtility = new TextFileUtility(StandardCharsets.UTF_8);
        text = "This is some test text! čćžšđ" + System.getProperty("line.separator") + "new line";

        rootDir = new File(System.getProperty("user.dir"));
        System.out.println(rootDir);
        if (!rootDir.getPath().contains("MPCCommons"))
            rootDir = Paths.get(rootDir.getPath(),"MPCCommons").toFile();
    }

    @Test(expected = FileCreateException.class)
    public void directoryDoesNotExist() throws IOException {
        File fileInNonexistingDir = new File("nonExistingDirectory" + File.separator + "fileFromText.txt");
        fileUtility.createFileWithText(fileInNonexistingDir, text);
    }

    @Test(expected = MPCexcpetions.IsNullException.class)
    public void createFileIsNull() throws IOException {
        fileUtility.createFileWithText(null, text);
    }

    public class withExistingTestFile {
        File file;

        @Before
        public void setUp() throws IOException {
            file = new File("existingTestFile.txt");
        }

        @After
        public void thearDown() throws IOException {
            file.delete();
        }

        @Test(expected = FileAlreadyExistException.class)
        public void createFileThatAlreadyExists() throws IOException {
            fileUtility.createFileWithText(file, text);
            fileUtility.createFileWithText(file, text);
        }
    }

    @Test(expected = FileDoesNotExistException.class)
    public void readNonExistingFile() throws IOException {
        File file = new File("readNonExistingFile.txt");
        fileUtility.readFileContentToString(file);
    }

    @Test(expected = MPCexcpetions.IsNullException.class)
    public void readNullFile() throws IOException {
        fileUtility.readFileContentToString(null);
    }

    @Test(expected = MPCexcpetions.IsNullException.class)
    public void createFileWithNullText() throws IOException {
        File file = new File("createFileWithNullText.txt");
        fileUtility.createFileWithText(file, null);
    }

    @Test
    public void canCreateAndReadFile() throws IOException {
        File file = new File("canCreateAndReadFile.txt");
        fileUtility.createFileWithText(file, text);
        assertTrue(file.exists());
        String textFromFile = fileUtility.readFileContentToString(file);
        assertEquals(text, textFromFile);
        file.delete();
    }

    @Test(expected = MPCexcpetions.IsNullException.class)
    public void createNewFileWithNullDir() throws IOException {
        fileUtility.createFileWithText(null, "newFile.java", text);
    }

    @Test(expected = MPCexcpetions.IsNullException.class)
    public void createNewFileWithNullFileName() throws IOException {
        fileUtility.createFileWithText(new File("dir"), null, text);
    }

    @Test(expected = FileCreateException.class)
    public void createNewFileWithNonExistingDir() throws IOException {
        fileUtility.createFileWithText(new File("nonexistingDir"), "newFile.java", text);
    }

    @Test(expected = FileCreateException.class)
    public void createNewFileWithEmptyFileName() throws IOException {
        fileUtility.createFileWithText(new File("dir"), "", text);
    }

    @Test
    public void canCreateNewFileWithText() throws IOException {
        File dir = new File("dir");
        dir.mkdir();
        File newFile = fileUtility.createFileWithText(dir, "newFile.java", text);
        assertTrue(newFile.exists());
        String textFromFile = fileUtility.readFileContentToString(newFile);
        assertEquals(text, textFromFile);
        newFile.delete();
        dir.delete();
    }

    @Test(expected = FileDoesNotExistException.class)
    public void appendToNonExistingFile() throws IOException {
        File file = new File("appendToNonExistingFile.txt");
        fileUtility.appendTextToFile(file, text);
    }

    @Test(expected = MPCexcpetions.IsNullException.class)
    public void appendToNullFile() throws IOException {
        fileUtility.appendTextToFile(null, text);
    }

    @Test(expected = MPCexcpetions.IsNullException.class)
    public void appendNullText() throws IOException {
        File file = new File("appendNullText.txt");
        fileUtility.appendTextToFile(file, null);
    }

    @Test
    public void canCreateAppendAndReadFile() throws IOException {
        File file = new File("canCreateAppendAndReadFile.txt");
        fileUtility.createFileWithText(file, text);
        fileUtility.appendTextToFile(file, "newText");
        String textFromFile = fileUtility.readFileContentToString(file);
        assertEquals(text + "newText", textFromFile);
        file.delete();
    }

    @Test
    public void noErrorWhenReadingFileWhichCausesTheError() throws IOException {

        File file = new File(rootDir+File.separator+"testInputData"+File.separator+"fileWithReadError.java");
        System.out.println(file);
        fileUtility.readFileContentToString(file);
    }
}
