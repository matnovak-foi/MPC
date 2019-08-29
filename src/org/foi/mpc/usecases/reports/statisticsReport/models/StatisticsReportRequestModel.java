package org.foi.mpc.usecases.reports.statisticsReport.models;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StatisticsReportRequestModel {
    public static enum ThresholdType {
        plagMatchesBased, calculatedPrecentageBased, topNBased, fixedPrecentageBased
    }

    public File workingDir;
    public File sourceDir;
    public List<String> tools;
    public List<String> techniques;
    public int inputDirDepth;
    public File assignementDir;
    public double thresholdValue;
    public ThresholdType thresholdType;

}
