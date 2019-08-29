package org.foi.mpc.cmdAcceptance;

import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.file.PropertiesFileUtility;
import org.foi.mpc.TestContext;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagJavaAdapter;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneJavaAdapter;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneTextAdapter;
import org.foi.mpc.executiontools.techniques.CommonCodeRemoveTechnique;
import org.foi.mpc.executiontools.techniques.NoTechniqueOriginal;
import org.foi.mpc.main.MultipleDetectionConfigReader;
import org.foi.mpc.main.MultipleDetectionConfiguration;
import org.foi.mpc.main.MultipleDetectionMain;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import static org.foi.mpc.main.MultipleDetectionMainTest.createTestConfiguration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CMDMultipleDetectionAcceptanceTest {
    File workingDir = new File("workingDir");
    File configurationFile = new File("testConfigurationFile.properties");

    @Before
    public void setUp(){
        workingDir.mkdir();
    }

    @After
    public void tearDown() throws IOException {
        DirectoryFileUtility.deleteDirectoryTree(workingDir);
        configurationFile.delete();
    }

    @Test
    public void canRunMultipleDetectionOnStudentsDirs(){
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        MultipleDetectionConfiguration configuration = createTestConfiguration();
        configuration.selectedWorkingDir = workingDir.getAbsolutePath();

        MultipleDetectionConfigReader reader = new MultipleDetectionConfigReader();
        reader.write(configurationFile,configuration);

        String[] args = {configurationFile.getAbsolutePath()};
        MultipleDetectionMain.main(args);

        String expected = "MULTIPLE DETECION DONE\n" +
                "\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"workingDir"+File.separator+"detection"+File.separator+"JPlagJava"+File.separator+"NoPreprocessing"+File.separator+"SubmissionFilesUnifier"+File.separator+"NWTiS"+File.separator+"2010-2011"+File.separator+"DZ2\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"workingDir"+File.separator+"detection"+File.separator+"JPlagJava"+File.separator+"NoPreprocessing"+File.separator+"SubmissionFilesUnifier"+File.separator+"NWTiS"+File.separator+"2010-2011"+File.separator+"DZ1\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"workingDir"+File.separator+"detection"+File.separator+"JPlagJava"+File.separator+"NoPreprocessing"+File.separator+"SubmissionFilesUnifier"+File.separator+"NWTiS"+File.separator+"2011-2012"+File.separator+"DZ2\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"workingDir"+File.separator+"detection"+File.separator+"JPlagJava"+File.separator+"NoPreprocessing"+File.separator+"SubmissionFilesUnifier"+File.separator+"NWTiS"+File.separator+"2011-2012"+File.separator+"DZ1\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"workingDir"+File.separator+"detection"+File.separator+"JPlagJava"+File.separator+"RemoveCommonCode"+File.separator+"SubmissionFilesUnifier"+File.separator+"NWTiS"+File.separator+"2010-2011"+File.separator+"DZ2\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"workingDir"+File.separator+"detection"+File.separator+"JPlagJava"+File.separator+"RemoveCommonCode"+File.separator+"SubmissionFilesUnifier"+File.separator+"NWTiS"+File.separator+"2010-2011"+File.separator+"DZ1\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"workingDir"+File.separator+"detection"+File.separator+"JPlagJava"+File.separator+"RemoveCommonCode"+File.separator+"SubmissionFilesUnifier"+File.separator+"NWTiS"+File.separator+"2011-2012"+File.separator+"DZ2\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"workingDir"+File.separator+"detection"+File.separator+"JPlagJava"+File.separator+"RemoveCommonCode"+File.separator+"SubmissionFilesUnifier"+File.separator+"NWTiS"+File.separator+"2011-2012"+File.separator+"DZ1\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"workingDir"+File.separator+"detection"+File.separator+"SIMGruneJava"+File.separator+"NoPreprocessing"+File.separator+"SubmissionFilesUnifier"+File.separator+"NWTiS"+File.separator+"2010-2011"+File.separator+"DZ2\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"workingDir"+File.separator+"detection"+File.separator+"SIMGruneJava"+File.separator+"NoPreprocessing"+File.separator+"SubmissionFilesUnifier"+File.separator+"NWTiS"+File.separator+"2010-2011"+File.separator+"DZ1\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"workingDir"+File.separator+"detection"+File.separator+"SIMGruneJava"+File.separator+"NoPreprocessing"+File.separator+"SubmissionFilesUnifier"+File.separator+"NWTiS"+File.separator+"2011-2012"+File.separator+"DZ2\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"workingDir"+File.separator+"detection"+File.separator+"SIMGruneJava"+File.separator+"NoPreprocessing"+File.separator+"SubmissionFilesUnifier"+File.separator+"NWTiS"+File.separator+"2011-2012"+File.separator+"DZ1\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"workingDir"+File.separator+"detection"+File.separator+"SIMGruneJava"+File.separator+"RemoveCommonCode"+File.separator+"SubmissionFilesUnifier"+File.separator+"NWTiS"+File.separator+"2010-2011"+File.separator+"DZ2\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"workingDir"+File.separator+"detection"+File.separator+"SIMGruneJava"+File.separator+"RemoveCommonCode"+File.separator+"SubmissionFilesUnifier"+File.separator+"NWTiS"+File.separator+"2010-2011"+File.separator+"DZ1\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"workingDir"+File.separator+"detection"+File.separator+"SIMGruneJava"+File.separator+"RemoveCommonCode"+File.separator+"SubmissionFilesUnifier"+File.separator+"NWTiS"+File.separator+"2011-2012"+File.separator+"DZ2\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"workingDir"+File.separator+"detection"+File.separator+"SIMGruneJava"+File.separator+"RemoveCommonCode"+File.separator+"SubmissionFilesUnifier"+File.separator+"NWTiS"+File.separator+"2011-2012"+File.separator+"DZ1\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"workingDir"+File.separator+"detection"+File.separator+"SIMGruneText"+File.separator+"NoPreprocessing"+File.separator+"SubmissionFilesUnifier"+File.separator+"NWTiS"+File.separator+"2010-2011"+File.separator+"DZ2\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"workingDir"+File.separator+"detection"+File.separator+"SIMGruneText"+File.separator+"NoPreprocessing"+File.separator+"SubmissionFilesUnifier"+File.separator+"NWTiS"+File.separator+"2010-2011"+File.separator+"DZ1\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"workingDir"+File.separator+"detection"+File.separator+"SIMGruneText"+File.separator+"NoPreprocessing"+File.separator+"SubmissionFilesUnifier"+File.separator+"NWTiS"+File.separator+"2011-2012"+File.separator+"DZ2\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"workingDir"+File.separator+"detection"+File.separator+"SIMGruneText"+File.separator+"NoPreprocessing"+File.separator+"SubmissionFilesUnifier"+File.separator+"NWTiS"+File.separator+"2011-2012"+File.separator+"DZ1\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"workingDir"+File.separator+"detection"+File.separator+"SIMGruneText"+File.separator+"RemoveCommonCode"+File.separator+"SubmissionFilesUnifier"+File.separator+"NWTiS"+File.separator+"2010-2011"+File.separator+"DZ2\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"workingDir"+File.separator+"detection"+File.separator+"SIMGruneText"+File.separator+"RemoveCommonCode"+File.separator+"SubmissionFilesUnifier"+File.separator+"NWTiS"+File.separator+"2010-2011"+File.separator+"DZ1\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"workingDir"+File.separator+"detection"+File.separator+"SIMGruneText"+File.separator+"RemoveCommonCode"+File.separator+"SubmissionFilesUnifier"+File.separator+"NWTiS"+File.separator+"2011-2012"+File.separator+"DZ2\n" +
                TestContext.doktorskiRadDir+"MPC"+File.separator+"workingDir"+File.separator+"detection"+File.separator+"SIMGruneText"+File.separator+"RemoveCommonCode"+File.separator+"SubmissionFilesUnifier"+File.separator+"NWTiS"+File.separator+"2011-2012"+File.separator+"DZ1";

        //System.out.println("A:"+outContent);
        //assertEquals(outContent.toString(),"TEST");
        assertTrue(outContent.toString().contains(expected));
        //TODO assertEquals taht Matches Dirs has some match count
    }
}
