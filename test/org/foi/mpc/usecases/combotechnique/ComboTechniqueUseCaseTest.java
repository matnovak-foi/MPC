package org.foi.mpc.usecases.combotechnique;

import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.mpc.MPCContext;
import org.foi.mpc.executiontools.factories.ComboTechiniquesLoader;
import org.foi.mpc.executiontools.factories.PreprocessingTechnique;
import org.foi.mpc.executiontools.factories.PreprocessingTechniqueFactory;
import org.foi.mpc.executiontools.techniques.CommonCodeRemoveTechnique;
import org.foi.mpc.executiontools.techniques.NoTechniqueOriginal;
import org.foi.mpc.executiontools.techniques.sherlock.SherlockNoCommentsPPTechnique;
import org.foi.mpc.usecases.UseCaseResponseErrorMessages;
import org.foi.mpc.usecases.combotechnique.models.AvailableTechniquesResponseModel;
import org.foi.mpc.usecases.combotechnique.models.ComboTechniqueResponseModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.*;

public class ComboTechniqueUseCaseTest {
    ComboTechniqueUseCase useCase;
    ComboTechniqueOutputBoundarySpy presenterSpy;
    PreprocessingTechniqueFactory techniqueFactory;
    File workingDir;

    @Before
    public void setUp() throws Exception {
        techniqueFactory = new PreprocessingTechniqueFactory();
        useCase = new ComboTechniqueUseCase(techniqueFactory);
        presenterSpy = new ComboTechniqueOutputBoundarySpy();

        workingDir = new File("workingDir");
    }

    @After
    public void tearDown() throws Exception {
        DirectoryFileUtility.deleteDirectoryTree(workingDir);
    }

    @Test
    public void isPPTestReportInputBoudary() {
        assertTrue(useCase instanceof ComboTechniqueInputBoundary);
    }

    @Test
    public void useCasePassesPresenterComboPartsToPresent(){
        List<String> comboParts = setUpUseCaseWithTwoComboParts();

        assertEquals(comboParts,presenterSpy.comboParts);
    }

    @Test
    public void ifComboPartsExistAddNewOnTop(){
        List<String> comboParts = new ArrayList<>();
        comboParts.add(NoTechniqueOriginal.NAME);
        useCase.addTechniquesToComboParts(presenterSpy, comboParts);

        List<String> comboParts2 = setUpUseCaseWithTwoComboParts();

        List<String> comboParts3 = new ArrayList<>();
        comboParts3.add(CommonCodeRemoveTechnique.NAME);
        useCase.addTechniquesToComboParts(presenterSpy, comboParts3);

        List<String> expectedComboParts = new ArrayList<>();
        expectedComboParts.addAll(comboParts2);
        expectedComboParts.addAll(comboParts3);

        assertEquals(expectedComboParts,presenterSpy.comboParts);
    }

    @Test
    public void deleteOneTechniqueFromComboParts(){
        setUpUseCaseWithTwoComboParts();

        List<String> comboPartsToRemove = new ArrayList<>();
        comboPartsToRemove.add(NoTechniqueOriginal.NAME);
        useCase.removeTechniquesFromComboParts(presenterSpy, comboPartsToRemove);

        List<String> expected = new ArrayList<>();
        expected.add(SherlockNoCommentsPPTechnique.NAME);
        assertEquals(expected,presenterSpy.comboParts);
    }

    @Test
    public void deleteMultipleTechniquesFromComboParts(){
        List<String> comboParts = setUpUseCaseWithTwoComboParts();

        useCase.removeTechniquesFromComboParts(presenterSpy, comboParts);

        assertEquals(new ArrayList<>(),presenterSpy.comboParts);
    }

    @Test
    public void createComboTechniqueFromEmptyPartsReturnError(){
        useCase.createNewComboTechnique(presenterSpy,"Name");
        ComboTechniqueResponseModel responseModel = new ComboTechniqueResponseModel();
        responseModel.comboTechniques = new ArrayList<>();
        responseModel.error = UseCaseResponseErrorMessages.notEnoughPartsForCombo;
        assertEquals(responseModel,presenterSpy.responseModel);
    }

    @Test
    public void createComboTechniqueFromOnePartReturnError(){
        List<String> comboParts = new ArrayList<>();
        comboParts.add("Technique1");

        useCase.addTechniquesToComboParts(presenterSpy, comboParts);
        useCase.createNewComboTechnique(presenterSpy,"Name");

        ComboTechniqueResponseModel responseModel = new ComboTechniqueResponseModel();
        responseModel.error = UseCaseResponseErrorMessages.notEnoughPartsForCombo;
        responseModel.comboTechniques = new ArrayList<>();
        assertEquals(responseModel,presenterSpy.responseModel);
    }

    @Test
    public void createComboTechniqueFromEmptyNameReturnError(){
        useCase.createNewComboTechnique(presenterSpy,"");

        ComboTechniqueResponseModel responseModel = new ComboTechniqueResponseModel();
        responseModel.error = UseCaseResponseErrorMessages.emptyComboTechniqueName;
        responseModel.comboTechniques = new ArrayList<>();
        assertEquals(responseModel,presenterSpy.responseModel);
    }

    @Test
    public void tryCreateComboTechniqueWithSameNameRetrunError(){
        String comboTechniqueName = "Name";

        setUpUseCaseWithTwoComboParts();
        useCase.createNewComboTechnique(presenterSpy,comboTechniqueName);
        setUpUseCaseWithTwoComboParts();
        useCase.createNewComboTechnique(presenterSpy,comboTechniqueName);

        ComboTechniqueResponseModel responseModel = new ComboTechniqueResponseModel();
        responseModel.error = UseCaseResponseErrorMessages.comboTechniqueNameExists;
        responseModel.comboTechniques = new ArrayList<>();
        responseModel.comboTechniques.add(comboTechniqueName);
        assertEquals(responseModel,presenterSpy.responseModel);
    }

    @Test
    public void createComboTechniqueWithDifferentNameSamePartsNoError(){
        setUpUseCaseWithTwoComboParts();
        useCase.createNewComboTechnique(presenterSpy,"Name");
        setUpUseCaseWithTwoComboParts();
        useCase.createNewComboTechnique(presenterSpy,"Name2");

        ComboTechniqueResponseModel responseModel = new ComboTechniqueResponseModel();
        responseModel.error = "";
        responseModel.comboTechniques = new ArrayList<>();
        responseModel.comboTechniques.add("Name");
        responseModel.comboTechniques.add("Name2");
        assertEquals(responseModel,presenterSpy.responseModel);
    }

    @Test
    public void createComboTechniqueAndClearComboParts(){
        List<String> comboParts = setUpUseCaseWithTwoComboParts();

        String comboTechniqueName = "Name";
        useCase.createNewComboTechnique(presenterSpy, comboTechniqueName);

        ComboTechniqueResponseModel responseModel = new ComboTechniqueResponseModel();
        responseModel.error = "";
        responseModel.comboTechniques = new ArrayList<>();
        responseModel.comboTechniques.add(comboTechniqueName);

        assertEquals(responseModel,presenterSpy.responseModel);
        assertEquals(new ArrayList<>(),presenterSpy.comboParts);

        assertNotNull(techniqueFactory.getComboTechnique("Name"));
        List<PreprocessingTechnique> techniqueParts = techniqueFactory.getComboTechniqueParts("Name");
        assertEquals(NoTechniqueOriginal.NAME,techniqueParts.get(0).getName());
        assertEquals(SherlockNoCommentsPPTechnique.NAME,techniqueParts.get(1).getName());
    }

    @Test
    public void callsComboTechniqueLoaderAndPresenterToLoadComboTechniquesFromFile(){
        ComboTechiniquesLoader loader = new ComboTechiniquesLoader(techniqueFactory){
            @Override
            public void loadMultipleTechniquesFromFile(File comboTechniquesFile) {
                techniqueFactory.createComboTechnique(comboTechniquesFile.getName(),new LinkedHashSet<>());
            }
        };

        useCase.setComboTechniqueLoader(loader);
        String inputFile = "comboTechniquesFile";
        useCase.loadComboTehniquesFromFile(presenterSpy, inputFile);
        assertEquals(inputFile,presenterSpy.responseModel.comboTechniques.get(0));
    }

    @Test
    public void useCaseClearsSelectedTehcniques(){
        useCase.clearSelectedTechniques(presenterSpy);

        assertTrue(presenterSpy.clearSelectedTechniquesCalled);
    }

    @Test
    public void useCaseReturnsAvailableTechniquesOnGetAvailableTechniques(){
        AvailableTechniquesResponseModel availableTTResponseModel = new AvailableTechniquesResponseModel();
        availableTTResponseModel.techniques = new PreprocessingTechniqueFactory().getAvailableTools();

        useCase.getAvailableTechniques(presenterSpy);

        assertEquals(availableTTResponseModel,presenterSpy.availableTTResponseModel);
    }

    @Test
    public void useCaseReturnsErrorIfWorkingDirDoesNotExistOnLoadExistingTechniques(){
        useCase.getProcessedTechniques(presenterSpy, null);
        assertEquals(UseCaseResponseErrorMessages.invalidWorkingDir,presenterSpy.availableTTResponseModel.errorMessage);
        assertEquals(new ArrayList<>(), presenterSpy.availableTTResponseModel.techniques);

        File detectionDir = new File(workingDir+File.separator+MPCContext.DETECTION_DIR);
        useCase.getProcessedTechniques(presenterSpy, detectionDir.getParentFile());
        assertEquals(UseCaseResponseErrorMessages.invalidWorkingDir,presenterSpy.availableTTResponseModel.errorMessage);
        assertEquals(new ArrayList<>(), presenterSpy.availableTTResponseModel.techniques);
    }

    @Test
    public void useCaseReturnsErrorIfProcessDirDoesNotExistOnLoadExistingTechniques(){
        File preprocessDir = new File(workingDir+File.separator+MPCContext.PREPROCESS_DIR);
        preprocessDir.getParentFile().mkdirs();
        useCase.getProcessedTechniques(presenterSpy, preprocessDir.getParentFile());
        assertEquals(UseCaseResponseErrorMessages.invalidPreprocessDir,presenterSpy.availableTTResponseModel.errorMessage);
        assertEquals(new ArrayList<>(), presenterSpy.availableTTResponseModel.techniques);
    }

    @Test
    public void useCaseReturnsAvailableToolsAndTechniquesOnGetAvailableTools(){
        File preprocessedDir = new File(workingDir+File.separator+MPCContext.PREPROCESS_DIR);
        preprocessedDir.mkdirs();
        File tool1 = new File(preprocessedDir+File.separator+"tool1");
        File tool2 = new File(preprocessedDir+File.separator+"tool2");
        File tool3 = new File(preprocessedDir+File.separator+"tool3");
        tool1.mkdirs();
        tool2.mkdirs();
        tool3.mkdirs();

        AvailableTechniquesResponseModel availableTTResponseModel = new AvailableTechniquesResponseModel();
        availableTTResponseModel.techniques = new ArrayList<>();
        availableTTResponseModel.techniques.add(tool1.getName());
        availableTTResponseModel.techniques.add(tool3.getName());
        availableTTResponseModel.techniques.add(tool2.getName());
        availableTTResponseModel.errorMessage = "";

        useCase.getProcessedTechniques(presenterSpy, preprocessedDir.getParentFile());

        assertEquals(availableTTResponseModel,presenterSpy.availableTTResponseModel);
    }

    private List<String> setUpUseCaseWithTwoComboParts() {
        List<String> comboParts = new ArrayList<>();
        comboParts.add(NoTechniqueOriginal.NAME);
        comboParts.add(SherlockNoCommentsPPTechnique.NAME);
        useCase.addTechniquesToComboParts(presenterSpy, comboParts);
        return comboParts;
    }

    private class ComboTechniqueOutputBoundarySpy implements ComboTechniqueOutputBoundary {
        public List<String> comboParts;
        public ComboTechniqueResponseModel responseModel;
        boolean clearSelectedTechniquesCalled = false;
        AvailableTechniquesResponseModel availableTTResponseModel;

        @Override
        public void presentComboParts(List<String> comboParts) {
            this.comboParts = comboParts;
        }

        @Override
        public void presentComboTechniques(ComboTechniqueResponseModel responseModel) {
            this.responseModel = responseModel;
        }

        @Override
        public void clearSelectedTechniques() {
            clearSelectedTechniquesCalled = true;
        }

        @Override
        public void presentAvailableTechniques(AvailableTechniquesResponseModel responseModel) {
            this.availableTTResponseModel = responseModel;
        }
    }
}