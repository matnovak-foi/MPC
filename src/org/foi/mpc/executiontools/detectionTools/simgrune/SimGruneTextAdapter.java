package org.foi.mpc.executiontools.detectionTools.simgrune;

import java.io.File;


public class SimGruneTextAdapter extends SimGruneAdapter {

    public static final String NAME = "SIMGruneText";

    public SimGruneTextAdapter() {
        super(new File(getSimExePath("sim_text")));
    }

    protected SimGruneTextAdapter(String params) {
        super(new File(getSimExePath("sim_text") + params));
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