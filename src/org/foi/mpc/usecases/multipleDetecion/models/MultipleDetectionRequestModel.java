package org.foi.mpc.usecases.multipleDetecion.models;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class MultipleDetectionRequestModel {
    public List<String> selectedTechniques;
    public List<String> selectedTools;
    public File selectedInputDir;
    public File selectedWorkingDir;
    public int inputDirDepth;

    @Override
    public String toString() {
        return "MultipleDetectionRequestModel{" +
                "selectedTechniques=" + selectedTechniques +
                ", selectedTools=" + selectedTools +
                ", selectedInputDir=" + selectedInputDir +
                ", selectedWorkingDir=" + selectedWorkingDir +
                ", inputDirDepth=" + inputDirDepth +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MultipleDetectionRequestModel)) return false;
        MultipleDetectionRequestModel that = (MultipleDetectionRequestModel) o;
        return inputDirDepth == that.inputDirDepth &&
                Objects.equals(selectedTechniques, that.selectedTechniques) &&
                Objects.equals(selectedTools, that.selectedTools) &&
                Objects.equals(selectedInputDir, that.selectedInputDir) &&
                Objects.equals(selectedWorkingDir, that.selectedWorkingDir);
    }
}
