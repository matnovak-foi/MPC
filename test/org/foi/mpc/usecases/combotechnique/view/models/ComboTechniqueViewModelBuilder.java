package org.foi.mpc.usecases.combotechnique.view.models;

import org.foi.mpc.usecases.reports.pptestreport.view.models.PPTestReportViewModel;
import org.foi.mpc.usecases.reports.pptestreport.view.models.PPTestReportViewModelBuilder;

import java.util.ArrayList;
import java.util.List;

public class ComboTechniqueViewModelBuilder {
    private ComboTechniqueViewModel viewModel;
    List<String> selectedTechniques;
    List<String> selectedComboTechniques;
    List<String> comboTechniqueParts;
    List<String> selectedComboPartsToDelete;

    public ComboTechniqueViewModelBuilder() {
        this.viewModel = new ComboTechniqueViewModel();
    }

    public ComboTechniqueViewModelBuilder updateModel(ComboTechniqueViewModel viewModel) {
        this.viewModel = viewModel;
        this.selectedTechniques = viewModel.getSelectedTechniques();
        this.comboTechniqueParts = viewModel.getComboTechniqueParts();
        this.selectedComboTechniques = viewModel.getSelectedComboTechniques();
        this.selectedComboPartsToDelete = viewModel.getSelectedComboPartsToDelete();
        return this;
    }


    public ComboTechniqueViewModelBuilder withSelectedTechnique(String techniqueName){
        if(selectedTechniques == null) selectedTechniques = new ArrayList<>();
        selectedTechniques.add(techniqueName);
        return this;
    }

    public ComboTechniqueViewModelBuilder withSelectedComboTechnique(String techniqueName) {
        if(selectedComboTechniques == null) selectedComboTechniques = new ArrayList<>();
        selectedComboTechniques.add(techniqueName);
        return  this;
    }

    public ComboTechniqueViewModelBuilder withNewComboTechniqueName(String comboName) {
        viewModel.setComboTechniqueName(comboName);
        return this;
    }

    public ComboTechniqueViewModelBuilder withComboPart(String techniqueName) {
        if(comboTechniqueParts == null) comboTechniqueParts = new ArrayList<>();
        comboTechniqueParts.add(techniqueName);
        return this;
    }

    public ComboTechniqueViewModelBuilder withSelectedComboPartToDelete(String comboPart) {
        if(selectedComboPartsToDelete == null) selectedComboPartsToDelete = new ArrayList<>();
        selectedComboPartsToDelete.add(comboPart);
        return this;
    }

    public ComboTechniqueViewModel build() {
        viewModel.setSelectedTechniques(selectedTechniques);
        viewModel.setSelectedComboTechniques(selectedComboTechniques);
        viewModel.setComboTechniqueParts(comboTechniqueParts);
        viewModel.setSelectedComboPartsToDelete(selectedComboPartsToDelete);
        return viewModel;
    }


    public ComboTechniqueViewModelBuilder withNoTechniqueSelected(){
        selectedTechniques = new ArrayList<>();
        return this;
    }
}
