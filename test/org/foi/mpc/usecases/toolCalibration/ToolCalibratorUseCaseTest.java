package org.foi.mpc.usecases.toolCalibration;

import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.directory.InMemoryDir;
import org.foi.mpc.MPCContext;
import org.foi.mpc.executiontools.calibrator.models.BaseToolCase;
import org.foi.mpc.executiontools.calibrator.SimilarityDetectionToolCalibrator;
import org.foi.mpc.executiontools.calibrator.models.CalibratedToolCase;
import org.foi.mpc.executiontools.calibrator.models.CalibratedToolParam;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagJavaAdapter;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneJavaAdapter;
import org.foi.mpc.executiontools.factories.SimilarityDetectionTool;
import org.foi.mpc.executiontools.calibrator.SimilarityDetectionToolCalibratorFactory;
import org.foi.mpc.executiontools.factories.SimilarityDetectionToolFactory;
import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.phases.executionphases.ExecutionPhase;
import org.foi.mpc.phases.runner.PhaseRunnerBuilder;
import org.foi.mpc.phases.runner.PhaseRunnerBuilderStub;
import org.foi.mpc.usecases.UseCaseResponseErrorMessages;
import org.foi.mpc.usecases.toolCalibration.models.ToolCalibrationRequestModel;
import org.foi.mpc.usecases.toolCalibration.models.ToolCalibrationResponseModel;
import org.foi.mpc.usecases.toolCalibration.models.ToolParam;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.*;

public class ToolCalibratorUseCaseTest implements ToolCalibrationOutputBoundary, PhaseRunnerBuilderStub.PhaseBuilderSpyListener {
    ToolCalibratorUseCase toolCalibrator;
    ToolCalibrationRequestModel requestModel;
    PhaseRunnerBuilderStub phaseRunnerBuilderStub;
    ToolCalibrationResponseModel responseModel;
    SimilarityDetectionToolCalibratorFactorySpy calibratorFactorySpy;
    MPCMatch returnMatch;
    ToolParam baseToolParam;

    @Before
    public void setUp(){
        phaseRunnerBuilderStub = new PhaseRunnerBuilderStub(MPCContext.MATCHES_DIR);
        phaseRunnerBuilderStub.spyListener = this;

        toolCalibrator = new ToolCalibratorUseCase(new SimilarityDetectionToolFactory(MPCContext.MATCHES_DIR), MPCContext.MATCHES_DIR);
        toolCalibrator.setPhaseRunnerBuilder(phaseRunnerBuilderStub);
        calibratorFactorySpy = new SimilarityDetectionToolCalibratorFactorySpy();
        toolCalibrator.setToolCalibratorFactory(calibratorFactorySpy);

        setUpReturnMatch(JPlagJavaAdapter.NAME+ File.separator+"case1"+File.separator+"matches");
        setUpInMemoryRequestModel();
    }

    private void setUpReturnMatch(String pathname) {
        MPCMatch match = new MPCMatch();
        match.similarity = 100;
        match.matchesDir = new File(pathname);
        returnMatch = match;
    }

    private void setUpInMemoryRequestModel() {
        requestModel = new ToolCalibrationRequestModel();
        requestModel.baseToolName = JPlagJavaAdapter.NAME;
        requestModel.baseToolParams = new ArrayList<>();
        baseToolParam = new ToolParam();
        baseToolParam.paramName = "t";
        baseToolParam.paramValue = 3;
        requestModel.baseToolParams.add(baseToolParam);
        requestModel.toCalibrateToolName = SimGruneJavaAdapter.NAME;
        InMemoryDir inputDir = new InMemoryDir("inputDir");
        inputDir.addFile(new InMemoryDir("case1"));
        inputDir.addFile(new InMemoryDir("case2"));
        requestModel.inputDirWithCalibrationCases = inputDir;

        requestModel.workingDir = new File("workingDir");
        requestModel.workingDir.mkdir();
    }

    @After
    public void tearDown() throws IOException {
        DirectoryFileUtility.deleteDirectoryTree(requestModel.workingDir);
    }

    @Test
    public void givenNoBaseToolReturnError(){
        requestModel.baseToolName = "";
        toolCalibrator.runCalibration(this,requestModel);
        assertEquals(responseModel.errorMessage, UseCaseResponseErrorMessages.noToolIsSelected);
    }

    @Test
    public void givenNoToCalibrateToolReturnError(){
        requestModel.toCalibrateToolName = "";
        toolCalibrator.runCalibration(this,requestModel);
        assertEquals(responseModel.errorMessage, UseCaseResponseErrorMessages.noToolIsSelected);
    }

    @Test
    public void givenNoSourceDirReturnError(){
        requestModel.inputDirWithCalibrationCases = new File("nonExisting");
        toolCalibrator.runCalibration(this,requestModel);
        assertEquals(responseModel.errorMessage, UseCaseResponseErrorMessages.invalidSourceDir);
    }

    @Test
    public void givenNoWorkingDirReturnError(){
        requestModel.workingDir = new File("nonExisting");
        toolCalibrator.runCalibration(this,requestModel);
        assertEquals(responseModel.errorMessage, UseCaseResponseErrorMessages.invalidWorkingDir);
    }

    @Test
    public void createCorrectPhaseRunnerForBaseTool(){
        toolCalibrator.runCalibration(this, requestModel);

        assertThat(phaseRunnerBuilderStub.toolList, IsCollectionWithSize.hasSize(1));
        assertEquals(JPlagJavaAdapter.NAME, phaseRunnerBuilderStub.toolList.get(0).getName());
        assertTrue(phaseRunnerBuilderStub.phaseRunnerWasRun);

        assertFalse(phaseRunnerBuilderStub.runnerSettings.runPreparePhase);
        assertFalse(phaseRunnerBuilderStub.runnerSettings.runPreprocessPhase);
        assertTrue(phaseRunnerBuilderStub.runnerSettings.runDetectionPhase);
        assertTrue(phaseRunnerBuilderStub.runnerSettings.runMPCMatchReadPhase);

        assertEquals(requestModel.workingDir, phaseRunnerBuilderStub.workingDir);
        assertEquals(requestModel.inputDirWithCalibrationCases, phaseRunnerBuilderStub.sourceDir);

        ExecutionPhase executionPhase = (ExecutionPhase) phaseRunnerBuilderStub.phaseRunner.getDetectionPhase();
        ExecutionPhase executionPhase2 = (ExecutionPhase) phaseRunnerBuilderStub.phaseRunner.getPreprocessPhase();
        assertEquals(requestModel.inputDirWithCalibrationCases,executionPhase.getSourceDir());
        assertEquals(requestModel.inputDirWithCalibrationCases,executionPhase2.getSourceDir());
        assertThat(phaseRunnerBuilderStub.runnerSettings.preprocessOutputDirs,hasItems(requestModel.inputDirWithCalibrationCases.listFiles()));
    }

    @Test
    public void givenWrongBaseToolParamJPlag(){
        ToolParam param = new ToolParam();
        param.paramName = "wrong";
        requestModel.baseToolParams = new ArrayList<>();
        requestModel.baseToolParams.add(param);
        toolCalibrator.runCalibration(this,requestModel);
        assertEquals(UseCaseResponseErrorMessages.invalidToolParam,responseModel.errorMessage);
    }

    @Test
    public void givenWrongBaseToolParamSIM(){
        requestModel.baseToolName = SimGruneJavaAdapter.NAME;
        ToolParam param = new ToolParam();
        param.paramName = "wrong";
        requestModel.baseToolParams = new ArrayList<>();
        requestModel.baseToolParams.add(param);
        toolCalibrator.runCalibration(this,requestModel);
        assertEquals(UseCaseResponseErrorMessages.invalidToolParam,responseModel.errorMessage);
    }

    @Test
    public void generatesCorrectBaseToolParamsForJPlag(){
        baseToolParam.paramValue = 100;
        requestModel.baseToolParams = new ArrayList<>();
        requestModel.baseToolParams.add(baseToolParam);

        toolCalibrator.runCalibration(this, requestModel);

        assertEquals(JPlagJavaAdapter.NAME, responseModel.baseToolName);
        assertEquals("t", responseModel.baseToolParams.get(0).paramName);
        assertEquals(100, responseModel.baseToolParams.get(0).paramValue);
        assertEquals("case1",responseModel.baseToolSimilarities.get(0).caseName);
        assertEquals(100,responseModel.baseToolSimilarities.get(0).similarity,0.001);
    }

    @Test
    public void generatesCorrectBaseToolParamsForSIM(){
        baseToolParam.paramName = "r";
        baseToolParam.paramValue = 100;
        requestModel.baseToolName = SimGruneJavaAdapter.NAME;
        requestModel.baseToolParams = new ArrayList<>();
        requestModel.baseToolParams.add(baseToolParam);

        toolCalibrator.runCalibration(this, requestModel);

        assertEquals(SimGruneJavaAdapter.NAME, responseModel.baseToolName);
        assertEquals("r", responseModel.baseToolParams.get(0).paramName);
        assertEquals(100, responseModel.baseToolParams.get(0).paramValue);
        assertEquals("case1",responseModel.baseToolSimilarities.get(0).caseName);
        assertEquals(100,responseModel.baseToolSimilarities.get(0).similarity,0.001);
    }

    @Test
    public void generatesCorrectBaseToolParamsIfMatchIsZero(){
        baseToolParam.paramValue = 100;
        phaseRunnerBuilderStub.returnSomeMatch = false;
        requestModel.baseToolParams = new ArrayList<>();
        requestModel.baseToolParams.add(baseToolParam);

        toolCalibrator.runCalibration(this, requestModel);

        assertEquals(JPlagJavaAdapter.NAME, responseModel.baseToolName);
        assertEquals(2,responseModel.baseToolSimilarities.size());
        assertEquals("case1",responseModel.baseToolSimilarities.get(0).caseName);
        assertEquals("case2",responseModel.baseToolSimilarities.get(1).caseName);
        assertEquals(0,responseModel.baseToolSimilarities.get(0).similarity,0.001);
        assertEquals(0,responseModel.baseToolSimilarities.get(1).similarity,0.001);
    }

    @Test
    public void generatesCorrectCalibratedToolParams(){
        requestModel.toCalibrateToolName = SimGruneJavaAdapter.NAME;

        toolCalibrator.runCalibration(this, requestModel);

        assertEquals(SimGruneJavaAdapter.NAME, responseModel.calibratedToolName);
        assertEquals(calibratorFactorySpy.calibratedToolCases.get(0).caseName,responseModel.calibratedToolSimilarities.get(0).caseName);
        assertEquals(calibratorFactorySpy.calibratedToolCases.get(0).optimalSimilarity,responseModel.calibratedToolSimilarities.get(0).optimalSimilarity,0.001);
        assertEquals(calibratorFactorySpy.calibratedToolCases.get(0).optimalSimilarityDiff,responseModel.calibratedToolSimilarities.get(0).optimalSimilarityDiff,0.001);
        assertEquals(calibratorFactorySpy.calibratedToolCases.get(0).caseBestSimilarity,responseModel.calibratedToolSimilarities.get(0).bestSimilarity,0.001);
        assertEquals(calibratorFactorySpy.calibratedToolCases.get(0).caseBestParams.get(0).paramName, responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(0).paramName);
        assertEquals(calibratorFactorySpy.calibratedToolCases.get(0).caseBestParams.get(0).paramValue, responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(0).paramValue);
    }

    @Test
    public void generatesCorrectOptimalToolParams(){
        requestModel.toCalibrateToolName = SimGruneJavaAdapter.NAME;

        toolCalibrator.runCalibration(this, requestModel);

        assertNotNull(responseModel.calibratedToolOptimalParams);
        assertEquals(calibratorFactorySpy.optimalToolParams.get(0).paramName, responseModel.calibratedToolOptimalParams.get(0).paramName);
        assertEquals(calibratorFactorySpy.optimalToolParams.get(0).paramValue, responseModel.calibratedToolOptimalParams.get(0).paramValue);

    }

    @Test
    public void generatesCorrectAllComboParamsDiff(){
        requestModel.toCalibrateToolName = SimGruneJavaAdapter.NAME;

        toolCalibrator.runCalibration(this, requestModel);

        assertNotNull(responseModel.diffForAllParamCombos);
        assertEquals(calibratorFactorySpy.expectedDiffForAllParamCombos, responseModel.diffForAllParamCombos);
    }

    @Test
    public void similarityDetectionToolCalibratorWasCalled(){
        requestModel.toCalibrateToolName = SimGruneJavaAdapter.NAME;
        toolCalibrator.setToolCalibratorFactory(calibratorFactorySpy);
        toolCalibrator.runCalibration(this,requestModel);

        assertEquals(requestModel.toCalibrateToolName, calibratorFactorySpy.calledTool[0]);
        assertEquals(responseModel.baseToolSimilarities.get(0).caseName, calibratorFactorySpy.passedSimilarities.get(0).caseName);
        assertEquals(responseModel.baseToolSimilarities.get(0).similarity, calibratorFactorySpy.passedSimilarities.get(0).similarity, 0.01);
    }

    private class SimilarityDetectionToolCalibratorFactorySpy extends SimilarityDetectionToolCalibratorFactory {
        final String[] calledTool = new String[1];
        List<BaseToolCase> passedSimilarities;
        List<CalibratedToolCase> calibratedToolCases = new ArrayList<>();
        List<CalibratedToolParam> optimalToolParams = new ArrayList<>();
        Map<List<CalibratedToolParam>, Float> expectedDiffForAllParamCombos = new HashMap<>();

        @Override
        public SimilarityDetectionToolCalibrator getCalibrator(SimilarityDetectionTool tool, PhaseRunnerBuilder phaseRunnerBuilder) {
            return new SimilarityDetectionToolCalibrator(phaseRunnerBuilder) {
                @Override
                protected void runToolWithVariousParamCombinations(SimilarityDetectionTool tool) {
                    calledTool[0] = tool.getName();
                    passedSimilarities = baseToolCases;
                    CalibratedToolCase calibratedToolCase = new CalibratedToolCase();
                    calibratedToolCase.caseName = baseToolCases.get(0).caseName;
                    calibratedToolCase.optimalSimilarity = -1;
                    calibratedToolCase.optimalSimilarityDiff = -1;
                    calibratedToolCase.caseBestSimilarity = baseToolCases.get(0).similarity;
                    calibratedToolCase.caseBestParams = new ArrayList<>();
                    CalibratedToolParam calibratedToolParam = new CalibratedToolParam();
                    calibratedToolParam.paramName = "r";
                    calibratedToolParam.paramValue = 100;
                    calibratedToolCase.caseBestParams.add(calibratedToolParam);
                    calibratedToolCases.add(calibratedToolCase);
                    calibratedSimilarityCases = calibratedToolCases;
                    optimalParams.add(calibratedToolParam);
                    optimalToolParams = optimalParams;
                    diffForAllParamCombos = new HashMap<>();
                    diffForAllParamCombos.put(getCalibratedToolParams(),111f);
                    expectedDiffForAllParamCombos = diffForAllParamCombos;
                }

                @Override
                protected List<CalibratedToolParam> getCalibratedToolParams() {
                    return new ArrayList<>();
                }
            };
        }
    }

    @Override
    public MPCMatch getMatchToReturn() {
        return returnMatch;
    }

    @Override
    public void presentReport(ToolCalibrationResponseModel responseModel) {
        this.responseModel = responseModel;
    }
}
