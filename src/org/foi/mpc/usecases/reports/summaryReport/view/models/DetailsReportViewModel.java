package org.foi.mpc.usecases.reports.summaryReport.view.models;

import java.util.List;
import java.util.Objects;

public class DetailsReportViewModel {
    private PresentableReportSummaryTableRow selectedPair;
    private List<PresentableDetailSimilarityTable> similarityTable;
    private String resultToolName;
    private String resultTechniqueName;
    private String selectedWorkingDirPath;
    private String matchesDirPath;
    private String errorMessage;
    private String techniqueList;
    private String toolList;
    private PresentableDetailSimilarityTable selectedSimilarity;
    private PresentableDetailsMatchPart selectedMatchPart;
    private PresentableSideBySide presentableSideBySide;
    private String selectedSideBySideMarkingType;

    public PresentableReportSummaryTableRow getSelectedPair() {
        return selectedPair;
    }

    public void setSelectedPair(PresentableReportSummaryTableRow selectedPair) {
        this.selectedPair = selectedPair;
    }

    public String getResultToolName() {
        return resultToolName;
    }

    public void setResultToolName(String resultToolName) {
        this.resultToolName = resultToolName;
    }

    public String getResultTechniqueName() {
        return resultTechniqueName;
    }

    public void setResultTechniqueName(String resultTechniqueName) {
        this.resultTechniqueName = resultTechniqueName;
    }

    public String getSelectedWorkingDirPath() {
        return selectedWorkingDirPath;
    }

    public void setSelectedWorkingDirPath(String selectedWorkingDirPath) {
        this.selectedWorkingDirPath = selectedWorkingDirPath;
    }

    public String getMatchesDirPath() {
        return matchesDirPath;
    }

    public void setMatchesDirPath(String matchesDirPath) {
        this.matchesDirPath = matchesDirPath;
    }

    @Override
    public String toString() {
        return "DetailsReportViewModel{" +
                "selectedPair=" + selectedPair +
                ", resultToolName='" + resultToolName + '\'' +
                ", resultTechniqueName='" + resultTechniqueName + '\'' +
                ", selectedWorkingDirPath='" + selectedWorkingDirPath + '\'' +
                ", matchesDirPath='" + matchesDirPath + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DetailsReportViewModel)) return false;
        DetailsReportViewModel viewModel = (DetailsReportViewModel) o;
        return Objects.equals(selectedPair, viewModel.selectedPair) &&
                Objects.equals(resultToolName, viewModel.resultToolName) &&
                Objects.equals(resultTechniqueName, viewModel.resultTechniqueName) &&
                Objects.equals(selectedWorkingDirPath, viewModel.selectedWorkingDirPath) &&
                Objects.equals(matchesDirPath, viewModel.matchesDirPath);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setTechniqueList(String techniqueList) {
        this.techniqueList = techniqueList;
    }

    public String getTechniqueList() {
        return techniqueList;
    }

    public void setToolList(String toolList) {
        this.toolList = toolList;
    }

    public String getToolList() {
        return toolList;
    }

    public List<PresentableDetailSimilarityTable> getSimilarityTable() {
        return similarityTable;
    }

    public void setSimilarityTable(List<PresentableDetailSimilarityTable> similarityTable) {
        this.similarityTable = similarityTable;
    }

    public void setSelectedSimilarity(PresentableDetailSimilarityTable selectedSimilarity) {
        this.selectedSimilarity = selectedSimilarity;
    }

    public PresentableDetailSimilarityTable getSelectedSimilarity() {
        return selectedSimilarity;
    }

    public void setSelectedMatchPart(PresentableDetailsMatchPart selectedMatchPart) {
        this.selectedMatchPart = selectedMatchPart;
    }

    public PresentableDetailsMatchPart getSelectedMatchPart() {
        return selectedMatchPart;
    }

    public PresentableSideBySide getPresentableSideBySide() {
        return presentableSideBySide;
    }

    public void setPresentableSideBySide(PresentableSideBySide presentableSideBySide) {
        this.presentableSideBySide = presentableSideBySide;
    }

    public String getSelectedSideBySideMarkingType() {
        return selectedSideBySideMarkingType;
    }

    public void setSelectedSideBySideMarkingType(String selectedSideBySideMarkingType) {
        this.selectedSideBySideMarkingType = selectedSideBySideMarkingType;
    }


}
