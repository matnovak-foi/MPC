package org.foi.mpc.usecases.multipleDetecion.models;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MultipleDetectionResponseModel {
    public List<File> outputDirs = new ArrayList<>();
    public String errorMessages;

    @Override
    public String toString() {
        return "MultipleDetectionResponseModel{" +
                "outputDirs=" + outputDirs +
                ", errorMessages='" + errorMessages + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MultipleDetectionResponseModel)) return false;
        MultipleDetectionResponseModel that = (MultipleDetectionResponseModel) o;
        return Objects.equals(outputDirs, that.outputDirs) &&
                Objects.equals(errorMessages, that.errorMessages);
    }
}
