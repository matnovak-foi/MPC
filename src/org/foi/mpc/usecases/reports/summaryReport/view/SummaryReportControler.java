package org.foi.mpc.usecases.reports.summaryReport.view;

import org.foi.mpc.usecases.combotechnique.ComboTechniqueUseCase;
import org.foi.mpc.usecases.combotechnique.view.models.ComboTechniqueViewModel;
import org.foi.mpc.usecases.reports.summaryReport.SummaryReportInputBoundary;
import org.foi.mpc.usecases.reports.summaryReport.models.SummaryReportRequestModel;
import org.foi.mpc.usecases.reports.summaryReport.models.UpdatePlagInfoRequestModel;
import org.foi.mpc.usecases.reports.summaryReport.view.models.SummaryReportViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SummaryReportControler {
    private SummaryReportInputBoundary summaryReportUseCase;
    private ComboTechniqueViewModel techniqueViewModel;
    private SummaryReportViewModel viewModel;
    private SummaryReportPresenter presenter;

    public SummaryReportControler(SummaryReportViewModel viewModel, SummaryReportPresenter presenter, ComboTechniqueViewModel techniqueViewModel) {
        this.viewModel = viewModel;
        this.presenter = presenter;
        this.techniqueViewModel = techniqueViewModel;
    }

    public SummaryReportInputBoundary getSummaryReportUseCase() {
        return summaryReportUseCase;
    }

    public SummaryReportViewModel getViewModel() {
        return viewModel;
    }

    public SummaryReportPresenter getPresenter() {
        return presenter;
    }

    public void setSummaryReportUseCase(SummaryReportInputBoundary usecase) {
        this.summaryReportUseCase = usecase;
        this.summaryReportUseCase.getAvailableTools(presenter);
    }

    public void generateReport() {
        SummaryReportRequestModel requestModel = new SummaryReportRequestModel();
        requestModel.selectedTechniques = new ArrayList<>();

        requestModel.selectedInputDir = new File(viewModel.getSelectedInputDirPath());
        requestModel.sourceDirPath = new File(viewModel.getSourceDirPath());
        requestModel.selectedWorking = new File(viewModel.getWorkingDirPath());
        requestModel.selectedInputDirDepth = viewModel.getInputDirDepth();
        requestModel.selectedTools = viewModel.getSelectedTools();
        if(techniqueViewModel.getSelectedTechniques() != null)
            requestModel.selectedTechniques.addAll(techniqueViewModel.getSelectedTechniques());
        if(techniqueViewModel.getSelectedComboTechniques() != null)
            requestModel.selectedTechniques.addAll(techniqueViewModel.getSelectedComboTechniques());
        summaryReportUseCase.generateReport(requestModel,presenter);
    }

    public void loadDirList() {
        summaryReportUseCase.loadDirectoryList(new File(viewModel.getSourceDirPath()),viewModel.getInputDirDepth(),presenter);
    }

    public void clearNonProcessedTools() {
        this.summaryReportUseCase.getAvailableTools(presenter, new File(viewModel.getWorkingDirPath()));
    }

    public void updatePlagInfo() {
        UpdatePlagInfoRequestModel requestModel = new UpdatePlagInfoRequestModel();

        if(viewModel.getSelectedPair() != null) {
            requestModel.fileAname = viewModel.getSelectedPair().getFileA();
            requestModel.fileBname = viewModel.getSelectedPair().getFileB();
            requestModel.processed = viewModel.getSelectedPair().getProcessed();
            requestModel.plagiarized = viewModel.getSelectedPair().getPlagiarized();
        }

        requestModel.workingDir = new File(viewModel.getWorkingDirPath());
        requestModel.sourceDirPath = new File(viewModel.getSourceDirPath());
        requestModel.selectedInputDirPath = new File(viewModel.getSelectedInputDirPath());


        summaryReportUseCase.updatePlagInfo(requestModel,presenter);
    }
}
