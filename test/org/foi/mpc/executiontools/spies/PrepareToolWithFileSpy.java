package org.foi.mpc.executiontools.spies;

import org.foi.mpc.executiontools.factories.PrepareTools;
import org.foi.mpc.phases.executionphases.spies.ExecutionToolWithFileSpy;

import java.io.File;
import java.io.IOException;

public class PrepareToolWithFileSpy extends ExecutionToolWithFileSpy implements PrepareTools {
    public PrepareToolWithFileSpy(String callId) {
        super(callId);
    }

    @Override
    public void runPrepareTool(File dirToProcess) {
        callOrder += callId;
        try {
            tfu.appendTextToFile(dirToProcess.listFiles()[0], callId);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void runTool(File dirToProcess) {
        runPrepareTool(dirToProcess);
    }
}
