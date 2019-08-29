package org.foi.mpc.usecases.toolCalibration;

import org.foi.mpc.usecases.toolCalibration.models.ToolCalibrationResponseModel;

public interface ToolCalibrationOutputBoundary {
    void presentReport(ToolCalibrationResponseModel responseModel);
}
