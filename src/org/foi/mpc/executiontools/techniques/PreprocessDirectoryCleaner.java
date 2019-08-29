package org.foi.mpc.executiontools.techniques;

import org.foi.common.filesystem.directory.DirectoryFileUtility;

import java.io.File;
import java.io.IOException;

public class PreprocessDirectoryCleaner {
    private String subDirName;
    private File preprocessedDir;

    public class PreprocessDirectoryCleanException extends RuntimeException {
        public PreprocessDirectoryCleanException(String message) {
            super(message);
        }
    }

    public PreprocessDirectoryCleaner(String subDirName) {
        this.subDirName = subDirName;
    }

    public void clean(File preprocessedDir){
        this.preprocessedDir = preprocessedDir;

        try {
            movePreprocessFilesToPreprocessedDir();
        } catch (IOException e) {
            throw new PreprocessDirectoryCleanException(e.getMessage());
        }
    }

    private void movePreprocessFilesToPreprocessedDir() throws IOException {
        File tempFolder = copyFilesToTempDir();
        deleteAllFilesInPreprocessDir();
        copyFilesToEmptyPreprocessedDir(tempFolder);
        deleteTempDir(tempFolder);
    }

    private File copyFilesToTempDir() throws IOException {
        File sherlockPreprocessDir = new File(preprocessedDir.getPath()+File.separator+ subDirName);
        File tempFolder = new File(preprocessedDir.getParentFile()+File.separator+"tempCopyFolder");
        tempFolder.mkdirs();
        new DirectoryFileUtility().copyDirectoryTree(sherlockPreprocessDir,tempFolder);
        return tempFolder;
    }

    private void deleteAllFilesInPreprocessDir() throws IOException {
        DirectoryFileUtility.deleteDirectoryTree(preprocessedDir);
        preprocessedDir.mkdir();
    }

    private void copyFilesToEmptyPreprocessedDir(File tempFolder) throws IOException {
        new DirectoryFileUtility().copyDirectoryTree(tempFolder,preprocessedDir);
    }

    private void deleteTempDir(File tempFolder) throws IOException {
        DirectoryFileUtility.deleteDirectoryTree(tempFolder);
        if(tempFolder.getParent().equals("null")){
            tempFolder.getParentFile().delete();
        }
    }


}
