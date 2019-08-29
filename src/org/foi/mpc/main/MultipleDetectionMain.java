package org.foi.mpc.main;

import org.foi.mpc.MPCContext;
import org.foi.mpc.abstractfactories.FactoryProvider;
import org.foi.mpc.executiontools.factories.PreprocessingTechniqueFactory;
import org.foi.mpc.usecases.combotechnique.ComboTechniqueOutputBoundary;
import org.foi.mpc.usecases.combotechnique.ComboTechniqueUseCase;
import org.foi.mpc.usecases.combotechnique.models.AvailableTechniquesResponseModel;
import org.foi.mpc.usecases.combotechnique.models.ComboTechniqueResponseModel;
import org.foi.mpc.usecases.multipleDetecion.MultipleDetectionInputBoundary;
import org.foi.mpc.usecases.multipleDetecion.MultipleDetectionOutputBoundary;
import org.foi.mpc.usecases.multipleDetecion.MultipleDetectionUseCase;
import org.foi.mpc.usecases.multipleDetecion.models.MultipleDetectionRequestModel;
import org.foi.mpc.usecases.multipleDetecion.models.MultipleDetectionResponseModel;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class MultipleDetectionMain implements MultipleDetectionOutputBoundary {
    private MultipleDetectionOutputBoundary presenter;
    private MultipleDetectionInputBoundary useCase;
    private MultipleDetectionConfiguration configuration;
    private ComboTechniqueUseCase comboUseCase;
    private FactoryProvider factoryProvider;

    public static void main(String[] args){
        MultipleDetectionMain multipleDetectionMain = new MultipleDetectionMain();
        multipleDetectionMain.run(args);
    }

    public MultipleDetectionMain() {
        factoryProvider = new FactoryProvider(MPCContext.MATCHES_DIR);
        useCase = new MultipleDetectionUseCase();
        presenter = this;
    }

    public void run(String[] args){
        if(args.length != 1 || !args[0].endsWith(".properties")) {
            String input = "";
            if(args.length>0){
                input = args[0];
            }
            System.out.println("Wrong input parameters!"+input);
            return;
        }

        MultipleDetectionConfigReader configReader = new MultipleDetectionConfigReader();
        configuration = configReader.read(new File(args[0]));

        factoryProvider.createPreprocessingTechniqueFactory(configuration.templateExclusionConfigFile);
        factoryProvider.createSimilarityDetectionToolFactory(configuration.detectionToolsConfigFile);

        comboUseCase = new ComboTechniqueUseCase((PreprocessingTechniqueFactory) factoryProvider.getTechniqueFactory());
        comboUseCase.loadComboTehniquesFromFile(new ComboTechniquePresenterDummy(),configuration.comboTechniquesConfigFile);

        MultipleDetectionRequestModel requestModel = creteRequestModel(configuration);
        useCase.setUpFactories(factoryProvider);
        useCase.runMultipleDetecion(requestModel,presenter);
    }

    public String getPresentedText() {
        return "";
    }

    @Override
    public void presentResults(MultipleDetectionResponseModel responseModel) {
        System.out.println("MULTIPLE DETECION DONE");
        System.out.println(responseModel.errorMessages);
        for (File dir : responseModel.outputDirs)
            System.out.println(dir.getAbsolutePath());
    }

    public void setUseCase(MultipleDetectionInputBoundary useCase) {
        this.useCase = useCase;
    }

    public MultipleDetectionConfiguration getConfiguration() {
        return configuration;
    }

    static MultipleDetectionRequestModel creteRequestModel(MultipleDetectionConfiguration configuration) {
        MultipleDetectionRequestModel requestModel = new MultipleDetectionRequestModel();

        requestModel.inputDirDepth = configuration.inputDirDepth;
        requestModel.selectedInputDir = new File(configuration.selectedInputDir);
        requestModel.selectedWorkingDir = new File(configuration.selectedWorkingDir);
        requestModel.selectedTools = Arrays.asList(configuration.selectedTool.split(","));
        requestModel.selectedTechniques = Arrays.asList(configuration.selectedTechniques.split(","));

        return requestModel;
    }

    public ComboTechniqueUseCase getComboUseCase() {
        return comboUseCase;
    }

    public FactoryProvider getFactoryProvider() {
        return factoryProvider;
    }

    private class ComboTechniquePresenterDummy implements ComboTechniqueOutputBoundary {

        @Override
        public void presentComboParts(List<String> comboParts) {

        }

        @Override
        public void presentComboTechniques(ComboTechniqueResponseModel responseModel) {

        }

        @Override
        public void clearSelectedTechniques() {

        }

        @Override
        public void presentAvailableTechniques(AvailableTechniquesResponseModel responseModel) {

        }
    }
}
