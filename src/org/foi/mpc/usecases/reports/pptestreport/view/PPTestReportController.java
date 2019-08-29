package org.foi.mpc.usecases.reports.pptestreport.view;

import org.foi.mpc.usecases.combotechnique.view.models.ComboTechniqueViewModel;
import org.foi.mpc.usecases.reports.pptestreport.PPTestReportInputBoundary;
import org.foi.mpc.usecases.reports.pptestreport.PPTestReportOutputBoundary;
import org.foi.mpc.usecases.reports.pptestreport.models.PPTestReportRequestModel;
import org.foi.mpc.usecases.reports.pptestreport.view.models.PPTestReportViewModel;

import java.io.File;
import java.util.ArrayList;

public class PPTestReportController {
    private PPTestReportInputBoundary testReportUseCase;
    private PPTestReportViewModel viewModel;
    private ComboTechniqueViewModel techniqueViewModel;
    private PPTestReportPresenter presenter;

    public PPTestReportController(PPTestReportPresenter presenter, PPTestReportViewModel viewModel, ComboTechniqueViewModel techniqueViewModel) {
        this.presenter = presenter;
        this.viewModel = viewModel;
        this.techniqueViewModel = techniqueViewModel;
    }

    public PPTestReportViewModel getViewModel() {
        return viewModel;
    }

    public void generateReport() {
        PPTestReportRequestModel requestModel = new PPTestReportRequestModel();
        requestModel.selectedTechniques = new ArrayList<>();

        requestModel.selectedTools = viewModel.getSelectedTools();
        requestModel.selectedInputDir = new File(viewModel.getSourceDirPath());
        requestModel.selectedWorking = new File(viewModel.getWorkingDirPath());

        if(techniqueViewModel.getSelectedTechniques() != null)
            requestModel.selectedTechniques.addAll(techniqueViewModel.getSelectedTechniques());
        if(techniqueViewModel.getSelectedComboTechniques() != null)
            requestModel.selectedTechniques.addAll(techniqueViewModel.getSelectedComboTechniques());

        testReportUseCase.generateReport(requestModel, presenter);
    }

    public PPTestReportInputBoundary getTestReportUseCase() {
        return testReportUseCase;
    }

    public PPTestReportOutputBoundary getPresenter() {
        return presenter;
    }

    public void setTestReportUseCase(PPTestReportInputBoundary testReportUseCase) {
        this.testReportUseCase = testReportUseCase;
        this.testReportUseCase.getAvailableTools(presenter);
    }
}
