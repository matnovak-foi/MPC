package org.foi.mpc.phases.executionphases;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.foi.common.MPCexcpetions;
import org.foi.common.filesystem.directory.DirectoryFileUtility;

import static org.foi.common.filesystem.directory.DirectoryFileUtility.checkIfDirExists;
import static org.foi.common.filesystem.directory.DirectoryFileUtility.createTargetPath;

public class DirectoryToolIterator {

    private File workingDir;
    private File sourceDir;

    private List<File> dirsToProcess;
    private List<File> processedDirs;
    private List<ExecutionTool> executionTools;
    private int processedDirsCount;

    private ExecutionLogger logger = new ExecutionLogger();
    
    public DirectoryToolIterator(File workingDir, File sourceDir) {
        checkIfDirExists(workingDir);
        checkIfDirExists(sourceDir);
        this.workingDir = workingDir;
        this.sourceDir = sourceDir;
    }

    public void iterateToolsSequential(List<ExecutionTool> executionTools) {
        checkDirAndToolLists(executionTools);
        executeWithAllToolsSequential();
    }

    public void iterateToolsIndependent(List<ExecutionTool> executionTools) {
        checkDirAndToolLists(executionTools);
        executeWithAllTools();
    }

    private void checkDirAndToolLists(List<ExecutionTool> executionTools) {
        this.executionTools = executionTools;
        this.processedDirs = new ArrayList<>();
        if (dirsToProcess == null || executionTools == null) {
            throw new MPCexcpetions.IsNullException();
        }
        processedDirsCount = 0;
    }

    private void executeWithAllTools() {
        executionTools.forEach((tool) -> {
            executeOnAllSourceDirsWithTool(tool);
        });
    }

    private void executeWithAllToolsSequential() {
        executionTools.forEach((tool) -> {
            executeOnAllSourceDirsWithTool(tool);
            sourceDir = new File(workingDir.getPath()+File.separator+tool.getName());
            dirsToProcess.clear();
            dirsToProcess.addAll(processedDirs);
            processedDirs.clear();
        });
        makePorcessedDirsBeLastProcessedDirs();
    }

    private void executeOnAllSourceDirsWithTool(ExecutionTool tool) {
        for (File dirToProces : dirsToProcess) {
            Path dirToProcessCopy = getDirCopyPathForTool(tool, dirToProces);
            if(dirToProcessCopy.toFile().exists() && dirToProcessCopy.toFile().listFiles().length != 0){
                processedDirsCount++;
               this.processedDirs.add(dirToProcessCopy.toFile());
            } else {
                createProcessDirCopy(dirToProces, dirToProcessCopy);
                exectueToolOnDirectory(tool, dirToProcessCopy.toFile());
            }
        }
    }

    private Path getDirCopyPathForTool(ExecutionTool tool, File dirToProces) {
        File toolDir = createDirForTool(tool);
        Path dirToProcessCopy = createTargetPath(sourceDir.toPath(), toolDir.toPath(), dirToProces.toPath());
        return dirToProcessCopy;
    }

    private File createDirForTool(ExecutionTool tool) {
        File toolDir = new File(workingDir.getPath() + File.separator + tool.getName());
        toolDir.mkdir();
        return toolDir;
    }

    protected void createProcessDirCopy(File dirToProces, Path dirToProcessCopy) {
        try {
            dirToProcessCopy.toFile().mkdirs();
            new DirectoryFileUtility().copyDirectoryTree(dirToProces, dirToProcessCopy.toFile());
        } catch (Exception ex) {
            throw new DirectoryFileUtility.DirCopyException(ex.getMessage());
        }
    }
    
    private void exectueToolOnDirectory(ExecutionTool tool, File dirToProces) {
        processedDirsCount++;
        this.processedDirs.add(dirToProces);
        logger.beginRun(processedDirsCount,dirToProces,tool);
        tool.runTool(dirToProces);
        logger.endRun(processedDirsCount,dirToProces,tool);
    }

    private void makePorcessedDirsBeLastProcessedDirs() {
        processedDirs = dirsToProcess;
    }

    public int getProcessedDirsCount() {
        return processedDirsCount;
    }
    
    public List<File> getProcessedDirs() {
        return processedDirs;
    }

    public void setDirsToProcess(List<File> dirsToProcess) {
        this.dirsToProcess = dirsToProcess;
    }

}
