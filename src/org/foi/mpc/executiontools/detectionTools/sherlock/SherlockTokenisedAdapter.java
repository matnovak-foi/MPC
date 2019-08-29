package org.foi.mpc.executiontools.detectionTools.sherlock;

import org.foi.mpc.executiontools.detectionTools.SimilarityDetectionToolSettings;
import org.foi.mpc.executiontools.factories.SimilarityDetectionTool;

import java.io.File;

public class SherlockTokenisedAdapter extends SherlockAdapter implements SimilarityDetectionTool {

    public static final String NAME = "SherlockJava";

    public SherlockTokenisedAdapter() {
        super(SherlockFileType.TOKENISED);
        adapterSettings = changeDefuaultProfileSettings(adapterSettings);
    }
    
    private static SherlockAdapterSettings changeDefuaultProfileSettings(SherlockAdapterSettings adapterSettings){
        adapterSettings.minRunLenght = 3;
        adapterSettings.minStringLength = 5;
        adapterSettings.maxJumpDiff = 1;
        return adapterSettings;
    }

    @Override
    public String getName() {
        return NAME;
    }
    
    @Override
    public void runTool(File dirToProcess) {
        runPlagiarsimDetection(dirToProcess);
    }

    @Override
    public SherlockAdapterSettings getSettings() {
        return adapterSettings;
    }

    @Override
    public void setSettings(SimilarityDetectionToolSettings settings) {
        adapterSettings = (SherlockAdapterSettings) settings;
    }
}
