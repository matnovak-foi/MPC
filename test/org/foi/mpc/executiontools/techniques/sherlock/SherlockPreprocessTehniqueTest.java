package org.foi.mpc.executiontools.techniques.sherlock;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.foi.common.MPCexcpetions;
import org.foi.mpc.executiontools.techniques.PreprocessDirectoryCleaner;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.directory.DirectoryFileUtility.DirDoesNotExistException;
import org.foi.common.filesystem.directory.DirectoryFileUtility.DirIsEmptyException;
import org.foi.mpc.phases.executionphases.ExecutionTool;
import org.foi.mpc.executiontools.detectionTools.sherlock.SherlockAdapter;
import org.foi.mpc.executiontools.detectionTools.sherlock.SherlockOriginalAdapter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.foi.mpc.executiontools.detectionTools.sherlock.SherlockAdapter.SherlockFileType;

import org.foi.mpc.executiontools.factories.PreprocessingTechnique;
import org.foi.common.filesystem.directory.DirectoryCreator;

import static org.foi.common.filesystem.directory.DirectoryCreator.assertDirHasFilesCount;

import org.junit.After;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import uk.ac.warwick.dcs.cobalt.sherlock.FileTypeProfile;
import uk.ac.warwick.dcs.cobalt.sherlock.Settings;
import uk.ac.warwick.dcs.cobalt.sherlock.TokeniseFiles;

@RunWith(Parameterized.class)
public class SherlockPreprocessTehniqueTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Parameterized.Parameters(name = "{index}: classRun: {2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new SherlockNoCommentsNormalisedPPTechnique(), SherlockFileType.NOCOMMENTSNORMALISED, SherlockNoCommentsNormalisedPPTechnique.NAME},
                {new SherlockNoCommentsNoWhiteSpacesPPTechnique(), SherlockFileType.NOCOMMENTSNOWHITESPACES, SherlockNoCommentsNoWhiteSpacesPPTechnique.NAME},
                {new SherlockNoCommentsPPTechnique(), SherlockFileType.NOCOMMENTS, SherlockNoCommentsPPTechnique.NAME},
                {new SherlockNormalisePPTechnique(), SherlockFileType.NORMALISED, SherlockNormalisePPTechnique.NAME},
                {new SherlockNoWhiteSpacesPPTechnique(), SherlockFileType.NOWHITESPACES, SherlockNoWhiteSpacesPPTechnique.NAME}
        });
    }

    @Parameterized.Parameter(value = 0)
    public SherlockPreprocessingTechnique sherlock;
    @Parameterized.Parameter(value = 1)
    public SherlockFileType sherlockFileType;
    @Parameterized.Parameter(value = 2)
    public String techniqueName;

    protected File sourceDir;

    @Before
    public void setUp() throws IOException {
        sourceDir = new File("sourceDir");
        sourceDir.mkdir();
    }

    @After
    public void thearDown() throws IOException {
        DirectoryFileUtility.deleteDirectoryTree(sourceDir);
        Settings.getSherlockSettings().setJava(false);
        Settings.setLogFile(null);
        for (FileTypeProfile ftp : Settings.getFileTypes()) {
            ftp.clear();
        }
    }

    @Test(expected = MPCexcpetions.IsNullException.class)
    public void sourceDirIsNull() {
        sherlock.runPreporcess(null);
    }

    @Test(expected = DirDoesNotExistException.class)
    public void sourceDirDoesNotExist() {
        sherlock.runPreporcess(new File("NonExistingDir"));
    }

    @Test(expected = DirIsEmptyException.class)
    public void sourceDirIsEmpty() throws IOException {
        sherlock.runPreporcess(sourceDir);
    }

    @Test
    public void isExecutionPhaseAndHasCorrectGetName() throws IOException {
        assertTrue(sherlock instanceof ExecutionTool);
        assertTrue(sherlock instanceof PreprocessingTechnique);
        assertNotNull(sherlock.getName());
        assertEquals(techniqueName, sherlock.getName());
    }

    @Test
    public void preprocesIsRunOk() throws IOException {
        DirectoryCreator.createTestDirWithNFiles(sourceDir, 2);
        sherlock.runPreporcess(sourceDir);
        assertpreprocesObjectIsSetUpOk();
        assertSourceFilesWereLoaded();
        assertOnlyOneProfileIsEnabled(sherlockFileType);
        assertPreprocessingWorks(sherlock.getSherlockPreprocessDirName());
    }

    private void assertSourceFilesWereLoaded() throws IOException {
        assertTrue(Settings.getFileList().length == 2);
    }

    private void assertpreprocesObjectIsSetUpOk() throws IOException {
        assertTrue(sherlock.getProcess() != null);
        assertTrue(sherlock.getProcess() instanceof TokeniseFiles);
        assertFalse(sherlock.getProcess().getNatural());
        assertEquals(sherlock.getProcess().getPriority(), Thread.NORM_PRIORITY - 1);
    }

    @Test
    public void preprocessIsRunOkWhenNewInstanceOfSherlockIsCreated() throws IOException {
        DirectoryCreator.createTestDirWithNFiles(sourceDir, 2);
        SherlockAdapter newAdapterInstance = new SherlockOriginalAdapter();

        sherlock.runPreporcess(sourceDir);

        assertFalse(Settings.getFileTypes()[SherlockFileType.ORIGINAL.getValue()].isInUse());
        assertPreprocessingWorks(sherlock.getSherlockPreprocessDirName());
    }


    @Test
    public void runPrepprocessAndDeleteAreCalledInRightOrderOneTime() throws IOException {
        SherlockPreprocessingTechniqueSpy sherlockTechniqueSpy = new SherlockPreprocessingTechniqueSpy(sherlockFileType, sherlock.getName());
        sherlockTechniqueSpy.runTool(sourceDir);
        assertEquals("PreprocessDelete", sherlockTechniqueSpy.runOrder);
    }

    @Test
    public void whenRunToolEndResultContainsOnlyFilesWithCorrectExtension() throws IOException {
        DirectoryCreator dc = new DirectoryCreator();
        dc.createTestDirStructure(sourceDir, DirectoryCreator.DirTypes.submission);

        sherlock.runTool(sourceDir);

        assertDirHasFilesCount(2, sourceDir);
        assertTrue(sourceDir.listFiles()[0].getName().endsWith(".java"));
        assertTrue(sourceDir.listFiles()[1].getName().endsWith(".java"));
    }

    @Test
    public void errorWithRename() throws IOException {
        if(techniqueName.equalsIgnoreCase(SherlockNoCommentsPPTechnique.NAME)) {
            exception.expect(SherlockPreprocessingTechnique.DirectoryNameDuplication.class);
            File inputDir = new File("testInputData" + File.separator + "nocRenameError");
            DirectoryFileUtility dfu = new DirectoryFileUtility();
            dfu.copyDirectoryTree(inputDir, sourceDir);

            sherlock.runTool(sourceDir);

            assertDirHasFilesCount(2, sourceDir);
        }
    }

    private void assertOnlyOneProfileIsEnabled(SherlockFileType fileType) {
        for (int x = 0; x < Settings.NUMBEROFFILETYPES; x++) {
            FileTypeProfile profile = Settings.getFileTypes()[x];
            if (profile.isInUse()) {
                assertEquals(fileType.getValue(), x);
            }
        }
    }

    private void assertPreprocessingWorks(String preprocessFolderName) {
        File preprocessDir = new File(sourceDir.getPath() + File.separator + preprocessFolderName);
        assertTrue(preprocessDir.exists());
        assertDirHasFilesCount(2, preprocessDir);
    }

    private class SherlockPreprocessingTechniqueSpy extends SherlockPreprocessingTechnique {

        String runOrder = "";

        SherlockPreprocessingTechniqueSpy(SherlockFileType fileType, String techniqueName) {
            super(fileType, techniqueName);

            setCleaner(new PreprocessDirectoryCleaner(getName()){
                @Override
                public void clean(File preprocessedDir) {
                    runOrder += "Delete";
                }
            });
        }

        @Override
        public void runPreporcess(File sourceDir) {
            runOrder += "Preprocess";
        }
    }
}
