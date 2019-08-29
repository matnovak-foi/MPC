package org.foi.mpc.executiontools.detectionTools.simgrune;

import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;


public class SimGruneJavaAdapter extends SimGruneAdapter {

    public static final String NAME = "SIMGruneJava";
    private static String exePath;

    public SimGruneJavaAdapter() {
        super(new File(getSimExePath("sim_java")));
    }

    protected SimGruneJavaAdapter(String params) {
        super(new File(getSimExePath("sim_java") + params));
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