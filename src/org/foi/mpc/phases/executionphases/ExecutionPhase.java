package org.foi.mpc.phases.executionphases;

import org.foi.common.MPCexcpetions;
import org.foi.mpc.phases.runner.Phase;

import java.io.File;
import java.util.List;
import static org.foi.common.filesystem.directory.DirectoryFileUtility.checkIfDirExists;


public class ExecutionPhase implements Phase {
    public static class PhaseNotInstanciatedException extends RuntimeException {}

    private DirectoryToolIterator dirToolIterator;
    private File phaseWorkingDir;
    private boolean sequential;
    private List<ExecutionTool> executionTools;
    private File sourceDir;
    
    public ExecutionPhase(File workingDir, File sourceDir, String phaseSubDirName) {
        checkIfDirExists(workingDir);
        phaseWorkingDir = new File(workingDir + File.separator + phaseSubDirName);
        phaseWorkingDir.mkdir();
        dirToolIterator = new DirectoryToolIterator(phaseWorkingDir, sourceDir);
        this.sourceDir = sourceDir;
    }
    
    public void setDirsToProcess(List<File> dirsToProcess) {
        dirToolIterator.setDirsToProcess(dirsToProcess);
    }

    public void runPhase() {
        if (executionTools == null) {
            throw new MPCexcpetions.IsNullException();
        } else {
            if(sequential)
                dirToolIterator.iterateToolsSequential(executionTools);
            else
                dirToolIterator.iterateToolsIndependent(executionTools);
        }

    }

    public File getPhaseWorkingDir(){
        return phaseWorkingDir;
    }

    public List<File> getOutputDirs(){
        return dirToolIterator.getProcessedDirs();
    }

    public void setExecutionTools(List<ExecutionTool> executionTools) {
        this.executionTools = executionTools;
    }

    public void setSequential(boolean sequential) {
        this.sequential = sequential;
    }

    public List<ExecutionTool> getExecutionTools() {
        return executionTools;
    }

    public File getSourceDir() {
        return sourceDir;
    }

    public boolean getSequential(){
        return sequential;
    }
}
