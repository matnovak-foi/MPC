package org.foi.mpc.usecases.reports.summaryReport;

import org.apache.commons.math3.stat.StatUtils;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.mpc.abstractfactories.ExecutionToolFactory;
import org.foi.mpc.abstractfactories.FactoryProvider;
import org.foi.mpc.matches.PlagMatchesReader;
import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.phases.executionphases.ExecutionTool;
import org.foi.mpc.phases.readerphase.MPCMatchListener;
import org.foi.mpc.phases.runner.PhaseRunner;
import org.foi.mpc.phases.runner.PhaseRunnerBuilder;
import org.foi.mpc.usecases.UseCaseResponseErrorMessages;
import org.foi.mpc.usecases.multipleDetecion.DirectoryPreparer;
import org.foi.mpc.usecases.reports.avalibleTools.AvalibleToolsUseCase;
import org.foi.mpc.usecases.reports.avalibleTools.models.AvailableToolsResponseModel;
import org.foi.mpc.usecases.reports.statisticsReport.models.SimilarityDescriptiveStatistics;
import org.foi.mpc.usecases.reports.summaryReport.models.SummaryReportRequestModel;
import org.foi.mpc.usecases.reports.summaryReport.models.SummaryReportResponseModel;
import org.foi.mpc.usecases.reports.summaryReport.models.SummaryReportResponseModel.SummaryReportTableRow;
import org.foi.mpc.usecases.reports.summaryReport.models.UpdatePlagInfoRequestModel;
import org.foi.mpc.usecases.reports.summaryReport.models.UpdatePlagInfoResponseModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SummaryReportUseCase implements SummaryReportInputBoundary{
    private DirectoryPreparer directoryPreparer = new DirectoryPreparer();
    private FactoryProvider factoryProvider;
    private ExecutionToolFactory toolFactory;
    private ExecutionToolFactory techniqueFactory;

    private PlagMatchesReader plagMatchesReader;

    public SummaryReportUseCase(FactoryProvider factoryProvider) {
        this.factoryProvider = factoryProvider;
        techniqueFactory = factoryProvider.getTechniqueFactory();
        toolFactory = factoryProvider.getToolFactory();
    }

    public void setDirectoryPreparer(DirectoryPreparer directoryPreparer) {
        this.directoryPreparer = directoryPreparer;
    }

    public FactoryProvider getProvider() {
        return factoryProvider;
    }

    @Override
    public void loadDirectoryList(File inputDir, int inputDirDepth, SummaryReportOutputBoundary presenter) {
        List<File> dirs = directoryPreparer.createDirsToPorces(inputDir,inputDirDepth);
        Map<String, File> response = directoryPreparer.createMapForDisplay(inputDir, dirs);

        presenter.presentDirList(response);
    }

    @Override
    public void updatePlagInfo(UpdatePlagInfoRequestModel requestModel, SummaryReportOutputBoundary presenter) {
        UpdatePlagInfo plagInfo = new UpdatePlagInfo();
        plagInfo.updatePlagInfo(requestModel,presenter);
    }

    @Override
    public void getAvailableTools(SummaryReportOutputBoundary outputBoundary) {
        AvailableToolsResponseModel responseModel = new AvailableToolsResponseModel();
        responseModel.tools = toolFactory.getAvailableTools();
        outputBoundary.presentAvailableTools(responseModel);
    }

    @Override
    public void getAvailableTools(SummaryReportOutputBoundary outputBoundary, File workingDir) {
        AvailableToolsResponseModel responseModel = AvalibleToolsUseCase.getAvailableTools(workingDir);
        outputBoundary.presentAvailableTools(responseModel);
    }

    @Override
    public void generateReport(SummaryReportRequestModel requestModel, SummaryReportOutputBoundary presenter) {
        SummaryReportInfo summaryReportInfo = new SummaryReportInfo();
        summaryReportInfo.generateReport(requestModel,presenter);
    }

    private class SummaryReportInfo implements MPCMatchListener  {
        private SummaryReportResponseModel responseModel;
        private ExecutionTool tool;
        private ExecutionTool technique;

        public void generateReport(SummaryReportRequestModel requestModel, SummaryReportOutputBoundary presenter) {
            createEmptyResponseModel();

            if (isRequestMissingData(requestModel)) {
                presenter.presentReport(responseModel);
                return;
            }

            plagMatchesReader = PlagMatchesReader.createPlagMatchReader(requestModel.sourceDirPath,requestModel.selectedWorking,requestModel.selectedInputDir);

            runAllDetections(requestModel);
            calculateStatistics();

            responseModel.techniqueName = requestModel.selectedTechniques.get(0);
            responseModel.toolName = requestModel.selectedTools.get(0);
            presenter.presentReport(responseModel);
        }

        private void calculateStatistics() {
            responseModel.similarityStatistics = new SimilarityDescriptiveStatistics();
            double[] data = new double[responseModel.reportTable.size()];
            for(int i = 0; i< responseModel.reportTable.size(); i++){
                data[i] = (Double.valueOf(responseModel.reportTable.get(i).similarity));
            }

            //Arrays.sort(data);
            //System.out.println(Arrays.toString(data));
            responseModel.similarityStatistics.mean = StatUtils.mean(data);
            responseModel.similarityStatistics.mode = StatUtils.mode(data);
            responseModel.similarityStatistics.max = StatUtils.max(data);
            responseModel.similarityStatistics.min = StatUtils.min(data);
            responseModel.similarityStatistics.median =  StatUtils.percentile(data,50);
            responseModel.similarityStatistics.q1 =  StatUtils.percentile(data,25);
            responseModel.similarityStatistics.q3 =  StatUtils.percentile(data,75);
            responseModel.similarityStatistics.percentile99 =  StatUtils.percentile(data,99);
            responseModel.similarityStatistics.IRQ =  responseModel.similarityStatistics.q3 - responseModel.similarityStatistics.q1;
            responseModel.similarityStatistics.STD =  Math.sqrt(StatUtils.populationVariance(data));
        }

        private void createEmptyResponseModel() {
            responseModel = new SummaryReportResponseModel();
            responseModel.errorMessages = "";
            responseModel.reportTable = new ArrayList<>();
        }

        private void runAllDetections(SummaryReportRequestModel requestModel) {
            List<ExecutionTool> toolList = getDetectionTools(requestModel);
            List<File> dirsToProcess = directoryPreparer.createDirsToPorces(requestModel.selectedInputDir,0);
            List<ExecutionTool> techniqueList = getTechniques(requestModel);

            PhaseRunner phaseRunner = new PhaseRunnerBuilder(factoryProvider.getPhaseFactory()).
                    withSourceDir(requestModel.sourceDirPath).
                    withWorkingDir(requestModel.selectedWorking).
                    forToolList(toolList).
                    forTechiqueList(techniqueList).
                    toProcessDirs(dirsToProcess).
                    withAllPhasesEnabled().
                    withMatchReadListener(this).
                    build();

            phaseRunner.runPhases();
        }

        private boolean isRequestMissingData(SummaryReportRequestModel requestModel) {
            if(!requestModel.selectedWorking.exists()){
                responseModel.errorMessages = UseCaseResponseErrorMessages.invalidWorkingDir;
                return true;
            }

            if(requestModel.selectedInputDirDepth < 0){
                responseModel.errorMessages = UseCaseResponseErrorMessages.inputDirDepth;
                return true;
            }

            if(!requestModel.selectedInputDir.exists()){
                responseModel.errorMessages = UseCaseResponseErrorMessages.invalidSourceDir;
                return true;
            }

            if(!requestModel.selectedInputDir.getPath().contains(requestModel.sourceDirPath.getPath())){
                responseModel.errorMessages = UseCaseResponseErrorMessages.inputDirMismatch;
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
            } else if(requestModel.selectedTechniques.size() != 1){
                responseModel.errorMessages = UseCaseResponseErrorMessages.toManyTechniquesSelected;
                return true;
            }

            return false;
        }

        private List<ExecutionTool> getDetectionTools(SummaryReportRequestModel requestModel) {
            List<ExecutionTool> toolList = new ArrayList<>();
            tool = toolFactory.createTool(requestModel.selectedTools.get(0));
            toolList.add(tool);
            return toolList;
        }

        private List<ExecutionTool> getTechniques(SummaryReportRequestModel requestModel) {
            List<ExecutionTool> techniqueList = new ArrayList<>();

            technique = techniqueFactory.createTool(requestModel.selectedTechniques.get(0));
            techniqueList.add(technique);

            return techniqueList;
        }

        @Override
        public void processMatch(MPCMatch match) {
            checkIfExecutionToolNameIsOk(match,technique.getName());
            checkIfExecutionToolNameIsOk(match,tool.getName());

            SummaryReportTableRow reportPair = new SummaryReportTableRow();
            reportPair.studentA = DirectoryFileUtility.getFileNameWithoutExtension(match.fileAName);
            reportPair.studentB = DirectoryFileUtility.getFileNameWithoutExtension(match.fileBName);


            reportPair.fileAName = match.fileAName;
            reportPair.fileBName = match.fileBName;

            reportPair.similarityA = match.similarityA;
            reportPair.similarityB = match.similarityB;
            reportPair.similarity = match.similarity;

            reportPair.calculatedSimilarity = match.calculatedSimilarity;
            reportPair.calculatedSimilarityA = match.calculatedSimilarityA;
            reportPair.calculatedSimilarityB = match.calculatedSimilarityB;
            reportPair.plagiarized = plagMatchesReader.containsPlagPair(match.fileAName,match.fileBName);
            reportPair.processed = plagMatchesReader.containsPair(match.fileAName,match.fileBName);

            if(responseModel.matchesDirPath == null)
                responseModel.matchesDirPath = match.matchesDir;

            responseModel.reportTable.add(reportPair);
        }

        private void checkIfExecutionToolNameIsOk(MPCMatch match, String name) {
            if (match.matchesDir.getPath().startsWith(name+File.separator)
                    || match.matchesDir.getPath().contains(File.separator+name+File.separator) ) {
                return;
            }

            responseModel.errorMessages = UseCaseResponseErrorMessages.noSuchTechnique+":"+match.matchesDir.getPath();
        }
    }

    private class UpdatePlagInfo {
        UpdatePlagInfoResponseModel responseModel;

        public void updatePlagInfo(UpdatePlagInfoRequestModel requestModel, SummaryReportOutputBoundary presenter){
            createEmptyResponseModel();
            if(isRequestMissingData(requestModel)){
                presenter.presentUpdatedSummaryReportRowPlagInfo(responseModel);
                return;
            }

            plagMatchesReader = PlagMatchesReader.createPlagMatchReader(requestModel.sourceDirPath,requestModel.workingDir,requestModel.selectedInputDirPath);

            if(!plagMatchesReader.getDir().exists()){
                responseModel.errorMessages = UseCaseResponseErrorMessages.invalidAnalysisDir+":"+plagMatchesReader.getDir();
                presenter.presentUpdatedSummaryReportRowPlagInfo(responseModel);
                return;
            }

            createProcessedPlagiarizedResponsePart(requestModel);

            responseModel.fileAname = requestModel.fileAname;
            responseModel.fileBname = requestModel.fileBname;
            presenter.presentUpdatedSummaryReportRowPlagInfo(responseModel);
        }

        protected void createProcessedPlagiarizedResponsePart(UpdatePlagInfoRequestModel requestModel) {
            responseModel.processed = false;
            responseModel.plagiarized = false;
            responseModel.plagiarizedDisabled = false;
            responseModel.processedDisabled = false;

            if(requestModel.plagiarized){
                plagMatchesReader.addPlagiarizedMatch(requestModel.fileAname,requestModel.fileBname);
                responseModel.processed = true;
                responseModel.plagiarized = true;
                responseModel.plagiarizedDisabled = false;
                responseModel.processedDisabled = true;
            } else if(requestModel.processed) {
                if(plagMatchesReader.containsPlagPair(requestModel.fileAname, requestModel.fileBname))
                    plagMatchesReader.removePlagMatch(requestModel.fileAname, requestModel.fileBname);
                plagMatchesReader.addProcessedMatch(requestModel.fileAname, requestModel.fileBname);
                responseModel.processed = true;
            } else {
                    plagMatchesReader.removePlagMatch(requestModel.fileAname, requestModel.fileBname);
                    plagMatchesReader.removeProcessedMatch(requestModel.fileAname, requestModel.fileBname);
            }
        }

        private void createEmptyResponseModel() {
            responseModel = new UpdatePlagInfoResponseModel();
            responseModel.errorMessages = "";
        }

        private boolean isRequestMissingData(UpdatePlagInfoRequestModel requestModel) {
            if(!requestModel.workingDir.exists()){
                responseModel.errorMessages = UseCaseResponseErrorMessages.invalidWorkingDir;
                return true;
            }

            if(!requestModel.sourceDirPath.exists()){
                responseModel.errorMessages = UseCaseResponseErrorMessages.invalidSourceDir;
                return true;
            }

            if(!requestModel.selectedInputDirPath.getPath().contains(requestModel.sourceDirPath.getPath())){
                responseModel.errorMessages = UseCaseResponseErrorMessages.inputDirMismatch;
                return true;
            }

            if(requestModel.fileAname == null || requestModel.fileAname.isEmpty() ||
                    requestModel.fileBname == null || requestModel.fileBname.isEmpty()){
                responseModel.errorMessages = UseCaseResponseErrorMessages.noSelectedaStudentPairMatch;
                return true;
            }

            return false;
        }
    }
}
