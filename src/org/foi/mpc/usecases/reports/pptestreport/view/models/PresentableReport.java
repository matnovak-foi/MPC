package org.foi.mpc.usecases.reports.pptestreport.view.models;

import java.util.List;

public class PresentableReport {
    private String usernameA;
    private String usernameB;
    private String toolName;
    private List<PresentableReportPPTechnique> ppTechniques;

    public String getUsernameA() {
        return usernameA;
    }

    public void setUsernameA(String usernameA) {
        this.usernameA = usernameA;
    }

    public String getUsernameB() {
        return usernameB;
    }

    public void setUsernameB(String usernameB) {
        this.usernameB = usernameB;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public List<PresentableReportPPTechnique> getPpTechniques() {
        return ppTechniques;
    }

    public void setPpTechniques(List<PresentableReportPPTechnique> ppTechniques) {
        this.ppTechniques = ppTechniques;
    }

    @Override
    public String toString() {
        return "PresentableReport{" +
                "usernameA='" + usernameA + '\'' +
                ", usernameB='" + usernameB + '\'' +
                ", toolName='" + toolName + '\'' +
                ", ppTechniques=" + ppTechniques +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PresentableReport)) return false;
        PresentableReport report = (PresentableReport) o;

        return usernameA.equals(report.usernameA)
                && usernameB.equals(report.usernameB)
                && toolName.equals(report.toolName)
                && ppTechniques.equals(report.ppTechniques);
    }
}
