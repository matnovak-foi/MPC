package org.foi.mpc.phases;

import org.foi.mpc.MPCContext;
import org.foi.mpc.executiontools.prepareTools.ArchiveExtractor;
import org.foi.mpc.executiontools.prepareTools.Renamer;
import org.foi.mpc.executiontools.prepareTools.SubmissionFilesUnifier;
import org.foi.mpc.phases.executionphases.ExecutionPhase;
import org.foi.mpc.phases.executionphases.ExecutionTool;
import org.foi.mpc.phases.readerphase.MPCMatchReaderPhase;
import org.foi.mpc.phases.runner.Phase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhaseFactory {
    private String matchesDir;

    public PhaseFactory(String matchesDir) {
        this.matchesDir = matchesDir;
    }

    public ExecutionPhase createAssignmentPreparer(File workingDir, File datasetInputDirectory){
        ExecutionPhase preparer = new ExecutionPhase(workingDir, datasetInputDirectory,"prepared");
        preparer.setSequential(true);
        return preparer;
    }

    public ExecutionPhase createAssignmentsPreprocessor(File workingDir, File preprocessDirectory) {
        ExecutionPhase preprocessor = new ExecutionPhase(workingDir, preprocessDirectory,"preprocess");
        preprocessor.setSequential(false);
        return preprocessor;
    }

    public ExecutionPhase createSimilarityDetector(File workingDir, File preprocessDirectory) {
        ExecutionPhase detector = new ExecutionPhase(workingDir, preprocessDirectory,"detection");
        detector.setSequential(false);
        return detector;
    }

    public ExecutionPhase createDefaultSetupAssignmentPreparer(File workingDir, File testInputDir) {
        ExecutionPhase preparer = createAssignmentPreparer(workingDir, testInputDir);
        List<ExecutionTool> prepareToolsList = new ArrayList<>();
        prepareToolsList.add(new ArchiveExtractor());
        prepareToolsList.add(new Renamer());
        SubmissionFilesUnifier sfu = new SubmissionFilesUnifier();
        prepareToolsList.add(sfu);
        preparer.setExecutionTools(prepareToolsList);
        return  preparer;
    }

    public Phase createMPCMatchReadPhase() {
        MPCMatchReaderPhase readerPhase = new MPCMatchReaderPhase();
        readerPhase.setMatches_dir(matchesDir);
        return readerPhase;
    }
}
