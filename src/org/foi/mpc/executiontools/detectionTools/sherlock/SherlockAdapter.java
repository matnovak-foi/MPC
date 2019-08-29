package org.foi.mpc.executiontools.detectionTools.sherlock;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.foi.common.filesystem.directory.DirectoryFileUtility.*;

import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.mpc.phases.executionphases.ExecutionTool;
import uk.ac.warwick.dcs.cobalt.sherlock.FileTypeProfile;
import uk.ac.warwick.dcs.cobalt.sherlock.Samelines;
import uk.ac.warwick.dcs.cobalt.sherlock.Settings;
import uk.ac.warwick.dcs.cobalt.sherlock.SherlockProcess;
import uk.ac.warwick.dcs.cobalt.sherlock.SherlockProcessCallback;
import uk.ac.warwick.dcs.cobalt.sherlock.SherlockProcessException;
import uk.ac.warwick.dcs.cobalt.sherlock.TokeniseFiles;

//TODO  https://stackoverflow.com/questions/243274/how-to-unit-test-abstract-classes-extend-with-stubs/2947823#2947823
//Čini mi se da preprocess i dertection su dvije uloge što znači to nisu derivetives već nekakve instce
//I time tehnike nisu derivetives već one su derivetives od preprocessingTehniques i koriste sherlockAdapter
//a detection možda jesu derivetives
//You are specializing your abstract object, but all clients will use the derived class through its base interface.
//You are using an abstract base class to factor out duplication within objects in your design, and clients use the concrete implementations through their own interfaces.!

public abstract class SherlockAdapter implements SherlockProcessCallback, ExecutionTool {

    public enum SherlockFileType {
        TOKENISED(Settings.TOK),
        NOCOMMENTS(Settings.NOC),
        NOWHITESPACES(Settings.NOW),
        NORMALISED(Settings.NOR),
        NOCOMMENTSNOWHITESPACES(Settings.NCW),
        NOCOMMENTSNORMALISED(Settings.NCN),
        ORIGINAL(Settings.ORI),
        COMMENTS(Settings.COM),
        SENTANCEBASED(Settings.SEN);

        private int value;

        private SherlockFileType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    protected SherlockAdapterSettings adapterSettings;
    private SherlockProcess process;
    protected SherlockFileType activeFileType;

    protected SherlockAdapter(SherlockFileType activeFileType) {
        Settings.init();

        adapterSettings = new SherlockAdapterSettings();
        adapterSettings.logFileName = "logFile.log";
        adapterSettings = setUpDefualtProfileSettings(adapterSettings);

        this.activeFileType = activeFileType;
    }

    public void runPlagiarsimDetection(File sourceDir) {
        runPreporcess(sourceDir);
        prepareForDetection();
        runProcess();
        createMatches();
    }

    public void runPreporcess(File sourceDir) {
        checkIfSourceDirIsOk(sourceDir);
        setUpSherlockSettings(sourceDir);
        prepareForPreprocess(sourceDir);
        runProcess();
    }

    public SherlockFileType getActiveFileType() {
        return activeFileType;
    }

    protected void runProcess() {
        process.start();
        try {
            process.join();
        } catch (InterruptedException ex) {
            System.err.println("Error join thread preproces files");
        }
    }

    private static void disableAllFileTypesByDefault() {
        disableFileType(SherlockFileType.ORIGINAL);
        disableFileType(SherlockFileType.TOKENISED);
        disableFileType(SherlockFileType.SENTANCEBASED);
        disableFileType(SherlockFileType.NOCOMMENTSNORMALISED);
        disableFileType(SherlockFileType.NOCOMMENTS);
        disableFileType(SherlockFileType.NOCOMMENTSNOWHITESPACES);
        disableFileType(SherlockFileType.NORMALISED);
        disableFileType(SherlockFileType.COMMENTS);
        disableFileType(SherlockFileType.NOWHITESPACES);
    }

    private static void disableFileType(SherlockFileType fileType) {
        Settings.getFileTypes()[fileType.getValue()].setInUse(false);
    }

    private static void enableFileType(SherlockFileType fileType) {
        Settings.getFileTypes()[fileType.getValue()].setInUse(true);
    }

    private static SherlockAdapterSettings setUpDefualtProfileSettings(SherlockAdapterSettings adapterSettings) {
        adapterSettings.concatanate = false;
        adapterSettings.amalgamate = true;
        adapterSettings.maxBackwardJump = 1;
        adapterSettings.maxForwardJump = 3;
        adapterSettings.minRunLenght = 3;
        adapterSettings.minStringLength = 8;
        adapterSettings.maxJumpDiff = 3;
        adapterSettings.strictness = 2;
        return adapterSettings;
    }

    protected void prepareForDetection() {
        process = new Samelines(this);
    }

    protected void setUpSherlockSettings(File sourceDir) {
        disableAllFileTypesByDefault();
        enableFileType(activeFileType);
        Settings.setSourceDirectory(sourceDir);
        if(adapterSettings.matchesDirName != null)
            Settings.getSherlockSettings().setExcludeFile(adapterSettings.matchesDirName);
        Settings.setLogFile(new File(sourceDir.getPath() + File.separator + adapterSettings.logFileName));
        Settings.setRunningGUI(false);
        
        FileTypeProfile profile = getProfileSettings(activeFileType);
        profile.setConcatanate(adapterSettings.concatanate);
        profile.setAmalgamate(adapterSettings.amalgamate);
        profile.setMaxBackwardJump(adapterSettings.maxBackwardJump);
        profile.setMaxForwardJump(adapterSettings.maxForwardJump);
        profile.setMaxJumpDiff(adapterSettings.maxJumpDiff);
        profile.setMinRunLength(adapterSettings.minRunLenght);
        profile.setMinStringLength(adapterSettings.minStringLength);
        profile.setStrictness(adapterSettings.strictness);
    }

    protected FileTypeProfile getProfileSettings(SherlockFileType fileType) {
        return Settings.getFileTypes()[fileType.getValue()];
    }

    protected void prepareForPreprocess(File sourceDir) {
        loadFiles(sourceDir);
        process = new TokeniseFiles(this);
        process.setNatural(false);
        process.setPriority(Thread.NORM_PRIORITY - 1);
    }

    private void checkIfSourceDirIsOk(File sourceDir) throws DirIsEmptyException {
        checkIfDirExists(sourceDir);
        if (isEmptyDirectory(sourceDir)) {
            throw new DirIsEmptyException();
        }
    }

    private void loadFiles(File sourceDir) {
        List<File> filesList = readAllFiles(sourceDir);
        File[] files = new File[filesList.size()];
        files = filesList.toArray(files);
        Arrays.sort(files);//To be the same on linux and windows otherwise sherlock will have different numbers depending on the order
        Settings.setFileList(files);
    }
    
    List<File> readAllFiles(File sourceDir){
        List<File> files = new ArrayList<>();
        for(File file : sourceDir.listFiles()){
            if(file.isDirectory())
                files.addAll(readAllFiles(file));
            else
                files.add(file);
        }
        return files;
    }

    private void createMatches() {
        new DirectoryFileUtility().createSubDir(new File(Settings.getSourceDirectory().getPath()), "result");
        File matchesDir = new DirectoryFileUtility().createSubDir(new File(Settings.getSourceDirectory().getPath()), adapterSettings.matchesDirName);
        File matchDir = new File(Settings.getSourceDirectory().getPath()+File.separator+Settings.getSherlockSettings().getMatchDirectory());
        if(matchDir.exists()){
            SherlockMatchesWriter smw = new SherlockMatchesWriter(matchesDir, matchDir);
            try {
                Files.walkFileTree(matchDir.toPath(), smw);
            } catch (IOException ex) {
                Logger.getLogger(SherlockAdapter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public SherlockProcess getProcess() {
        return process;
    }

    public void setMatchesDirName(String matchesDirName) {
        adapterSettings.matchesDirName = matchesDirName;
    }

    @Override
    public void exceptionThrown(SherlockProcessException spe) {
        System.err.println("Exception thrown by Sherlock");
        System.err.println(spe);
    }


}
