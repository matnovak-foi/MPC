package org.foi.mpc.usecases.combotechnique.view;

import org.foi.mpc.usecases.combotechnique.ComboTechniqueInputBoundary;
import org.foi.mpc.usecases.combotechnique.ComboTechniqueOutputBoundary;
import org.foi.mpc.usecases.combotechnique.view.models.ComboTechniqueViewModel;

import java.io.File;

public class ComboTechniqueControler {
    private ComboTechniqueInputBoundary useCase;
    private ComboTechniqueViewModel viewModel;
    private ComboTechniquePresenter presenter;

    public ComboTechniqueControler(ComboTechniquePresenter presenter,ComboTechniqueViewModel viewModel) {
        this.viewModel = viewModel;
        this.presenter = presenter;
    }

    public void clearTechniqueSelection(){
        useCase.clearSelectedTechniques(presenter);
    }

    public void addTechniqueToCombo(){
        useCase.addTechniquesToComboParts(presenter,viewModel.getSelectedTechniques());
    }

    public void createComboTechnique(){
        useCase.createNewComboTechnique(presenter,viewModel.getComboTechniqueName());
    }

    public void deleteTechniqueFromCombo(){
        useCase.removeTechniquesFromComboParts(presenter,viewModel.getSelectedComboPartsToDelete());
    }

    public void loadComboTehniquesFromFile(String file) {
        useCase.loadComboTehniquesFromFile(presenter,file);
    }

    public ComboTechniqueInputBoundary getUseCase() {
        return useCase;
    }

    public void setComboTechniqueUseCase(ComboTechniqueInputBoundary comboUseCase) {
        this.useCase = comboUseCase;
        this.useCase.getAvailableTechniques(presenter);
    }

    public ComboTechniqueOutputBoundary getPresenter() {
        return presenter;
    }

    public void loadProcessedTechniques(File workingDir) {
        this.useCase.getProcessedTechniques(presenter,workingDir);
    }
}
