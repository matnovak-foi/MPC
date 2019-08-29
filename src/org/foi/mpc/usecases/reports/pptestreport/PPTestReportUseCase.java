package org.foi.mpc.usecases.reports.pptestreport;

import org.foi.mpc.abstractfactories.ExecutionToolFactory;
import org.foi.mpc.abstractfactories.FactoryProvider;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.mpc.phases.executionphases.ExecutionTool;
import org.foi.mpc.phases.readerphase.MPCMatchListener;
import org.foi.mpc.phases.runner.PhaseRunner;
import org.foi.mpc.phases.runner.PhaseRunnerBuilder;
import org.foi.mpc.usecases.UseCaseResponseErrorMessages;
import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.usecases.multipleDetecion.DirectoryPreparer;
import org.foi.mpc.usecases.reports.avalibleTools.models.AvailableToolsResponseModel;
import org.foi.mpc.usecases.reports.pptestreport.models.PPTestReportRequestModel;
import org.foi.mpc.usecases.reports.pptestreport.models.PPTestReportResponseModel;
import org.foi.mpc.usecases.reports.pptestreport.models.PPTestReportResponseModel.PPTestReportTechnique;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PPTestReportUseCase implements PPTestReportInputBoundary, MPCMatchListener {
    private FactoryProvider factoryProvider;
    private ExecutionToolFactory toolFactory;
    private ExecutionToolFactory techniqueFactory;
    private ExecutionTool tool;

    private PPTestReportResponseModel responseModel;
    private PPTestReportRequestModel requestModel;
    private DirectoryPreparer directoryPreparer = new DirectoryPreparer();


    public PPTestReportUseCase(FactoryProvider factoryProvider, String matchesDir) {
        this.factoryProvider = factoryProvider;
        techniqueFactory = factoryProvider.getTechniqueFactory();
        toolFactory = factoryProvider.getToolFactory();
    }

    public FactoryProvider getProvider() {
        return factoryProvider;
    }

    @Override
    public void getAvailableTools(PPTestReportOutputBoundary outputBoundary) {
        AvailableToolsResponseModel responseModel = new AvailableToolsResponseModel();
        responseModel.tools = toolFactory.getAvailableTools();
        outputBoundary.presentAvailableTools(responseModel);
    }

    public void generateReport(PPTestReportRequestModel requestModel, PPTestReportOutputBoundary presenter) {
        this.requestModel = requestModel;

        createEmptyResponseModel();

        if (isRequestMissingData(requestModel)) {
            presenter.presentReport(responseModel);
            return;
        }

        runAllDetections();

        responseModel.toolName = requestModel.selectedTools.get(0);
        presenter.presentReport(responseModel);
    }

    private void createEmptyResponseModel() {
        responseModel = new PPTestReportResponseModel();
        responseModel.errorMessages = "";
        responseModel.ppReportTable = new ArrayList<>();
    }

    private void runAllDetections() {
        List<ExecutionTool> toolList = getDetectionTools(requestModel);
        List<File> dirsToProcess = directoryPreparer.createDirsToPorces(requestModel.selectedInputDir,0);
        List<ExecutionTool> techniqueList = getTechniques(requestModel);

        PhaseRunner phaseRunner = new PhaseRunnerBuilder(factoryProvider.getPhaseFactory()).
                withSourceDir(requestModel.selectedInputDir).
                withWorkingDir(requestModel.selectedWorking).
                forToolList(toolList).
                forTechiqueList(techniqueList).
                toProcessDirs(dirsToProcess).
                withAllPhasesEnabled().
                withMatchReadListener(this).
                build();

        phaseRunner.runPhases();
    }

    //TODO is repeated in summary report useCase maybe extract it somehow
    private boolean isRequestMissingData(PPTestReportRequestModel requestModel) {
        if(!requestModel.selectedWorking.exists()){
            responseModel.errorMessages = UseCaseResponseErrorMessages.invalidWorkingDir;
            return true;
        }

        if(!requestModel.selectedInputDir.exists()){
            responseModel.errorMessages = UseCaseResponseErrorMessages.invalidSourceDir;
            return true;
        }

        if(requestModel.selectedTools.isEmpty()){
            responseModel.errorMessages = UseCaseResponseErrorMessages.noToolIsSelected;
            return true;
        } else if(requestModel.selectedTools.size() != 1){
            responseModel.errorMessages = UseCaseResponseErrorMessages.toManyToolsSelected;
            return true;
        }

        if(requestModel.selectedTechniques.isEmpty()){
            responseModel.errorMessages = UseCaseResponseErrorMessages.noTechniqueIsSelected;
            return true;
        }

        return false;
    }

    private List<ExecutionTool> getDetectionTools(PPTestReportRequestModel requestModel) {
        List<ExecutionTool> toolList = new ArrayList<>();
        tool = toolFactory.createTool(requestModel.selectedTools.get(0));
        toolList.add(tool);
        return toolList;
    }

    private List<ExecutionTool> getTechniques(PPTestReportRequestModel requestModel) {
        List<ExecutionTool> techniqueList = new ArrayList<>();
        for (String techniqueNameToExecute : requestModel.selectedTechniques) {
            ExecutionTool technique = techniqueFactory.createTool(techniqueNameToExecute);
            techniqueList.add(technique);
        }
        return techniqueList;
    }

    @Override
    public void processMatch(MPCMatch match) {
        responseModel.usernameA = DirectoryFileUtility.getFileNameWithoutExtension(match.fileAName);
        responseModel.usernameB = DirectoryFileUtility.getFileNameWithoutExtension(match.fileBName);

        PPTestReportTechnique reportTechnique = new PPTestReportTechnique();
        reportTechnique.name = getTechniqueName(match);

        reportTechnique.similarityA = match.similarityA;
        reportTechnique.similarityB = match.similarityB;
        reportTechnique.similarity = match.similarity;
        responseModel.ppReportTable.add(reportTechnique);
    }

    private String getTechniqueName(MPCMatch match) {
        for (String techniqueNameToExecute : requestModel.selectedTechniques) {
            if (match.matchesDir.getPath().startsWith(techniqueNameToExecute+File.separator)
                    || match.matchesDir.getPath().contains(File.separator+techniqueNameToExecute+File.separator) ) {
                return techniqueNameToExecute;
            }
        }

        return UseCaseResponseErrorMessages.noSuchTechnique;
    }

    public void setDirectoryPreparer(DirectoryPreparer directoryPreparer) {
        this.directoryPreparer = directoryPreparer;
    }
}
