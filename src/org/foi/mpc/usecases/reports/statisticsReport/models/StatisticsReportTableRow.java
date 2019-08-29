package org.foi.mpc.usecases.reports.statisticsReport.models;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StatisticsReportTableRow {

    public String tool;
    public String technique;
    public File matchesDir;

    public int indicatedMatches;
    public int falsePositives;
    public int falseNegatives;
    public int truePositives;
    public List<StatMatch> matches = new ArrayList<>();
    public SimilarityDescriptiveStatistics descStat = new SimilarityDescriptiveStatistics();
    public double precision;
    public double recall;
    public double F1;
    public double threshold;
    public int includedMatches;
    public int numberOfMatches;

    public static class StatMatch{
       public String fileAName;
       public String fileBName;
       public double similarity;

        @Override
        public String toString() {
            return "StatMatch{" +
                    "fileAName='" + fileAName + '\'' +
                    ", fileBName='" + fileBName + '\'' +
                    ", similarity=" + similarity +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "StatisticsReportTableRow{" +
                "tool='" + tool + '\'' +
                ", technique='" + technique + '\'' +
                ", matchesDir=" + matchesDir +
                ", indicatedMatches=" + indicatedMatches +
                ", falsePositives=" + falsePositives +
                ", falseNegatives=" + falseNegatives +
                ", truePositives=" + truePositives +
                ", matches=" + matches +
                ", descStat=" + descStat +
                ", precision=" + precision +
                ", recall=" + recall +
                ", F1=" + F1 +
                '}';
    }
}
