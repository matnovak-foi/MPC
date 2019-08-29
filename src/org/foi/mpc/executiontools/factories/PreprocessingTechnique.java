package org.foi.mpc.executiontools.factories;

import java.io.File;
import org.foi.mpc.phases.executionphases.ExecutionTool;


public interface PreprocessingTechnique extends ExecutionTool{

    public void runPreporcess(File dirToProcess);
}