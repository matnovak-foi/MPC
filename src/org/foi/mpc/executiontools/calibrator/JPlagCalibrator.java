package org.foi.mpc.executiontools.calibrator;

import org.foi.mpc.executiontools.calibrator.models.CalibratedToolParam;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagAdapterSettings;
import org.foi.mpc.executiontools.factories.SimilarityDetectionTool;
import org.foi.mpc.phases.runner.PhaseRunnerBuilder;

import java.util.ArrayList;
import java.util.List;

public class JPlagCalibrator extends SimilarityDetectionToolCalibrator {

    public JPlagCalibrator(PhaseRunnerBuilder phaseRunnerBuilder) {
        super(phaseRunnerBuilder);
    }

    @Override
    protected void runToolWithVariousParamCombinations(SimilarityDetectionTool tool) {
        for (int minTokenMatch = 1; minTokenMatch < 30; minTokenMatch++) {
            tool = setUpParamCombination(minTokenMatch);
            runToolWithParamCombination(tool);
        }
    }

    private SimilarityDetectionTool setUpParamCombination(int minTokenMatch) {
        JPlagAdapterSettings settings = (JPlagAdapterSettings) toolToCalibrate.getSettings();
        settings.minTokenMatch = minTokenMatch;
        toolToCalibrate.setSettings(settings);
        return toolToCalibrate;
    }

    @Override
    protected List<CalibratedToolParam> getCalibratedToolParams() {
        JPlagAdapterSettings settings = (JPlagAdapterSettings) toolToCalibrate.getSettings();
        CalibratedToolParam param = new CalibratedToolParam();
        param.paramName = "t";
        param.paramValue = settings.minTokenMatch;
        List<CalibratedToolParam> calibratedParams = new ArrayList<>();
        calibratedParams.add(param);
        return calibratedParams;
    }
}
