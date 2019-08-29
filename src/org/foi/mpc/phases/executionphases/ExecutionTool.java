package org.foi.mpc.phases.executionphases;

import java.io.File;


public interface ExecutionTool {
    public String getName();
    public void runTool(File dirToProcess);
}
