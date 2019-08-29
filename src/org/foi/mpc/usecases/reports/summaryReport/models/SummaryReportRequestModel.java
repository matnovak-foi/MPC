package org.foi.mpc.usecases.reports.summaryReport.models;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class SummaryReportRequestModel {
    public List<String> selectedTools;
    public File selectedInputDir;
    public File selectedWorking;
    public List<String> selectedTechniques;
    public int selectedInputDirDepth;
    public File sourceDirPath;

    @Override
    public String toString() {
        return "SummaryReportRequestModel{" +
                "selectedTools='" + selectedTools + '\'' +
                ", selectedInputDir=" + selectedInputDir +
                ", selectedWorking=" + selectedWorking +
                ", selectedTechniques=" + selectedTechniques +
                ", selectedInputDirDepth=" + selectedInputDirDepth +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SummaryReportRequestModel)) return false;
        SummaryReportRequestModel that = (SummaryReportRequestModel) o;
        return selectedInputDirDepth == that.selectedInputDirDepth &&
                Objects.equals(selectedTools, that.selectedTools) &&
                Objects.equals(selectedInputDir, that.selectedInputDir) &&
                Objects.equals(selectedWorking, that.selectedWorking) &&
                Objects.equals(selectedTechniques, that.selectedTechniques);
    }
}
