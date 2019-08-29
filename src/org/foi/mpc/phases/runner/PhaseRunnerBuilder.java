package org.foi.mpc.phases.runner;

import org.foi.mpc.MPCContext;
import org.foi.mpc.phases.PhaseFactory;
import org.foi.mpc.phases.executionphases.ExecutionPhase;
import org.foi.mpc.phases.executionphases.ExecutionTool;
import org.foi.mpc.phases.readerphase.MPCMatchListener;
import org.foi.mpc.phases.readerphase.MPCMatchReaderPhase;

import java.io.File;
import java.util.List;

public class PhaseRunnerBuilder {

    private PhaseRunner phaseRunner;
    private PhaseFactory phaseFactory;
    private PhaseRunnerSettings settings;

    private List<ExecutionTool> toolList;
    private List<File> dirsToProcess;
    private List<ExecutionTool> techniqueList;
    private File workingDir;
    private File sourceDir;
    private MPCMatchListener matchReadListener;

    public PhaseRunnerBuilder(PhaseFactory phaseFactory) {
        this.phaseFactory = phaseFactory;
        settings = new PhaseRunnerSettings();
        disableAllPhases();
    }

    public PhaseRunnerBuilder(String matchesDir) {
        this.phaseFactory = new PhaseFactory(matchesDir);
        settings = new PhaseRunnerSettings();
        disableAllPhases();
    }

    public PhaseRunnerBuilder withSourceDir(File sourceDir) {
        this.sourceDir = sourceDir;
        return this;
    }

    public PhaseRunnerBuilder withWorkingDir(File workingDir) {
        this.workingDir = workingDir;
        return this;
    }

    public PhaseRunnerBuilder forToolList(List<ExecutionTool> toolList) {
        this.toolList = toolList;
        return this;
    }

    public PhaseRunnerBuilder forTechiqueList(List<ExecutionTool> techniqueList) {
        this.techniqueList = techniqueList;
        return this;
    }

    public PhaseRunnerBuilder toProcessDirs(List<File> dirsToProcess) {
        this.dirsToProcess = dirsToProcess;
        return this;
    }

    public List<File> getDirsToProcess(){
        return dirsToProcess;
    }

    public PhaseRunnerBuilder withAllPhasesEnabled() {
        enableAllPhases();
        return this;
    }

    public PhaseRunnerBuilder withMatchReadListener(MPCMatchListener matchReadListener) {
        this.matchReadListener = matchReadListener;
        return this;
    }

    private void enableAllPhases() {
        settings.runPreparePhase = true;
        settings.runDetectionPhase = true;
        settings.runPreprocessPhase = true;
        settings.runMPCMatchReadPhase = true;
    }

    private void disableAllPhases() {
        settings.runPreparePhase = false;
        settings.runDetectionPhase = false;
        settings.runPreprocessPhase = false;
        settings.runMPCMatchReadPhase = false;
    }

    public PhaseRunnerBuilder withMatchReadPhaseDisabled() {
        enableAllPhases();
        settings.runMPCMatchReadPhase = false;
        return this;
    }

    public PhaseRunnerBuilder withMatchReadAndDetectionPhaseDisabled() {
        enableAllPhases();
        settings.runMPCMatchReadPhase = false;
        settings.runDetectionPhase = false;
        return this;
    }

    public PhaseRunnerBuilder withPreparePhaseAndPreprocessPhaseDisabled() {
        enableAllPhases();
        settings.runPreparePhase = false;
        settings.runPreprocessPhase = false;
        return this;
    }

    public PhaseRunner build() {
        phaseRunner = new PhaseRunner(settings);

        ExecutionPhase preparePhase = createPreparePhase();
        ExecutionPhase preprocessPhase = createPreprocessPhase(preparePhase);
        createDetectionPhase(preprocessPhase);
        createMatchReadPhase();

        return phaseRunner;
    }

    private ExecutionPhase createPreparePhase() {
        ExecutionPhase preparePhase = phaseFactory.createDefaultSetupAssignmentPreparer(workingDir, sourceDir);
        preparePhase.setDirsToProcess(dirsToProcess);
        phaseRunner.setPreparePhase(preparePhase);
        return preparePhase;
    }

    private ExecutionPhase createPreprocessPhase(ExecutionPhase preparePhase) {
        ExecutionPhase preprocessPhase;
        if(!settings.runPreparePhase) {
            settings.prepareOutputDirs = dirsToProcess;
            preprocessPhase = phaseFactory.createAssignmentsPreprocessor(workingDir, sourceDir);
        }else {
            preprocessPhase = phaseFactory.createAssignmentsPreprocessor(workingDir, preparePhase.getPhaseWorkingDir());
        }
        preprocessPhase.setExecutionTools(techniqueList);
        phaseRunner.setPreprocessPhase(preprocessPhase);

        return preprocessPhase;
    }

    private void createDetectionPhase(ExecutionPhase preprocessPhase) {
        ExecutionPhase detectionPhase;
        if(!settings.runPreprocessPhase){
            settings.preprocessOutputDirs = dirsToProcess;
            detectionPhase = phaseFactory.createSimilarityDetector(workingDir, sourceDir);
        }else {
            detectionPhase = phaseFactory.createSimilarityDetector(workingDir, preprocessPhase.getPhaseWorkingDir());
        }
        detectionPhase.setExecutionTools(toolList);
        phaseRunner.setDetectionPhase(detectionPhase);
    }

    private void createMatchReadPhase() {
        MPCMatchReaderPhase readPhase = (MPCMatchReaderPhase) phaseFactory.createMPCMatchReadPhase();
        readPhase.setListener(matchReadListener);
        phaseRunner.setMPCMatchReadPhase(readPhase);
    }
}
