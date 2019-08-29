package org.foi.mpc.executiontools.techniques;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.directory.InMemoryDir;
import org.foi.common.filesystem.directory.InMemoryDirectoryFileUtility;
import org.foi.common.filesystem.file.InMemoryFile;
import org.foi.common.filesystem.file.InMemoryTextFileUtility;
import org.foi.common.filesystem.file.TextFileUtility;
import org.foi.mpc.executiontools.factories.PreprocessingTechnique;
import org.foi.mpc.phases.executionphases.ExecutionTool;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.*;

@RunWith(HierarchicalContextRunner.class)
public class RemoveCommentsTechniqueTest {

    private RemoveCommentsTechnique rct;
    private File testDir;

    @Before
    public void setUp() {
        rct = new RemoveCommentsTechnique();
        testDir = new File("testDir");
        testDir.mkdir();
    }

    @After
    public void tearDown() throws IOException {
        DirectoryFileUtility.deleteDirectoryTree(testDir);
    }

    @Test
    public void isExecutionPhaseAndHasCorrectGetName() throws IOException {
        assertTrue(rct instanceof ExecutionTool);
        assertTrue(rct instanceof PreprocessingTechnique);
        assertNotNull(rct.getName());
        assertEquals("RemoveComments", rct.getName());
    }

    public class withInMemoryFiles {
        InMemoryDir testDir;
        InMemoryFile file;
        InMemoryTextFileUtility tfu = new InMemoryTextFileUtility(StandardCharsets.UTF_8);
        File resultFile;

        @Before
        public void setUp() {
            testDir = new InMemoryDir("testDir");
            file = new InMemoryFile("file1.java");
            testDir.addFile(file);

            rct.setDc(new InMemoryDirectoryFileUtility());
            rct.setTfu(tfu);

            resultFile = new File(testDir.getName() + File.separator + rct.getName() + File.separator + file.getName());
        }

        @Test
        public void createsComentsRemoveDir() {
            rct.runPreporcess(testDir);
            assertEquals(2, testDir.listFiles().length);
            List<String> dirContent = Arrays.asList(testDir.list());
            assertThat(dirContent, hasItem(rct.getName()));
        }

        @Test
        public void fileIsOkLeaveItAsIs() throws IOException {
            file.setContent("test");
            rct.runPreporcess(testDir);
            assertEquals("test", tfu.readFileContentToString(resultFile));
        }

        @Test
        public void ignoreSingleSlash() throws IOException {
            file.setContent("test/comment");
            rct.runPreporcess(testDir);
            assertEquals("test/comment", tfu.readFileContentToString(resultFile));
        }

        @Test
        public void removeSimpleOneLineComment() throws IOException {
            file.setContent("test//comment\n" +
                    "test//comment\n" +
                    "test//System.out.println(\"http://elf.foi.hr\")\n" +
                    "test//comment with /* */ multiline comment");
            rct.runPreporcess(testDir);
            assertEquals("test\ntest\ntest\ntest", tfu.readFileContentToString(resultFile));
        }

        @Test
        public void removeSimpleMulltiLineComment() throws IOException {
            file.setContent("test/*comment*/\n" +
                    "test/*comment*/\n" +
                    "test/*System.out.println(\"http://elf.foi.hr\")*/\n" +
                    "test/* with single comment // */");
            rct.runPreporcess(testDir);
            assertEquals("test\ntest\ntest\ntest", tfu.readFileContentToString(resultFile));
        }

        @Test
        public void removeCommentsWithVariousSymbols() throws IOException {
            file.setContent("test/**comment*/\n" +
                    "test/**comment**/\n" +
                    "test/*/** commnet */\n" +
                    "test/*/*** commnet */\n" +
                    "test/*/** comm***ne//t ****/\n" +
                    "test//// comment ////\n" +
                    "test 'string //str /* str */' \"string //str /* str */ \"");
            rct.runPreporcess(testDir);
            assertEquals("test\ntest\ntest\ntest\ntest\ntest\n"+
                    "test 'string //str /* str */' \"string //str /* str */ \"",
                    tfu.readFileContentToString(resultFile));
        }

        @Test
        public void removeSimpleOneLineCommentNotEndingWithNewLine() throws IOException {
            file.setContent("test//comment");
            rct.runPreporcess(testDir);
            assertEquals("test", tfu.readFileContentToString(resultFile));
        }

        @Test
        public void doNotTreatUrlAsCommentInString() throws IOException {
            String input = "System.out.println(\"http://elf.foi.hr\")\n" +
                    "System.out.println(\"http:/*elf.foi.hr*/\")\n" +
                    "pw.println(\"<script src=\\\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js\\\"></script>\");";
            file.setContent(input);
            rct.runPreporcess(testDir);
            assertEquals(input, tfu.readFileContentToString(resultFile));
        }

        @Test
        public void doNotTreatCommentsInString() throws IOException {
            String input = "galleryPath = sc.getRealPath(\"WEB-INF\") + \"\\\\\" + konfig.dajPostavku(\"dirGalerija\");" +
                    "if (p.isMimeType(\"text/*\")) {\n" +
                    "            String s = (String) p.getContent();\n" +
                    "            textIsHtml = p.isMimeType(\"text/html\");\n" +
                    "            return s;\n" +
                    "        }\n";
            String input2 = input +  "/**\n" +
                    "     * Metoda šalje poruku sa svim predefiniram parametrima, te nakon uspješnog\n" +
                    "     * slanja poruku premješta u izlaznu mapu.\n" +
                    "     * \n" +
                    "     * @return Vraća poruku o ishodu izvršavanja\n" +
                    "     */";
            file.setContent(input2);
            rct.runPreporcess(testDir);
            assertEquals(input, tfu.readFileContentToString(resultFile));
        }

        @Test
        public void doNotRemoveOpenMultiCommentIfNotClosed() throws IOException {
            String input = "/* line one\n" +
                    "* line two\n" +
                    "* last line no close";
            file.setContent(input);
            rct.runPreporcess(testDir);
            assertEquals("/*", tfu.readFileContentToString(resultFile));
        }

        @Test
        public void deleteMultiCommentPartIfCommentDoesNotHaveBeginComment() throws IOException {
            String input = "* no begin line one\n" +
                    "* line two\n" +
                    "* last line */";
            file.setContent(input);
            rct.runPreporcess(testDir);
            assertEquals("*/", tfu.readFileContentToString(resultFile));
        }

        @Test
        public void deleteMultiCommentWithNoBeginingOrEndSign() throws IOException {
            String input = "* no begin line one\n" +
                    "* line two\n" +
                    "* last line *";
            file.setContent(input);
            rct.runPreporcess(testDir);
            assertEquals("", tfu.readFileContentToString(resultFile));
        }

        @Test
        public void lastSignIsStarInMultiLineCommentDoNotBreak() throws IOException {
            String input = "/* no begin line one\n" +
                    "* line two\n" +
                    "* last line *";
            file.setContent(input);
            rct.runPreporcess(testDir);
            assertEquals("/*", tfu.readFileContentToString(resultFile));
        }
    }

    public class withRealFiles {
        TextFileUtility tfu = new TextFileUtility(StandardCharsets.UTF_8);
        DirectoryFileUtility dfu = new DirectoryFileUtility();

        @Before
        public void setUp() {
            rct.setDc(dfu);
            rct.setTfu(tfu);
        }

        @Test
        public void runsCleaner() {
            final boolean[] cleanIsRun = {false};
            rct.setCleaner(new PreprocessDirectoryCleaner(rct.getName()){
                @Override
                public void clean(File preprocessedDir) {
                    cleanIsRun[0] = true;
                }
            });

            rct.runTool(testDir);

            assertTrue(cleanIsRun[0]);
        }

        @Test
        public void canWorkWithFilesInRootDir() throws IOException {
            File file = new File(testDir + File.separator + "file1.java");
            tfu.createFileWithText(file, "/* some comment */\n" +
                    "something ...");

            rct.runTool(testDir);

            assertEquals(1, testDir.listFiles().length);
            assertEquals("\nsomething ...", tfu.readFileContentToString(testDir.listFiles()[0]));
        }

        @Test
        public void canWorkWithFilesInSubDir() throws IOException {
            File subDir = new File(testDir + File.separator + "subdir");
            subDir.mkdir();
            File file = new File(subDir + File.separator + "file1.java");
            tfu.createFileWithText(file, "/* some comment */\n" +
                    "something ...");

            rct.runTool(testDir);

            assertEquals(1, testDir.listFiles().length);
            assertEquals("\nsomething ...", tfu.readFileContentToString(testDir.listFiles()[0]));
        }

        @Test
        public void realCaseExample() throws IOException {
            File file = new File("testInputData" +File.separator+"rcTest"+File.separator+"student1.java");
            File testFile = dfu.copyFile(file,testDir);

            File expectedFile = new File("testInputData" +File.separator+"rcTest"+File.separator+"student1_expected.java");
            rct.runTool(testDir);
            assertEquals(tfu.readFileContentToString(expectedFile), tfu.readFileContentToString(testFile));
        }

        @Test
        public void realCaseExample2() throws IOException {
            File file = new File("testInputData" +File.separator+"rcTest"+File.separator+"student2.java");
            File testFile = dfu.copyFile(file,testDir);

            File expectedFile = new File("testInputData" +File.separator+"rcTest"+File.separator+"student2_expected.java");
            rct.runTool(testDir);
            assertEquals(tfu.readFileContentToString(expectedFile), tfu.readFileContentToString(testFile));
        }

        @Test
        public void realCaseExample3() throws IOException {
            File file = new File("testInputData" +File.separator+"rcTest"+File.separator+"student3.java");
            File testFile = dfu.copyFile(file,testDir);

            File expectedFile = new File("testInputData" +File.separator+"rcTest"+File.separator+"student3_expected.java");
            rct.runTool(testDir);
            assertEquals(tfu.readFileContentToString(expectedFile), tfu.readFileContentToString(testFile));
        }
    }
}
