package org.foi.mpc.executiontools.detectionTools.sherlock;

import org.foi.mpc.executiontools.detectionTools.SimilarityDetectionToolSettings;

public class SherlockAdapterSettings implements SimilarityDetectionToolSettings{
    public String logFileName;
    public boolean amalgamate;
    public boolean concatanate;
    public int maxBackwardJump;
    public int maxForwardJump;
    public int minRunLenght;
    public int minStringLength;
    public int maxJumpDiff;
    public int strictness;
    public String matchesDirName;
}
