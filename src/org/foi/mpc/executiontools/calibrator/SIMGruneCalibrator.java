package org.foi.mpc.executiontools.calibrator;

import org.foi.mpc.executiontools.calibrator.models.CalibratedToolParam;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneAdapterSettings;
import org.foi.mpc.executiontools.factories.SimilarityDetectionTool;
import org.foi.mpc.phases.runner.PhaseRunnerBuilder;

import java.util.ArrayList;
import java.util.List;

public class SIMGruneCalibrator extends SimilarityDetectionToolCalibrator {
    public SIMGruneCalibrator(PhaseRunnerBuilder phaseRunnerBuilder) {
        super(phaseRunnerBuilder);
    }

    @Override
    protected void runToolWithVariousParamCombinations(SimilarityDetectionTool tool) {
        for (int minRunLength = 1; minRunLength < 30; minRunLength++) {
            tool = setUpParamCombination(minRunLength);
            runToolWithParamCombination(tool);
        }
    }

    private SimilarityDetectionTool setUpParamCombination(int minRunLength) {
        SimGruneAdapterSettings settings = (SimGruneAdapterSettings) toolToCalibrate.getSettings();
        settings.minRunLength = minRunLength;
        toolToCalibrate.setSettings(settings);
        return toolToCalibrate;
    }

    @Override
    protected List<CalibratedToolParam> getCalibratedToolParams() {
        SimGruneAdapterSettings settings = (SimGruneAdapterSettings) toolToCalibrate.getSettings();
        CalibratedToolParam param = new CalibratedToolParam();
        param.paramName = "r";
        param.paramValue = settings.minRunLength;
        List<CalibratedToolParam> calibratedParams = new ArrayList<>();
        calibratedParams.add(param);
        return calibratedParams;
    }
}