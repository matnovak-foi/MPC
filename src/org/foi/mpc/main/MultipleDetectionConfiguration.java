package org.foi.mpc.main;

import java.util.Objects;

public class MultipleDetectionConfiguration {
    public String selectedTechniques;
    public String selectedTool;
    public String selectedInputDir;
    public String selectedWorkingDir;
    public int inputDirDepth;
    public String comboTechniquesConfigFile;
    public String templateExclusionConfigFile;
    public String detectionToolsConfigFile;

    @Override
    public String toString() {
        return "MultipleDetectionConfiguration{" +
                "selectedTechniques='" + selectedTechniques + '\'' +
                ", selectedTool='" + selectedTool + '\'' +
                ", selectedInputDir='" + selectedInputDir + '\'' +
                ", selectedWorkingDir='" + selectedWorkingDir + '\'' +
                ", inputDirDepth=" + inputDirDepth +
                ", comboTechniquesConfigFile='" + comboTechniquesConfigFile + '\'' +
                ", templateExclusionConfigFile='" + templateExclusionConfigFile + '\'' +
                ", detectionToolsConfigFile='" + detectionToolsConfigFile + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MultipleDetectionConfiguration)) return false;
        MultipleDetectionConfiguration that = (MultipleDetectionConfiguration) o;
        return inputDirDepth == that.inputDirDepth &&
                Objects.equals(selectedTechniques, that.selectedTechniques) &&
                Objects.equals(selectedTool, that.selectedTool) &&
                Objects.equals(selectedInputDir, that.selectedInputDir) &&
                Objects.equals(selectedWorkingDir, that.selectedWorkingDir) &&
                Objects.equals(comboTechniquesConfigFile, that.comboTechniquesConfigFile) &&
                Objects.equals(templateExclusionConfigFile, that.templateExclusionConfigFile) &&
                Objects.equals(detectionToolsConfigFile, that.detectionToolsConfigFile);
    }
}
