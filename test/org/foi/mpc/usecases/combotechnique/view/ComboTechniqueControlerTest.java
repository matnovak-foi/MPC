package org.foi.mpc.usecases.combotechnique.view;

import org.foi.mpc.usecases.combotechnique.ComboTechniqueInputBoundary;
import org.foi.mpc.usecases.combotechnique.ComboTechniqueOutputBoundary;
import org.foi.mpc.usecases.combotechnique.view.models.ComboTechniqueViewModel;
import org.foi.mpc.usecases.combotechnique.view.models.ComboTechniqueViewModelBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ComboTechniqueControlerTest {
    ComboTechniqueViewModel techniqueViewModel;
    ComboTechniqueViewModelBuilder techniqueViewModelBuilder;
    ComboTechniqueInputBoundarySpy comboUseCaseSpy;
    ComboTechniquePresenter techniquePresenter;
    ComboTechniqueControler techniqueControler;

    @Before
    public void setUp(){
        techniqueViewModelBuilder = new ComboTechniqueViewModelBuilder();
        techniqueViewModel = techniqueViewModelBuilder.build();

        techniquePresenter = new ComboTechniquePresenter(techniqueViewModel);
        comboUseCaseSpy = new ComboTechniqueInputBoundarySpy();

        techniqueControler = new ComboTechniqueControler(techniquePresenter, techniqueViewModel);
        techniqueControler.setComboTechniqueUseCase(comboUseCaseSpy);
    }

    @Test
    public void presenterAndFileNameArePassedToUseCaseOnControllerLoadComboTehcnique(){
        techniqueControler.loadComboTehniquesFromFile("comboFile");

        assertSame(techniqueControler.getPresenter(), comboUseCaseSpy.presenter);
        assertEquals("comboFile",comboUseCaseSpy.comboTechniqueFileName);
    }

    @Test
    public void presenterIsPassedToTheUseCaseOnCreationCallingGetAvailable() {
        assertSame(techniqueControler.getPresenter(), comboUseCaseSpy.presenter);
        assertTrue(comboUseCaseSpy.getAvailableTechniquesWasCalled);
    }

    @Test
    public void onAddTechniquePassesToComboUseCase() {
        techniqueViewModel = techniqueViewModelBuilder
                .withSelectedTechnique("Technique")
                .build();

        techniqueControler.addTechniqueToCombo();

        List<String> expected = new ArrayList<>();
        expected.add("Technique");
        assertEquals(expected, comboUseCaseSpy.addedTechniques);
        assertSame(techniqueControler.getPresenter(), comboUseCaseSpy.presenter);
    }

    @Test
    public void onDeleteTechniquePassesToComboUseCase() {
        techniqueViewModel = techniqueViewModelBuilder
                .withSelectedComboPartToDelete("DeleteTechnique")
                .build();
        techniqueControler.deleteTechniqueFromCombo();

        List<String> expected = new ArrayList<>();
        expected.add("DeleteTechnique");
        assertEquals(expected, comboUseCaseSpy.removedTechniques);
        assertSame(techniqueControler.getPresenter(), comboUseCaseSpy.presenter);
    }

    @Test
    public void onCreateComboTechniquePassesToComboUseCase() {
        techniqueViewModel = techniqueViewModelBuilder
                .withNewComboTechniqueName("ComboTechnique")
                .build();

        techniqueControler.createComboTechnique();

        assertEquals("ComboTechnique",comboUseCaseSpy.comboTechniqueName);
        assertSame(techniqueControler.getPresenter(), comboUseCaseSpy.presenter);
    }

    @Test
    public void presenterIsPassedToTheUseCaseOnClearTechniqueSelection(){
        techniqueControler.clearTechniqueSelection();

        assertSame(techniqueControler.getPresenter(), comboUseCaseSpy.presenter);
        assertTrue(comboUseCaseSpy.getClearSelectedTechniquesWasCalled);
    }

    @Test
    public void presenterIsPassedToLoadProcessedTools() {
        File workingDir = new File("workingDir");
        techniqueControler.loadProcessedTechniques(workingDir);
        assertSame(techniqueControler.getPresenter(), comboUseCaseSpy.presenter);
        assertEquals(workingDir,comboUseCaseSpy.workingDir);
    }

private class ComboTechniqueInputBoundarySpy implements ComboTechniqueInputBoundary {
    List<String> addedTechniques;
    List<String> removedTechniques;
    ComboTechniqueOutputBoundary presenter;
    String comboTechniqueName;
    String comboTechniqueFileName;
    public boolean getClearSelectedTechniquesWasCalled = false;
    public boolean getAvailableTechniquesWasCalled = false;
    public File workingDir;

    @Override
    public void addTechniquesToComboParts(ComboTechniqueOutputBoundary presenter, List<String> comboParts) {
        addedTechniques = comboParts;
        this.presenter = presenter;
    }

    @Override
    public void removeTechniquesFromComboParts(ComboTechniqueOutputBoundary presenter, List<String> comboParts) {
        removedTechniques = comboParts;
        this.presenter = presenter;
    }

    @Override
    public void createNewComboTechnique(ComboTechniqueOutputBoundary presenter, String comboTechniqueName) {
        this.presenter = presenter;
        this.comboTechniqueName = comboTechniqueName;
    }

    @Override
    public void loadComboTehniquesFromFile(ComboTechniqueOutputBoundary presenter, String inputFile) {
        this.presenter = presenter;
        this.comboTechniqueFileName = inputFile;
    }

    @Override
    public void clearSelectedTechniques(ComboTechniqueOutputBoundary presenter) {
        this.presenter = presenter;
        getClearSelectedTechniquesWasCalled = true;
    }

    @Override
    public void getProcessedTechniques(ComboTechniqueOutputBoundary presenterSpy, File workingDir) {
        this.presenter = presenterSpy;
        this.workingDir = workingDir;
    }

    @Override
    public void getAvailableTechniques(ComboTechniqueOutputBoundary presenter) {
        this.presenter = presenter;
        getAvailableTechniquesWasCalled = true;
    }

}
}