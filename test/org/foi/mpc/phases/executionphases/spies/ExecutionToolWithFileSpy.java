package org.foi.mpc.phases.executionphases.spies;

import org.foi.common.filesystem.file.TextFileUtility;
import org.foi.mpc.phases.executionphases.ExecutionTool;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ExecutionToolWithFileSpy implements ExecutionTool {
    protected String callId;
    public static String callOrder="";
    protected TextFileUtility tfu = new TextFileUtility(StandardCharsets.UTF_8);

    public ExecutionToolWithFileSpy(String callId) {
        this.callId = callId;
    }

    @Override
    public String getName() {
        return "ExecutionToolSpy"+callId;
    }

    @Override
    public void runTool(File dirToProcess) {
        callOrder += callId;
        try {
            tfu.appendTextToFile(dirToProcess.listFiles()[0], callId);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
