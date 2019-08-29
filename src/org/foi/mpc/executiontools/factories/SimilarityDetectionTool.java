package org.foi.mpc.executiontools.factories;

import java.io.File;

import org.foi.mpc.executiontools.detectionTools.SimilarityDetectionToolSettings;
import org.foi.mpc.phases.executionphases.ExecutionTool;


public interface SimilarityDetectionTool extends ExecutionTool {
    public void runPlagiarsimDetection(File sourceDir);
    public void setMatchesDirName(String matchesDirName);
    public SimilarityDetectionToolSettings getSettings();
    public void setSettings(SimilarityDetectionToolSettings settings);
}