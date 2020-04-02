package org.foi.mpc.executiontools.prepareTools;

import org.foi.common.filesystem.file.TextFileUtility;
import org.foi.mpc.MPCContext;
import org.foi.mpc.phases.executionphases.ExecutionLogger;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SubmissionFilesUnifier extends AssignementDirectoryIterator {
    public static final String NAME = "SubmissionFilesUnifier";

    private TextFileUtility tfu;
    private List<String> mergeExtensions = new ArrayList<>();
    private String mergeFileExtension;
    private File mergedFile;

    public SubmissionFilesUnifier() {
        tfu = new TextFileUtility(StandardCharsets.UTF_8);
        this.mergeExtensions.add(".java");
        this.mergeFileExtension = "java";
        //TODO system will break if .java is removed as extension
        //need test for that situation where only PHP files are tested
        //maybee make mergeFileExtensionSomethingNew or that .java is always added as extension
    }

    public File getMergedFile() {
        return mergedFile;
    }

    @Override
    public void runPrepareTool(File dirToProcess) {
        unifyFilesInDirToOne(dirToProcess);
    }

    @Override
    public String getName() {
        return NAME;
    }

    public void setMergeExtensions(List<String> mergeExtensions) {
        this.mergeExtensions = mergeExtensions;
    }

    public void setTfu(TextFileUtility tfu) {
        this.tfu = tfu;
    }

    public File unifyFilesInDirToOne (File directory) {
        createEmptyMergeFile(directory);
        unifyFilesInDir(directory);
        return mergedFile;
    }

    private void unifyFilesInDir(File directory) {
        if(MPCContext.CONSOLE_PRINT){
            System.out.println("MERGE FILES IN:"+directory.getPath());
        }
        File[] directoryElementsForMerge = getElementsForMerge(directory);
        mergeElementsInDir(directoryElementsForMerge);
        deleteMergedElements(directoryElementsForMerge);
        deleteFilesNotForMerging(directory);
    }

    private File[] getElementsForMerge(File directory) {
        return directory.listFiles(new FileExtensionFilter(mergeExtensions));
    }

    private void mergeElementsInDir(File[] directoryElementsForMerge) {
        for (File element : directoryElementsForMerge) {
            if (element.isDirectory()) {
                unifyFilesInDir(element);
            } else {
                if(!mergedFile.equals(element))
                    mergedFile = mergeFiles(mergedFile, element);
            }
        }
    }

    private void createEmptyMergeFile(File directory) {
        String mergedFileName =directory.getName() + "." + mergeFileExtension;
        File mergedFileExists = new File(directory.getPath()+File.separator+mergedFileName);
        if(directory.list().length==1 && mergedFileExists.exists()) {
            mergedFile = mergedFileExists;
        } else if(mergedFileExists.exists()) {
            try {
                mergedFile = mergedFileExists;
                tfu.appendTextToFile(mergedFile, tfu.getLineSeparator());
            } catch (IOException e) {
                throw new TextFileUtility.FileReadWriteException(e.getMessage());
            }
        } else {
            try {
                if(MPCContext.CONSOLE_PRINT){
                    System.out.println("MERGE FILES IN:"+directory.getPath());
                    System.out.println("FILE IN:"+mergedFileName);
                }
                mergedFile = tfu.createFileWithText(directory, mergedFileName, "");
            } catch (IOException e) {
                throw new TextFileUtility.FileReadWriteException(e.getMessage());
            }
        }
    }

    private void deleteMergedElements(File[] directoryElementsForMerge) {
        for (File element : directoryElementsForMerge) {
                if(!element.equals(mergedFile))
                    element.delete();
        }
    }

    private File mergeFiles(File file1, File file2) {
        try {
            String file2content = tfu.readFileContentToString(file2);
            file2content += tfu.getLineSeparator();
            tfu.appendTextToFile(file1, file2content);
        } catch (IOException e) {
            new TextFileUtility.FileReadWriteException(e.getMessage());
        }
        return file1;
    }

    private void deleteFilesNotForMerging(File directory) {
        File[] directoryFilesToDelete = directory.listFiles(new FileExtensionFilterInversed(mergeExtensions));
        for (File file : directoryFilesToDelete) {
            file.delete();
        }
    }

    private class FileExtensionFilter implements FileFilter {

        List<String> extensions;

        public FileExtensionFilter(List<String> extensionsToAccept) {
            this.extensions = extensionsToAccept;
        }

        @Override
        public boolean accept(File file) {
            for (String extension : extensions) {
                if (file.isDirectory())
                    return true;
                if (file.getName().endsWith(extension))
                    return true;
            }
            return false;
        }
    }

    private class FileExtensionFilterInversed implements FileFilter {

        List<String> extensions;

        public FileExtensionFilterInversed(List<String> extensionsToDiscart) {
            this.extensions = extensionsToDiscart;
        }

        @Override
        public boolean accept(File file) {
            for (String extension : extensions) {
                if (file.isDirectory())
                    return false;
                if (file.getName().endsWith(extension))
                    return false;
            }
            return true;
        }
    }
}
