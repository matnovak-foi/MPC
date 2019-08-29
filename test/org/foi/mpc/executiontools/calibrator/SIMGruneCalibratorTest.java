package org.foi.mpc.executiontools.calibrator;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.directory.InMemoryDir;
import org.foi.mpc.MPCContext;
import org.foi.mpc.executiontools.calibrator.models.BaseToolCase;
import org.foi.mpc.executiontools.calibrator.models.CalibratedToolCase;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneJavaAdapter;
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

import static org.foi.mpc.executiontools.calibrator.SimilarityDetectionToolCalibrator.*;
import static org.junit.Assert.assertEquals;

@RunWith(HierarchicalContextRunner.class)
public class SIMGruneCalibratorTest implements PhaseRunnerBuilderStub.PhaseBuilderSpyListener {
    private SimGruneJavaAdapter simTool = new SimGruneJavaAdapter();
    private SIMGruneCalibrator calibrator;
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

        calibrator = new SIMGruneCalibrator(phaseRunnerBuilderStub);

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
    public void findCorrectSimilarityOnFirstRun(){
        returnOkMatchOnRun = 1;
        List<CalibratedToolCase> simCalibrated = calibrator.calibrateToolForSimilarity(baseSimilarities, simTool).calibratedToolCaseList;

        assertEquals(29, phaseRunnerBuilderStub.wasRunTimes);
        assertEquals("testDir1",simCalibrated.get(0).caseName);
        assertEquals(100,simCalibrated.get(0).caseBestSimilarity, 0.001);
        assertEquals("r",simCalibrated.get(0).caseBestParams.get(0).paramName);
        assertEquals(1,simCalibrated.get(0).caseBestParams.get(0).paramValue);
    }

    @Test
    public void findCorrectSimilarityOnSecondRun(){
        returnOkMatchOnRun = 2;
        List<CalibratedToolCase> simCalibrated = calibrator.calibrateToolForSimilarity(baseSimilarities, simTool).calibratedToolCaseList;

        assertEquals(29, phaseRunnerBuilderStub.wasRunTimes);
        assertEquals("testDir1",simCalibrated.get(0).caseName);
        assertEquals("r",simCalibrated.get(0).caseBestParams.get(0).paramName);
        assertEquals(2,simCalibrated.get(0).caseBestParams.get(0).paramValue);
        assertEquals(100,simCalibrated.get(0).caseBestSimilarity, 0.001);
    }

    @Test
    public void generateSimilarityIfIsZero(){
        phaseRunnerBuilderStub.returnSomeMatch = false;
        List<CalibratedToolCase> simCalibrated = calibrator.calibrateToolForSimilarity(baseSimilarities, simTool).calibratedToolCaseList;

        assertEquals(29, phaseRunnerBuilderStub.wasRunTimes);
        assertEquals("testDir1",simCalibrated.get(0).caseName);
        assertEquals("r",simCalibrated.get(0).caseBestParams.get(0).paramName);
        assertEquals(2,simCalibrated.get(0).caseBestParams.get(0).paramValue);
        assertEquals(0,simCalibrated.get(0).caseBestSimilarity, 0.001);
    }

    @Test
    public void fromThreeCallsFirstIsBest(){
        returnOkMatchOnRun = 0;
        calibrator.calibrateToolForSimilarity(baseSimilarities, simTool);
        calibrator.processMatch(createMatch(100,"testDir1"));
        calibrator.processMatch(createMatch(0,"testDir1"));
        calibrator.processMatch(createMatch(0,"testDir1"));
        CalibratedToolCase calibratedCase = calibrator.getCalibratedSimilarityCases().get(0);
        assertEquals(100,calibratedCase.caseBestSimilarity,0.001);
    }

    @Test
    public void fromThreeCallsSecondIsBest(){
        returnOkMatchOnRun = 0;
        calibrator.calibrateToolForSimilarity(baseSimilarities, simTool);
        calibrator.processMatch(createMatch(0,"testDir1"));
        calibrator.processMatch(createMatch(100,"testDir1"));
        calibrator.processMatch(createMatch(0,"testDir1"));
        CalibratedToolCase calibratedCase = calibrator.getCalibratedSimilarityCases().get(0);
        assertEquals(100,calibratedCase.caseBestSimilarity,0.001);
    }

    @Test
    public void fromThreeCallsThirdIsBest(){
        returnOkMatchOnRun = 0;
        calibrator.calibrateToolForSimilarity(baseSimilarities, simTool);
        calibrator.processMatch(createMatch(0,"testDir1"));
        calibrator.processMatch(createMatch(0,"testDir1"));
        calibrator.processMatch(createMatch(100,"testDir1"));
        CalibratedToolCase calibratedCase = calibrator.getCalibratedSimilarityCases().get(0);
        assertEquals(100,calibratedCase.caseBestSimilarity,0.001);
    }

    @Test
    public void noSimilarityIsBestClosestIsFirst(){
        returnOkMatchOnRun = 0;
        calibrator.calibrateToolForSimilarity(baseSimilarities, simTool);
        calibrator.processMatch(createMatch(99,"testDir1"));
        calibrator.processMatch(createMatch(98,"testDir1"));
        calibrator.processMatch(createMatch(97,"testDir1"));
        CalibratedToolCase calibratedCase = calibrator.getCalibratedSimilarityCases().get(0);
        assertEquals(99,calibratedCase.caseBestSimilarity,0.001);
    }

    @Test
    public void noSimilarityIsBestClosestIsSecond(){
        returnOkMatchOnRun = 0;
        calibrator.calibrateToolForSimilarity(baseSimilarities, simTool);
        calibrator.processMatch(createMatch(98,"testDir1"));
        calibrator.processMatch(createMatch(99,"testDir1"));
        calibrator.processMatch(createMatch(97,"testDir1"));
        CalibratedToolCase calibratedCase = calibrator.getCalibratedSimilarityCases().get(0);
        assertEquals(99,calibratedCase.caseBestSimilarity,0.001);
    }

    @Test
    public void noSimilarityIsBestClosestIsThird(){
        returnOkMatchOnRun = 0;
        calibrator.calibrateToolForSimilarity(baseSimilarities, simTool);
        calibrator.processMatch(createMatch(97,"testDir1"));
        calibrator.processMatch(createMatch(98,"testDir1"));
        calibrator.processMatch(createMatch(99,"testDir1"));
        CalibratedToolCase calibratedCase = calibrator.getCalibratedSimilarityCases().get(0);
        assertEquals(99,calibratedCase.caseBestSimilarity,0.001);
    }

    @Test(expected = CasesMismatchException.class)
    public void invalidCalibrationCaseDirName(){
        baseSimilarities.clear();
        calibrator.calibrateToolForSimilarity(baseSimilarities, simTool);
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
        returnMatch.matchesDir = new File(simTool.getName()+File.separator+caseName+File.separator+MPCContext.MATCHES_DIR);
        returnMatch.similarity = similarity;
        return returnMatch;
    }
}
