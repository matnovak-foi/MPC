package org.foi.mpc.executiontools.calibrator;

import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.mpc.executiontools.calibrator.models.BaseToolCase;
import org.foi.mpc.executiontools.calibrator.models.CalibratedToolCase;
import org.foi.mpc.executiontools.calibrator.models.CalibratedToolCases;
import org.foi.mpc.executiontools.calibrator.models.CalibratedToolParam;
import org.foi.mpc.executiontools.factories.SimilarityDetectionTool;
import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.phases.executionphases.ExecutionTool;
import org.foi.mpc.phases.readerphase.MPCMatchListener;
import org.foi.mpc.phases.runner.PhaseRunner;
import org.foi.mpc.phases.runner.PhaseRunnerBuilder;

import java.io.File;
import java.io.IOException;
import java.util.*;

public abstract class SimilarityDetectionToolCalibrator implements MPCMatchListener {
    public static class CasesMismatchException extends RuntimeException {
        public CasesMismatchException(String message) {
            super(message);
        }
    }

    public static class ResetDirException extends RuntimeException {
        public ResetDirException(String message) {
            super(message);
        }
    }

    private PhaseRunnerBuilder phaseRunnerBuilder;
    protected List<BaseToolCase> baseToolCases;
    protected List<CalibratedToolCase> calibratedSimilarityCases;
    protected List<CalibratedToolParam> optimalParams = new ArrayList<>();
    private Map<String,CurrentOptimalToolData> currentOptimalData = new HashMap<>();
    protected Map<List<CalibratedToolParam>,Float> diffForAllParamCombos = new HashMap<>();
    protected SimilarityDetectionTool toolToCalibrate;

    public static String getCalibrationToolCaseNameFromMatchDir(File matchesDir){
        return matchesDir.getParentFile().getName();
    }

    public List<CalibratedToolCase> getCalibratedSimilarityCases() {
        return calibratedSimilarityCases;
    }
    public List<CalibratedToolParam> getOptimalParams() {
        return optimalParams;
    }

    public SimilarityDetectionToolCalibrator(PhaseRunnerBuilder phaseRunnerBuilder) {
        this.phaseRunnerBuilder = phaseRunnerBuilder;
    }

    public CalibratedToolCases calibrateToolForSimilarity(List<BaseToolCase> baseToolCases, SimilarityDetectionTool toolToCalibrate) {
        this.baseToolCases = baseToolCases;
        this.toolToCalibrate = toolToCalibrate;
        initializeCalibrtedCasesWithZeroSimilarity();
        resetCurrentOptimalDataToZeroSimilaritites();
        tearDownParamCombinationDirs(phaseRunnerBuilder.build());
        runToolWithVariousParamCombinations(toolToCalibrate);

        CalibratedToolCases casesResponse = new CalibratedToolCases();
        casesResponse.calibratedToolCaseList = calibratedSimilarityCases;
        casesResponse.optimalParams = optimalParams;
        casesResponse.diffForAllParamCombos = diffForAllParamCombos;
        return casesResponse;
    }

    private void initializeCalibrtedCasesWithZeroSimilarity(){
        calibratedSimilarityCases = new ArrayList<>();
        for(BaseToolCase baseToolCase : baseToolCases){
            CalibratedToolCase calibratedToolCase = createCalibratedToolCase(baseToolCase.caseName);
            calibratedToolCase.caseBestParams = getCalibratedToolParams();
            calibratedSimilarityCases.add(calibratedToolCase);
            calibratedToolCase.optimalSimilarityDiff = baseToolCase.similarity;
        }
    }

    private void resetCurrentOptimalDataToZeroSimilaritites() {
        currentOptimalData.clear();
        for (BaseToolCase baseToolCase : baseToolCases) {
            CurrentOptimalToolData toolData  = new CurrentOptimalToolData();
            toolData.similiarity = 0;
            toolData.similarityDiff = baseToolCase.similarity;
            currentOptimalData.put(baseToolCase.caseName,toolData);
        }
    }

    abstract protected void runToolWithVariousParamCombinations(SimilarityDetectionTool tool);

    protected void runToolWithParamCombination(SimilarityDetectionTool tool) {
        PhaseRunner phaseRunner = runParamCombination(tool);
        if(isFirstIteration() || currentIterationHasBeterOptimal()) {
            updateOptimalInfoForAllCases();
        }
        storeIterationDiff();
        tearDownParamCombinationDirs(phaseRunner);
        resetCurrentOptimalDataToZeroSimilaritites();
    }

    private PhaseRunner runParamCombination(SimilarityDetectionTool detectionTool) {
        List<ExecutionTool> toolList = new ArrayList<>();
        toolList.add(detectionTool);
        PhaseRunner phaseRunner = phaseRunnerBuilder
                .withMatchReadListener(this)
                .forToolList(toolList)
                .build();
        phaseRunner.runPhases();
        return phaseRunner;
    }

    private boolean isFirstIteration(){
        return optimalParams.isEmpty();
    }

    private boolean currentIterationHasBeterOptimal(){
        return getNewTotalDiff() < getCurrentTotalDiff();
    }

    private float getCurrentTotalDiff(){
        float sum=0;
        for (CalibratedToolCase calibratedCase : calibratedSimilarityCases) {
            sum += calibratedCase.optimalSimilarityDiff;
        }
        return sum;
    }

    private float getNewTotalDiff() {
        float sum = 0;
        for (String caseName : this.currentOptimalData.keySet()) {
            CurrentOptimalToolData newOptimalData = this.currentOptimalData.get(caseName);
            sum += newOptimalData.similarityDiff;
        }
        return sum;
    }

    private void updateOptimalInfoForAllCases(){
        for(String caseName : this.currentOptimalData.keySet()) {
            CurrentOptimalToolData newOptimalData = this.currentOptimalData.get(caseName);
            CalibratedToolCase calibratedCase = findCalibratedToolCase(caseName);
            calibratedCase.optimalSimilarityDiff = newOptimalData.similarityDiff;
            calibratedCase.optimalSimilarity = newOptimalData.similiarity;
        }
        this.optimalParams = getCalibratedToolParams();
    }

    private void storeIterationDiff(){
        diffForAllParamCombos.put(getCalibratedToolParams(),getNewTotalDiff());
    }

    private void tearDownParamCombinationDirs(PhaseRunner phaseRunner) {
        if(phaseRunner.getSettings().detectionOutputDirs != null) {
            for (File dir : phaseRunner.getSettings().detectionOutputDirs) {
                try {
                    if (dir.list().length != 0)
                        DirectoryFileUtility.deleteDirectoryTree(dir);
                } catch (IOException e) {
                    throw new ResetDirException(e.getMessage());
                }
            }
        }
    }

    private CalibratedToolCase createCalibratedToolCase(String caseName) {
        CalibratedToolCase calibratedCase = new CalibratedToolCase();
        calibratedCase.caseName = caseName;
        calibratedCase.caseBestSimilarity = 0;
        calibratedCase.caseBestParams = new ArrayList<>();
        return calibratedCase;
    }

    @Override
    public void processMatch(MPCMatch match) {
        CalibratedToolCase calibratedCase = findCalibratedToolCase(getCalibrationToolCaseNameFromMatchDir(match.matchesDir));
        float baseSimilarity = findBaseSimilarityForCase(calibratedCase.caseName);

        float currentSimilarityDiffToOptimum = Math.abs(calibratedCase.caseBestSimilarity - baseSimilarity);
        float newSimilarityDiffDiffToOptimum = Math.abs(match.similarity - baseSimilarity);
        if (calibratedCase.caseBestParams == null || newSimilarityDiffDiffToOptimum < currentSimilarityDiffToOptimum) {
            setUpCalibratedToolCaseBestPart(match, calibratedCase);
        }

        setUpCurrentIterationOptimalToolData(match, calibratedCase, newSimilarityDiffDiffToOptimum);
    }

    private CalibratedToolCase findCalibratedToolCase(String caseName) {
        for (CalibratedToolCase calibratedCase : calibratedSimilarityCases) {
            if (calibratedCase.caseName.equalsIgnoreCase(caseName))
                return calibratedCase;
        }
        throw new CasesMismatchException("Calibrated case name and calibration tool case name mismatch!" + caseName);
    }

    private float findBaseSimilarityForCase(String caseName) {
        for (BaseToolCase baseCase : baseToolCases) {
            if (baseCase.caseName.equalsIgnoreCase(caseName))
                return baseCase.similarity;
        }
        throw new CasesMismatchException("Base case name and calibration tool case name mismatch!" + caseName);
    }

    private CalibratedToolCase setUpCalibratedToolCaseBestPart(MPCMatch match, CalibratedToolCase calibratedCase) {
        calibratedCase.caseBestSimilarity = match.similarity;
        calibratedCase.caseBestParams = new ArrayList<>();
        calibratedCase.caseBestParams = getCalibratedToolParams();
        return calibratedCase;
    }

    protected abstract List<CalibratedToolParam> getCalibratedToolParams();

    private void setUpCurrentIterationOptimalToolData(MPCMatch match, CalibratedToolCase calibratedCase, float newSimilarityDiffDiffToOptimum) {
        CurrentOptimalToolData currentOptimalToolData = new CurrentOptimalToolData();
        currentOptimalToolData.similarityDiff = newSimilarityDiffDiffToOptimum;
        currentOptimalToolData.similiarity = match.similarity;
        this.currentOptimalData.remove(calibratedCase.caseName);
        this.currentOptimalData.put(calibratedCase.caseName,currentOptimalToolData);
    }

    private class CurrentOptimalToolData {
        public float similiarity;
        public float similarityDiff;
    }
}
