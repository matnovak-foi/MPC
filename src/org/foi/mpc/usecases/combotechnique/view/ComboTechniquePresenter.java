package org.foi.mpc.usecases.combotechnique.view;

import org.foi.mpc.usecases.combotechnique.ComboTechniqueOutputBoundary;
import org.foi.mpc.usecases.combotechnique.models.AvailableTechniquesResponseModel;
import org.foi.mpc.usecases.combotechnique.models.ComboTechniqueResponseModel;
import org.foi.mpc.usecases.combotechnique.view.models.ComboTechniqueViewModel;

import java.util.ArrayList;
import java.util.List;

public class ComboTechniquePresenter implements ComboTechniqueOutputBoundary {
    private ComboTechniqueViewModel viewModel;

    public ComboTechniquePresenter(ComboTechniqueViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentComboParts(List<String> comboParts) {
        viewModel.setComboTechniqueParts(comboParts);
    }

    @Override
    public void presentComboTechniques(ComboTechniqueResponseModel responseModel) {
        viewModel.setErrorMessage(responseModel.error);
        viewModel.setAvalibleComboTechniques(responseModel.comboTechniques);
    }

    @Override
    public void clearSelectedTechniques() {
        viewModel.setSelectedTechniques(new ArrayList<>());
        viewModel.setSelectedComboTechniques(new ArrayList<>());
    }

    @Override
    public void presentAvailableTechniques(AvailableTechniquesResponseModel responseModel) {
        viewModel.setAvailableTechniques(responseModel.techniques);
        viewModel.setErrorMessage(responseModel.errorMessage);
    }


}
