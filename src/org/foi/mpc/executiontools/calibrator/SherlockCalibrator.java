package org.foi.mpc.executiontools.calibrator;

import org.foi.mpc.executiontools.calibrator.models.CalibratedToolParam;
import org.foi.mpc.executiontools.detectionTools.sherlock.SherlockAdapterSettings;
import org.foi.mpc.executiontools.factories.SimilarityDetectionTool;
import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.phases.runner.PhaseRunnerBuilder;

import java.util.ArrayList;
import java.util.List;

public class SherlockCalibrator extends SimilarityDetectionToolCalibrator {
    private SherlockCalibraorConfig config;

    public SherlockCalibrator(PhaseRunnerBuilder phaseRunnerBuilder) {
        super(phaseRunnerBuilder);
        config = new SherlockCalibraorConfig();
    }

    public void setSherlockCalibratorConfig(SherlockCalibraorConfig config){
        this.config = config;
    }

    @Override
    protected void runToolWithVariousParamCombinations(SimilarityDetectionTool tool) {
        for (int strictness : config.strictness_s) {
            for (int minStrLen : config.minStrLens) {
                for (int maxJmpDiff : config.maxJmpDiffs) {
                    for (int maxFwdJmp : config.maxFwdJmps) {
                        for (int minRunLength : config.minRunLengths) {
                            tool = setUpParamCombination(minRunLength,maxFwdJmp,maxJmpDiff,
                                                            minStrLen,strictness,config.maxBwdJmp);
                            runToolWithParamCombination(tool);
                        }
                    }
                }
            }
        }
    }

    private SimilarityDetectionTool setUpParamCombination(int minRunLength,
                                                          int maxFwdJmp,
                                                          int maxJmpDiff,
                                                          int minStrLen,
                                                          int strictness,
                                                          int maxBwdJmp) {
        SherlockAdapterSettings settings = (SherlockAdapterSettings) toolToCalibrate.getSettings();
        settings.minRunLenght = minRunLength;
        settings.maxForwardJump = maxFwdJmp;
        settings.maxJumpDiff = maxJmpDiff;
        settings.minStringLength = minStrLen;
        settings.strictness = strictness;
        settings.maxBackwardJump = maxBwdJmp;
        toolToCalibrate.setSettings(settings);
        return toolToCalibrate;
    }


    @Override
    protected List<CalibratedToolParam> getCalibratedToolParams() {
        List<CalibratedToolParam> calibratedToolParams = new ArrayList<>();
        SherlockAdapterSettings settings = (SherlockAdapterSettings) toolToCalibrate.getSettings();

        CalibratedToolParam param1 = new CalibratedToolParam();
        param1.paramName = "maxBackwardJump";
        param1.paramValue = settings.maxBackwardJump;
        calibratedToolParams.add(param1);

        CalibratedToolParam param2 = new CalibratedToolParam();
        param2.paramName = "maxForwardJump";
        param2.paramValue = settings.maxForwardJump;
        calibratedToolParams.add(param2);

        CalibratedToolParam param3 = new CalibratedToolParam();
        param3.paramName = "minRunLenght";
        param3.paramValue = settings.minRunLenght;
        calibratedToolParams.add(param3);

        CalibratedToolParam param4 = new CalibratedToolParam();
        param4.paramName = "minStringLength";
        param4.paramValue = settings.minStringLength;
        calibratedToolParams.add(param4);

        CalibratedToolParam param5 = new CalibratedToolParam();
        param5.paramName = "maxJumpDiff";
        param5.paramValue = settings.maxJumpDiff;
        calibratedToolParams.add(param5);

        CalibratedToolParam param6 = new CalibratedToolParam();
        param6.paramName = "strictness";
        param6.paramValue = settings.strictness;
        calibratedToolParams.add(param6);

        return calibratedToolParams;
    }
}
