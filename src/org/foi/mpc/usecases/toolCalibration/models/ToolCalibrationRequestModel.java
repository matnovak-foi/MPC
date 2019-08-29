package org.foi.mpc.usecases.toolCalibration.models;

import java.io.File;
import java.util.List;

public class ToolCalibrationRequestModel {
    public String baseToolName;
    public List<ToolParam> baseToolParams;
    public File inputDirWithCalibrationCases;
    public File workingDir;
    public String toCalibrateToolName;

    @Override
    public String toString() {
        return "ToolCalibrationRequestModel{" +
                "baseToolName='" + baseToolName + '\'' +
                ", baseToolParams=" + baseToolParams +
                ", inputDirWithCalibrationCases=" + inputDirWithCalibrationCases +
                ", workingDir=" + workingDir +
                ", toCalibrateToolName='" + toCalibrateToolName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ToolCalibrationRequestModel)) return false;

        ToolCalibrationRequestModel that = (ToolCalibrationRequestModel) o;

        if (!baseToolName.equals(that.baseToolName)) return false;
        if (!baseToolParams.equals(that.baseToolParams)) return false;
        if (!inputDirWithCalibrationCases.equals(that.inputDirWithCalibrationCases)) return false;
        if (!workingDir.equals(that.workingDir)) return false;
        return toCalibrateToolName.equals(that.toCalibrateToolName);
    }
}
