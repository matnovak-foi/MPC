package org.foi.mpc.usecases.multipleDetecion;

import org.foi.mpc.MPCContext;
import org.foi.mpc.abstractfactories.ExecutionToolFactory;
import org.foi.mpc.abstractfactories.FactoryProvider;
import org.foi.mpc.phases.executionphases.ExecutionTool;
import org.foi.mpc.phases.runner.PhaseRunner;
import org.foi.mpc.phases.runner.PhaseRunnerBuilder;
import org.foi.mpc.usecases.UseCaseResponseErrorMessages;
import org.foi.mpc.usecases.multipleDetecion.models.MultipleDetectionRequestModel;
import org.foi.mpc.usecases.multipleDetecion.models.MultipleDetectionResponseModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MultipleDetectionUseCase implements MultipleDetectionInputBoundary {
    private DirectoryPreparer directoryPreparer = new DirectoryPreparer();
    private MultipleDetectionResponseModel responseModel;
    private PhaseRunnerBuilder phaseRunnerBuilder;
    private ExecutionToolFactory techniqueFactory;
    private ExecutionToolFactory toolFactory;

    public MultipleDetectionUseCase() {
        phaseRunnerBuilder = new PhaseRunnerBuilder(MPCContext.MATCHES_DIR);
    }

    @Override
    public void runMultipleDetecion(MultipleDetectionRequestModel requestModel, MultipleDetectionOutputBoundary presenter) {
        createEmptyResponseModel();

        if(isRequestDataMissing(requestModel)){
            presenter.presentResults(responseModel);
            return;
        }

        runAllDetections(requestModel);

        presenter.presentResults(responseModel);
    }

    private void createEmptyResponseModel() {
        responseModel = new MultipleDetectionResponseModel();
        responseModel.errorMessages = "";
        responseModel.outputDirs = new ArrayList<>();
    }

    private void runAllDetections(MultipleDetectionRequestModel requestModel) {
        List<ExecutionTool> tools = createTools(toolFactory,requestModel.selectedTools);
        List<ExecutionTool> techniqueList = createTools(techniqueFactory,requestModel.selectedTechniques);
        List<File> dirsToProcess = directoryPreparer.createDirsToPorces(requestModel.selectedInputDir, requestModel.inputDirDepth);

        PhaseRunner phaseRunner = phaseRunnerBuilder
                .withWorkingDir(requestModel.selectedWorkingDir)
                .withSourceDir(requestModel.selectedInputDir)
                .forToolList(tools)
                .forTechiqueList(techniqueList)
                .toProcessDirs(dirsToProcess)
                .withMatchReadPhaseDisabled()
                .build();

        phaseRunner.runPhases();

        responseModel.outputDirs = phaseRunner.getSettings().detectionOutputDirs;
    }

    List<File> createDirsToPorces(File selectedInputDir, int inputDirDepth) {

        return directoryPreparer.createDirsToPorces(selectedInputDir, inputDirDepth);
    }

    private List<ExecutionTool> createTools(ExecutionToolFactory factory, List<String> toolNames) {
        List<ExecutionTool> tools = new ArrayList<>();
        for(String toolName : toolNames){
            ExecutionTool tool = factory.createTool(toolName);
            tools.add(tool);
        }
        return tools;
    }

    private boolean isRequestDataMissing(MultipleDetectionRequestModel requestModel) {
        if(!requestModel.selectedWorkingDir.exists()){
            responseModel.errorMessages = UseCaseResponseErrorMessages.invalidWorkingDir;
            return true;
        }
        if(!requestModel.selectedInputDir.exists()) {
            responseModel.errorMessages = UseCaseResponseErrorMessages.invalidSourceDir;
            return true;
        }
        if(requestModel.selectedTools.isEmpty()){
            responseModel.errorMessages = UseCaseResponseErrorMessages.noToolIsSelected;
            return true;
        }
        if(requestModel.selectedTechniques.isEmpty()){
            responseModel.errorMessages = UseCaseResponseErrorMessages.noTechniqueIsSelected;
            return true;
        }
        if(requestModel.inputDirDepth < 0){
            responseModel.errorMessages = UseCaseResponseErrorMessages.inputDirDepth;
            return true;
        }
        return false;
    }


    public void setPhaseRunnerBuilder(PhaseRunnerBuilder phaseRunnerBuilder) {
        this.phaseRunnerBuilder = phaseRunnerBuilder;
    }

    public void setUpFactories(FactoryProvider factoryProvider){
        techniqueFactory = factoryProvider.getTechniqueFactory();
        toolFactory = factoryProvider.getToolFactory();
    }
}
