package org.foi.mpc.phases.runner;

import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.phases.executionphases.ExecutionTool;
import org.foi.mpc.phases.readerphase.MPCMatchListener;
import org.foi.mpc.phases.readerphase.MPCMatchReaderPhase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhaseRunnerBuilderStub extends PhaseRunnerBuilder {
    public List<ExecutionTool> toolList;
    public List<ExecutionTool> techniqueList;
    public List<File> fakeDetectionOutputDirs = new ArrayList<>();
    public File sourceDir;
    public File workingDir;
    public boolean phaseRunnerWasRun;
    public int wasRunTimes = 0;
    public PhaseRunnerSettings runnerSettings;
    private MPCMatchListener listener;
    public PhaseBuilderSpyListener spyListener;
    public boolean returnSomeMatch = true;
    public PhaseRunner phaseRunner;

    public PhaseRunnerBuilderStub(String matchesDir) {
        super(matchesDir);
    }

    @Override
    public PhaseRunnerBuilder forToolList(List<ExecutionTool> toolList) {
        this.toolList = toolList;
        return super.forToolList(toolList);
    }

    @Override
    public PhaseRunnerBuilder forTechiqueList(List<ExecutionTool> techniqueList) {
        this.techniqueList = techniqueList;
        return super.forTechiqueList(techniqueList);
    }

    @Override
    public PhaseRunnerBuilder withSourceDir(File sourceDir) {
        this.sourceDir = sourceDir;
        return super.withSourceDir(sourceDir);
    }

    @Override
    public PhaseRunnerBuilder withWorkingDir(File workingDir) {
        this.workingDir = workingDir;
        return super.withWorkingDir(workingDir);
    }

    @Override
    public PhaseRunnerBuilder withMatchReadListener(MPCMatchListener matchReadListener) {
        this.listener = matchReadListener;
        return super.withMatchReadListener(matchReadListener);
    }

    @Override
    public PhaseRunner build() {
        phaseRunner = super.build();
        PhaseRunner phaseRunnerSpy = new PhaseRunner(phaseRunner.getSettings()) {
            @Override
            public void runPhases() {
                phaseRunnerWasRun = true;
                wasRunTimes++;
                runnerSettings = getSettings();
                runnerSettings.detectionOutputDirs = fakeDetectionOutputDirs;
                if(returnSomeMatch) listener.processMatch(spyListener.getMatchToReturn());
            }

        };
        return phaseRunnerSpy;
    }

    public interface PhaseBuilderSpyListener {
        public MPCMatch getMatchToReturn();
    }
}
