package org.foi.mpc.usecases.toolCalibration;

import org.foi.mpc.executiontools.calibrator.SimilarityDetectionToolCalibrator;
import org.foi.mpc.executiontools.calibrator.SimilarityDetectionToolSettingFactory;
import org.foi.mpc.executiontools.calibrator.models.CalibratedToolCase;
import org.foi.mpc.executiontools.calibrator.models.CalibratedToolCases;
import org.foi.mpc.executiontools.calibrator.models.CalibratedToolParam;
import org.foi.mpc.executiontools.factories.SimilarityDetectionTool;
import org.foi.mpc.executiontools.calibrator.SimilarityDetectionToolCalibratorFactory;
import org.foi.mpc.executiontools.factories.SimilarityDetectionToolFactory;
import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.phases.executionphases.ExecutionTool;
import org.foi.mpc.phases.readerphase.MPCMatchListener;
import org.foi.mpc.phases.runner.PhaseRunner;
import org.foi.mpc.phases.runner.PhaseRunnerBuilder;
import org.foi.mpc.usecases.UseCaseResponseErrorMessages;
import org.foi.mpc.usecases.toolCalibration.models.*;

import java.io.File;
import java.util.*;

import static org.foi.mpc.executiontools.calibrator.SimilarityDetectionToolCalibrator.*;

import org.foi.mpc.executiontools.calibrator.models.BaseToolCase;

public class ToolCalibratorUseCase implements ToolCalibratorInputBoundary, MPCMatchListener {

    SimilarityDetectionToolFactory toolFactory;
    SimilarityDetectionToolSettingFactory settingFactory;
    PhaseRunnerBuilder phaseRunnerBuilder;
    ToolCalibrationResponseModel responseModel;
    SimilarityDetectionToolCalibratorFactory toolCalibratorFactory;


    public ToolCalibratorUseCase(SimilarityDetectionToolFactory toolFactory, String matchesDirName) {
        this.toolFactory = toolFactory;
        this.settingFactory = new SimilarityDetectionToolSettingFactory();
        this.phaseRunnerBuilder = new PhaseRunnerBuilder(matchesDirName);
        this.toolCalibratorFactory = new SimilarityDetectionToolCalibratorFactory();
    }

    public void setPhaseRunnerBuilder(PhaseRunnerBuilder phaseRunnerBuilder) {
        this.phaseRunnerBuilder = phaseRunnerBuilder;
    }

    public void setToolCalibratorFactory(SimilarityDetectionToolCalibratorFactory toolCalibratorFactory) {
        this.toolCalibratorFactory = toolCalibratorFactory;
    }

    @Override
    public void runCalibration(ToolCalibrationOutputBoundary presenter, ToolCalibrationRequestModel requestModel) {
        initializeResponseModel();

        if (requestIsNotMissingData(requestModel) && preparedBaseToolResponsePart(requestModel)) {
            SimilarityDetectionTool toolToCalibrate = toolFactory.createTool(requestModel.toCalibrateToolName);
            CalibratedToolCases calibratedToolCases = calibrateTool(toolToCalibrate);
            responseModel.calibratedToolName = toolToCalibrate.getName();
            fillCalibratedToolResponsePart(calibratedToolCases);
        }

        presenter.presentReport(responseModel);
    }

    private void initializeResponseModel() {
        responseModel = new ToolCalibrationResponseModel();
        responseModel.calibratedToolSimilarities = new ArrayList<>();
        responseModel.baseToolSimilarities = new ArrayList<>();
        responseModel.calibratedToolOptimalParams = new ArrayList<>();
        responseModel.errorMessage = "";
    }

    private boolean requestIsNotMissingData(ToolCalibrationRequestModel requestModel) {
        if(!requestModel.workingDir.exists()){
            responseModel.errorMessage = UseCaseResponseErrorMessages.invalidWorkingDir;
            return false;
        }

        if(!requestModel.inputDirWithCalibrationCases.exists()){
            responseModel.errorMessage = UseCaseResponseErrorMessages.invalidSourceDir;
            return false;
        }

        if(requestModel.baseToolName.isEmpty()){
            responseModel.errorMessage = UseCaseResponseErrorMessages.noToolIsSelected;
            return false;
        }

        if(requestModel.toCalibrateToolName.isEmpty()){
            responseModel.errorMessage = UseCaseResponseErrorMessages.noToolIsSelected;
            return false;
        }

        return true;
    }

    private boolean preparedBaseToolResponsePart(ToolCalibrationRequestModel requestModel) {
        SimilarityDetectionTool baseTool = toolFactory.createTool(requestModel.baseToolName);
        responseModel.baseToolName = baseTool.getName();

        try {
            baseTool = settingFactory.setCalibrationParams(baseTool, requestModel.baseToolParams);
        } catch (SimilarityDetectionToolSettingFactory.InvalidSimilarityToolParamException e) {
            responseModel.errorMessage = UseCaseResponseErrorMessages.invalidToolParam;
            return false;
        }

        List<File> casesToProcess = getDirsWithCases(requestModel);
        runSimilarityDetectionOnBaseTool(requestModel, baseTool, casesToProcess);
        fillBaseToolResponseParams(baseTool);
        fillBaseToolResponseWith0SimilarityCases(casesToProcess);

        return true;
    }

    private void runSimilarityDetectionOnBaseTool(ToolCalibrationRequestModel requestModel, SimilarityDetectionTool baseTool, List<File> caseDirsToProcess) {
        PhaseRunner runner = phaseRunnerBuilder
                            .forToolList(createDetectionToolList(baseTool))
                            .withPreparePhaseAndPreprocessPhaseDisabled()
                            .toProcessDirs(caseDirsToProcess)
                            .withWorkingDir(requestModel.workingDir)
                            .withSourceDir(requestModel.inputDirWithCalibrationCases)
                            .withMatchReadListener(this)
                            .build();
        runner.runPhases();
    }

    private void fillBaseToolResponseParams(SimilarityDetectionTool baseTool) {
        List<ToolParam> baseToolParams = settingFactory.getCalibrationParams(baseTool.getSettings());
        responseModel.baseToolParams = new ArrayList<>();
        responseModel.baseToolParams.addAll(baseToolParams);
    }

    private void fillBaseToolResponseWith0SimilarityCases(List<File> dirsToProcess) {
        for(File file : dirsToProcess){
            if(isZeroSimilarityCase(file)) {
                creteBaseToolZeroSimilarityCase(file);
            }
        }
    }

    private boolean isZeroSimilarityCase(File file) {
        for(CaseSimilarityBaseTool baseTool1 : responseModel.baseToolSimilarities) {
            if(file.getName().equalsIgnoreCase(baseTool1.caseName)){
                return false;
            }
        }
        return true;
    }

    private void creteBaseToolZeroSimilarityCase(File file) {
        CaseSimilarityBaseTool missingBaseTool = new CaseSimilarityBaseTool();
        missingBaseTool.caseName = file.getName();
        missingBaseTool.similarity = 0;
        responseModel.baseToolSimilarities.add(missingBaseTool);
    }

    private CalibratedToolCases calibrateTool(SimilarityDetectionTool toolToCalibrate) {
        SimilarityDetectionToolCalibrator calibrator = toolCalibratorFactory.getCalibrator(toolToCalibrate, phaseRunnerBuilder);
        List<BaseToolCase> baseToolCases = prepareBaseToolSimilarityCases();
        return calibrator.calibrateToolForSimilarity(baseToolCases,toolToCalibrate);
    }

    private List<BaseToolCase> prepareBaseToolSimilarityCases() {
        List<BaseToolCase> baseToolCases = new ArrayList<>();
        for(CaseSimilarityBaseTool caseSimilarityBaseTool : responseModel.baseToolSimilarities){
            BaseToolCase baseSimilarity = new BaseToolCase();
            baseSimilarity.caseName = caseSimilarityBaseTool.caseName;
            baseSimilarity.similarity = caseSimilarityBaseTool.similarity;
            baseToolCases.add(baseSimilarity);
        }
        return baseToolCases;
    }

    private void fillCalibratedToolResponsePart(CalibratedToolCases calibratedSimilarities) {
        List<CaseSimilarityCalibratedTool> responseCases = fillCalibrateToolResponseCases(calibratedSimilarities.calibratedToolCaseList);
        List<ToolParam> optimalParams = fillCalibratedToolParamsToResponseParams(calibratedSimilarities.optimalParams);
        responseModel.diffForAllParamCombos = fillDiffForAllParamCombos(calibratedSimilarities.diffForAllParamCombos);
        responseModel.calibratedToolSimilarities.addAll(responseCases);
        responseModel.calibratedToolOptimalParams.addAll(optimalParams);
    }

    private Map<List<ToolParam>,Float> fillDiffForAllParamCombos(Map<List<CalibratedToolParam>,Float> diffForAllParamCombos){
        Map<List<ToolParam>,Float> responseDiffForAllParamCombos = new HashMap<>();
        for(List<CalibratedToolParam> calibratedToolParams : diffForAllParamCombos.keySet()){
            List<ToolParam> respnseKey = fillCalibratedToolParamsToResponseParams(calibratedToolParams);
            Float responseValue = diffForAllParamCombos.get(calibratedToolParams);
            responseDiffForAllParamCombos.put(respnseKey,responseValue);
        }
        return  responseDiffForAllParamCombos;
    }

    private List<CaseSimilarityCalibratedTool> fillCalibrateToolResponseCases(List<CalibratedToolCase> calibratedSimilarities) {
        List<CaseSimilarityCalibratedTool> calibratedToolCases = new ArrayList<>();
        for(CalibratedToolCase calibratedToolCase : calibratedSimilarities){
            CaseSimilarityCalibratedTool caseSimilarityCalibratedTool = fillCaseData(calibratedToolCase);
            calibratedToolCases.add(caseSimilarityCalibratedTool);
        }
        return calibratedToolCases;
    }

    private CaseSimilarityCalibratedTool fillCaseData(CalibratedToolCase calibratedToolCase) {
        CaseSimilarityCalibratedTool caseSimilarityCalibratedTool = new CaseSimilarityCalibratedTool();
        caseSimilarityCalibratedTool.caseName = calibratedToolCase.caseName;
        caseSimilarityCalibratedTool.optimalSimilarity = calibratedToolCase.optimalSimilarity;
        caseSimilarityCalibratedTool.optimalSimilarityDiff = calibratedToolCase.optimalSimilarityDiff;
        caseSimilarityCalibratedTool.bestSimilarity = calibratedToolCase.caseBestSimilarity;
        caseSimilarityCalibratedTool.calibratedToolParams = fillCalibratedToolParamsToResponseParams(calibratedToolCase.caseBestParams);
        return caseSimilarityCalibratedTool;
    }

    private List<ToolParam> fillCalibratedToolParamsToResponseParams(List<CalibratedToolParam> calibratedToolParams) {
        List<ToolParam> responseToolParams = new ArrayList<>();
        for(CalibratedToolParam calibratedToolParam : calibratedToolParams) {
            ToolParam param = new ToolParam();
            param.paramName = calibratedToolParam.paramName;
            param.paramValue = calibratedToolParam.paramValue;
            responseToolParams.add(param);
        }
        return responseToolParams;
    }

    private List<ExecutionTool> createDetectionToolList(SimilarityDetectionTool baseTool) {
        List<ExecutionTool> toolList = new ArrayList<>();
        toolList.add(baseTool);
        return toolList;
    }

    private List<File> getDirsWithCases(ToolCalibrationRequestModel requestModel) {
        List<File> dirsToProcess = new ArrayList<>();
        dirsToProcess.addAll(Arrays.asList(requestModel.inputDirWithCalibrationCases.listFiles()));
        return dirsToProcess;
    }

    @Override
    public void processMatch(MPCMatch match) {
            String calibrationCaseName = getCalibrationToolCaseNameFromMatchDir(match.matchesDir);
            CaseSimilarityBaseTool caseSimilarityBaseTool = new CaseSimilarityBaseTool();
            caseSimilarityBaseTool.caseName = calibrationCaseName;
            caseSimilarityBaseTool.similarity = match.similarity;
            responseModel.baseToolSimilarities.add(caseSimilarityBaseTool);
    }
}

