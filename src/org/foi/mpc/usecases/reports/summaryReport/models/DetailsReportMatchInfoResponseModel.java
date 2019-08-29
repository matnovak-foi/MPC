package org.foi.mpc.usecases.reports.summaryReport.models;

import java.io.File;
import java.util.List;

public class DetailsReportMatchInfoResponseModel {
    public String errorMessages;

    public List<DetailsReportToolTechniquesSimilatiry> toolsTechniquesList;

    public static class DetailsReportToolTechniquesSimilatiry {

        public String tool;
        public String technique;
        public float similarity;
        public float similarityA;
        public float similarityB;
        public float calculatedSimilarity;
        public float calculatedSimilarityA;
        public float calculatedSimilarityB;
        public List<DetailsReportMatchParts> matchParts;
        public int totalLineCountA;
        public int totalLineCountB;
        public File matchesDir;

        public static class DetailsReportMatchParts {
            public int startLineNumberA;
            public int startLineNumberB;
            public int endLineNumberA;
            public int endLineNumberB;
            public double similarity;
            public double similarityA;
            public double similarityB;
            public int lineCountA;
            public int lineCountB;
        }
    }
}
