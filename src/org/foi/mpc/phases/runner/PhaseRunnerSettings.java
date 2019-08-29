package org.foi.mpc.phases.runner;

import java.io.File;
import java.util.List;

public class PhaseRunnerSettings {
    public boolean runPreparePhase;
    public boolean runPreprocessPhase;
    public boolean runDetectionPhase;
    public boolean runMPCMatchReadPhase;

    public List<File> prepareOutputDirs;
    public List<File> preprocessOutputDirs;
    public List<File> detectionOutputDirs;
}