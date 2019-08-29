package org.foi.mpc.executiontools.techniques.sherlock;

import java.io.File;

import org.foi.mpc.executiontools.techniques.PreprocessDirectoryCleaner;
import org.foi.mpc.executiontools.detectionTools.sherlock.SherlockAdapter;
import org.foi.mpc.executiontools.factories.PreprocessingTechnique;

public class SherlockPreprocessingTechnique extends SherlockAdapter implements PreprocessingTechnique{

    static public class DirectoryNameDuplication extends RuntimeException {
        public DirectoryNameDuplication(String message) {
            super(message);
        }
    }

    private String sherlockTechniqueName;
    private String sherlockPreprocessDirName;
    private String sherlockPreprocessingExtension;
    private PreprocessDirectoryCleaner cleaner;

    protected SherlockPreprocessingTechnique(SherlockFileType fileType, String techniqueName) {
        super(fileType);
        this.sherlockTechniqueName = techniqueName;
        sherlockPreprocessDirName =  getProfileSettings(activeFileType).getDirectory();
        sherlockPreprocessingExtension = getProfileSettings(activeFileType).getExtension();
        cleaner = new PreprocessDirectoryCleaner(sherlockPreprocessDirName);
    }

    public void setCleaner(PreprocessDirectoryCleaner cleaner) {
        this.cleaner = cleaner;
    }

    @Override
    public String getName() {
        return sherlockTechniqueName;
    }

    public String getSherlockPreprocessDirName() {
        return sherlockPreprocessDirName;
    }

    @Override
    public void runTool(File dirToProcess) {
        for(File dir : dirToProcess.listFiles()){
            if(dir.getName().equalsIgnoreCase(sherlockPreprocessDirName)){
                throw new DirectoryNameDuplication("Student dir name cannot be "+sherlockPreprocessDirName);
            }
        }

        runPreporcess(dirToProcess);
        cleanPreprocessedDir(dirToProcess);
    }

    protected void cleanPreprocessedDir(File dirToProcess){
        cleaner.clean(dirToProcess);
        removePreprocessExtensionOnFiles(dirToProcess);
    }

    private void removePreprocessExtensionOnFiles(File dirToProcess) {
        for(File file : dirToProcess.listFiles()){
            String name = file.getName();
            System.out.println(name);
            String newName = name.substring(0,name.lastIndexOf("."+ sherlockPreprocessingExtension));
            file.renameTo(new File(file.getParentFile().getPath()+File.separator+newName));
        }
    }

}