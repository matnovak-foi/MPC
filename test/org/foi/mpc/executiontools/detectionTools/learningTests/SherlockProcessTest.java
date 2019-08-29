package org.foi.mpc.executiontools.detectionTools.learningTests;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.file.TextFileUtility;
import org.foi.common.filesystem.file.TextCreator;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

import uk.ac.warwick.dcs.cobalt.sherlock.FileTypeProfile;
import uk.ac.warwick.dcs.cobalt.sherlock.Samelines;
import uk.ac.warwick.dcs.cobalt.sherlock.Settings;
import uk.ac.warwick.dcs.cobalt.sherlock.SherlockProcess;
import uk.ac.warwick.dcs.cobalt.sherlock.SherlockProcessCallback;
import uk.ac.warwick.dcs.cobalt.sherlock.SherlockProcessException;
import uk.ac.warwick.dcs.cobalt.sherlock.TokeniseFiles;

@Ignore
public class SherlockProcessTest implements SherlockProcessCallback {
    File testDir;
    
    @Before
    public void setUp() throws IOException {
        TextFileUtility tfu = new TextFileUtility(StandardCharsets.UTF_8);
        testDir = new File("SherlockProcessTestDir");
        testDir.mkdir();
        File file1 = new File(testDir.getPath()+ File.separator + "test.txt");
        File file2 = new File(testDir.getPath()+ File.separator + "test2.txt");
        tfu.createFileWithText(file1, TextCreator.getSourceCodeExample1());
        tfu.createFileWithText(file2, TextCreator.getSourceCodeExample1());
        testDir.mkdir();
        
        setUpSherlock(new File[]{file1, file2});
    }
    
    @After
    public void tearDown() throws IOException {
       DirectoryFileUtility.deleteDirectoryTree(testDir);
    }

    
    private void setUpSherlock(File... files){
        Settings.setSourceDirectory(testDir);
        Settings.init();
        Settings.getSherlockSettings().setJava(true);
        setAllTypeSettings(false, true, true, false, 1, 3, 3, 8, 3, 2);
        setTypeSettings(Settings.TOK, true, false, 1, 3, 3, 5, 1, 2);
        Settings.setFileList(files);
        File logFile = new File(testDir.getAbsolutePath()+"logFile.log");
        System.out.println(Settings.getLogFile().getAbsolutePath());
        Settings.setLogFile(logFile);
      
    }

    
    @Test
    public void intializeSherlock() {
        SherlockProcess sp = new Samelines(this);
    }

    @Test
    public void runSherlockPreprocess() {
        sherlockPreprocess();
        File noComments = new File(testDir.getPath()+File.separator+"nocomments");
        File noCommentsNormalised = new File(testDir.getPath()+File.separator+"nocomnor");
        File noComentsNoWhiteSpaces = new File(testDir.getPath()+File.separator+"nocomwhi");
        File normalised = new File(testDir.getPath()+File.separator+"normalised");
        File noWhiteSpaces = new File(testDir.getPath()+File.separator+"nowhite");
        File original = new File(testDir.getPath()+File.separator+"original");
        File tokenised = new File(testDir.getPath()+File.separator+"tokenised");
        assertTrue(noComments.exists());
        assertTrue(noCommentsNormalised.exists());
        assertTrue(noComentsNoWhiteSpaces.exists());
        assertTrue(normalised.exists());
        assertTrue(noWhiteSpaces.exists());
        assertTrue(original.exists());
        assertTrue(tokenised.exists());
        assertTrue(tokenised.list().length == 2);
    }

    @Test
    public void runSherlock() {
        sherlockPreprocess();
        
        SherlockProcess sp = new Samelines(this);
        sp.start();
        try {
            sp.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(SherlockProcessTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        File matches = new File(testDir.getPath()+File.separator+"match");
        assertTrue(matches.exists());
        assertTrue(matches.list().length == 7);
    }
    
    private void sherlockPreprocess(){
        SherlockProcess process = new TokeniseFiles(this);
        process.setNatural(false);
        process.setPriority(Thread.NORM_PRIORITY - 1);
        process.start();
        try {
            process.join();
        } catch (InterruptedException ex) {
            System.err.println("Error join thread preproces files");
        }
    }
    
    private void setAllTypeSettings(boolean useCommentsOnly, boolean useTokenized, boolean amalgamate, boolean concatanate, int maxBackwardJump, int maxForwardJump, int minRunLenght, int minStringLength, int maxJumpDiff, int strictness) {
        for (int x = 0; x < Settings.NUMBEROFFILETYPES; x++) {
            FileTypeProfile profile = Settings.getFileTypes()[x];
            if (x == Settings.SEN || x == Settings.COM) {
                profile.setInUse(useCommentsOnly);
            } else if (x == Settings.TOK) {
                profile.setInUse(useTokenized);
            } else {
                profile.setInUse(true);
            }

            profile.setAmalgamate(amalgamate);
            profile.setConcatanate(concatanate);
            profile.setMaxBackwardJump(maxBackwardJump);
            profile.setMaxForwardJump(maxForwardJump);
            profile.setMinRunLength(minRunLenght);
            profile.setMinStringLength(minStringLength);
            profile.setMaxJumpDiff(maxJumpDiff);
            profile.setStrictness(strictness);

            //profile.store();
        }
    }

    private void setTypeSettings(int type, boolean amalgamate, boolean concatanate, int maxBackwardJump, int maxForwardJump, int minRunLenght, int minStringLength, int maxJumpDiff, int strictness) {
        FileTypeProfile profile = Settings.getFileTypes()[type];

        profile.setAmalgamate(amalgamate);
        profile.setConcatanate(concatanate);
        profile.setMaxBackwardJump(maxBackwardJump);
        profile.setMaxForwardJump(maxForwardJump);
        profile.setMinRunLength(minRunLenght);
        profile.setMinStringLength(minStringLength);
        profile.setMaxJumpDiff(maxJumpDiff);
        profile.setStrictness(strictness);

        //profile.store();
    }
    
    

    @Override
    public void exceptionThrown(SherlockProcessException spe) {

    }

}
