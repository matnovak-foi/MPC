package org.foi.mpc.usecases.reports.summaryReport.models;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class DetailsReportMatchInfoRequestModel {
    public String selectedTool;
    public String selectedTechnique;
    public File selectedWorkingDirPath;
    public String selectedStudentA;
    public String selectedStudentB;
    public String selectedFileAName;
    public String selectedFileBName;
    public File selectedMatchesDir;
    public List<String> toolList;
    public List<String> techniqueList;

    @Override
    public String toString() {
        return "DetailsReportMatchInfoRequestModel{" +
                "selectedTools='" + selectedTool + '\'' +
                ", selectedTechnique='" + selectedTechnique + '\'' +
                ", selectedWorkingDirPath=" + selectedWorkingDirPath +
                ", selectedStudentA='" + selectedStudentA + '\'' +
                ", selectedStudentB='" + selectedStudentB + '\'' +
                ", selectedFileAName='" + selectedFileAName + '\'' +
                ", selectedFileBName='" + selectedFileBName + '\'' +
                ", selectedMatchesDir=" + selectedMatchesDir +
                ", toolList=" + toolList +
                ", techniqueList=" + techniqueList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DetailsReportMatchInfoRequestModel)) return false;
        DetailsReportMatchInfoRequestModel that = (DetailsReportMatchInfoRequestModel) o;
        return Objects.equals(selectedTool, that.selectedTool) &&
                Objects.equals(selectedTechnique, that.selectedTechnique) &&
                Objects.equals(selectedWorkingDirPath, that.selectedWorkingDirPath) &&
                Objects.equals(selectedStudentA, that.selectedStudentA) &&
                Objects.equals(selectedStudentB, that.selectedStudentB) &&
                Objects.equals(selectedFileAName, that.selectedFileAName) &&
                Objects.equals(selectedFileBName, that.selectedFileBName) &&
                Objects.equals(selectedMatchesDir, that.selectedMatchesDir);
    }

}
