package org.foi.mpc.usecases.reports.pptestreport.view.models;

import java.util.ArrayList;
import java.util.List;

public class PPTestReportViewModelBuilder {

    private PPTestReportViewModel viewModel;

    public PPTestReportViewModelBuilder() {
        this.viewModel = new PPTestReportViewModel();
    }

    public PPTestReportViewModelBuilder updateModel(PPTestReportViewModel viewModel) {
        this.viewModel = viewModel;
        return this;
    }

    public PPTestReportViewModelBuilder withTool(String toolName){
        List<String> tools = new ArrayList<>();
        tools.add(toolName);
        viewModel.setSelectedTools(tools);
        return this;
    }

    public PPTestReportViewModelBuilder withSourceDir(String sourceDir) {
        viewModel.setSourceDirPath(sourceDir);
        return this;
    }

    public PPTestReportViewModelBuilder withWorkingDir(String workingDir) {
        viewModel.setWorkingDirPath(workingDir);
        return this;
    }

    public PPTestReportViewModel build() {
        return viewModel;
    }

}
