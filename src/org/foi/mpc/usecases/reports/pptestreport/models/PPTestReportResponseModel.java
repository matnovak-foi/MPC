package org.foi.mpc.usecases.reports.pptestreport.models;

import java.util.List;

public class PPTestReportResponseModel {
    public String errorMessages;
    public String usernameA;
    public String usernameB;
    public String toolName;

    public List<PPTestReportTechnique> ppReportTable;

    public static class PPTestReportTechnique {
        public String name;
        public float similarityA;
        public float similarityB;
        public float similarity;

        @Override
        public String toString() {
            return "PPTestReportTechnique{" +
                    "name='" + name + '\'' +
                    ", similarityA=" + similarityA +
                    ", similarityB=" + similarityB +
                    ", similarity=" + similarity +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PPTestReportTechnique)) return false;

            PPTestReportTechnique that = (PPTestReportTechnique) o;

            return name.equals(that.name)
                    && Float.compare(that.similarityA, similarityA) == 0
                    && Float.compare(that.similarityB, similarityB) == 0
                    && Float.compare(that.similarity, similarity) == 0;
        }
    }


}
