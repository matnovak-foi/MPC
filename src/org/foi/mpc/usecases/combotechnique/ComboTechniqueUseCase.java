package org.foi.mpc.usecases.combotechnique;

import org.foi.common.CollectionHelper;
import org.foi.mpc.MPCContext;
import org.foi.mpc.executiontools.factories.ComboTechiniquesLoader;
import org.foi.mpc.executiontools.factories.PreprocessingTechniqueFactory;
import org.foi.mpc.usecases.UseCaseResponseErrorMessages;
import org.foi.mpc.usecases.combotechnique.models.AvailableTechniquesResponseModel;
import org.foi.mpc.usecases.combotechnique.models.ComboTechniqueResponseModel;

import java.io.File;
import java.util.*;

public class ComboTechniqueUseCase implements ComboTechniqueInputBoundary {
    private Set<String> newTechniqueComboParts = new LinkedHashSet<>();
    private ComboTechniqueResponseModel responseModel;
    private PreprocessingTechniqueFactory ptf;
    private ComboTechiniquesLoader comboTechniqueLoader;


    public ComboTechniqueUseCase(PreprocessingTechniqueFactory ptf) {
        this.ptf = ptf;
        comboTechniqueLoader = new ComboTechiniquesLoader(ptf);
    }

    @Override
    public void getAvailableTechniques(ComboTechniqueOutputBoundary outputBoundary) {
        AvailableTechniquesResponseModel responseModel = new AvailableTechniquesResponseModel();
        responseModel.techniques = ptf.getAvailableTools();
        outputBoundary.presentAvailableTechniques(responseModel);
    }

    @Override
    public void clearSelectedTechniques(ComboTechniqueOutputBoundary outputBoundary) {
        outputBoundary.clearSelectedTechniques();
    }

    @Override
    public void getProcessedTechniques(ComboTechniqueOutputBoundary presenter, File workingDir) {
        AvailableTechniquesResponseModel responseModel = new AvailableTechniquesResponseModel();
        responseModel.techniques = new ArrayList<>();
        responseModel.errorMessage = "";

        File preprocessDir = new File(workingDir+File.separator+MPCContext.PREPROCESS_DIR);

        if(workingDir == null || !workingDir.exists()) {
            responseModel.errorMessage = UseCaseResponseErrorMessages.invalidWorkingDir;
        } else if(!preprocessDir.exists()){
            responseModel.errorMessage = UseCaseResponseErrorMessages.invalidPreprocessDir;
        } else {
            for (File tools : preprocessDir.listFiles())
                responseModel.techniques.add(tools.getName());
        }

        presenter.presentAvailableTechniques(responseModel);
    }

    @Override
    public void addTechniquesToComboParts(ComboTechniqueOutputBoundary presenter, List<String> selectedTechniques) {
        for (String technique : selectedTechniques) {
            newTechniqueComboParts.add(technique);
        }
        presentComboParts(presenter);
    }

    @Override
    public void removeTechniquesFromComboParts(ComboTechniqueOutputBoundary presenter, List<String> selectedTechniquesToRemove) {
        newTechniqueComboParts.removeAll(selectedTechniquesToRemove);
        presentComboParts(presenter);
    }

    @Override
    public void createNewComboTechnique(ComboTechniqueOutputBoundary presenter, String comboTechniqueName) {
        createEmptyResponseModel();

        if(isRequestMissingData(comboTechniqueName)){
            presenter.presentComboTechniques(responseModel);
        } else {
            ptf.createComboTechnique(comboTechniqueName,newTechniqueComboParts);
            newTechniqueComboParts = new LinkedHashSet<>();
            presentComboParts(presenter);
        }

        responseModel.comboTechniques.addAll(ptf.getAvailableComboTechniques());
        presenter.presentComboTechniques(responseModel);
    }

    private void createEmptyResponseModel() {
        responseModel = new ComboTechniqueResponseModel();
        responseModel.error = "";
        responseModel.comboTechniques = new ArrayList<>();
    }

    private boolean isRequestMissingData(String comboTechniqueName) {
        if(comboTechniqueName.isEmpty()){
            responseModel.error = UseCaseResponseErrorMessages.emptyComboTechniqueName;
            return true;
        } else if(newTechniqueComboParts.size()<2) {
            responseModel.error = UseCaseResponseErrorMessages.notEnoughPartsForCombo;
            return true;
        } else if(ptf.containsComboTechnique(comboTechniqueName)){
            responseModel.error = UseCaseResponseErrorMessages.comboTechniqueNameExists;
            return true;
        }

        return false;
    }

    private void presentComboParts(ComboTechniqueOutputBoundary presenter) {
        List<String> responseModel = CollectionHelper.convertSetToList(newTechniqueComboParts);
        presenter.presentComboParts(responseModel);
    }

    @Override
    public void loadComboTehniquesFromFile(ComboTechniqueOutputBoundary presenter, String inputFile) {
        comboTechniqueLoader.loadMultipleTechniquesFromFile(new File(inputFile));
        createEmptyResponseModel();
        responseModel.comboTechniques.addAll(ptf.getAvailableComboTechniques());
        presenter.presentComboTechniques(responseModel);
    }

    public void setComboTechniqueLoader(ComboTechiniquesLoader comboTechniqueLoader) {
        this.comboTechniqueLoader = comboTechniqueLoader;
    }

    public ComboTechiniquesLoader getComboTechniqueLoader() {
        return comboTechniqueLoader;
    }
}
