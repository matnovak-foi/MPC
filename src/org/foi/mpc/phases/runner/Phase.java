package org.foi.mpc.phases.runner;

import java.io.File;
import java.util.List;

public interface Phase {
    public List<File> getOutputDirs();
    public void setDirsToProcess(List<File> dirsToProcess);
    public void runPhase();
}
