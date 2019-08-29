package org.foi.mpc.usecases.combotechnique.view;

import org.foi.mpc.executiontools.factories.PreprocessingTechniqueFactory;
import org.foi.mpc.usecases.combotechnique.ComboTechniqueOutputBoundary;
import org.foi.mpc.usecases.combotechnique.models.AvailableTechniquesResponseModel;
import org.foi.mpc.usecases.combotechnique.models.ComboTechniqueResponseModel;
import org.foi.mpc.usecases.combotechnique.view.models.ComboTechniqueViewModel;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ComboTechniquePresenterTest {

    ComboTechniqueViewModel techniquesViewModel;
    ComboTechniquePresenter techniquesPresenter;

    @Before
    public void setUp(){
        techniquesViewModel = new ComboTechniqueViewModel();
        techniquesPresenter = new ComboTechniquePresenter(techniquesViewModel);
    }

    @Test
    public void isPPTestReportOutputBoundary() {
        assertTrue(techniquesPresenter instanceof ComboTechniqueOutputBoundary);
    }

    @Test
    public void translatesComboPartsResponseModelToViewModel(){
        List<String> comboParts = new ArrayList<>();
        comboParts.add("Technique1");
        comboParts.add("Technique2");

        techniquesPresenter.presentComboParts(comboParts);

        assertSame(comboParts,techniquesViewModel.getComboTechniqueParts());
    }

    @Test
    public void translatesResponseModelOfAvalibleTechniquesToViewModel() {
        AvailableTechniquesResponseModel responseModel = new AvailableTechniquesResponseModel();
        responseModel.techniques = new PreprocessingTechniqueFactory().getAvailableTools();

        techniquesPresenter.presentAvailableTechniques(responseModel);

        assertEquals(responseModel.techniques, techniquesViewModel.getAvailableTechniques());
    }

    @Test
    public void translatesComboTechniquesRepsonseModelToViewModel(){
        ComboTechniqueResponseModel responseModel = new ComboTechniqueResponseModel();
        responseModel.error = "Error";
        responseModel.comboTechniques = new ArrayList<>();
        responseModel.comboTechniques.add("Technique1");

        techniquesPresenter.presentComboTechniques(responseModel);

        assertEquals(responseModel.error,techniquesViewModel.getErrorMessage());
        assertEquals(responseModel.comboTechniques, techniquesViewModel.getAvalibleComboTechniques());
    }

    @Test
    public void clearSelectedTechniques(){
        List<String> data = new ArrayList<>();
        data.add("test");
        techniquesViewModel.setSelectedComboTechniques(data);
        techniquesViewModel.setSelectedTechniques(data);
        techniquesPresenter.clearSelectedTechniques();
        assertEquals(0,techniquesViewModel.getSelectedTechniques().size());
        assertEquals(0,techniquesViewModel.getSelectedComboTechniques().size());
    }

    @Test
    public void translatesResponseModelOfAvalibleTechniquesToViewModelOnPresentProcessedTehcniques() {
        AvailableTechniquesResponseModel responseModel = new AvailableTechniquesResponseModel();
        responseModel.techniques = new PreprocessingTechniqueFactory().getAvailableTools();
        responseModel.errorMessage = "errorMessage";

        techniquesPresenter.presentAvailableTechniques(responseModel);

        assertEquals(responseModel.techniques, techniquesViewModel.getAvailableTechniques());
        assertEquals(responseModel.errorMessage,techniquesViewModel.getErrorMessage());
    }
}