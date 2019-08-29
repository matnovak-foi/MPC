package org.foi.mpc.executiontools.factories;

import java.io.File;
import org.foi.mpc.phases.executionphases.ExecutionTool;


public interface PrepareTools extends ExecutionTool {

    public void runPrepareTool(File dirToProcess);

}