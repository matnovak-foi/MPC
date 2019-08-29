package org.foi.mpc.executiontools.detectionTools.JPlag;

import java.io.File;
import jplag.AllMatches;
import jplag.ExitException;
import jplag.Program;
import jplag.SortedVector;
import jplag.clustering.Cluster;
import jplag.options.CommandLineOptions;
import jplag.options.Options;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import static org.foi.common.filesystem.directory.DirectoryFileUtility.checkIfDirExists;
import static org.foi.common.filesystem.directory.DirectoryFileUtility.isEmptyDirectory;

import org.foi.mpc.executiontools.detectionTools.SimilarityDetectionToolSettings;
import org.foi.mpc.executiontools.factories.SimilarityDetectionTool;

public abstract class JPlagAdapter implements SimilarityDetectionTool {

    public static class JPlagException extends RuntimeException {

        public JPlagException(String message) {
            super(message);
        }

    }
    
    public enum JPlagLanguage {
        Java("java17"),
        Text("text");

        private String value;

        private JPlagLanguage(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private CommandLineOptions JPlagOptions;
    private JPlagAdapterSettings settings;
    private Program program;


    protected JPlagAdapter(JPlagLanguage language) {
        settings = new JPlagAdapterSettings();
        setUpDefaultJPlagSettings(language);
    }

    private void setUpDefaultJPlagSettings(JPlagLanguage language) {
        settings.includeFileExtensions = new String[]{".java"};
        settings.languageName = language.getValue();
        settings.outputLogName = "JPlag.log";
        settings.resultDirName = "result";
        settings.minTokenMatch = 1;
        settings.minStoreMatchAbove = 0;
    }

    @Override
    public void runPlagiarsimDetection(File sourceDir) {
        checkIfDirExists(sourceDir);
        if (isEmptyDirectory(sourceDir)) {
            throw new DirectoryFileUtility.DirIsEmptyException();
        }

        setUpJPlagSettings(sourceDir);
        createAndRunJplagProgram();
    }

    protected void createAndRunJplagProgram() {
        try {
            program = new ProgramWraper(JPlagOptions, settings.matchesDirName);
            program.run();
        } catch (ExitException ex) {
            throw new JPlagException(ex.getMessage());
        }
    }

    private void setUpJPlagSettings(File sourceDir) throws JPlagException {
        String outputLog = sourceDir.getAbsolutePath() + File.separator + settings.outputLogName;
        String resultDir = sourceDir.getAbsolutePath() + File.separator + settings.resultDirName;
        String includeFileExtensions = String.join(",", settings.includeFileExtensions);
        String args = sourceDir
                + " -external"
                + " -l " + settings.languageName
                + " -t " + settings.minTokenMatch
                + " -o " + outputLog
                + " -r " + resultDir
                + " -s"
                + " -m " + settings.minStoreMatchAbove + "%"
                + " -p " + includeFileExtensions
                + " -vq ";
        try {
            JPlagOptions = new CommandLineOptions(args.split(" "));
        } catch (ExitException ex) {
            throw new JPlagException(ex.getMessage());
        }
    }

    protected CommandLineOptions getJPlagOptions() {
        return JPlagOptions;
    }

    protected Program getJPlagProgram() {
        return program;
    }

    @Override
    public void setMatchesDirName(String matchesDirName) {
        settings.matchesDirName = matchesDirName;
    }

    @Override
    public JPlagAdapterSettings getSettings() {
        return settings;
    }

    @Override
    public void setSettings(SimilarityDetectionToolSettings settings) {
        this.settings = (JPlagAdapterSettings) settings;
    }

    private class ProgramWraper extends Program {
        private String matchesDirName;

        public ProgramWraper(Options options, String matchesDirName) throws ExitException {
            super(options);
            this.matchesDirName = matchesDirName;
        }

        @Override
        protected void writeResults(int[] dist, SortedVector<AllMatches> avgmatches, SortedVector<AllMatches> maxmatches, SortedVector<AllMatches> minmatches, Cluster clustering) throws ExitException {
            new DirectoryFileUtility().createSubDir(new File(get_root_dir()),"result");
            File matchesDir = new DirectoryFileUtility().createSubDir(new File(get_root_dir()),matchesDirName);

            if (avgmatches != null) {
                JplagMatchesWriter jmc = new JplagMatchesWriter(matchesDir, get_root_dir(), use_externalSearch());
                jmc.writeAllMatchesToDirectory(avgmatches);
            }
        }
    }


}