package org.foi.mpc.executiontools.calibrator;

import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagAdapter;
import org.foi.mpc.executiontools.detectionTools.sherlock.SherlockAdapter;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneAdapter;
import org.foi.mpc.executiontools.factories.SimilarityDetectionTool;
import org.foi.mpc.phases.runner.PhaseRunnerBuilder;

public class SimilarityDetectionToolCalibratorFactory {
    public SimilarityDetectionToolCalibrator getCalibrator(SimilarityDetectionTool tool, PhaseRunnerBuilder phaseRunnerBuilder)  {
        if(tool instanceof SimGruneAdapter)
            return new SIMGruneCalibrator(phaseRunnerBuilder);
        else if(tool instanceof SherlockAdapter)
            return new SherlockCalibrator(phaseRunnerBuilder);
        else if(tool instanceof JPlagAdapter)
            return new JPlagCalibrator(phaseRunnerBuilder);
        else
            return null;
    }
}
