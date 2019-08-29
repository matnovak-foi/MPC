package org.foi.mpc.executiontools.detectionTools.JPlag;

import org.foi.mpc.executiontools.detectionTools.SimilarityDetectionToolSettings;

public class JPlagAdapterSettings implements SimilarityDetectionToolSettings{
    public int minStoreMatchAbove;
    public int minTokenMatch;
    String languageName;
    String outputLogName;
    String resultDirName;
    String[] includeFileExtensions;
    public String matchesDirName;
}
