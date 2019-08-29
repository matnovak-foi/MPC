package org.foi.mpc.executiontools.prepareTools;

import org.foi.mpc.executiontools.factories.PrepareTools;
import org.foi.common.filesystem.directory.DirectoryFileUtility;

import java.io.File;

public abstract class AssignementDirectoryIterator implements PrepareTools {

    @Override
    public void runTool(File assignmentDir) {
        DirectoryFileUtility.checkIfDirExists(assignmentDir);
        for (File file : assignmentDir.listFiles()) {
            runPrepareTool(file);
        }
    }
}
