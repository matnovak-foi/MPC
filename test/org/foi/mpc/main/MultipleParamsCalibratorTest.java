package org.foi.mpc.main;

import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.file.PropertiesFileUtility;
import org.foi.common.filesystem.file.TextFileUtility;
import org.foi.mpc.TestContext;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagJavaAdapter;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneJavaAdapter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static org.foi.mpc.MPCContext.CalibrationConfigProperties.*;
import static org.junit.Assert.*;

public class MultipleParamsCalibratorTest {

    ToolCalibratorSpy calibratorSpy;
    File propFile = new File("testPropFile.properties");
    File resultDir = new File("testResultDir");

    @Before
    public void setUp(){
        Properties prop = new Properties();
        prop.setProperty(BASE_TOOL_NAME, JPlagJavaAdapter.NAME);
        prop.setProperty(BASE_PARAM_NAME+"1", "t");
        prop.setProperty(BASE_PARAM_VALUE+"1", "9");
        prop.setProperty(CALIBRATE_TOOL_NAME, SimGruneJavaAdapter.NAME);
        prop.setProperty(INPUT_DIR, "inputDir");
        prop.setProperty(WORKING_DIR, "workingDir");
        PropertiesFileUtility.createNewPropertiesFile(prop,propFile);
        resultDir.mkdir();
    }

    @After
    public void tearDown() throws IOException {
        propFile.delete();
        DirectoryFileUtility.deleteDirectoryTree(resultDir);
    }

    @Test
    public void canRunMultipleCalibrations() throws IOException {
        calibratorSpy = new ToolCalibratorSpy();
        MultipleParamsCalibrator calibrator = new MultipleParamsCalibrator();
        calibrator.setToolCalibrator(calibratorSpy);
        calibrator.run(new String[]{propFile.getAbsolutePath(),resultDir.getAbsolutePath()});
        assertEquals(29, calibratorSpy.runTimes);
        String expectedRuns = "\n"+
                TestContext.doktorskiRadDir+"MPC"+File.separator+"testPropFile.properties t=1\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"testPropFile.properties t=2\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"testPropFile.properties t=3\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"testPropFile.properties t=4\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"testPropFile.properties t=5\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"testPropFile.properties t=6\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"testPropFile.properties t=7\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"testPropFile.properties t=8\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"testPropFile.properties t=9\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"testPropFile.properties t=10\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"testPropFile.properties t=11\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"testPropFile.properties t=12\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"testPropFile.properties t=13\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"testPropFile.properties t=14\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"testPropFile.properties t=15\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"testPropFile.properties t=16\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"testPropFile.properties t=17\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"testPropFile.properties t=18\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"testPropFile.properties t=19\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"testPropFile.properties t=20\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"testPropFile.properties t=21\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"testPropFile.properties t=22\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"testPropFile.properties t=23\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"testPropFile.properties t=24\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"testPropFile.properties t=25\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"testPropFile.properties t=26\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"testPropFile.properties t=27\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"testPropFile.properties t=28\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"testPropFile.properties t=29";
        assertEquals(expectedRuns,calibratorSpy.argsRuns);
        assertEquals(29, resultDir.listFiles().length);
        for(File files : resultDir.listFiles()) {
            TextFileUtility tfu = new TextFileUtility(StandardCharsets.UTF_8);
            assertEquals(resultDir.getAbsolutePath(),tfu.readFileContentToString(files));
        }
    }

    public class ToolCalibratorSpy extends ToolCalibration {
        int runTimes = 0;
        String argsRuns = "";

        @Override
        void run(String[] args) {
            runTimes++;
            File file = new File(args[0]);
            Properties properties = PropertiesFileUtility.readProperties(file);
            argsRuns=argsRuns+"\n"+args[0]+" "+properties.getProperty(BASE_PARAM_NAME+"1")+"="+properties.getProperty(BASE_PARAM_VALUE+"1");
        }

        @Override
        public String getPresentedText() {
            return resultDir.getAbsolutePath();
        }
    }
}