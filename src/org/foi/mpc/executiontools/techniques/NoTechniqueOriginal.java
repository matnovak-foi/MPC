package org.foi.mpc.executiontools.techniques;

import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.mpc.executiontools.factories.PreprocessingTechnique;

import java.io.File;
import java.io.IOException;

public class NoTechniqueOriginal implements PreprocessingTechnique {
    public static final String NAME = "NoPreprocessing";

    public class NoTechniqueRunException extends RuntimeException {
        public NoTechniqueRunException(String message) {
            super(message);
        }
    }

    @Override
    public void runPreporcess(File dirToProcess) {
        for (File subDir : dirToProcess.listFiles()) {
            if (subDir.isDirectory()) {
                if (subDir.list().length == 1) {
                    try {
                        new DirectoryFileUtility().copyDirectoryTree(subDir, dirToProcess);
                        DirectoryFileUtility.deleteDirectoryTree(subDir);
                    } catch (IOException e) {
                        throw new NoTechniqueRunException(e.getMessage());
                    }
                } else {
                    throw new NoTechniqueRunException("Submission needs to be prepared with SubmissionUnifier before it can be used, must have one file in dir!");
                }
            }
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void runTool(File dirToProcess) {
        runPreporcess(dirToProcess);
    }
}
