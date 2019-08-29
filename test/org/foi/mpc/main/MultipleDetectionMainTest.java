package org.foi.mpc.main;

import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.mpc.TestContext;
import org.foi.mpc.abstractfactories.ExecutionToolFactory;
import org.foi.mpc.abstractfactories.FactoryProvider;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagJavaAdapter;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneJavaAdapter;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneTextAdapter;
import org.foi.mpc.executiontools.factories.ComboTechiniquesLoader;
import org.foi.mpc.executiontools.factories.PreprocessingTechniqueFactory;
import org.foi.mpc.executiontools.factories.SimilarityDetectionToolFactory;
import org.foi.mpc.executiontools.techniques.CommonCodeRemoveTechnique;
import org.foi.mpc.executiontools.techniques.NoTechniqueOriginal;
import org.foi.mpc.phases.executionphases.ExecutionTool;
import org.foi.mpc.usecases.combotechnique.ComboTechniqueUseCase;
import org.foi.mpc.usecases.multipleDetecion.MultipleDetectionInputBoundary;
import org.foi.mpc.usecases.multipleDetecion.MultipleDetectionOutputBoundary;
import org.foi.mpc.usecases.multipleDetecion.models.MultipleDetectionRequestModel;
import org.foi.mpc.usecases.multipleDetecion.models.MultipleDetectionResponseModel;
import org.foi.mpc.usecases.reports.pptestreport.PPTestReportUseCase;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import static junit.framework.TestCase.assertSame;
import static org.foi.mpc.main.MultipleDetectionMain.creteRequestModel;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.endsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MultipleDetectionMainTest implements MultipleDetectionInputBoundary {
    MultipleDetectionMain main;
    private String[] args = {"testPropertiesFile.properties"};
    private File testPropertiesFile = new File("testPropertiesFile.properties");
    private MultipleDetectionRequestModel useCaseRequestModel;
    private MultipleDetectionOutputBoundary useCasePresenter;
    private MultipleDetectionConfiguration configuration;
    private MultipleDetectionResponseModel useCaseResponseModel;
    private SimilarityDetectionToolFactory toolFactory;
    private PreprocessingTechniqueFactory techniqueFactory;

    @Before
    public void setUp() {
        configuration = createTestConfiguration();
        MultipleDetectionConfigReader reader = new MultipleDetectionConfigReader();
        reader.write(testPropertiesFile, configuration);

        main = new MultipleDetectionMain();
        main.setUseCase(this);
        useCaseResponseModel = new MultipleDetectionResponseModel();
    }

    @After
    public void tearDown() throws IOException {
        testPropertiesFile.delete();
    }

    @Test
    public void ifWrongParamsAreGivenReturnError() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String[] args = {};
        main.run(args);

        String[] args2 = {"file.notproperties"};
        main.run(args2);

        assertEquals("Wrong input parameters!\n" +
                "Wrong input parameters!file.notproperties", outContent.toString().trim());
    }

    @Test
    public void canReadPropertiesFile() {
        main.run(args);

        assertEquals(configuration, main.getConfiguration());
    }

    @Test
    public void passesCorrectRequestModelToUseCase() {
        main.run(args);

        assertNotNull(useCaseRequestModel);
        assertEquals(creteRequestModel(configuration), useCaseRequestModel);
    }

    @Test
    public void passesItselfAsPresenterToUseCase() {
        main.run(args);
        assertNotNull(useCasePresenter);
        assertSame(main, useCasePresenter);
    }

    @Test
    public void printsDetectionOutputDirs() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        useCaseResponseModel.outputDirs = new ArrayList<>();
        useCaseResponseModel.outputDirs.add(new File("testDir1"));
        useCaseResponseModel.outputDirs.add(new File("testDir2"));
        useCaseResponseModel.errorMessages = "errorMessage";

        main.run(args);

        assertThat(outContent.toString().trim(),endsWith("MULTIPLE DETECION DONE\nerrorMessage\n" +
                System.getProperty("user.dir")+File.separator+"testDir1\n" +
                System.getProperty("user.dir")+File.separator+"testDir2"));
    }

    @Test
    public void loadsComboTechniquesFromFile(){
        main.run(args);

        ComboTechniqueUseCase useCase = main.getComboUseCase();
        ComboTechiniquesLoader comboLoader = useCase.getComboTechniqueLoader();

        assertEquals(TestContext.doktorskiRadDir+"MPC"+File.separator+"comboTehniques.properties",comboLoader.getComboTechniqueLoadedFile().getPath());
    }

    @Override
    public void setUpFactories(FactoryProvider factoryProvider) {
        toolFactory = (SimilarityDetectionToolFactory) factoryProvider.getToolFactory();
        techniqueFactory = (PreprocessingTechniqueFactory) factoryProvider.getTechniqueFactory();
    }

    @Override
    public void runMultipleDetecion(MultipleDetectionRequestModel requestModel, MultipleDetectionOutputBoundary presenter) {
        useCaseRequestModel = requestModel;
        useCasePresenter = presenter;
        presenter.presentResults(useCaseResponseModel);
    }

    @Test
    public void passesCorrectConfigurationToTemplateExclusionTechnique(){
        main.run(args);

        File file =  techniqueFactory.getTemplateExclusionConfigFile();
        assertEquals(TestContext.doktorskiRadDir+"MPC"+File.separator+"templateExclusionConfiguration.properties",file.getPath());
    }

    @Test
    public void passesCorrectConfigurationFileToSimilarityDetectionTool(){
        main.run(args);

        File file = toolFactory.getDetectionToolConfigFileName();
        assertEquals(TestContext.doktorskiRadDir+"MPC"+File.separator+"detectionToolsConfiguration.properties",file.getPath());
    }

    public static MultipleDetectionConfiguration createTestConfiguration() {
        MultipleDetectionConfiguration configuration = new MultipleDetectionConfiguration();
        configuration.inputDirDepth = 3;
        configuration.selectedInputDir = "testInputData" + File.separator + "multipleDetectionTest";
        configuration.selectedWorkingDir = "workingDir";
        configuration.selectedTechniques = NoTechniqueOriginal.NAME + "," + CommonCodeRemoveTechnique.NAME;
        configuration.selectedTool = JPlagJavaAdapter.NAME + "," + SimGruneJavaAdapter.NAME + "," + SimGruneTextAdapter.NAME;
        configuration.comboTechniquesConfigFile = TestContext.doktorskiRadDir+"MPC"+File.separator+"comboTehniques.properties";
        configuration.templateExclusionConfigFile = TestContext.doktorskiRadDir+"MPC"+File.separator+"templateExclusionConfiguration.properties";
        configuration.detectionToolsConfigFile = TestContext.doktorskiRadDir+"MPC"+File.separator+"detectionToolsConfiguration.properties";
        return configuration;
    }
}
