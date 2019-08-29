package org.foi.mpc.usecases.toolCalibration;

import org.foi.mpc.usecases.toolCalibration.models.ToolCalibrationResponseModel;
import org.foi.mpc.usecases.toolCalibration.models.ToolCalibrationRequestModel;

public interface ToolCalibratorInputBoundary {
    void runCalibration(ToolCalibrationOutputBoundary presenter, ToolCalibrationRequestModel requestModel);
}
