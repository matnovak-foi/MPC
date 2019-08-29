package org.foi.mpc.executiontools.calibrator;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.directory.InMemoryDir;
import org.foi.mpc.MPCContext;
import org.foi.mpc.executiontools.calibrator.models.BaseToolCase;
import org.foi.mpc.executiontools.calibrator.models.CalibratedToolCase;
import org.foi.mpc.executiontools.detectionTools.sherlock.SherlockTokenisedAdapter;
import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.phases.runner.PhaseRunnerBuilderStub;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.foi.mpc.executiontools.calibrator.SimilarityDetectionToolCalibrator.CasesMismatchException;
import static org.junit.Assert.assertEquals;

@RunWith(HierarchicalContextRunner.class)
public class SherlockCalibratorTest implements PhaseRunnerBuilderStub.PhaseBuilderSpyListener {
    private SherlockTokenisedAdapter sherlockTool = new SherlockTokenisedAdapter();
    private SherlockCalibrator calibrator;
    private File workingDir;
    private PhaseRunnerBuilderStub phaseRunnerBuilderStub;
    private List<BaseToolCase> baseSimilarities;
    private int returnOkMatchOnRun;
    SherlockCalibraorConfig testConfig = new SherlockCalibraorConfig();

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

        calibrator = new SherlockCalibrator(phaseRunnerBuilderStub);

        baseSimilarities = new ArrayList<>();
        BaseToolCase similarity1 = new BaseToolCase();
        similarity1.similarity = 100;
        similarity1.caseName = "testDir1";
        baseSimilarities.add(similarity1);


        int[] values = {1,2};
        testConfig.maxFwdJmps = values;
        testConfig.maxJmpDiffs = values;
        testConfig.minRunLengths = values;
        testConfig.minStrLens = values;
        testConfig.strictness_s = values;
        testConfig.maxBwdJmp = 1;
        calibrator.setSherlockCalibratorConfig(testConfig);
    }

    @After
    public void tearDown() throws IOException {
        DirectoryFileUtility.deleteDirectoryTree(workingDir);
    }

    @Test
    public void findCorrectSimilarityOnFirstRun(){
        returnOkMatchOnRun = 1;
        List<CalibratedToolCase> sherlockCalibrated = calibrator.calibrateToolForSimilarity(baseSimilarities, sherlockTool).calibratedToolCaseList;

        assertEquals(32, phaseRunnerBuilderStub.wasRunTimes);
        assertEquals("testDir1",sherlockCalibrated.get(0).caseName);
        assertEquals(100,sherlockCalibrated.get(0).caseBestSimilarity, 0.001);
        assertEquals("maxBackwardJump",sherlockCalibrated.get(0).caseBestParams.get(0).paramName);
        assertEquals(1,sherlockCalibrated.get(0).caseBestParams.get(0).paramValue);
        assertEquals("maxForwardJump",sherlockCalibrated.get(0).caseBestParams.get(1).paramName);
        assertEquals(1,sherlockCalibrated.get(0).caseBestParams.get(1).paramValue);
        assertEquals("minRunLenght",sherlockCalibrated.get(0).caseBestParams.get(2).paramName);
        assertEquals(1,sherlockCalibrated.get(0).caseBestParams.get(2).paramValue);
        assertEquals("minStringLength",sherlockCalibrated.get(0).caseBestParams.get(3).paramName);
        assertEquals(1,sherlockCalibrated.get(0).caseBestParams.get(3).paramValue);
        assertEquals("maxJumpDiff",sherlockCalibrated.get(0).caseBestParams.get(4).paramName);
        assertEquals(1,sherlockCalibrated.get(0).caseBestParams.get(4).paramValue);
        assertEquals("strictness",sherlockCalibrated.get(0).caseBestParams.get(5).paramName);
        assertEquals(1,sherlockCalibrated.get(0).caseBestParams.get(5).paramValue);

    }

    @Test
    public void findCorrectSimilarityOnSecondRun(){
        returnOkMatchOnRun = 2;
        List<CalibratedToolCase> sherlockCalibrated = calibrator.calibrateToolForSimilarity(baseSimilarities, sherlockTool).calibratedToolCaseList;

        assertEquals(32, phaseRunnerBuilderStub.wasRunTimes);
        assertEquals("testDir1",sherlockCalibrated.get(0).caseName);
        assertEquals("maxBackwardJump",sherlockCalibrated.get(0).caseBestParams.get(0).paramName);
        assertEquals(1,sherlockCalibrated.get(0).caseBestParams.get(0).paramValue);
        assertEquals("maxForwardJump",sherlockCalibrated.get(0).caseBestParams.get(1).paramName);
        assertEquals(1,sherlockCalibrated.get(0).caseBestParams.get(1).paramValue);
        assertEquals("minRunLenght",sherlockCalibrated.get(0).caseBestParams.get(2).paramName);
        assertEquals(2,sherlockCalibrated.get(0).caseBestParams.get(2).paramValue);
        assertEquals("minStringLength",sherlockCalibrated.get(0).caseBestParams.get(3).paramName);
        assertEquals(1,sherlockCalibrated.get(0).caseBestParams.get(3).paramValue);
        assertEquals("maxJumpDiff",sherlockCalibrated.get(0).caseBestParams.get(4).paramName);
        assertEquals(1,sherlockCalibrated.get(0).caseBestParams.get(4).paramValue);
        assertEquals("strictness",sherlockCalibrated.get(0).caseBestParams.get(5).paramName);
        assertEquals(1,sherlockCalibrated.get(0).caseBestParams.get(5).paramValue);
    }

    @Test
    public void noCorrectMatchRunAllIterations() {
        phaseRunnerBuilderStub.returnSomeMatch = false;
        calibrator.calibrateToolForSimilarity(baseSimilarities, sherlockTool);
        assertEquals(32, phaseRunnerBuilderStub.wasRunTimes);
    }

    @Test(expected = CasesMismatchException.class)
    public void invalidCalibrationCaseDirName(){
        baseSimilarities.clear();
        calibrator.calibrateToolForSimilarity(baseSimilarities, sherlockTool);
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
        returnMatch.matchesDir = new File(sherlockTool.getName()+File.separator+caseName+File.separator+MPCContext.MATCHES_DIR);
        returnMatch.similarity = similarity;
        return returnMatch;
    }
}
