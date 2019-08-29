package org.foi.mpc.executiontools.calibrator;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.foi.mpc.MPCContext;
import org.foi.mpc.executiontools.calibrator.models.BaseToolCase;
import org.foi.mpc.executiontools.calibrator.models.CalibratedToolCase;
import org.foi.mpc.executiontools.calibrator.models.CalibratedToolCases;
import org.foi.mpc.executiontools.calibrator.models.CalibratedToolParam;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneJavaAdapter;
import org.foi.mpc.executiontools.factories.SimilarityDetectionTool;
import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.phases.runner.PhaseRunnerBuilder;
import org.foi.mpc.phases.runner.PhaseRunnerBuilderDummy;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(HierarchicalContextRunner.class)
public class SimilarityDetectionToolCalibratorTest {

    private SimGruneJavaAdapter tool = new SimGruneJavaAdapter();
    private int testParamDirs;
    private int testParamIterations;
    private int bestIterationCase1;
    private int bestIterationCase2;
    private int testParam;
    private SimilarityDetectionToolCalibrator calibrator;
    private List<BaseToolCase> baseSimilarities;

    @Before
    public void setUp() throws Exception {
        calibrator = new SimilarityDetectionToolCalibratorStub(new PhaseRunnerBuilderDummy(""));
    }

    @After
    public void tearDown() throws Exception {
    }

    public class bestSimilarityFoundBasedOnDifference {

        @Before
        public void setUp() {
            baseSimilarities = new ArrayList<>();
            BaseToolCase similarity1 = new BaseToolCase();
            similarity1.similarity = 100;
            similarity1.caseName = "testDir1";
            baseSimilarities.add(similarity1);

            BaseToolCase similarity2 = new BaseToolCase();
            similarity2.similarity = 50;
            similarity2.caseName = "testDir2";
            baseSimilarities.add(similarity2);
        }

        @Test
        public void withNoMatch(){
            testParamIterations = 1;
            testParamDirs = 1;
            bestIterationCase1 = 1;
            CalibratedToolCases cases = calibrator.calibrateToolForSimilarity(baseSimilarities, tool);

            CalibratedToolCase calibratedCase1 = cases.calibratedToolCaseList.get(0);
            CalibratedToolCase calibratedCase2 = cases.calibratedToolCaseList.get(1);

            assertEquals(100, calibratedCase1.caseBestSimilarity, 0.001);
            assertEquals(100, calibratedCase1.optimalSimilarity, 0.001);
            assertEquals(0, calibratedCase1.optimalSimilarityDiff, 0.001);

            assertEquals(0, calibratedCase2.caseBestSimilarity, 0.001);
            assertEquals(0, calibratedCase2.optimalSimilarity, 0.001);
            assertEquals(50, calibratedCase2.optimalSimilarityDiff, 0.001);

            assertEquals(1,cases.optimalParams.get(0).paramValue);

            assertEquals(50,(Float) cases.diffForAllParamCombos.values().toArray()[0], 0.001);
        }

        @Test
        public void withTwoDirsAndOneIteration() {
            testParamIterations = 1;
            testParamDirs = 2;
            bestIterationCase1 = 1;
            bestIterationCase2 = 1;

            CalibratedToolCases cases = calibrator.calibrateToolForSimilarity(baseSimilarities, tool);

            CalibratedToolCase calibratedCase1 = cases.calibratedToolCaseList.get(0);
            CalibratedToolCase calibratedCase2 = cases.calibratedToolCaseList.get(1);

            assertEquals(100, calibratedCase1.caseBestSimilarity, 0.001);
            assertEquals(100, calibratedCase1.optimalSimilarity, 0.001);
            assertEquals(0, calibratedCase1.optimalSimilarityDiff, 0.001);

            assertEquals(50, calibratedCase2.caseBestSimilarity, 0.001);
            assertEquals(50, calibratedCase2.optimalSimilarity, 0.001);
            assertEquals(0, calibratedCase2.optimalSimilarityDiff, 0.001);

            assertEquals(1,cases.optimalParams.get(0).paramValue);
        }

        @Test
        public void withOneDirAndThreeIterationsLastIsBest() {
            testParamIterations = 3;
            testParamDirs = 1;
            bestIterationCase1 = 3;

            CalibratedToolCases cases = calibrator.calibrateToolForSimilarity(baseSimilarities, tool);

            CalibratedToolCase calibratedCase1 = cases.calibratedToolCaseList.get(0);

            assertEquals(100, calibratedCase1.caseBestSimilarity, 0.001);
            assertEquals(100, calibratedCase1.optimalSimilarity, 0.001);
            assertEquals(0, calibratedCase1.optimalSimilarityDiff, 0.001);
            assertEquals(3,cases.optimalParams.get(0).paramValue);
            assertEquals(1,cases.optimalParams.size());
        }

        @Test
        public void withTwoDirsAndTwoIterationLastIsBestForBoth() {
            testParamIterations = 3;
            testParamDirs = 2;
            bestIterationCase1 = 3;
            bestIterationCase2 = 3;

            CalibratedToolCases cases = calibrator.calibrateToolForSimilarity(baseSimilarities, tool);

            CalibratedToolCase calibratedCase1 = cases.calibratedToolCaseList.get(0);
            CalibratedToolCase calibratedCase2 = cases.calibratedToolCaseList.get(1);

            assertEquals(100, calibratedCase1.caseBestSimilarity, 0.001);
            assertEquals(100, calibratedCase1.optimalSimilarity, 0.001);
            assertEquals(0, calibratedCase1.optimalSimilarityDiff, 0.001);
            assertEquals(50, calibratedCase2.caseBestSimilarity, 0.001);
            assertEquals(50, calibratedCase2.optimalSimilarity, 0.001);
            assertEquals(0, calibratedCase2.optimalSimilarityDiff, 0.001);
            assertEquals(3,cases.optimalParams.get(0).paramValue);
            assertEquals(1,cases.optimalParams.size());
        }

        @Test
        public void withTwoDirsAndThreeIterationsMiddleIsBestForBoth() {
            testParamIterations = 3;
            testParamDirs = 2;
            bestIterationCase1 = 2;
            bestIterationCase2 = 2;

            CalibratedToolCases cases = calibrator.calibrateToolForSimilarity(baseSimilarities, tool);

            CalibratedToolCase calibratedCase1 = cases.calibratedToolCaseList.get(0);
            CalibratedToolCase calibratedCase2 = cases.calibratedToolCaseList.get(1);

            assertEquals(100, calibratedCase1.caseBestSimilarity, 0.001);
            assertEquals(100, calibratedCase1.optimalSimilarity, 0.001);
            assertEquals(0, calibratedCase1.optimalSimilarityDiff, 0.001);
            assertEquals(50, calibratedCase2.caseBestSimilarity, 0.001);
            assertEquals(50, calibratedCase2.optimalSimilarity, 0.001);
            assertEquals(0, calibratedCase2.optimalSimilarityDiff, 0.001);
            assertEquals(2,cases.optimalParams.get(0).paramValue);
            assertEquals(1,cases.optimalParams.size());
        }

        @Test
        public void withTwoDirsAndThreeIterationsMiddleIsOptimalAndForNoneBest() {
            testParamIterations = 5;
            testParamDirs = 2;
            testParamDirs = 2;
            bestIterationCase1 = 1;
            bestIterationCase2 = 5;

            CalibratedToolCases cases = calibrator.calibrateToolForSimilarity(baseSimilarities, tool);

            CalibratedToolCase calibratedCase1 = cases.calibratedToolCaseList.get(0);
            CalibratedToolCase calibratedCase2 = cases.calibratedToolCaseList.get(1);

            assertEquals(3,calibrator.getOptimalParams().get(0).paramValue);
            assertEquals(100, calibratedCase1.caseBestSimilarity, 0.001);
            assertEquals(94, calibratedCase1.optimalSimilarity, 0.001);
            assertEquals(6, calibratedCase1.optimalSimilarityDiff, 0.001);
            assertEquals(50, calibratedCase2.caseBestSimilarity, 0.001);
            assertEquals(49, calibratedCase2.optimalSimilarity, 0.001);
            assertEquals(1, calibratedCase2.optimalSimilarityDiff, 0.001);
            assertEquals(1,cases.optimalParams.size());
        }

        @Test
        public void containsInfoAboutDiffForEveryParamCombo() {
            testParamIterations = 5;
            testParamDirs = 2;
            testParamDirs = 2;
            bestIterationCase1 = 1;
            bestIterationCase2 = 5;

            CalibratedToolCases cases = calibrator.calibrateToolForSimilarity(baseSimilarities, tool);
            Float[] expected = {9f,10f,14f,10f,7f};
            int i =0;
            MatcherAssert.assertThat(cases.diffForAllParamCombos.values(),org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder(expected));

//            for(List<CalibratedToolParam> key : cases.diffForAllParamCombos.keySet()){
//                System.out.println(cases.diffForAllParamCombos.get(key).floatValue());
//                //assertEquals(expected[i],cases.diffForAllParamCombos.get(key).floatValue(),0.001);
//                i++;
//            }
        }
    }

    private class SimilarityDetectionToolCalibratorStub extends SimilarityDetectionToolCalibrator {
        public SimilarityDetectionToolCalibratorStub(PhaseRunnerBuilder phaseRunnerBuilder) {
            super(phaseRunnerBuilder);
        }

        @Override
        protected void runToolWithVariousParamCombinations(SimilarityDetectionTool tool) {
            for (testParam = 1; testParam <= testParamIterations; testParam++) {
                if(bestIterationCase1==testParam)
                    calibrator.processMatch(createMatch(100, "testDir1"));
                else
                    calibrator.processMatch(createMatch(100 - (testParam * 2), "testDir1"));

                if(testParamDirs==2){
                    if(bestIterationCase2==testParam)
                        calibrator.processMatch(createMatch(50, "testDir2"));
                    else
                        calibrator.processMatch(createMatch(40 + (testParam * testParam), "testDir2"));

                }
                runToolWithParamCombination(null);
            }
        }

        @Override
        protected List<CalibratedToolParam> getCalibratedToolParams() {
            List<CalibratedToolParam> paramList = new ArrayList<>();
            CalibratedToolParam param = new CalibratedToolParam();
            param.paramName = "testParam";
            param.paramValue = testParam;
            paramList.add(param);
            return paramList;
        }
    }

    private MPCMatch createMatch(float similarity, String caseName) {
        MPCMatch returnMatch = new MPCMatch();
        returnMatch.matchesDir = new File(tool.getName() + File.separator + caseName + File.separator + MPCContext.MATCHES_DIR);
        returnMatch.similarity = similarity;
        return returnMatch;
    }
}