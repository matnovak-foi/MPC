package org.foi.mpc.executiontools.detectionTools.JPlag;

import java.io.File;

public class JPlagJavaAdapter extends JPlagAdapter {
    public static final String NAME = "JPlagJava";

    public JPlagJavaAdapter() {
        super(JPlagLanguage.Java);
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