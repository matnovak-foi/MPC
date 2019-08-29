package org.foi.mpc.executiontools.detectionTools.sherlock;

import org.foi.mpc.executiontools.detectionTools.SimilarityDetectionToolSettings;
import org.foi.mpc.executiontools.factories.SimilarityDetectionTool;

import java.io.File;


public class SherlockOriginalAdapter extends SherlockAdapter implements SimilarityDetectionTool {

    public static final String NAME = "SherlockText";

    public SherlockOriginalAdapter() {
        super(SherlockFileType.ORIGINAL);
    }
    
    @Override
    public String getName(){
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
