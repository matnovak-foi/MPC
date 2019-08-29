package org.foi.mpc.phases.runner;

import org.foi.mpc.phases.executionphases.ExecutionPhase;

public class PhaseRunner {
    private Phase preparePhase;
    private Phase preprocessPhase;
    private Phase detectionPhase;
    private Phase mpcMatchReadPhase;
    private PhaseRunnerSettings settings;

    public PhaseRunner(PhaseRunnerSettings settings){
        this.settings = settings;
    }

    public void runPhases() {
        if (settings.runPreparePhase) {
            runPhase(preparePhase);
            settings.prepareOutputDirs = preparePhase.getOutputDirs();
        }

        if (settings.runPreprocessPhase) {
            if (preprocessPhase != null) preprocessPhase.setDirsToProcess(settings.prepareOutputDirs);
            runPhase(preprocessPhase);
            settings.preprocessOutputDirs = preprocessPhase.getOutputDirs();
        }

        if (settings.runDetectionPhase) {
            if (detectionPhase != null) detectionPhase.setDirsToProcess(settings.preprocessOutputDirs);
            runPhase(detectionPhase);
            settings.detectionOutputDirs = detectionPhase.getOutputDirs();
        }

        if (settings.runMPCMatchReadPhase) {
            if (mpcMatchReadPhase != null) mpcMatchReadPhase.setDirsToProcess(settings.detectionOutputDirs);
            runPhase(mpcMatchReadPhase);
        }
    }

    private void runPhase(Phase phase) throws ExecutionPhase.PhaseNotInstanciatedException {
        if(phase == null) {
            throw new ExecutionPhase.PhaseNotInstanciatedException();
        } else {
            phase.runPhase();
        }
    }

    public void setPreparePhase(Phase preparer) {
        this.preparePhase = preparer;
    }

    public void setPreprocessPhase(Phase preprocesser) {
        this.preprocessPhase = preprocesser;
    }

    public void setDetectionPhase(Phase detector) {
        this.detectionPhase = detector;
    }

    public void setMPCMatchReadPhase(Phase mpcMatchReadPhase) {
        this.mpcMatchReadPhase = mpcMatchReadPhase;
    }

    public Phase getPreparePhase() {
        return preparePhase;
    }

    public PhaseRunnerSettings getSettings() {
        return settings;
    }

    public Phase getDetectionPhase() {
        return detectionPhase;
    }

    public Phase getPreprocessPhase() {
        return preprocessPhase;
    }
}
