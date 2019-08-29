package org.foi.mpc.usecases.reports.statisticsReport.view;

import org.foi.mpc.usecases.combotechnique.ComboTechniqueUseCase;
import org.foi.mpc.usecases.combotechnique.view.models.ComboTechniqueViewModel;
import org.foi.mpc.usecases.reports.statisticsReport.StatisticsReportInputBoundary;
import org.foi.mpc.usecases.reports.statisticsReport.StatisticsReportOutputBoundary;
import org.foi.mpc.usecases.reports.statisticsReport.StatisticsReportUseCase;
import org.foi.mpc.usecases.reports.statisticsReport.models.StatisticsReportRequestModel;
import org.foi.mpc.usecases.reports.statisticsReport.models.StatisticsReportRequestModel.ThresholdType;
import org.foi.mpc.usecases.reports.statisticsReport.view.model.StatisticsReportViewModel;

import java.io.File;

public class StatisticsReportControler {
    private StatisticsReportInputBoundary useCase;
    private StatisticsReportOutputBoundary presenter;
    private StatisticsReportViewModel viewModel;
    private ComboTechniqueViewModel techniqueViewModel;

    public StatisticsReportControler(StatisticsReportViewModel reportViewModel, StatisticsReportOutputBoundary presenter, ComboTechniqueViewModel techniqueViewModel) {
        this.viewModel = reportViewModel;
        this.presenter = presenter;
        this.techniqueViewModel =techniqueViewModel;

        this.viewModel.setThresholdValue(3);
        this.viewModel.setThresholdType(ThresholdType.calculatedPrecentageBased.toString());
    }

    public StatisticsReportInputBoundary getStatisticsReportUseCase() {
        return useCase;
    }

    public StatisticsReportViewModel getViewModel() {
        return viewModel;
    }

    public StatisticsReportOutputBoundary getPresenter() {
        return presenter;
    }

    public ComboTechniqueViewModel getTechniquesViewModel() {
        return techniqueViewModel;
    }

    public void setUseCase(StatisticsReportInputBoundary useCase) {
        this.useCase = useCase;
    }

    public void loadProcessedTools(){
        this.useCase.getAvailableTools(presenter, new File(viewModel.getWorkingDirPath()));
    }

    public void loadDirList() {
        this.useCase.loadDirectoryList(new File(viewModel.getSourceDirPath()),viewModel.getInputDirDepth(),presenter);
    }

    public void generateReport() {
        StatisticsReportRequestModel requestModel = new StatisticsReportRequestModel();
        requestModel.tools = viewModel.getSelectedTools();
        requestModel.techniques = techniqueViewModel.getSelectedTechniques();
        requestModel.assignementDir = new File(viewModel.getSelectedInputDirPath());
        requestModel.workingDir = new File(viewModel.getWorkingDirPath());
        requestModel.sourceDir = new File(viewModel.getSourceDirPath());
        requestModel.inputDirDepth = viewModel.getInputDirDepth();
        requestModel.thresholdValue = viewModel.getThresholdValue();

        for (ThresholdType type : ThresholdType.values()) {
            if(viewModel.getThresholdType().equalsIgnoreCase(type.name())){
                requestModel.thresholdType = type;
                break;
            }
        }

        this.useCase.generateReport(requestModel,presenter);
    }
}
