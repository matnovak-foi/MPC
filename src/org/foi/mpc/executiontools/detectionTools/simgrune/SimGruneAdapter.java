package org.foi.mpc.executiontools.detectionTools.simgrune;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import clover.org.apache.velocity.app.event.implement.EscapeXmlReference;
import org.foi.common.filesystem.directory.DirectoryFileUtility;

import static org.foi.common.filesystem.directory.DirectoryFileUtility.checkIfDirExists;
import static org.foi.common.filesystem.directory.DirectoryFileUtility.isEmptyDirectory;

import org.foi.mpc.executiontools.detectionTools.SimilarityDetectionToolSettings;
import org.foi.mpc.executiontools.factories.SimilarityDetectionTool;

public abstract class SimGruneAdapter implements SimilarityDetectionTool {

    public class runProcessException extends RuntimeException {
        public runProcessException(String message) {
            super(message);
        }
    }

    private Process process;
    private SimGruneResultParser sgrp;
    private File simExePath;
    private SimGruneAdapterSettings settings;
    DirectoryFileUtility dfu;

    public SimGruneAdapter(File simExePath) {
        this.simExePath = simExePath;
        settings = new SimGruneAdapterSettings();
        settings.outputFileName = "result.txt";
        settings.resultDirName = "result";
        settings.minRunLength = 2;
        settings.precentRunDirName = "precentRun";
        settings.precentOutputFileName = "resultPrecent.txt";

        dfu = new DirectoryFileUtility();
    }

    protected static String getSimExePath(String simExeName){
        File simExeDir = null;
        try {
            String dirPath = SimGruneAdapter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            if(dirPath==null) {
                String path = SimGruneAdapter.class.getResource(SimGruneAdapter.class.getSimpleName() + ".class").getPath();
                dirPath = path.substring(0, path.indexOf(SimGruneAdapter.class.getName().replaceAll("[.]", "/")));
                dirPath = URLDecoder.decode(dirPath, "UTF-8");
            }
            simExeDir = new File(dirPath);
            if(simExeDir.getName().contains(".jar")){
                simExeDir = simExeDir.getParentFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String exePath  = simExeDir +File.separator+"tools"+File.separator+simExeName;
        String OS = System.getProperty("os.name").toLowerCase();
        if(OS.indexOf("win") >= 0) {
            exePath += ".exe";
        }

        return exePath;
    }

    @Override
    public void runPlagiarsimDetection(File sourceDir) {
        checkIfDirExists(sourceDir);
        if (isEmptyDirectory(sourceDir)) {
            throw new DirectoryFileUtility.DirIsEmptyException();
        }

        dfu.createSubDir(sourceDir, getResultDir(sourceDir).getName());
        File resultFile = new File(getResultDir(sourceDir).getAbsolutePath() +File.separator + settings.outputFileName);

        runSim(" -R -a -e -n -s"
                + " -r " + settings.minRunLength
                + " -o " + resultFile.getAbsolutePath()
                + " " + sourceDir.getPath());

        File matchesDir = new DirectoryFileUtility().createSubDir(sourceDir, settings.matchesDirName);
        sgrp = new SimGruneResultParser(matchesDir);
        sgrp.processResultFile(resultFile);

        runPrecentageDetection(sourceDir);
    }

    private void runPrecentageDetection(File sourceDir){
        File precentRunDir = new File(sourceDir+File.separator+settings.precentRunDirName);
        preparePrecantageRunDir(sourceDir, precentRunDir);

        File resultFile = new File(getResultDir(sourceDir).getAbsolutePath() +File.separator + settings.precentOutputFileName);
        runSim(" -R -a -e -p -s"
                + " -r " + settings.minRunLength
                + " -o " + resultFile.getAbsolutePath()
                + " " + precentRunDir.getPath());

        sgrp.processResultFile(resultFile);
    }

    private void preparePrecantageRunDir(File sourceDir, File precentRunDir) {
        List<String> excludeDirs = new ArrayList<>();
        excludeDirs.add(settings.resultDirName);
        excludeDirs.add(settings.matchesDirName);
        excludeDirs.add(settings.precentRunDirName);


        dfu.createSubDir(sourceDir, settings.precentRunDirName);
        try {
            dfu.copyDirectoryTree(sourceDir,precentRunDir,excludeDirs);
        } catch (IOException e) {
            throw new DirectoryFileUtility.FileCopyException(sourceDir+" to "+precentRunDir+" Error: "+e.getMessage());
        }
    }

    private File getResultDir(File sourceDir) {
        File resultDir = new File(sourceDir.getPath() + File.separator + settings.resultDirName);
        return resultDir;
    }

    public void runSim(String params) {
        try {
            Runtime runTime = Runtime.getRuntime();
            process = runTime.exec(simExePath.getAbsolutePath() + params);
            process.waitFor();
        } catch (Exception ex) {
            throw new runProcessException(ex.getMessage());
        }
    }

    @Override
    public void setMatchesDirName(String matchesDirName) {
        settings.matchesDirName = matchesDirName;
    }

    protected Process getProcess() {
        return process;
    }

    @Override
    public SimGruneAdapterSettings getSettings() {
        return settings;
    }

    @Override
    public void setSettings(SimilarityDetectionToolSettings settings) {
        this.settings = (SimGruneAdapterSettings) settings;
    }
}
