package org.foi.mpc.executiontools.spies;

import java.io.File;

import org.foi.mpc.executiontools.detectionTools.SimilarityDetectionToolSettings;
import org.foi.mpc.executiontools.factories.SimilarityDetectionTool;
import org.foi.mpc.phases.executionphases.spies.ExecutionToolSpy;

public class SimilarityDetectionToolSpy extends ExecutionToolSpy implements SimilarityDetectionTool {

    @Override
    public void runPlagiarsimDetection(File sourceDir) {
        wasRuned = true;
        runCounter++;
        this.sourceDir = sourceDir;
    }

    @Override
    public void setMatchesDirName(String matchesDirName) {
        
    }

    @Override
    public SimilarityDetectionToolSettings getSettings() {
        return null;
    }

    @Override
    public void setSettings(SimilarityDetectionToolSettings settings) {

    }

    @Override
    public void runTool(File dirToProcess) {
        runPlagiarsimDetection(dirToProcess);
    }
}
