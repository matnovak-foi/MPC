package org.foi.mpc.usecases.reports.view.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class StandardReportInputData {
    protected List<String> availableTools;
    protected List<String> selectedTools;
    protected String sourceDirPath;
    protected String workingDirPath;
    protected String errorMessage;
    protected boolean disabledLoadProcessedToolsAndTechniques = false;
    protected boolean disabledLoadToolsAndTechniquesFromFile = false;

    public boolean isDisabledLoadProcessedToolsAndTechniques() {
        return disabledLoadProcessedToolsAndTechniques;
    }

    public void setDisabledLoadProcessedToolsAndTechniques(boolean disabledLoadProcessedToolsAndTechniques) {
        this.disabledLoadProcessedToolsAndTechniques = disabledLoadProcessedToolsAndTechniques;
    }

    public boolean isDisabledLoadToolsAndTechniquesFromFile() {
        return disabledLoadToolsAndTechniquesFromFile;
    }

    public void setDisabledLoadToolsAndTechniquesFromFile(boolean disabledLoadToolsAndTechniquesFromFile) {
        this.disabledLoadToolsAndTechniquesFromFile = disabledLoadToolsAndTechniquesFromFile;
    }

    public StandardReportInputData() {
        availableTools = new ArrayList<>();
    }

    public List<String> getAvailableTools() {
        return availableTools;
    }

    public String getSourceDirPath() {
        return sourceDirPath;
    }

    public void setSourceDirPath(String sourceDirPath) {
        this.sourceDirPath = sourceDirPath;
    }

    public List<String> getSelectedTools(){
        return selectedTools;
    }

    public void setSelectedTools(List<String> selectedTools) {
        this.selectedTools = selectedTools;
    }

    public void setWorkingDirPath(String workingDirPath) {
        this.workingDirPath = workingDirPath;
    }

    public String getWorkingDirPath() {
        return workingDirPath;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setAvailableTools(List<String> availableTools) {
        this.availableTools = availableTools;
    }
}
