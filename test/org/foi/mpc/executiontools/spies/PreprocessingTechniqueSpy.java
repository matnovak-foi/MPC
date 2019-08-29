package org.foi.mpc.executiontools.spies;

import java.io.File;

import org.foi.mpc.executiontools.factories.PreprocessingTechnique;
import org.foi.mpc.phases.executionphases.spies.ExecutionToolSpy;

public class PreprocessingTechniqueSpy extends ExecutionToolSpy implements PreprocessingTechnique {



    @Override
    public void runPreporcess(File dirToProcess) {
        wasRuned = true;
        runCounter++;
        this.sourceDir = dirToProcess;
    }

    @Override
    public void runTool(File dirToProcess) {
        runPreporcess(dirToProcess);
    }


}
