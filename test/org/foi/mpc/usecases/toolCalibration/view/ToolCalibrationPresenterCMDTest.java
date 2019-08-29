package org.foi.mpc.usecases.toolCalibration.view;

import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagJavaAdapter;
import org.foi.mpc.executiontools.detectionTools.sherlock.SherlockTokenisedAdapter;
import org.foi.mpc.usecases.UseCaseResponseErrorMessages;
import org.foi.mpc.usecases.toolCalibration.models.CaseSimilarityBaseTool;
import org.foi.mpc.usecases.toolCalibration.models.CaseSimilarityCalibratedTool;
import org.foi.mpc.usecases.toolCalibration.models.ToolCalibrationResponseModel;
import org.foi.mpc.usecases.toolCalibration.models.ToolParam;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

public class ToolCalibrationPresenterCMDTest {

    private ToolCalibrationPresenterCMD presenter;
    private ToolCalibrationResponseModel responseModel;

    @Before
    public void setUp(){
        presenter = new ToolCalibrationPresenterCMD();
        responseModel = new ToolCalibrationResponseModel();
    }

    @Test
    public void printsErrorIfResponseIsNull(){
        presenter.presentReport(null);
        assertEquals(UseCaseResponseErrorMessages.responseModelIsNull,presenter.getTextToPrint());
    }

    @Test
    public void printsCorrectResultOnScreenOnError(){
        responseModel.errorMessage = UseCaseResponseErrorMessages.invalidWorkingDir;
        presenter.presentReport(responseModel);
        assertEquals(responseModel.errorMessage,presenter.getTextToPrint());
    }

    @Test
    public void printsCorrectResultOnScreenWhenThereIsNoError(){
        responseModel.errorMessage = "";
        responseModel.baseToolName = JPlagJavaAdapter.NAME;
        responseModel.baseToolSimilarities = new ArrayList<>();
        CaseSimilarityBaseTool baseToolCase1 = new CaseSimilarityBaseTool();
        baseToolCase1.caseName = "case1";
        baseToolCase1.similarity = 10;
        CaseSimilarityBaseTool baseToolCase2 = new CaseSimilarityBaseTool();
        baseToolCase2.caseName = "case2";
        baseToolCase2.similarity = 11;
        responseModel.baseToolSimilarities.add(baseToolCase1);
        responseModel.baseToolSimilarities.add(baseToolCase2);
        responseModel.baseToolParams = new ArrayList<>();
        responseModel.baseToolParams.add(createToolParam("a"));
        responseModel.baseToolParams.add(createToolParam("b"));
        responseModel.calibratedToolOptimalParams = new ArrayList<>();
        responseModel.calibratedToolOptimalParams.add(createToolParam("c"));
        responseModel.calibratedToolOptimalParams.add(createToolParam("d"));

        responseModel.calibratedToolName = SherlockTokenisedAdapter.NAME;
        responseModel.calibratedToolSimilarities = new ArrayList<>();
        responseModel.calibratedToolSimilarities.add(createCalibrateToolCase("case1"));
        responseModel.calibratedToolSimilarities.add(createCalibrateToolCase("case2"));
        responseModel.diffForAllParamCombos = new LinkedHashMap<>();
        responseModel.diffForAllParamCombos.put(responseModel.baseToolParams,112f);
        responseModel.diffForAllParamCombos.put(responseModel.calibratedToolOptimalParams,111f);

        presenter.presentReport(responseModel);

        String output = "" +
                "CALIBRATION SUCCESSFUL\n" +
                "\n" +
                "Calibrated Tool: SherlockJava\n" +
                "Base Tool: JPlagJava\n" +
                "Base Tool Params: a = 2, b = 2, \n" +
                "CASES\n" +
                "\n" +
                "Optimal params for all cases:\n" +
                "  Params: c = 2, d = 2, \n" +
                "\n" +
                "Case: case1\n" +
                "  Similarity Base Tool: 10.0\n" +
                "  Optimal params similarity Calibrated: 11.0\n" +
                "  Diff to base tool: 0.0\n" +
                "  Best Similarity Calibrated: 11.0;   Best Params: c = 2, d = 2, \n" +
                "Case: case2\n" +
                "  Similarity Base Tool: 11.0\n" +
                "  Optimal params similarity Calibrated: 11.0\n" +
                "  Diff to base tool: 0.0\n" +
                "  Best Similarity Calibrated: 11.0;   Best Params: c = 2, d = 2, " +
                "\n ALL COMBO PARAM DIFFS \n" +
                "  Param combo: a = 2, b = 2, \n" +
                "  Total diff: 112.0\n" +
                "  Param combo: c = 2, d = 2, \n" +
                "  Total diff: 111.0\n";
        assertEquals(output,presenter.getTextToPrint());
    }

    private ToolParam createToolParam(String paramName) {
        ToolParam param2 = new ToolParam();
        param2.paramName = paramName;
        param2.paramValue = 2;
        return param2;
    }

    private CaseSimilarityCalibratedTool createCalibrateToolCase(String caseName) {
        CaseSimilarityCalibratedTool calibTool = new CaseSimilarityCalibratedTool();
        calibTool.caseName = caseName;
        calibTool.optimalSimilarityDiff = 0;
        calibTool.optimalSimilarity = 11;
        calibTool.bestSimilarity = 11;
        calibTool.calibratedToolParams = new ArrayList<>();
        calibTool.calibratedToolParams.add(createToolParam("c"));
        calibTool.calibratedToolParams.add(createToolParam("d"));
        return calibTool;
    }
}
