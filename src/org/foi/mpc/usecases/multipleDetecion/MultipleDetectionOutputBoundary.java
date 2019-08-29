package org.foi.mpc.usecases.multipleDetecion;

import org.foi.mpc.usecases.multipleDetecion.models.MultipleDetectionResponseModel;

public interface MultipleDetectionOutputBoundary {
    public void presentResults(MultipleDetectionResponseModel responseModel);
}
