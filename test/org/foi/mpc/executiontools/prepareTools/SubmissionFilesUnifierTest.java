package org.foi.mpc.executiontools.prepareTools;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.file.TextFileUtility;
import org.foi.mpc.executiontools.factories.PrepareTools;
import org.foi.common.filesystem.directory.InMemoryDir;
import org.foi.common.filesystem.file.InMemoryFile;
import org.foi.common.filesystem.file.InMemoryTextFileUtility;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(HierarchicalContextRunner.class)
public class SubmissionFilesUnifierTest {

    SubmissionFilesUnifier merger;
    String ls = TextFileUtility.getLineSeparator();

    @Before
    public void setUp(){
        merger = new SubmissionFilesUnifier();
    }

    @Test
    public void isPreparerToolAndHasCorecctName() {
        assertTrue(merger instanceof PrepareTools);
        assertEquals(SubmissionFilesUnifier.NAME, merger.getName());
    }

    public class withInMemoryFiles {

        InMemoryDir directory;
        InMemoryTextFileUtility tfu = new InMemoryTextFileUtility(StandardCharsets.UTF_8);

        @Before
        public void setUp(){
            directory = new InMemoryDir("directory");
            merger.setTfu(tfu);
            InMemoryFile javaFile1 = new InMemoryFile("javaFile1.java");
            InMemoryFile javaFile2 = new InMemoryFile("javaFile2.java");
            javaFile1.setContent("a");
            javaFile2.setContent("b");
            directory.addFile(javaFile1);
            directory.addFile(javaFile2);
        }

        @After
        public void tearDown() throws IOException {
            directory = null;
        }

        @Test
        public void canMergeTwoFiles() throws IOException {
            merger.runPrepareTool(directory);
            assertEquals("a"+ls+"b"+ls, tfu.readFileContentToString(merger.getMergedFile()));
            assertEquals(1, directory.listFiles().length);
        }

        @Test
        public void mergeFileNameIsSameAsDirName(){
            File mergedFile =merger.unifyFilesInDirToOne(directory);
            assertEquals(directory.getName(), DirectoryFileUtility.getFileNameWithoutExtension(mergedFile));
        }

        @Test
        public void canRunMergeTwice() throws IOException {
            merger.runPrepareTool(directory);
            assertEquals("a"+ls+"b"+ls, tfu.readFileContentToString(merger.getMergedFile()));
            assertEquals(1, directory.listFiles().length);

            InMemoryFile javaFileNewRun = new InMemoryFile("javaFileNewRun.java");
            javaFileNewRun.setContent("x");
            directory.addFile(javaFileNewRun);
            merger.runPrepareTool(directory);
            assertEquals("x"+ls, tfu.readFileContentToString(merger.getMergedFile()));
            assertEquals(1, directory.listFiles().length);
        }

        @Test
        public void canMergeOnlyJavaFilesOthersAreDeleted() throws IOException {
            InMemoryFile textFile = new InMemoryFile("textFile.txt");
            textFile.setContent("c");
            directory.addFile(textFile);
            merger.runPrepareTool(directory);
            assertEquals("a"+ls+"b"+ls, tfu.readFileContentToString(merger.getMergedFile()));
            assertEquals(1, directory.listFiles().length);
        }

        @Test
        public void canMergeOnlyJavaAndPHPFilesOthersAreDeleted() throws IOException {
            InMemoryFile textFile = new InMemoryFile("textFile.txt");
            textFile.setContent("c");
            directory.addFile(textFile);
            InMemoryFile phpFile = new InMemoryFile("phpFile.php");
            phpFile.setContent("d");
            directory.addFile(phpFile);

            List<String> extensions = new ArrayList<>();
            extensions.add(".java");
            extensions.add(".php");

            merger.setMergeExtensions(extensions);
            merger.runPrepareTool(directory);
            assertEquals("a"+ls+"b"+ls+"d"+ls, tfu.readFileContentToString(merger.getMergedFile()));
            assertEquals(1, directory.listFiles().length);
        }

        @Test
        public void canMergeFilesInSubDirWhichIsThenDeleted() throws IOException {
            InMemoryDir subDir = new InMemoryDir("subDir");
            InMemoryFile javaFile3 = new InMemoryFile("javaFile3.java");
            javaFile3.setContent("c");
            subDir.addFile(javaFile3);

            InMemoryDir subSubDir = new InMemoryDir("subSubDir");
            InMemoryFile javaFile4 = new InMemoryFile("javaFile4.java");
            javaFile4.setContent("d");
            subSubDir.addFile(javaFile4);

            subDir.addFile(subSubDir);
            directory.addFile(subDir);

            merger.runPrepareTool(directory);
            assertEquals("a"+ls+"b"+ls+"c"+ls+"d"+ls, tfu.readFileContentToString(merger.getMergedFile()));
            assertEquals(1, directory.listFiles().length);
        }
    }

    public class withRealFiles {

        File directory;
        TextFileUtility tfu = new TextFileUtility(StandardCharsets.UTF_8);

        @Before
        public void setUp(){
            directory = new File("testDir");
            directory.mkdir();
        }

        @After
        public void tearDown() throws IOException {
           DirectoryFileUtility.deleteDirectoryTree(directory);
        }

        @Test
        public void dirWasAlreadyUnified() throws IOException {
            tfu.createFileWithText(directory,directory.getName()+".java","mergedFile");
            File mergedFile = merger.unifyFilesInDirToOne(directory);
            assertEquals(1, directory.listFiles().length);
            assertEquals("mergedFile", tfu.readFileContentToString(mergedFile));
        }

        @Test(expected = TextFileUtility.FileAlreadyExistException.class)
        public void dirHasFileNameAsMergeFile() throws IOException {
            tfu.createFileWithText(directory,directory.getName()+".java","sameNameFile");
            tfu.createFileWithText(directory,"newFile.java","someOtherFile");
            merger.unifyFilesInDirToOne(directory);
        }

        @Test
        public void canRunRealMergeAndDelete() throws IOException {
            File file1 = new File(directory.getPath()+File.separator+"file1.java");
            File file2 = new File(directory.getPath()+File.separator+"file2.java");
            File textFile = new File(directory.getPath()+File.separator+"textFile.txt");
            File subDir = new File(directory.getPath()+File.separator+"subDir");
            subDir.mkdir();
            File file3 = new File(subDir.getPath()+File.separator+"file3.java");

            tfu.createFileWithText(file1,"a");
            tfu.createFileWithText(file2,"b");
            tfu.createFileWithText(textFile,"c");
            tfu.createFileWithText(file3,"d");
            File mergedFile = merger.unifyFilesInDirToOne(directory);
            assertEquals(1, directory.listFiles().length);
            assertEquals("a"+ls+"b"+ls+"d", tfu.readFileContentToString(mergedFile));
        }
    }
}
