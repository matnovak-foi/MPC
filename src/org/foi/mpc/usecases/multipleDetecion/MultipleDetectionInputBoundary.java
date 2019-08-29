package org.foi.mpc.usecases.multipleDetecion;

import org.foi.mpc.abstractfactories.FactoryProvider;
import org.foi.mpc.usecases.multipleDetecion.models.MultipleDetectionRequestModel;

public interface MultipleDetectionInputBoundary {
    public void setUpFactories(FactoryProvider factoryProvider);
    public void runMultipleDetecion(MultipleDetectionRequestModel requestModel, MultipleDetectionOutputBoundary presenter);
}
