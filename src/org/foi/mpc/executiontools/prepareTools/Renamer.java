package org.foi.mpc.executiontools.prepareTools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Renamer extends AssignementDirectoryIterator {
    public static final String NAME = "Renamer";
    private List<File> unrenamedDirs = new ArrayList<>();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void runPrepareTool(File submissionDir) {
        if (submissionDir.isDirectory()) {
            rename(submissionDir);
        }
    }

    public File rename(File directory) {
        String name = rename(directory.getName());
        if (name.contains("_") || name.contains(" ")) {
            unrenamedDirs.add(directory);
        }

        File newDir = new File(directory.getParent()+File.separator+name);
        if (directory.renameTo(newDir))
            return newDir;

        return directory;
    }

    private String rename(String directoryName) {
        String[] directoryNameParts = directoryName.split("_");

        if (directoryName.contains("assignsubmission_file_")) {
            if (directoryNameParts.length == 7)
                return directoryNameParts[4];
            else
                return directoryName;
        }

        return directoryNameParts[0];
    }

    public List<File> getUnrenamedDirs() {
        return unrenamedDirs;
    }
}
