package org.foi.mpc.usecases.reports.pptestreport.models;

import java.io.File;
import java.util.List;

public class PPTestReportRequestModel {
    public List<String> selectedTechniques;
    public List<String> selectedTools;
    public File selectedInputDir;
    public File selectedWorking;

    @Override
    public String toString() {
        return "PPTestReportRequestModel{" +
                "selectedTechniques=" + selectedTechniques +
                ", selectedTools='" + selectedTools + '\'' +
                ", selectedInputDir=" + selectedInputDir +
                ", selectedWorking=" + selectedWorking +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PPTestReportRequestModel)) return false;

        PPTestReportRequestModel that = (PPTestReportRequestModel) o;

        return selectedTechniques.equals(that.selectedTechniques)
                && selectedTools.equals(that.selectedTools)
                && selectedInputDir.equals(that.selectedInputDir)
                && selectedWorking.equals(that.selectedWorking);
    }
}
