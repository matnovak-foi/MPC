package org.foi.mpc.usecases.reports.summaryReport.view;

import org.foi.mpc.usecases.reports.avalibleTools.models.AvailableToolsResponseModel;
import org.foi.mpc.usecases.reports.summaryReport.SummaryReportOutputBoundary;
import org.foi.mpc.usecases.reports.summaryReport.models.SummaryReportResponseModel;
import org.foi.mpc.usecases.reports.summaryReport.models.UpdatePlagInfoResponseModel;
import org.foi.mpc.usecases.reports.summaryReport.view.models.PresentableReportSummary;
import org.foi.mpc.usecases.reports.summaryReport.view.models.PresentableReportSummaryTableRow;
import org.foi.mpc.usecases.reports.summaryReport.view.models.SummaryReportViewModel;
import org.foi.mpc.usecases.reports.view.models.SelectOption;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SummaryReportPresenter implements SummaryReportOutputBoundary {

    private SummaryReportViewModel viewModel;

    public SummaryReportPresenter(SummaryReportViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentAvailableTools(AvailableToolsResponseModel responseModel) {
        viewModel.setAvailableTools(responseModel.tools);
        viewModel.setErrorMessage(responseModel.errorMessage);
    }

    @Override
    public void presentReport(SummaryReportResponseModel responseModel) {
        PresentableReportSummary summaryReport = new PresentableReportSummary();
        summaryReport.setResultToolName(responseModel.toolName);
        summaryReport.setResultTechniqueName(responseModel.techniqueName);
        summaryReport.setSelectedInputDirPath(viewModel.getSelectedInputDirPath());

        if(responseModel.similarityStatistics != null) {
            summaryReport.setMax(responseModel.similarityStatistics.max);
            summaryReport.setMin(responseModel.similarityStatistics.min);
            summaryReport.setMean(responseModel.similarityStatistics.mean);
            summaryReport.setMedian(responseModel.similarityStatistics.median);
            List<Double> modes = Arrays.stream(responseModel.similarityStatistics.mode).boxed().collect(Collectors.toList());
            summaryReport.setMode(modes);
            summaryReport.setQ1(responseModel.similarityStatistics.q1);
            summaryReport.setQ3(responseModel.similarityStatistics.q3);
            summaryReport.setIRQ(responseModel.similarityStatistics.IRQ);
            summaryReport.setSTD(responseModel.similarityStatistics.STD);
            summaryReport.setMedianPlus3IRQ(responseModel.similarityStatistics.median+(responseModel.similarityStatistics.IRQ *3));
            summaryReport.setMedianPlus2p5IRQ(responseModel.similarityStatistics.median+(responseModel.similarityStatistics.IRQ *2.5));
            summaryReport.setMedianPlus2IRQ(responseModel.similarityStatistics.median+(responseModel.similarityStatistics.IRQ *2));
            summaryReport.setPercentile99(responseModel.similarityStatistics.percentile99);
        }

        if(responseModel.matchesDirPath != null)
            summaryReport.setMatchesDirPath(responseModel.matchesDirPath.getPath());
        List<PresentableReportSummaryTableRow> reportTable = createPresentableSummaryTable(responseModel);
        summaryReport.setReportTable(reportTable);

        viewModel.setSummaryReport(summaryReport);
        viewModel.setErrorMessage(responseModel.errorMessages);
        viewModel.setSelectedPair(null);
    }

    @Override
    public void presentDirList(Map<String,File> dirList) {
        List<SelectOption> viewDirList = new ArrayList<>();
        for (String name: dirList.keySet()) {
            viewDirList.add(new SelectOption(name,dirList.get(name).getPath()));
        }
        viewModel.setInputDirList(viewDirList);
    }

    @Override
    public void presentUpdatedSummaryReportRowPlagInfo(UpdatePlagInfoResponseModel responseModel) {
        String errorMessage = "No such row presenter can not update view!";
        if(viewModel.getSummaryReport() == null || viewModel.getSummaryReport().getReportTable() == null || viewModel.getSummaryReport().getReportTable().size() == 0){
            viewModel.setErrorMessage(errorMessage);
            return;
        }

        if(responseModel.errorMessages != null && !responseModel.errorMessages.isEmpty()) {
            viewModel.setErrorMessage(responseModel.errorMessages);
            return;
        }

        PresentableReportSummaryTableRow summaryTableRow = new PresentableReportSummaryTableRow();
        summaryTableRow.setFileA(responseModel.fileAname);
        summaryTableRow.setFileB(responseModel.fileBname);
        summaryTableRow.setStudentA("intentionalWrongA");
        int rowIndex = viewModel.getSummaryReport().getReportTable().indexOf(summaryTableRow);
        if(rowIndex == -1){
            viewModel.setErrorMessage(errorMessage);
        } else {
            PresentableReportSummary reportSummary = viewModel.getSummaryReport();
            List<PresentableReportSummaryTableRow> reportTable = reportSummary.getReportTable();

            summaryTableRow = reportTable.get(rowIndex);
            summaryTableRow.setProcessed(responseModel.processed);
            summaryTableRow.setPlagiarized(responseModel.plagiarized);
            summaryTableRow.setProcessedDisabled(responseModel.processedDisabled);
            summaryTableRow.setPlagiarizedDisabled(responseModel.plagiarizedDisabled);

            reportSummary.setReportTable(reportTable);
            viewModel.setSummaryReport(reportSummary);

            viewModel.setErrorMessage(responseModel.errorMessages);
            viewModel.setSelectedPair(summaryTableRow);
        }
    }

    private List<PresentableReportSummaryTableRow> createPresentableSummaryTable(SummaryReportResponseModel responseModel) {
        List<PresentableReportSummaryTableRow> report = new ArrayList<>();
        if(responseModel.reportTable != null) {
            for (SummaryReportResponseModel.SummaryReportTableRow reportTable : responseModel.reportTable) {
                PresentableReportSummaryTableRow reportRow = new PresentableReportSummaryTableRow();
                reportRow.setPlagiarized(reportTable.plagiarized);
                reportRow.setProcessed(reportTable.processed);
                reportRow.setStudentA(reportTable.studentA);
                reportRow.setStudentB(reportTable.studentB);
                reportRow.setSimilarity(reportTable.similarity);
                reportRow.setSimilarityA(reportTable.similarityA);
                reportRow.setSimilarityB(reportTable.similarityB);
                reportRow.setCalculatedSimilarity(reportTable.calculatedSimilarity);
                reportRow.setCalculatedSimilarityA(reportTable.calculatedSimilarityA);
                reportRow.setCalculatedSimilarityB(reportTable.calculatedSimilarityB);
                if(reportTable.fileAName != null)
                    reportRow.setFileA(reportTable.fileAName);
                if(reportTable.fileBName != null)
                    reportRow.setFileB(reportTable.fileBName);
                report.add(reportRow);
            }
        }
        return report;
    }
}
