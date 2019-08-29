package org.foi.mpc.phases.runner;

import java.io.File;
import java.util.List;

import org.foi.mpc.phases.executionphases.ExecutionPhase;
import org.foi.mpc.phases.runner.Phase;

public class PhaseSpy implements Phase {

    public boolean runPhaseCalled = false;
    List<File> dirsToProcess;

    @Override
    public void runPhase() {
        runPhaseCalled = true;
    }

    public List<File> getDirsToProcess() {
        return dirsToProcess;
    }

    @Override
    public List<File> getOutputDirs() {
        return dirsToProcess;
    }

    @Override
    public void setDirsToProcess(List<File> dirsToProcess) {
        this.dirsToProcess = dirsToProcess;
    }
}