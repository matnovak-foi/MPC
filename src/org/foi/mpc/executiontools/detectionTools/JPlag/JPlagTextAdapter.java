package org.foi.mpc.executiontools.detectionTools.JPlag;

import org.foi.mpc.executiontools.detectionTools.SimilarityDetectionToolSettings;

import java.io.File;



public class JPlagTextAdapter extends JPlagAdapter {

    public static final String NAME = "JPlagText";

    public JPlagTextAdapter() {
        super(JPlagLanguage.Text);
    }

    @Override
    public String getName() {
        return NAME;
    }
    
    @Override
    public void runTool(File dirToProcess) {
        runPlagiarsimDetection(dirToProcess);
    }

}