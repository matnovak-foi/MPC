package org.foi.mpc.executiontools.detectionTools.sherlock;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.foi.mpc.MPCContext;
import org.foi.mpc.executiontools.detectionTools.AdaptersTestTemplate;
import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.matches.models.MPCMatchPart;
import org.foi.mpc.executiontools.factories.SimilarityDetectionToolFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.foi.mpc.executiontools.detectionTools.sherlock.SherlockAdapter.SherlockFileType;

import org.foi.common.filesystem.directory.DirectoryCreator;
import static org.foi.common.filesystem.directory.DirectoryCreator.assertDirHasFilesCount;
import org.junit.After;
import uk.ac.warwick.dcs.cobalt.sherlock.FileTypeProfile;
import uk.ac.warwick.dcs.cobalt.sherlock.Samelines;
import uk.ac.warwick.dcs.cobalt.sherlock.Settings;
import uk.ac.warwick.dcs.cobalt.sherlock.TokeniseFiles;

@RunWith(HierarchicalContextRunner.class)
public class SherlockOriginalAdapterTest extends AdaptersTestTemplate {

    @Before
    @Override
    public void setUp() throws IOException {
        super.setUp();
        adapter = (new SimilarityDetectionToolFactory(MPCContext.MATCHES_DIR)).createTool(SherlockOriginalAdapter.NAME);
    }

    @After
    public void thearDown() throws IOException {
        super.tearDown();
        Settings.getSherlockSettings().setJava(false);
        Settings.setLogFile(null);
        for (FileTypeProfile ftp : Settings.getFileTypes()) {
            ftp.clear();
        }
    }

    public class withDummySourceDirAndNoRealRun extends AdaptersTestTemplate.withDummySourceDirAndNoRealRun {

        @Before
        @Override
        public void setUp() throws IOException {
            super.setUp();
            adapter = new SherlockOriginalAdapterStub();
        }

        @Test
        @Override
        public void defaultOptionsAreOk() {
            adapter.runPlagiarsimDetection(sourceDir);
            defultGlobalSettingForDetectionAreOk();
            defaultProfileSettingForDetectionAreOK();
        }

        private void defultGlobalSettingForDetectionAreOk() {
            assertGlobalSettingForDetectionAreOK();
            assertFileTypeIsEnabled(SherlockFileType.ORIGINAL);
            assertFileTypeIsDisabled(SherlockFileType.TOKENISED);
        }

        private void assertGlobalSettingForDetectionAreOK() {
            File logFile = new File(sourceDir.getPath() + File.separator + "logFile.log");
            assertEquals(logFile.getPath(), Settings.getLogFile().getPath());
            assertTrue(Settings.getSherlockSettings().isJava());
            assertFalse(Settings.isRunningGUI());
            assertFileTypeIsDisabled(SherlockFileType.SENTANCEBASED);
            assertFileTypeIsDisabled(SherlockFileType.COMMENTS);
            assertFileTypeIsDisabled(SherlockFileType.NOCOMMENTS);
            assertFileTypeIsDisabled(SherlockFileType.NORMALISED);
            assertFileTypeIsDisabled(SherlockFileType.NOWHITESPACES);
            assertFileTypeIsDisabled(SherlockFileType.NOCOMMENTSNORMALISED);
            assertFileTypeIsDisabled(SherlockFileType.NOCOMMENTSNOWHITESPACES);
        }

        private void assertFileTypeIsEnabled(SherlockFileType fileType) {
            assertTrue(Settings.getFileTypes()[fileType.getValue()].isInUse());
        }

        private void assertFileTypeIsDisabled(SherlockFileType fileType) {
            assertFalse(Settings.getFileTypes()[fileType.getValue()].isInUse());
        }

        private void defaultProfileSettingForDetectionAreOK() {
            FileTypeProfile profile = getProfileSettings(SherlockFileType.ORIGINAL);
            assertTrue(profile.getAmalgamate());
            assertFalse(profile.getConcatanate());
            assertEquals(profile.getMaxBackwardJump(), 1);
            assertEquals(profile.getMaxForwardJump(), 3);
            assertEquals(profile.getMinRunLength(), 3);
            assertEquals(profile.getMinStringLength(), 8);
            assertEquals(profile.getMaxJumpDiff(), 3);
            assertEquals(profile.getStrictness(), 2);

            assertEquals(sourceDir, Settings.getSourceDirectory());
        }

        @Test
        public void sherlockPrepareForDetectionWorks() throws IOException {
            SherlockAdapter sa = (SherlockAdapter) adapter;
            Settings.setFileList(new File[]{});
            sa.prepareForDetection();

            assertTrue(sa.getProcess() != null);
            assertTrue(sa.getProcess() instanceof Samelines);
        }

        @Test
        public void preprocesObjectIsSetUpOk() throws IOException {
            SherlockAdapter sa = (SherlockAdapter) adapter;
            sa.prepareForPreprocess(sourceDir);

            assertTrue(sa.getProcess() != null);
            assertTrue(sa.getProcess() instanceof TokeniseFiles);
            assertFalse(sa.getProcess().getNatural());
            assertEquals(sa.getProcess().getPriority(), Thread.NORM_PRIORITY - 1);
        }

        private FileTypeProfile getProfileSettings(SherlockFileType fileType) {
            return Settings.getFileTypes()[fileType.getValue()];
        }
        
        @Override
        public void isExecutionPhaseAndHasCorrectGetName() {
            super.isExecutionPhaseAndHasCorrectGetName();
            assertEquals(SherlockOriginalAdapter.NAME,adapter.getName());
        }
    }

    @Test
    public void noErrorsWhenProgramIsRunWithInvalidSubmissions() throws IOException {
        DirectoryCreator.createTestDirWithNFiles(sourceDir, 2);
        adapter.runPlagiarsimDetection(sourceDir);
    }

    public class withValidIdenticalFiles extends AdaptersTestTemplate.withValidIdenticalFiles {

        @Test
        public void sourceFilesWereLoaded() throws IOException {
            SherlockAdapter sa = (SherlockAdapter) adapter;
            sa.prepareForPreprocess(sourceDir);

            assertTrue(Settings.getFileList().length == 2);
        }

        @Test
        public void preprocesIsRun() throws IOException {
            File originalDir = new File(sourceDir.getPath() + File.separator + "original");
            SherlockAdapter sa = (SherlockAdapter) adapter;
            sa.runPreporcess(sourceDir);

            assertFileTypeIsEnabled(SherlockFileType.ORIGINAL);
            assertEquals(Settings.getSourceDirectory().toPath(), sourceDir.toPath());
            assertTrue(originalDir.exists());
            assertDirHasFilesCount(2, originalDir);
        }

        @Test
        public void sherlockDetectionWithPreprocessWorks() throws IOException {
            adapter.runPlagiarsimDetection(sourceDir);

            assertPreprocessingWorks(sourceDir, "original");
            assertDetectionWorks(sourceDir);
        }

        private void assertPreprocessingWorks(File sourceDir, String preprocessFolderName) {
            File preprocessDir = new File(sourceDir.getPath() + File.separator + preprocessFolderName);
            assertTrue(preprocessDir.exists());
            assertDirHasFilesCount(2, preprocessDir);
        }

        private void assertDetectionWorks(File sourceDir) {
            File match = new File(sourceDir.getPath() + File.separator + "match");
            assertTrue(match.exists());
            assertDirHasFilesCount(1, match);
        }

        @Override
        protected void assertEndLinesOf100PrecentMatchPart(List<MPCMatchPart> matchPart) {
            assertEquals(19, matchPart.get(0).endLineNumberA);
            assertEquals(19, matchPart.get(0).endLineNumberB);
        }

    }

    public class withValidNonIdenticalFiles extends AdaptersTestTemplate.withValidNonIdenticalFiles {

        @Override
        protected void assertMatchDataIsOkFileContaingingFileTwice(File matchFile, MPCMatch match) {
            assertEquals(matchFile.getName().split("-")[0], match.fileAName);
            assertEquals(matchFile.getName().split("-")[1], match.fileBName);
            System.out.println(match.similarity+" "+match.similarityA+" "+match.similarityB+" "+ match.matchParts.size());
            assertEquals(100, match.similarity, 0.0001);
            assertEquals(100, match.similarityA, 0.0001);
            assertEquals(100, match.similarityB, 0.0001);
            assertEquals(2, match.matchParts.size());
        }

        @Override
        protected void assertMatchDataIsOk(File matchFile, MPCMatch match) {
            assertEquals(matchFile.getName().split("-")[0], match.fileAName);
            assertEquals(matchFile.getName().split("-")[1], match.fileBName);
            assertEquals(59, match.similarity, 0.0001);
            assertEquals(59, match.similarityA, 0.0001);
            assertEquals(59, match.similarityB, 0.0001);
            assertEquals(1, match.matchParts.size());
        }

        @Override
        protected void assertFirstMatchPartIsOk(List<MPCMatchPart> matchPart) {
            assertEquals(13, matchPart.get(0).startLineNumberA);
            assertEquals(7, matchPart.get(0).startLineNumberB);
            assertEquals(25, matchPart.get(0).endLineNumberA);
            assertEquals(19, matchPart.get(0).endLineNumberB);
            assertEquals(59, matchPart.get(0).similarity, 0.0001);
            assertEquals(59, matchPart.get(0).similarityA, 0.0001);
            assertEquals(59, matchPart.get(0).similarityB, 0.0001);
        }

        @Override
        protected void assertSecondMatchPartIsOk(List<MPCMatchPart> matchPart) {
           //DID NOT GENERATE SECOND PART BUT TOKENIZED VERISION DID
        }

        @Override
        protected void assertCalculatedMatchSimilarityIsOk(MPCMatch match) {
           assertEquals(match.similarity, match.calculatedSimilarity, 0.0001);
           assertEquals(match.similarityA, match.calculatedSimilarityA, 0.0001);
           assertEquals(match.similarityB, match.calculatedSimilarityB, 0.0001);
        }
    }

    @Test
    public void isNotThreadSafe() throws InterruptedException {
        SherlockAdapter sa = (SherlockAdapter) adapter;
        Runnable r = new Runnable() {
            @Override
            public void run() {
                File sourceDir = new File("isThreadSafe");
                sa.setUpSherlockSettings(sourceDir);
            }
        };
        Thread t = new Thread(r);
        File otherSourceDir = new File("otherDir");
        Settings.setSourceDirectory(otherSourceDir);
        t.start();
        t.join();
        assertNotEquals(otherSourceDir.getPath(), Settings.getSourceDirectory().getPath());
        //TODO make it thread safe by making a main method in SherlockAdapter and provide an second
        //adapter that calls this one as separate process.
    }

    private void assertFileTypeIsEnabled(SherlockFileType fileType) {
        assertTrue(Settings.getFileTypes()[fileType.getValue()].isInUse());
    }

    private class SherlockOriginalAdapterStub extends SherlockOriginalAdapter {

        @Override
        protected void runProcess() {
        }

    }
}
