package org.foi.mpc.usecases.reports.statisticsReport;

import helper.fileFilters.DirectoryFilter;
import org.apache.commons.math3.stat.StatUtils;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.file.OnlyFilesFilter;
import org.foi.mpc.MPCContext;
import org.foi.mpc.executiontools.prepareTools.SubmissionFilesUnifier;
import org.foi.mpc.matches.PlagMatchesReader;
import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.phases.executionphases.ExecutionLogger;
import org.foi.mpc.phases.readerphase.MPCMatchListener;
import org.foi.mpc.phases.readerphase.MPCMatchReaderPhase;
import org.foi.mpc.usecases.UseCaseResponseErrorMessages;
import org.foi.mpc.usecases.multipleDetecion.DirectoryPreparer;
import org.foi.mpc.usecases.reports.avalibleTools.AvalibleToolsUseCase;
import org.foi.mpc.usecases.reports.avalibleTools.models.AvailableToolsResponseModel;
import org.foi.mpc.usecases.reports.statisticsReport.models.StatisticsReportRequestModel;
import org.foi.mpc.usecases.reports.statisticsReport.models.StatisticsReportRequestModel.ThresholdType;
import org.foi.mpc.usecases.reports.statisticsReport.models.StatisticsReportResponseModel;
import org.foi.mpc.usecases.reports.statisticsReport.models.StatisticsReportTableRow;
import org.foi.mpc.usecases.reports.statisticsReport.models.StatisticsReportTableRow.StatMatch;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.*;

public class StatisticsReportUseCase implements StatisticsReportInputBoundary {
    DirectoryPreparer directoryPreparer = new DirectoryPreparer();
    StatisticsReportGenerator generator;

    public int getProcessedMatchesDirs() {
        return generator.matchesDirs.size();
    }

    public StatisticsReportUseCase(MPCMatchReaderPhase mpcMatchReaderPhase) {
        generator = new StatisticsReportGenerator(mpcMatchReaderPhase);
    }

    @Override
    public void getAvailableTools(StatisticsReportOutputBoundary outputBoundary, File workingDir) {
        AvailableToolsResponseModel responseModel = AvalibleToolsUseCase.getAvailableTools(workingDir);
        outputBoundary.presentAvailableTools(responseModel);
    }

    @Override
    public void loadDirectoryList(File inputDir, int inputDirDepth, StatisticsReportOutputBoundary presenter) {
        List<File> dirs = directoryPreparer.createDirsToPorces(inputDir, inputDirDepth);
        Map<String, File> response = directoryPreparer.createMapForDisplay(inputDir, dirs);
        presenter.presentDirList(response);
    }

    @Override
    public void generateReport(StatisticsReportRequestModel requestModel, StatisticsReportOutputBoundary presenter) {
        generator.generateReport(requestModel, presenter);
    }

    public void setDirectoryPreparer(DirectoryPreparer directoryPreparer) {
        this.directoryPreparer = directoryPreparer;
    }

    private class StatisticsReportGenerator implements MPCMatchListener {
        MPCMatchReaderPhase mpcMatchReaderPhase;
        StatisticsReportResponseModel responseModel;
        PlagMatchesReader plagMatchesReader;
        Map<MatchesDirKey, File> matchesDirs = new HashMap<>();
        Map<MatchesDirKey, StatisticsReportTableRow> statTableRows = new LinkedHashMap<>();

        public StatisticsReportGenerator(MPCMatchReaderPhase mpcMatchReaderPhase) {
            mpcMatchReaderPhase.setListener(this);
            this.mpcMatchReaderPhase = mpcMatchReaderPhase;
        }

        public void generateReport(StatisticsReportRequestModel requestModel, StatisticsReportOutputBoundary presenter) {
            matchesDirs.clear();
            statTableRows.clear();
            createEmptyResponseModel();

            if (isRequestMissingData(requestModel)) {
                presenter.presentStatististicsReport(responseModel);
                return;
            }

            plagMatchesReader = PlagMatchesReader.createPlagMatchReader(requestModel.sourceDir, requestModel.workingDir, requestModel.assignementDir);

            if (!plagMatchesReader.getDir().exists()) {
                responseModel.errorMessages = UseCaseResponseErrorMessages.invalidAnalysisDir + ":" + plagMatchesReader.getDir();
                presenter.presentStatististicsReport(responseModel);
                return;
            }

            if (!prepareMatchesDirs(requestModel)) {
                presenter.presentStatististicsReport(responseModel);
                return;
            }

            responseModel.assignementDir = requestModel.assignementDir;
            responseModel.plagiarizedMatches = plagMatchesReader.plagMatchesCount();


            ExecutionLogger logger1 = new ExecutionLogger();
            logger1.setStartTime("");
            mpcMatchReaderPhase.setDirsToProcess(new ArrayList<>(matchesDirs.values()));
            mpcMatchReaderPhase.runPhase();
            logger1.setEndTime("");
            logger1.printTimeElapsed("StatisticsReportUseCase");

            for (MatchesDirKey key : statTableRows.keySet()) {
                StatisticsReportTableRow reportTableRow = statTableRows.get(key);
                reportTableRow.tool = key.tool;
                reportTableRow.technique = key.technique;
                reportTableRow.matchesDir = new File(matchesDirs.get(key).getPath() + File.separator + MPCContext.MATCHES_DIR);//statTableRows.get(key).get(0).matchesDir;

                reportTableRow.matches = sortMatchesDescendingBySimilarity(reportTableRow.matches);
                reportTableRow.includedMatches = reportTableRow.matches.size();
                int numberOfFiles = reportTableRow.matchesDir.getParentFile().listFiles(new OnlyFilesFilter(".java")).length;
                reportTableRow.numberOfMatches = ((numberOfFiles-1)*numberOfFiles)/2;
                double[] data = new double[reportTableRow.matches.size()];
                for (int i = 0; i < reportTableRow.matches.size(); i++) {
                    data[i] = reportTableRow.matches.get(i).similarity;
                }

                reportTableRow.descStat.median = StatUtils.percentile(data, 50);
                reportTableRow.descStat.q1 = StatUtils.percentile(data, 25);
                reportTableRow.descStat.q3 = StatUtils.percentile(data, 75);
                reportTableRow.descStat.mean = StatUtils.mean(data);
                reportTableRow.descStat.IRQ = reportTableRow.descStat.q3 - reportTableRow.descStat.q1;
                reportTableRow.descStat.STD = Math.sqrt(StatUtils.populationVariance(data));
                reportTableRow.descStat.max = StatUtils.max(data);
                reportTableRow.descStat.min = StatUtils.min(data);

                prepareThreshold(requestModel, reportTableRow);

                reportTableRow.indicatedMatches = 0;
                reportTableRow.truePositives = 0;
                reportTableRow.falsePositives = 0;

                for (StatMatch statMatch : reportTableRow.matches) {
                    if ((isPrecentageBasedThreshold(requestModel.thresholdType) && statMatch.similarity > reportTableRow.threshold)
                            || (isTopNBasedThreshold(requestModel.thresholdType) && reportTableRow.indicatedMatches < reportTableRow.threshold)) {
                        reportTableRow.indicatedMatches++;
                        if (plagMatchesReader.containsPlagPair(statMatch.fileAName, statMatch.fileBName))
                            reportTableRow.truePositives++;
                        else
                            reportTableRow.falsePositives++;
                    } else {
                        if (plagMatchesReader.containsPlagPair(statMatch.fileAName, statMatch.fileBName))
                            reportTableRow.falseNegatives++;
                    }
                }

                reportTableRow.falseNegatives = responseModel.plagiarizedMatches - reportTableRow.truePositives;
                reportTableRow.precision = reportTableRow.truePositives / (double) (reportTableRow.truePositives + reportTableRow.falsePositives);
                reportTableRow.recall = reportTableRow.truePositives / (double) (reportTableRow.truePositives + reportTableRow.falseNegatives);
                double beta = 1;
                double p = reportTableRow.precision;
                double r = reportTableRow.recall;
                reportTableRow.F1 = (((beta * beta) + 1) * (p * r)) / ((beta * beta) * (p + r));

                responseModel.reportTable.add(reportTableRow);
            }

            presenter.presentStatististicsReport(responseModel);
        }

        private boolean isPrecentageBasedThreshold(ThresholdType thresholdType) {
            return thresholdType.equals(ThresholdType.calculatedPrecentageBased) || thresholdType.equals(ThresholdType.fixedPrecentageBased);
        }

        private boolean isTopNBasedThreshold(ThresholdType thresholdType) {
            return thresholdType.equals(ThresholdType.topNBased) || thresholdType.equals(ThresholdType.plagMatchesBased);
        }

        private List<StatMatch> sortMatchesDescendingBySimilarity(List<StatMatch> reportTableRowMatches) {
            Collections.sort(reportTableRowMatches, (o1, o2) -> {
                if (o1.similarity > o2.similarity)
                    return -1;
                else if (o1.similarity < o2.similarity)
                    return 1;
                else
                    return 0;
            });

            return reportTableRowMatches;
        }

        private void prepareThreshold(StatisticsReportRequestModel requestModel, StatisticsReportTableRow reportTableRow) {
            if (requestModel.thresholdType.equals(ThresholdType.calculatedPrecentageBased)) {
                reportTableRow.threshold = reportTableRow.descStat.IRQ * requestModel.thresholdValue + reportTableRow.descStat.median;
            } else if (requestModel.thresholdType.equals(ThresholdType.fixedPrecentageBased)) {
                reportTableRow.threshold = requestModel.thresholdValue;
            } else if (requestModel.thresholdType.equals(ThresholdType.plagMatchesBased)) {
                reportTableRow.threshold = responseModel.plagiarizedMatches;
            } else if (requestModel.thresholdType.equals(ThresholdType.topNBased)) {
                reportTableRow.threshold = requestModel.thresholdValue;
            } else {
                reportTableRow.threshold = -1;
            }
        }

        private void createEmptyResponseModel() {
            responseModel = new StatisticsReportResponseModel();
            responseModel.errorMessages = "";
            responseModel.reportTable = new ArrayList<>();
        }

        private boolean isRequestMissingData(StatisticsReportRequestModel requestModel) {
            if (!requestModel.workingDir.exists()) {
                responseModel.errorMessages = UseCaseResponseErrorMessages.invalidWorkingDir;
                return true;
            }

            if (requestModel.inputDirDepth < 0) {
                responseModel.errorMessages = UseCaseResponseErrorMessages.inputDirDepth;
                return true;
            }

            if (!requestModel.sourceDir.exists()) {
                responseModel.errorMessages = UseCaseResponseErrorMessages.invalidSourceDir;
                return true;
            }

            if (!requestModel.assignementDir.getPath().contains(requestModel.sourceDir.getPath())) {
                responseModel.errorMessages = UseCaseResponseErrorMessages.inputDirMismatch;
                return true;
            }

            if (requestModel.tools.isEmpty()) {
                responseModel.errorMessages = UseCaseResponseErrorMessages.noToolIsSelected;
                return true;
            }

            if (requestModel.techniques.isEmpty()) {
                responseModel.errorMessages = UseCaseResponseErrorMessages.noTechniqueIsSelected;
                return true;
            }

            return false;
        }

        private boolean prepareMatchesDirs(StatisticsReportRequestModel requestModel) {
            for (String tool : requestModel.tools) {
                for (String technique : requestModel.techniques) {
                    File toolTechDir = new File(requestModel.workingDir + File.separator +
                            MPCContext.DETECTION_DIR + File.separator +
                            tool + File.separator +
                            technique + File.separator + SubmissionFilesUnifier.NAME);
                    File matchesDir = new File(
                            DirectoryFileUtility.getRelativizedDir(requestModel.sourceDir, requestModel.assignementDir, toolTechDir)
                                    + File.separator + MPCContext.MATCHES_DIR);
                    if (!matchesDir.exists()) {
                        responseModel.errorMessages = UseCaseResponseErrorMessages.invalidMatchesDir + ":" + matchesDir;
                        return false;
                    } else {
                        MatchesDirKey key = new MatchesDirKey(tool, technique);
                        matchesDirs.put(key, matchesDir.getParentFile());
                        StatisticsReportTableRow tableRow = new StatisticsReportTableRow();
                        statTableRows.put(key, tableRow);
                    }
                }
            }
            return true;
        }

        @Override
        public void processMatch(MPCMatch match) {
            for (MatchesDirKey matchesDirKey : statTableRows.keySet()) {
                // System.out.println(match);
                if (match.matchesDir.getPath().contains(matchesDirKey.tool)
                        && match.matchesDir.getPath().contains(matchesDirKey.technique)) {
                    StatisticsReportTableRow tableRow = this.statTableRows.get(matchesDirKey);
                    StatMatch statMatch = new StatMatch();
                    statMatch.fileAName = match.fileAName;
                    statMatch.fileBName = match.fileBName;
                    statMatch.similarity = Double.valueOf(match.similarity);
                    tableRow.matches.add(statMatch);
                    this.statTableRows.put(matchesDirKey, tableRow);
                    break;
                }
            }
        }

        private class MatchesDirKey {
            private String tool;
            private String technique;

            public MatchesDirKey(String tool, String technique) {
                this.tool = tool;
                this.technique = technique;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof MatchesDirKey)) return false;
                MatchesDirKey that = (MatchesDirKey) o;
                return Objects.equals(tool, that.tool) &&
                        Objects.equals(technique, that.technique);
            }

            @Override
            public int hashCode() {
                return Objects.hash(tool, technique);
            }

            @Override
            public String
            toString() {
                return "MatchesDirKey{" +
                        "tool='" + tool + '\'' +
                        ", technique='" + technique + '\'' +
                        '}';
            }
        }
    }
}
