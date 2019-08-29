package org.foi.mpc.executiontools.detectionTools;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.List;

import org.foi.mpc.MPCContext;
import org.foi.common.MPCexcpetions;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.file.TextFileUtility;
import org.foi.mpc.matches.MPCMatchFileUtility;
import org.foi.mpc.phases.executionphases.ExecutionTool;
import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.executiontools.factories.SimilarityDetectionTool;
import org.foi.mpc.matches.models.MPCMatchPart;
import org.foi.common.filesystem.directory.DirectoryCreator;
import static org.foi.common.filesystem.directory.DirectoryCreator.assertDirHasFilesCount;
import org.foi.common.filesystem.file.TextCreator;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


@Ignore
public abstract class AdaptersTestTemplate {

    protected File sourceDir;
    protected SimilarityDetectionTool adapter;
    protected MPCMatchFileUtility matchFileUtility;

    @Before
    public void setUp() throws IOException {
        sourceDir = new File("sourceDir");
        sourceDir.mkdir();
        matchFileUtility = new MPCMatchFileUtility();
    }

    @After
    public void tearDown() throws IOException {
        DirectoryFileUtility.deleteDirectoryTree(sourceDir);
    }

    @Test(expected = DirectoryFileUtility.DirIsEmptyException.class)
    public void sourceDirIsEmpty() throws IOException, InterruptedException {
        sourceDir = new File(sourceDir + File.separator + "emptydir");
        sourceDir.mkdir();
        adapter.runPlagiarsimDetection(sourceDir);
    }

    protected abstract class withDummySourceDirAndNoRealRun {

        @Before
        public void setUp() throws IOException {
            Files.createFile(Paths.get(sourceDir.getPath(), "notEmptyDir.dummy"), new FileAttribute[]{});
        }

        @Test
        public void isSimilarityDetectionTool() {
            assertTrue(adapter instanceof SimilarityDetectionTool);
        }

        @Test(expected = MPCexcpetions.IsNullException.class)
        public void runWithSourceDirNull() {
            adapter.runPlagiarsimDetection(null);
        }

        @Test(expected = DirectoryFileUtility.DirDoesNotExistException.class)
        public void runWithNonExistingSourceDir() {
            adapter.runPlagiarsimDetection(new File("unexistingDir"));
        }
        
        @Test
        public void isExecutionPhaseAndHasCorrectGetName(){
            assertTrue(adapter instanceof ExecutionTool);
            assertNotNull(adapter.getName());
        }

        @Test
        public abstract void defaultOptionsAreOk();
    }

    protected abstract class withValidIdenticalFiles {

        protected TextFileUtility tfu;
        protected File matchesDir;
        protected File javaFile1;
        protected File javaFile2;

        @Before
        public void setUp() throws IOException {
            tfu = new TextFileUtility(StandardCharsets.UTF_8);
            javaFile1 = new File(sourceDir.getPath() + File.separator + "file1.java");
            javaFile2 = new File(sourceDir.getPath() + File.separator + "file2.java");
            tfu.createFileWithText(javaFile1, TextCreator.validMiniCodeExcample());
            tfu.createFileWithText(javaFile2, TextCreator.validMiniCodeExcample());
            matchesDir = new File(sourceDir.getPath() + File.separator + MPCContext.MATCHES_DIR);
        }

        @Test
        public void programIsCreatedAndRuned() throws IOException, InterruptedException {
            adapter.runPlagiarsimDetection(sourceDir);
            File result = new File(sourceDir.getPath() + File.separator + "result");
            assertTrue(matchesDir.exists());
            assertTrue(result.exists());
        }
        
        @Test
        public void programIsCreatedAndRunedWithRunToolMethod() throws IOException, InterruptedException {
            adapter.runTool(sourceDir);
            File result = new File(sourceDir.getPath() + File.separator + "result");
            assertTrue(matchesDir.exists());
            assertTrue(result.exists());
        }

        @Test(expected = DirectoryFileUtility.DirAleradyExistsException.class)
        public void matchesFolderExists() throws Exception {
            matchesDir = new File(sourceDir.getPath() + File.separator + MPCContext.MATCHES_DIR);
            matchesDir.mkdir();
            adapter.runPlagiarsimDetection(sourceDir);
        }

        @Test(expected = DirectoryFileUtility.DirAleradyExistsException.class)
        public void resultFolderExists() throws Exception {
            File result = new File(sourceDir.getPath() + File.separator + "result");
            result.mkdir();
            adapter.runPlagiarsimDetection(sourceDir);
        }

        @Test
        public void matchFileIsCreatedAndTypeOfMatch() throws Exception {
            adapter.runPlagiarsimDetection(sourceDir);

            assertDirHasFilesCount(1, matchesDir);

            File matchFile = matchesDir.listFiles()[0];
            assertTrue(matchFileUtility.readFromFile(matchFile) instanceof MPCMatch);
        }
        
        @Test
        public void withFilesInSubDirStructure() throws IOException{
            javaFile1.delete();
            javaFile2.delete();
            File subDir = new File(sourceDir.getPath()+File.separator+"subDir"+File.separator+"subSubDir");
            File subDir2 = new File(sourceDir.getPath()+File.separator+"subDir2"+File.separator+"subSubDir2");
            subDir.mkdirs();
            subDir2.mkdirs();
            javaFile1 = new File(subDir.getPath() + File.separator + javaFile1.getName());
            javaFile2 = new File(subDir2.getPath() + File.separator + javaFile2.getName());
            tfu.createFileWithText(javaFile1, TextCreator.validMiniCodeExcample());
            tfu.createFileWithText(javaFile2, TextCreator.validMiniCodeExcample());
            adapter.runPlagiarsimDetection(sourceDir);
            assertTrue(matchesDir.exists());
            DirectoryCreator.assertDirHasFilesCount(1, matchesDir);
        }

        @Test
        public void twoIdenticalFilesMatchFileCreated() throws Exception {
            adapter.runPlagiarsimDetection(sourceDir);
            assertDirHasFilesCount(1, matchesDir);
            
            File matchFile = new File(sourceDir.getPath() + File.separator + MPCContext.MATCHES_DIR+ File.separator+"file1.java-file2.java-match");
            if(!matchFile.exists()){
                matchFile = new File(sourceDir.getPath() + File.separator + MPCContext.MATCHES_DIR+ File.separator+"file2.java-file1.java-match");
                assertTestedFileNamesAreOk(matchFile, true);
            } else {
                assertTestedFileNamesAreOk(matchFile, false);
            } 
            
            assert100PrecentMatchesAreOk(matchesDir);
        }

        @Test
        public void threeIdenticalFilesThreeMatchFilesCreated() throws Exception {
            File javaFile3 = new File(sourceDir.getPath() + File.separator + "file3.java");
            tfu.createFileWithText(javaFile3, TextCreator.validMiniCodeExcample());
            adapter.runPlagiarsimDetection(sourceDir);
            assertDirHasFilesCount(3, matchesDir);
            assert100PrecentMatchesAreOk(matchesDir);
        }

        private void assertTestedFileNamesAreOk(File matchFile, boolean reverese) throws IOException, ClassNotFoundException {
            MPCMatch matches = matchFileUtility.readFromFile(matchFile);
            if(reverese){
                assertEquals("file2.java", matches.fileAName);
                assertEquals("file1.java", matches.fileBName);
            } else {
                assertEquals("file1.java", matches.fileAName);
                assertEquals("file2.java", matches.fileBName);
            }
            assertEquals(sourceDir.getAbsolutePath(),matches.sourceDir.getAbsolutePath());
            assertEquals(matchesDir.getAbsolutePath(),matches.matchesDir.getAbsolutePath());
        }

        protected void assert100PrecentMatchesAreOk(File matchesDir) throws IOException, ClassNotFoundException {
            for (File matchFile : matchesDir.listFiles()) {
                MPCMatch match = matchFileUtility.readFromFile(matchFile);
                assertEquals(matchFile.getName().split("-")[0], match.fileAName);
                assertEquals(matchFile.getName().split("-")[1], match.fileBName);
                assertEquals(100, match.similarity, 0.0001);
                assertEquals(100, match.similarityA, 0.0001);
                assertEquals(100, match.similarityB, 0.0001);
                assertEquals(1, match.matchParts.size());
                List<MPCMatchPart> matchPart = match.matchParts;
                assertEquals(1, matchPart.get(0).startLineNumberA);
                assertEquals(1, matchPart.get(0).startLineNumberB);
                assertEndLinesOf100PrecentMatchPart(matchPart);
                assertEquals(100, matchPart.get(0).similarity, 0.0001);
                assertEquals(100, matchPart.get(0).similarityA, 0.0001);
                assertEquals(100, matchPart.get(0).similarityB, 0.0001);
            }
        }

        protected abstract void assertEndLinesOf100PrecentMatchPart(List<MPCMatchPart> matchPart);
    }

    protected abstract class withValidNonIdenticalFiles {

        TextFileUtility tfu;

        @Before
        public void setUp() throws IOException {
            tfu = new TextFileUtility(StandardCharsets.UTF_8);
            File javaFile3 = new File(sourceDir.getPath() + File.separator + "file3.java");
            tfu.createFileWithText(javaFile3, TextCreator.validMini2CodeExcample());
        }

        @Test
        public void filesWithTwoFileMathcesParts() throws Exception {
            File javaFile1 = new File(sourceDir.getPath() + File.separator + "file1.java");
            tfu.createFileWithText(javaFile1, TextCreator.validMiniCodeExcample());

            adapter.runPlagiarsimDetection(sourceDir);
            File matchFile = new File(sourceDir.getPath() + File.separator +MPCContext.MATCHES_DIR+ File.separator+"file3.java-file1.java-match");
            if(!matchFile.exists())
                matchFile = new File(sourceDir.getPath() + File.separator +MPCContext.MATCHES_DIR+ File.separator+"file1.java-file3.java-match");
            
            MPCMatch match = matchFileUtility.readFromFile(matchFile);
            
            assertMatchDataIsOk(matchFile, match);
            assertFirstMatchPartIsOk(match.matchParts);
            assertSecondMatchPartIsOk(match.matchParts);
            assertCalculatedMatchSimilarityIsOk(match);
        }

        @Test
        public void fileContainingFileTwice() throws IOException, ClassNotFoundException {
            File javaFile4 = new File(sourceDir.getPath() + File.separator + "file4.java");
            tfu.createFileWithText(javaFile4, TextCreator.validMini2CodeExcample()+TextCreator.validMini2CodeExcample());

            adapter.runPlagiarsimDetection(sourceDir);

            File matchFile = new File(sourceDir.getPath() + File.separator +MPCContext.MATCHES_DIR+ File.separator+"file4.java-file3.java-match");
            if(!matchFile.exists())
                matchFile = new File(sourceDir.getPath() + File.separator +MPCContext.MATCHES_DIR+ File.separator+"file3.java-file4.java-match");
            MPCMatch match = matchFileUtility.readFromFile(matchFile);

            assertMatchDataIsOkFileContaingingFileTwice(matchFile, match);
        }

        protected  abstract void assertMatchDataIsOkFileContaingingFileTwice(File matchFile, MPCMatch match);

        protected abstract void assertMatchDataIsOk(File matchFile, MPCMatch match);

        protected abstract void assertFirstMatchPartIsOk(List<MPCMatchPart> matchPart);

        protected abstract void assertSecondMatchPartIsOk(List<MPCMatchPart> matchPart);

        protected abstract void assertCalculatedMatchSimilarityIsOk(MPCMatch match);
    }

}
