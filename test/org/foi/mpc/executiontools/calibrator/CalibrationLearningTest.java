package org.foi.mpc.executiontools.calibrator;

import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagAdapterSettings;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagJavaAdapter;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagTextAdapter;
import org.foi.mpc.executiontools.detectionTools.sherlock.SherlockAdapterSettings;
import org.foi.mpc.executiontools.detectionTools.sherlock.SherlockOriginalAdapter;
import org.foi.mpc.executiontools.detectionTools.sherlock.SherlockTokenisedAdapter;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneAdapterSettings;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneJavaAdapter;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneTextAdapter;
import org.foi.mpc.executiontools.factories.SimilarityDetectionTool;
import org.foi.mpc.matches.MPCMatchFileUtility;
import org.foi.mpc.matches.models.MPCMatch;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;

@RunWith(Parameterized.class)
@Ignore
public class CalibrationLearningTest {

    private static String CALIBATION_ROOT_DIR = "testInputData"+File.separator+"calibrationLearningTests";
    private static String MATCHES_DIR = "matches";
    private static String MATCH_NAMEA = "001.java-002.java-match";
    private static String MATCH_NAMEB = "002.java-001.java-match";


    @Parameterized.Parameters(name = "inputDir : {0}")
    public static String[] data() {
        //String [] inputDirs = {"100Precent","100PrecentMixedSimple","100PrecentMixedComplex"};
        //String[] inputDirs = {"50Precent","50PrecentMixedSimple","50PrecentMixedComplex"};
        //String[] inputDirs = {"50Precent","50PrecentMixedSimple"};
        //String[] inputDirs = {"0Precent","soco003-004"};
        //String[] inputDirs = {"0Precent","0Precent2"};
        String[] inputDirs = {"0Precent","0Precent2","50Precent","50PrecentMixedSimple","soco003-004"};
        return inputDirs;
    }

    @Parameterized.Parameter(value = 0)
    public String inputDir;

    public File calibrationInputDir;
    DirectoryFileUtility dfu = new DirectoryFileUtility();
    private File calibrationWorkingDir;

    @Before
    public void setUp(){
        calibrationWorkingDir = new File("calibrationWorkingDir");
        calibrationInputDir = new File(CALIBATION_ROOT_DIR+File.separator+inputDir);
    }

    @After
    public void tearDown() throws IOException {
        DirectoryFileUtility.deleteDirectoryTree(calibrationWorkingDir);
    }

    @Test
    public void runJPlag() throws IOException {
        JPlagJavaAdapter javaAdapter = new JPlagJavaAdapter();
        JPlagTextAdapter textAdapter = new JPlagTextAdapter();
        String result = "";
        result = testJPlag(calibrationInputDir, javaAdapter);
        result += testJPlag(calibrationInputDir, textAdapter);
        System.out.println(result);
    }

    private String testJPlag(File inputDir, SimilarityDetectionTool tool) throws IOException {
        String result = tool.getName()+"\n";
        for(int i=1;i<30;i++) {
            prepareWorkingDir(inputDir);

            JPlagAdapterSettings settings = (JPlagAdapterSettings) tool.getSettings();
            settings.minTokenMatch = i;
            settings.matchesDirName = MATCHES_DIR;
            tool.setSettings(settings);

            result += runTestIterationOn(tool,i);
        }
        return result;
    }

    @Test
    public void runSim() throws IOException {
        SimGruneJavaAdapter javaAdapter = new SimGruneJavaAdapter();
        SimGruneTextAdapter textAdapter = new SimGruneTextAdapter();
        String result = "";
        result = testSIM(calibrationInputDir, javaAdapter);
        result += testSIM(calibrationInputDir, textAdapter);
        System.out.println(result);
    }

    private String testSIM(File inputDir, SimilarityDetectionTool tool) throws IOException {
        String result = tool.getName()+"\n";
        for(int i=1;i<30;i++) {
            prepareWorkingDir(inputDir);

            SimGruneAdapterSettings settings = (SimGruneAdapterSettings) tool.getSettings();
            settings.minRunLength = i;
            settings.matchesDirName = MATCHES_DIR;
            tool.setSettings(settings);

            result += runTestIterationOn(tool, i);
        }
        return result;
    }

    @Test
    public void runSherlock() throws IOException {
        SherlockTokenisedAdapter javaAdapter = new SherlockTokenisedAdapter();
        SherlockOriginalAdapter textAdapter = new SherlockOriginalAdapter();
        String result = "";
        //result = testSherlock(calibrationInputDir, javaAdapter);
        //result += "\n\n\n";
        result += testSherlock(calibrationInputDir, textAdapter);
        System.out.println(result);
    }

    private String testSherlock(File inputDir, SimilarityDetectionTool tool) throws IOException {
        String result = tool.getName()+"\n";
        int i=0;


            //for(int x=1;x<20;x++) {
            for (int minRunLenght = 1; minRunLenght < 15; minRunLenght++) {
                //if(minRunLenght==11)
                //    minRunLenght=14;
                result += "--------minRunLenght"+ minRunLenght +"\n";
                for (int maxFwdJMP = 1; maxFwdJMP < 20; maxFwdJMP++) {
                    //if(maxFwdJMP==5)
                    //    maxFwdJMP = 11;
                    //if(maxFwdJMP == 13)
                    //    maxFwdJMP = 15;
                    //if(maxFwdJMP == 16)
                    //    maxFwdJMP = 17;

                    prepareWorkingDir(inputDir);

                    SherlockAdapterSettings settings = (SherlockAdapterSettings) tool.getSettings();
                    settings.minRunLenght = minRunLenght; //6,6
                    settings.maxForwardJump = maxFwdJMP; //3,3
                    settings.maxBackwardJump = 1; //1,1 - no cahnge in combination with minRunLength and maxFwdJMP
                    settings.maxJumpDiff = 3; //3,3 1-3
                    settings.minStringLength = 8; //8,8  1 na 2 ima razliku dalje sve isto
                    settings.strictness = 2;//2,2 1-3 after 3 jako male sličnosti a morale bi biti preko 40
                    settings.matchesDirName = MATCHES_DIR;
                    tool.setSettings(settings);

                    result += "-maxFwdJMP"+ maxFwdJMP +"\n";
                    result += runTestIterationOn(tool, i);
                    i++;

                }
                //result += "x"+ x +"\n";
            }
        //}

        return result;
    }

    private String runTestIterationOn(SimilarityDetectionTool tool, int iteration) {
        String result = "";
        tool.runTool(calibrationWorkingDir);
        MPCMatchFileUtility mfu = new MPCMatchFileUtility();
        try {
            MPCMatch match;
            File a = new File(calibrationWorkingDir + File.separator + MATCHES_DIR+ File.separator + MATCH_NAMEA);
            if(a.exists())
                match = mfu.readFromFile(a);
            else
                match = mfu.readFromFile(new File(calibrationWorkingDir + File.separator + MATCHES_DIR+ File.separator + MATCH_NAMEB));
            result=iteration+" Similarity: " + match.similarity+"\n";
        } catch (Exception e) {
            return "";
        }
        return result;
    }

    private void prepareWorkingDir(File inputDir) throws IOException {
        DirectoryFileUtility.deleteDirectoryTree(calibrationWorkingDir);
        if(calibrationWorkingDir.exists())
            prepareWorkingDir(inputDir);
        calibrationWorkingDir.mkdir();
        dfu.copyDirectoryTree(inputDir, calibrationWorkingDir);
    }

}
