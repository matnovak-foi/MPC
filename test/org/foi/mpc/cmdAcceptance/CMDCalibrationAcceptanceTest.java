package org.foi.mpc.cmdAcceptance;

import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.mpc.MPCContext;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagJavaAdapter;
import org.foi.mpc.executiontools.detectionTools.sherlock.SherlockTokenisedAdapter;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneJavaAdapter;
import org.foi.mpc.executiontools.factories.SimilarityDetectionToolFactory;
import org.foi.mpc.usecases.toolCalibration.ToolCalibrationOutputBoundary;
import org.foi.mpc.usecases.toolCalibration.ToolCalibratorUseCase;
import org.foi.mpc.usecases.toolCalibration.models.ToolCalibrationRequestModel;
import org.foi.mpc.usecases.toolCalibration.models.ToolCalibrationResponseModel;
import org.foi.mpc.usecases.toolCalibration.models.ToolParam;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CMDCalibrationAcceptanceTest implements ToolCalibrationOutputBoundary {
    private ToolCalibrationRequestModel requestModel;
    private ToolCalibratorUseCase useCase;
    private ToolCalibrationResponseModel responseModel;
    File workingDir;

    @Before
    public void setUp() {
        workingDir = new File("testWorkingDir");
        workingDir.mkdir();

        requestModel = new ToolCalibrationRequestModel();
        requestModel.baseToolName = JPlagJavaAdapter.NAME;
        requestModel.baseToolParams = new ArrayList<>();
        ToolParam baseToolParam = new ToolParam();
        baseToolParam.paramName = "t";
        baseToolParam.paramValue = 3;
        requestModel.baseToolParams.add(baseToolParam);
        requestModel.workingDir = workingDir;
        useCase = new ToolCalibratorUseCase(new SimilarityDetectionToolFactory(MPCContext.MATCHES_DIR), MPCContext.MATCHES_DIR);
    }

    @After
    public void tearDown() throws IOException {
        DirectoryFileUtility.deleteDirectoryTree(workingDir);
    }
    @Test
    public void calibrationSIMTestWorksCase1() {
        requestModel.toCalibrateToolName = SimGruneJavaAdapter.NAME;
        requestModel.inputDirWithCalibrationCases = new File("testInputData" + File.separator + "calibrationAcceptanceTest");

        useCase.runCalibration(this, requestModel);

        assertEquals("", responseModel.errorMessage);

        assertEquals(JPlagJavaAdapter.NAME, responseModel.baseToolName);
        assertEquals("t", responseModel.baseToolParams.get(0).paramName);
        assertEquals(3, responseModel.baseToolParams.get(0).paramValue);
        assertEquals("0Precent", responseModel.baseToolSimilarities.get(0).caseName);
        assertEquals(10.0, responseModel.baseToolSimilarities.get(0).similarity, 0.1);
        assertEquals("0Precent2", responseModel.baseToolSimilarities.get(1).caseName);
        assertEquals(11.3, responseModel.baseToolSimilarities.get(1).similarity, 0.1);

        assertEquals(SimGruneJavaAdapter.NAME, responseModel.calibratedToolName);
        assertEquals("0Precent", responseModel.calibratedToolSimilarities.get(0).caseName);
        assertEquals(11.5, responseModel.calibratedToolSimilarities.get(0).bestSimilarity, 0.1);
        assertEquals("0Precent2", responseModel.calibratedToolSimilarities.get(1).caseName);
        assertEquals(8.5, responseModel.calibratedToolSimilarities.get(1).bestSimilarity, 0.1);
        assertEquals("r", responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(0).paramName);
        assertEquals(11, responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(0).paramValue);
        assertEquals("r", responseModel.calibratedToolSimilarities.get(1).calibratedToolParams.get(0).paramName);
        assertEquals(11, responseModel.calibratedToolSimilarities.get(1).calibratedToolParams.get(0).paramValue);

        File baseToolFolder = new File(workingDir+File.separator+"detection"+File.separator+requestModel.baseToolName);
        assertDirectoryIsClean(baseToolFolder);
        File calibratedToolFolder = new File(workingDir+File.separator+"detection"+File.separator+requestModel.toCalibrateToolName);
        assertDirectoryIsClean(calibratedToolFolder);
    }

    private void assertDirectoryIsClean(File baseToolFolder) {
        assertEquals(0,baseToolFolder.listFiles().length);
    }

    @Test
    public void calibrationSIMTestSelfWorksCase() {
        requestModel.baseToolName = SimGruneJavaAdapter.NAME;
        requestModel.baseToolParams = new ArrayList<>();
        ToolParam baseToolParam = new ToolParam();
        baseToolParam.paramName = "r";
        baseToolParam.paramValue = 22;
        requestModel.baseToolParams.add(baseToolParam);
        requestModel.toCalibrateToolName = SimGruneJavaAdapter.NAME;
        requestModel.inputDirWithCalibrationCases = new File("testInputData" + File.separator + "calibrationAcceptanceTest2");

        useCase.runCalibration(this, requestModel);

        assertEquals("", responseModel.errorMessage);
        assertEquals(SimGruneJavaAdapter.NAME, responseModel.baseToolName);
        assertEquals("r", responseModel.baseToolParams.get(0).paramName);
        assertEquals(22, responseModel.baseToolParams.get(0).paramValue);

        assertEquals("50Precent", responseModel.baseToolSimilarities.get(1).caseName);
        assertEquals(58.5, responseModel.baseToolSimilarities.get(1).similarity, 0.1);
        assertEquals("50PrecentMixedSimple", responseModel.baseToolSimilarities.get(0).caseName);
        assertEquals(67, responseModel.baseToolSimilarities.get(0).similarity, 0.1);

        assertEquals(SimGruneJavaAdapter.NAME, responseModel.calibratedToolName);
        assertEquals("50Precent", responseModel.calibratedToolSimilarities.get(1).caseName);
        assertEquals(58.5, responseModel.calibratedToolSimilarities.get(1).bestSimilarity, 0.1);
        assertEquals("50PrecentMixedSimple", responseModel.calibratedToolSimilarities.get(0).caseName);
        assertEquals(67, responseModel.calibratedToolSimilarities.get(0).bestSimilarity, 0.1);
        assertEquals("r", responseModel.calibratedToolSimilarities.get(1).calibratedToolParams.get(0).paramName);
        assertEquals(12, responseModel.calibratedToolSimilarities.get(1).calibratedToolParams.get(0).paramValue);
        assertEquals("r", responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(0).paramName);
        assertEquals(13, responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(0).paramValue);

        File baseToolFolder = new File(workingDir+File.separator+"detection"+File.separator+requestModel.baseToolName);
        assertDirectoryIsClean(baseToolFolder);
        File calibratedToolFolder = new File(workingDir+File.separator+"detection"+File.separator+requestModel.toCalibrateToolName);
        assertDirectoryIsClean(calibratedToolFolder);
    }

    @Test
    public void calibrationSIMTestWorksCase2() {
        requestModel.toCalibrateToolName = SimGruneJavaAdapter.NAME;
        requestModel.inputDirWithCalibrationCases = new File("testInputData" + File.separator + "calibrationAcceptanceTest2");

        useCase.runCalibration(this, requestModel);

        assertEquals("", responseModel.errorMessage);

        assertEquals(JPlagJavaAdapter.NAME, responseModel.baseToolName);
        assertEquals("t", responseModel.baseToolParams.get(0).paramName);
        assertEquals(3, responseModel.baseToolParams.get(0).paramValue);
        assertEquals("50Precent", responseModel.baseToolSimilarities.get(1).caseName);
        assertEquals(63.448277, responseModel.baseToolSimilarities.get(1).similarity, 0.1);
        assertEquals("50PrecentMixedSimple", responseModel.baseToolSimilarities.get(0).caseName);
        assertEquals(68.50126, responseModel.baseToolSimilarities.get(0).similarity, 0.1);

        assertEquals(SimGruneJavaAdapter.NAME, responseModel.calibratedToolName);
        assertEquals("50Precent", responseModel.calibratedToolSimilarities.get(1).caseName);
        assertEquals(64, responseModel.calibratedToolSimilarities.get(1).bestSimilarity, 0.1);
        assertEquals("50PrecentMixedSimple", responseModel.calibratedToolSimilarities.get(0).caseName);
        assertEquals(69, responseModel.calibratedToolSimilarities.get(0).bestSimilarity, 0.1);
        assertEquals("r", responseModel.calibratedToolSimilarities.get(1).calibratedToolParams.get(0).paramName);
        assertEquals(9, responseModel.calibratedToolSimilarities.get(1).calibratedToolParams.get(0).paramValue);
        assertEquals("r", responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(0).paramName);
        assertEquals(11, responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(0).paramValue);
    }


    @Test
    public void calibrationSherlockTestWorksCase1() {
        requestModel.toCalibrateToolName = SherlockTokenisedAdapter.NAME;
        requestModel.inputDirWithCalibrationCases = new File("testInputData" + File.separator + "calibrationAcceptanceTest");

        useCase.runCalibration(this, requestModel);

        assertEquals("", responseModel.errorMessage);

        assertEquals(JPlagJavaAdapter.NAME, responseModel.baseToolName);
        assertEquals("t", responseModel.baseToolParams.get(0).paramName);
        assertEquals(3, responseModel.baseToolParams.get(0).paramValue);
        assertEquals("0Precent", responseModel.baseToolSimilarities.get(0).caseName);
        assertEquals(10.0, responseModel.baseToolSimilarities.get(0).similarity, 0.1);
        assertEquals("0Precent2", responseModel.baseToolSimilarities.get(1).caseName);
        assertEquals(11.3, responseModel.baseToolSimilarities.get(1).similarity, 0.1);

        assertEquals(SherlockTokenisedAdapter.NAME, responseModel.calibratedToolName);
        assertEquals("0Precent", responseModel.calibratedToolSimilarities.get(0).caseName);
        assertEquals(17, responseModel.calibratedToolSimilarities.get(0).bestSimilarity, 0.1);
        assertEquals("0Precent2", responseModel.calibratedToolSimilarities.get(1).caseName);
        assertEquals(8, responseModel.calibratedToolSimilarities.get(1).bestSimilarity, 0.1);

        assertEquals("maxBackwardJump", responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(0).paramName);
        assertEquals(1, responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(0).paramValue);
        assertEquals("maxForwardJump", responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(1).paramName);
        assertEquals(1, responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(1).paramValue);
        assertEquals("minRunLenght", responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(2).paramName);
        assertEquals(2, responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(2).paramValue);
        assertEquals("minStringLength", responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(3).paramName);
        assertEquals(2, responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(3).paramValue);
        assertEquals("maxJumpDiff", responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(4).paramName);
        assertEquals(1, responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(4).paramValue);
        assertEquals("strictness", responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(5).paramName);
        assertEquals(3, responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(5).paramValue);

        assertEquals("maxBackwardJump", responseModel.calibratedToolSimilarities.get(1).calibratedToolParams.get(0).paramName);
        assertEquals(1, responseModel.calibratedToolSimilarities.get(1).calibratedToolParams.get(0).paramValue);
        assertEquals("maxForwardJump", responseModel.calibratedToolSimilarities.get(1).calibratedToolParams.get(1).paramName);
        assertEquals(3, responseModel.calibratedToolSimilarities.get(1).calibratedToolParams.get(1).paramValue);
        assertEquals("minRunLenght", responseModel.calibratedToolSimilarities.get(1).calibratedToolParams.get(2).paramName);
        assertEquals(1, responseModel.calibratedToolSimilarities.get(1).calibratedToolParams.get(2).paramValue);
        assertEquals("minStringLength", responseModel.calibratedToolSimilarities.get(1).calibratedToolParams.get(3).paramName);
        assertEquals(2, responseModel.calibratedToolSimilarities.get(1).calibratedToolParams.get(3).paramValue);
        assertEquals("maxJumpDiff", responseModel.calibratedToolSimilarities.get(1).calibratedToolParams.get(4).paramName);
        assertEquals(1, responseModel.calibratedToolSimilarities.get(1).calibratedToolParams.get(4).paramValue);
        assertEquals("strictness", responseModel.calibratedToolSimilarities.get(1).calibratedToolParams.get(5).paramName);
        assertEquals(3, responseModel.calibratedToolSimilarities.get(1).calibratedToolParams.get(5).paramValue);
    }

    @Test
    public void calibrationSherlockTestWorksCase2() {
        requestModel.toCalibrateToolName = SherlockTokenisedAdapter.NAME;
        requestModel.inputDirWithCalibrationCases = new File("testInputData" + File.separator + "calibrationAcceptanceTest2");

        useCase.runCalibration(this, requestModel);

        assertEquals("", responseModel.errorMessage);

        assertEquals(JPlagJavaAdapter.NAME, responseModel.baseToolName);
        assertEquals("t", responseModel.baseToolParams.get(0).paramName);
        assertEquals(3, responseModel.baseToolParams.get(0).paramValue);
        assertEquals("50Precent", responseModel.baseToolSimilarities.get(1).caseName);
        assertEquals(63.448277, responseModel.baseToolSimilarities.get(1).similarity, 0.1);
        assertEquals("50PrecentMixedSimple", responseModel.baseToolSimilarities.get(0).caseName);
        assertEquals(68.50126, responseModel.baseToolSimilarities.get(0).similarity, 0.1);

        assertEquals(SherlockTokenisedAdapter.NAME, responseModel.calibratedToolName);
        assertEquals("50Precent", responseModel.calibratedToolSimilarities.get(1).caseName);
        assertEquals(45, responseModel.calibratedToolSimilarities.get(1).bestSimilarity, 0.1);
        assertEquals("50PrecentMixedSimple", responseModel.calibratedToolSimilarities.get(0).caseName);
        assertEquals(44, responseModel.calibratedToolSimilarities.get(0).bestSimilarity, 0.1);

        assertEquals("maxBackwardJump", responseModel.calibratedToolSimilarities.get(1).calibratedToolParams.get(0).paramName);
        assertEquals(1, responseModel.calibratedToolSimilarities.get(1).calibratedToolParams.get(0).paramValue);
        assertEquals("maxForwardJump", responseModel.calibratedToolSimilarities.get(1).calibratedToolParams.get(1).paramName);
        assertEquals(1, responseModel.calibratedToolSimilarities.get(1).calibratedToolParams.get(1).paramValue);
        assertEquals("minRunLenght", responseModel.calibratedToolSimilarities.get(1).calibratedToolParams.get(2).paramName);
        assertEquals(1, responseModel.calibratedToolSimilarities.get(1).calibratedToolParams.get(2).paramValue);
        assertEquals("minStringLength", responseModel.calibratedToolSimilarities.get(1).calibratedToolParams.get(3).paramName);
        assertEquals(2, responseModel.calibratedToolSimilarities.get(1).calibratedToolParams.get(3).paramValue);
        assertEquals("maxJumpDiff", responseModel.calibratedToolSimilarities.get(1).calibratedToolParams.get(4).paramName);
        assertEquals(1, responseModel.calibratedToolSimilarities.get(1).calibratedToolParams.get(4).paramValue);
        assertEquals("strictness", responseModel.calibratedToolSimilarities.get(1).calibratedToolParams.get(5).paramName);
        assertEquals(3, responseModel.calibratedToolSimilarities.get(1).calibratedToolParams.get(5).paramValue);

        assertEquals("maxBackwardJump", responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(0).paramName);
        assertEquals(1, responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(0).paramValue);
        assertEquals("maxForwardJump", responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(1).paramName);
        assertEquals(2, responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(1).paramValue);
        assertEquals("minRunLenght", responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(2).paramName);
        assertEquals(4, responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(2).paramValue);
        assertEquals("minStringLength", responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(3).paramName);
        assertEquals(2, responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(3).paramValue);
        assertEquals("maxJumpDiff", responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(4).paramName);
        assertEquals(1, responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(4).paramValue);
        assertEquals("strictness", responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(5).paramName);
        assertEquals(3, responseModel.calibratedToolSimilarities.get(0).calibratedToolParams.get(5).paramValue);
    }

    @Override
    public void presentReport(ToolCalibrationResponseModel responseModel) {
        this.responseModel = responseModel;
    }
}
