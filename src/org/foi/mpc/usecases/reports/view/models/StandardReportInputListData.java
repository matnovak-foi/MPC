package org.foi.mpc.usecases.reports.view.models;

import java.util.ArrayList;
import java.util.List;

public abstract class StandardReportInputListData extends StandardReportInputData {
    protected int inputDirDepth;
    protected List<SelectOption> inputDirList = new ArrayList<>();
    protected String selectedInputDirPath;

    public int getInputDirDepth() {
        return inputDirDepth;
    }

    public void setInputDirDepth(int inputDirDepth) {
        this.inputDirDepth = inputDirDepth;
    }

    public List<SelectOption> getInputDirList() {
        return inputDirList;
    }

    public void setInputDirList(List<SelectOption> inputDirList) {
        this.inputDirList = inputDirList;
    }

    public void setSelectedInputDirPath(String selectedInputDirPath) {
        this.selectedInputDirPath = selectedInputDirPath;
    }

    public String getSelectedInputDirPath() {
        return selectedInputDirPath;
    }
}
