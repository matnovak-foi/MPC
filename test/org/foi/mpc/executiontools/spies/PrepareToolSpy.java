package org.foi.mpc.executiontools.spies;

import java.io.File;
import java.util.List;

import org.foi.mpc.executiontools.factories.PrepareTools;
import org.foi.mpc.phases.executionphases.spies.ExecutionToolSpy;

public class PrepareToolSpy extends ExecutionToolSpy implements PrepareTools {

    @Override
    public void runPrepareTool(File dirToProcess) {
        wasRuned = true;
        runCounter++;
        this.sourceDir = dirToProcess;
    }

    @Override
    public void runTool(File dirToProcess) {
        runPrepareTool(dirToProcess);
    }
}
