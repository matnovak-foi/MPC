package org.foi.mpc.executiontools.detectionTools.spector;

import java.io.File;
import java.util.ArrayList;

import org.foi.common.filesystem.directory.DirectoryFileUtility;

import static org.foi.common.filesystem.directory.DirectoryFileUtility.checkIfDirExists;
import static org.foi.common.filesystem.directory.DirectoryFileUtility.isEmptyDirectory;

import org.foi.mpc.executiontools.detectionTools.SimilarityDetectionToolSettings;
import org.foi.mpc.executiontools.factories.SimilarityDetectionTool;
import org.foi.mpc.executiontools.detectionTools.spector.SpectorAdapterSettings.SpectorInputOption;
import org.foi.mpc.executiontools.detectionTools.spector.SpectorAdapterSettings.SpectorOrderOption;
import org.foi.mpc.executiontools.detectionTools.spector.SpectorAdapterSettings.SpectorOutputOption;
import spector.Presenter;
import spector.Spector;

public class SpectorAdapter implements SimilarityDetectionTool {

    public static final String NAME = "Spector";
    private SpectorAdapterSettings settings;

    public SpectorAdapter() {
        settings = new SpectorAdapterSettings();
        settings.inputOption = SpectorInputOption.FILE;
        settings.orderOption = SpectorOrderOption.DESCENDING;
        settings.outputOptions.add(SpectorOutputOption.SUMMARY);
        settings.resultDirName = "result";
    }
    
    @Override
    public void runTool(File dirToProcess) {
        runPlagiarsimDetection(dirToProcess);
    }

    @Override
    public void runPlagiarsimDetection(File sourceDir) {
        checkIfDirExists(sourceDir);
        if (isEmptyDirectory(sourceDir)) {
            throw new DirectoryFileUtility.DirIsEmptyException();
        }

        String[] args = setUpSpectorSettings(sourceDir);
        Spector.main(args);
    }

    @Override
    public void setMatchesDirName(String matchesDirName) {
        settings.matchesDirName = matchesDirName;
    }

    @Override
    public SpectorAdapterSettings getSettings() {
        return settings;
    }

    @Override
    public void setSettings(SimilarityDetectionToolSettings settings) {
        this.settings = (SpectorAdapterSettings) settings;
    }

    @Override
    public String getName() {
        return NAME;
    }

    protected String[] setUpSpectorSettings(File sourceDir) {
        Spector.out = new PresenterWrapper(sourceDir);
        Spector.output = new ArrayList<>();
        String args
                = "--input " + settings.inputOption.getValue()
                + " --recurse"
                + " --bidirectional"
                + " --order " + settings.orderOption.getValue()
                + " --output " + getSpectorOutputOptions()
                + " --place " + getResultDir(sourceDir).getPath()
                + " --inspect " + sourceDir.getPath();
        return args.split(" ");
    }

    private File getResultDir(File sourceDir) {
        File resultDir = new File(sourceDir.getPath() + File.separator + settings.resultDirName);
        new DirectoryFileUtility().createSubDir(sourceDir, resultDir.getName());
        return resultDir;
    }

    private String getSpectorOutputOptions() {
        String outputOptions = "";
        for (SpectorOutputOption option : settings.outputOptions) {
            outputOptions += option.getValue();
        }
        return outputOptions;
    }

    private class PresenterWrapper extends Presenter {

        private File sourceDir;

        public PresenterWrapper(File sourceDir) {
            super();
            this.sourceDir = sourceDir;
        }

        @Override
        public void printSummary() {

        }

        @Override
        public void printMatches() {

        }

        @Override
        public void printFullSummary() {
            File matchesDir = new DirectoryFileUtility().createSubDir(sourceDir, settings.matchesDirName);
            SpectorMatchesWriter smw = new SpectorMatchesWriter(matchesDir);
            smw.writeAllMatchesToDirectory(comparisons);
        }

        
    }
}
