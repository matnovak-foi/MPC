package org.foi.mpc.usecases.reports.summaryReport.view;

import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.mpc.usecases.reports.summaryReport.DetailsReportInputBoundary;
import org.foi.mpc.usecases.reports.summaryReport.DetailsReportOutputBoundary;
import org.foi.mpc.usecases.reports.summaryReport.models.DetailsReportMatchInfoRequestModel;
import org.foi.mpc.usecases.reports.summaryReport.models.SideBySideCompariosnRequestModel;
import org.foi.mpc.usecases.reports.summaryReport.models.SideBySideCompariosnRequestModel.SideBySideType;
import org.foi.mpc.usecases.reports.summaryReport.view.models.DetailsReportViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DetailsReportControler {
    private DetailsReportViewModel viewModel;
    private DetailsReportOutputBoundary presenter;
    private DetailsReportInputBoundary useCase;

    public DetailsReportControler(DetailsReportViewModel viewModel, DetailsReportOutputBoundary presenter, DetailsReportInputBoundary useCase) {
        this.viewModel = viewModel;
        this.presenter = presenter;
        this.useCase = useCase;
        this.viewModel.setSelectedSideBySideMarkingType(SideBySideType.NoMarking.name());
    }


    public DetailsReportViewModel getViewModel() {
        return viewModel;
    }

    public DetailsReportOutputBoundary getPresenter() {
        return presenter;
    }

    public DetailsReportInputBoundary getUseCase() {
        return useCase;
    }

    public void generateDetailInfoForMatch() {

        DetailsReportMatchInfoRequestModel requestModel = new DetailsReportMatchInfoRequestModel();
        requestModel.selectedTechnique = viewModel.getResultTechniqueName();
        requestModel.selectedTool = viewModel.getResultToolName();

        if (viewModel.getSelectedWorkingDirPath() != null) {
            requestModel.selectedWorkingDirPath = new File(viewModel.getSelectedWorkingDirPath());
        }

        if (viewModel.getMatchesDirPath() != null)
            requestModel.selectedMatchesDir = new File(viewModel.getMatchesDirPath());

        if (viewModel.getSelectedPair() != null) {
            requestModel.selectedStudentA = viewModel.getSelectedPair().getStudentA();
            requestModel.selectedStudentB = viewModel.getSelectedPair().getStudentB();
            requestModel.selectedFileAName = viewModel.getSelectedPair().getFileA();
            requestModel.selectedFileBName = viewModel.getSelectedPair().getFileB();
        }

        requestModel.toolList = createExecutionToolList(requestModel.toolList,viewModel.getToolList());
        requestModel.techniqueList = createExecutionToolList(requestModel.techniqueList,viewModel.getTechniqueList());

        useCase.generateDetailInfoForMatch(requestModel, presenter);

    }

    private List<String> createExecutionToolList(List<String> executionToolList, String executionTools){
        executionToolList = new ArrayList<>();

        for(String tool : executionTools.split(","))
            executionToolList.add(tool.trim());

        return executionToolList;
    }

    public void generateMatchPartSideBySideComaprisonView() {
        SideBySideCompariosnRequestModel requestModel = new SideBySideCompariosnRequestModel();

        if (viewModel.getSelectedPair() != null) {
            requestModel.studentAfileName = viewModel.getSelectedPair().getFileA();
            requestModel.studentBfileName = viewModel.getSelectedPair().getFileB();
        }

        if(viewModel.getSelectedMatchPart() != null) {
            requestModel.startLineA = viewModel.getSelectedMatchPart().getStartLineNumberA();
            requestModel.startLineB = viewModel.getSelectedMatchPart().getStartLineNumberB();
            requestModel.endLineA = viewModel.getSelectedMatchPart().getEndLineNumberA();
            requestModel.endLineB = viewModel.getSelectedMatchPart().getEndLineNumberB();
            requestModel.matchesDir = new File(viewModel.getSelectedMatchPart().getMatchesDir());
        }

        requestModel.sideBySideType = SideBySideType.NoMarking;
        if(viewModel.getSelectedSideBySideMarkingType().equalsIgnoreCase(SideBySideType.MarkingWumpz.name()))
            requestModel.sideBySideType = SideBySideType.MarkingWumpz;
        else if(viewModel.getSelectedSideBySideMarkingType().equalsIgnoreCase(SideBySideType.MarkingJYCR.name()))
            requestModel.sideBySideType = SideBySideType.MarkingJYCR;

        useCase.generateMatchPartSidBySideComparion(requestModel,presenter);
    }

    public void generateFullFileSideBySideComaprisonView() {
        SideBySideCompariosnRequestModel requestModel = new SideBySideCompariosnRequestModel();

        if (viewModel.getSelectedPair() != null) {
            requestModel.studentAfileName = viewModel.getSelectedPair().getFileA();
            requestModel.studentBfileName = viewModel.getSelectedPair().getFileB();
        }

        if(viewModel.getSelectedSimilarity() != null)
            requestModel.matchesDir = new File(viewModel.getSelectedSimilarity().getMatchesDir());

        useCase.generateFullFileSidBySideComparion(requestModel,presenter);
    }


    public void initToolAndTechniqueList() {
        File preprocess = new File(viewModel.getSelectedWorkingDirPath()+File.separator+"preprocess");
        File detection = new File(viewModel.getSelectedWorkingDirPath()+File.separator+"detection");

        viewModel.setToolList(DirectoryFileUtility.getDirListFromFolder(detection));
        viewModel.setTechniqueList(DirectoryFileUtility.getDirListFromFolder(preprocess));
    }


}

