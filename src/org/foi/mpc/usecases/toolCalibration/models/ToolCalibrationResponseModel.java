package org.foi.mpc.usecases.toolCalibration.models;

import org.foi.mpc.executiontools.calibrator.models.CalibratedToolParam;

import java.util.List;
import java.util.Map;

public class ToolCalibrationResponseModel {
    public String baseToolName;
    public List<ToolParam> baseToolParams;
    public List<CaseSimilarityBaseTool> baseToolSimilarities;
    public String calibratedToolName;
    public List<CaseSimilarityCalibratedTool> calibratedToolSimilarities;
    public String errorMessage;
    public List<ToolParam> calibratedToolOptimalParams;
    public Map<List<ToolParam>,Float> diffForAllParamCombos;

    @Override
    public String toString() {
        return "ToolCalibrationResponseModel{" +
                "baseToolName='" + baseToolName + '\'' +
                ", baseToolParams=" + baseToolParams +
                ", baseToolSimilarities=" + baseToolSimilarities +
                ", calibratedToolName='" + calibratedToolName + '\'' +
                ", calibratedToolSimilarities=" + calibratedToolSimilarities +
                ", errorMessage='" + errorMessage + '\'' +
                ", calibratedToolOptimalParams=" + calibratedToolOptimalParams +
                ", diffForAllParamCombos=" + diffForAllParamCombos +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ToolCalibrationResponseModel)) return false;

        ToolCalibrationResponseModel that = (ToolCalibrationResponseModel) o;

        if (!baseToolName.equals(that.baseToolName)) return false;
        if (!baseToolParams.equals(that.baseToolParams)) return false;
        if (!baseToolSimilarities.equals(that.baseToolSimilarities)) return false;
        if (!calibratedToolName.equals(that.calibratedToolName)) return false;
        if (!calibratedToolSimilarities.equals(that.calibratedToolSimilarities)) return false;
        if (!errorMessage.equals(that.errorMessage)) return false;
        if (!calibratedToolOptimalParams.equals(that.calibratedToolOptimalParams)) return false;
        return diffForAllParamCombos.equals(that.diffForAllParamCombos);
    }
}
