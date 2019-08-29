package org.foi.mpc.phases.executionphases.spies;

import org.foi.mpc.phases.executionphases.ExecutionTool;

import java.io.File;

public class ExecutionToolSpy implements ExecutionTool {

    protected boolean wasRuned = false;
    protected int runCounter = 0;
    protected File sourceDir;
    private static int instanceIDCounter = 0;
    protected int instanceID;
    private String name;

    @Override
    public void runTool(File dirToProcess) {
        wasRuned = true;
        runCounter++;
        this.sourceDir = dirToProcess;
    }

    public ExecutionToolSpy() {
        this.instanceID = instanceIDCounter;
        instanceIDCounter++;
        name = "ExecutionToolSpy"+instanceID;
    }

    public boolean runMethodWasCalled() {
        return wasRuned;
    }

    public int wasRunHowManyTimes() {
        return runCounter;
    }

    public void resetRunTimeCounter() {
        runCounter = 0;
    }

    public File getSoruceDir() {
        return sourceDir;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}
