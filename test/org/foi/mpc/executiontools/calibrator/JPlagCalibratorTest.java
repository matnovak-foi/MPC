package org.foi.mpc.executiontools.calibrator;

import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.directory.InMemoryDir;
import org.foi.mpc.MPCContext;
import org.foi.mpc.executiontools.calibrator.models.BaseToolCase;
import org.foi.mpc.executiontools.calibrator.models.CalibratedToolCase;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagJavaAdapter;
import org.foi.mpc.executiontools.detectionTools.sherlock.SherlockTokenisedAdapter;
import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.phases.runner.PhaseRunnerBuilderStub;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JPlagCalibratorTest implements PhaseRunnerBuilderStub.PhaseBuilderSpyListener {
    private JPlagJavaAdapter jplagTool = new JPlagJavaAdapter();
    JPlagCalibrator calibrator;
    private File workingDir;
    private PhaseRunnerBuilderStub phaseRunnerBuilderStub;
    private List<BaseToolCase> baseSimilarities;
    private int returnOkMatchOnRun;

    @Before
    public void setUp(){
        workingDir = new File("workingDir");
        workingDir.mkdir();
        InMemoryDir inputDir = new InMemoryDir("inputDir");
        InMemoryDir testDir1 = new InMemoryDir("testDir1");
        inputDir.addFile(testDir1);

        phaseRunnerBuilderStub = new PhaseRunnerBuilderStub(MPCContext.MATCHES_DIR);
        phaseRunnerBuilderStub.spyListener = this;

        phaseRunnerBuilderStub.withWorkingDir(workingDir)
                .withSourceDir(inputDir)
                .toProcessDirs(Arrays.asList(inputDir.listFiles()))
                .withPreparePhaseAndPreprocessPhaseDisabled()
                .build();

        calibrator = new JPlagCalibrator(phaseRunnerBuilderStub);

        baseSimilarities = new ArrayList<>();
        BaseToolCase similarity1 = new BaseToolCase();
        similarity1.similarity = 100;
        similarity1.caseName = "testDir1";
        baseSimilarities.add(similarity1);
    }

    @After
    public void tearDown() throws IOException {
        DirectoryFileUtility.deleteDirectoryTree(workingDir);
    }

    @Test
    public void findCorrectSimilarity(){
        returnOkMatchOnRun = 1;
        List<CalibratedToolCase> jplagCalibrated = calibrator.calibrateToolForSimilarity(baseSimilarities, jplagTool).calibratedToolCaseList;

        assertEquals(29, phaseRunnerBuilderStub.wasRunTimes);
        assertEquals("testDir1",jplagCalibrated.get(0).caseName);
        assertEquals(100,jplagCalibrated.get(0).caseBestSimilarity, 0.001);
        assertEquals("t",jplagCalibrated.get(0).caseBestParams.get(0).paramName);
        assertEquals(1,jplagCalibrated.get(0).caseBestParams.get(0).paramValue);
    }

    @Test
    public void noCorrectMatchRunAll29Iterations() {
        phaseRunnerBuilderStub.returnSomeMatch = false;
        calibrator.calibrateToolForSimilarity(baseSimilarities, jplagTool);
        assertEquals(29, phaseRunnerBuilderStub.wasRunTimes);
    }

    @Test(expected = SimilarityDetectionToolCalibrator.CasesMismatchException.class)
    public void invalidCalibrationCaseDirName(){
        baseSimilarities.clear();
        calibrator.calibrateToolForSimilarity(baseSimilarities, jplagTool);
    }

    @Override
    public MPCMatch getMatchToReturn() {
        MPCMatch returnMatch = createMatch(100,"testDir1");
        if(returnOkMatchOnRun == 1)
            returnMatch.similarity = 100;
        else
            returnMatch.similarity = 0;
        returnOkMatchOnRun--;
        return returnMatch;
    }

    private MPCMatch createMatch(float similarity, String caseName){
        MPCMatch returnMatch = new MPCMatch();
        returnMatch.matchesDir = new File(jplagTool.getName()+File.separator+caseName+File.separator+MPCContext.MATCHES_DIR);
        returnMatch.similarity = similarity;
        return returnMatch;
    }
}
