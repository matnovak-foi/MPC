package org.foi.mpc.usecases.reports.statisticsReport.view;

import org.foi.mpc.usecases.reports.avalibleTools.models.AvailableToolsResponseModel;
import org.foi.mpc.usecases.reports.statisticsReport.StatisticsReportOutputBoundary;
import org.foi.mpc.usecases.reports.statisticsReport.models.StatisticsReportResponseModel;
import org.foi.mpc.usecases.reports.statisticsReport.models.StatisticsReportTableRow;
import org.foi.mpc.usecases.reports.statisticsReport.view.model.StatisticsReportViewModel;
import org.foi.mpc.usecases.reports.statisticsReport.view.model.StatisticsReportViewModel.PresentableStatisticsReportTableRow;
import org.foi.mpc.usecases.reports.view.models.SelectOption;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatisticsReportPresenter implements StatisticsReportOutputBoundary {

    StatisticsReportViewModel viewModel;

    public StatisticsReportPresenter(StatisticsReportViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public StatisticsReportViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void presentAvailableTools(AvailableToolsResponseModel responseModel) {
        viewModel.setAvailableTools(responseModel.tools);
        viewModel.setErrorMessage(responseModel.errorMessage);
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
    public void presentStatististicsReport(StatisticsReportResponseModel responseModel) {
        viewModel.setErrorMessage(responseModel.errorMessages);
        viewModel.setReportTable(new ArrayList<>());

        if(responseModel.assignementDir != null)
            viewModel.setAssigmentDirPath(responseModel.assignementDir.getPath());


        viewModel.setPlagiarizedMatches(responseModel.plagiarizedMatches);

        List<PresentableStatisticsReportTableRow> tableReport = new ArrayList<>();
        if(responseModel.reportTable != null) {
            for (StatisticsReportTableRow tableRow : responseModel.reportTable) {
                PresentableStatisticsReportTableRow presentableTableRow = new PresentableStatisticsReportTableRow();
                presentableTableRow.setTool(tableRow.tool);
                presentableTableRow.setTechnique(tableRow.technique);
                presentableTableRow.setMatchesDir(tableRow.matchesDir.getPath());

                presentableTableRow.setF1(tableRow.F1);
                presentableTableRow.setFalseNegatives(tableRow.falseNegatives);
                presentableTableRow.setFalsePositives(tableRow.falsePositives);
                presentableTableRow.setTruePositives(tableRow.truePositives);
                presentableTableRow.setIndicatedMatches(tableRow.indicatedMatches);
                presentableTableRow.setIncludedMatches(tableRow.includedMatches);
                presentableTableRow.setNumberOfMatches(tableRow.numberOfMatches);
                presentableTableRow.setPrecision(tableRow.precision);
                presentableTableRow.setRecall(tableRow.recall);
                presentableTableRow.setTreshold(tableRow.threshold);

                presentableTableRow.setIRQ(tableRow.descStat.IRQ);
                presentableTableRow.setMax(tableRow.descStat.max);
                presentableTableRow.setMin(tableRow.descStat.min);
                presentableTableRow.setMean(tableRow.descStat.mean);
                presentableTableRow.setMedian(tableRow.descStat.median);
                presentableTableRow.setQ1(tableRow.descStat.q1);
                presentableTableRow.setQ3(tableRow.descStat.q3);
                presentableTableRow.setSTD(tableRow.descStat.STD);
                presentableTableRow.setMedianPlus3IRQ(-1);
                presentableTableRow.setMedianPlus2p5IRQ(-1);
                presentableTableRow.setMedianPlus2IRQ(-1);
                presentableTableRow.setPercentile99(tableRow.descStat.percentile99);
                if(tableRow.descStat.mode != null) {
                    List<Double> modes = Arrays.stream(tableRow.descStat.mode).boxed().collect(Collectors.toList());
                    presentableTableRow.setMode(modes);
                }


                tableReport.add(presentableTableRow);
            }
        }
        viewModel.setReportTable(tableReport);
    }
}
