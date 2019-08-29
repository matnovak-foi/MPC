package org.foi.mpc.summaryReport.view;

import org.foi.mpc.usecases.reports.summaryReport.DetailsReportUseCase;
import org.foi.mpc.usecases.reports.summaryReport.view.DetailsReportControler;
import org.foi.mpc.usecases.reports.summaryReport.view.DetailsReportPresenter;
import org.foi.mpc.usecases.reports.summaryReport.view.models.DetailsReportViewModel;
import org.foi.mpc.usecases.reports.summaryReport.view.models.PresentableDetailSimilarityTable;
import org.foi.mpc.usecases.reports.summaryReport.view.models.PresentableReportSummaryTableRow;
import org.primefaces.event.SelectEvent;

import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;

@ManagedBean(name="summaryReportDetail")
@SessionScoped
public class SummaryReportDetailBean {
    DetailsReportViewModel viewModel;
    DetailsReportControler controler;
    DetailsReportPresenter presenter;
    DetailsReportUseCase useCase;

    public SummaryReportDetailBean() {
        viewModel = new DetailsReportViewModel();


        presenter = new DetailsReportPresenter(viewModel);
        useCase = new DetailsReportUseCase();
        controler = new DetailsReportControler(viewModel, presenter, useCase);
    }

    public void onPairSelect(SelectEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        PresentableReportSummaryTableRow pair = (PresentableReportSummaryTableRow) event.getObject();
        String selectedPair = pair.getStudentA()+"-"+pair.getStudentB();

        FacesMessage msg = new FacesMessage("Pair Selected", selectedPair);
        context.addMessage(null, msg);

        this.viewModel.setSelectedSimilarity(null);
        this.viewModel.setSelectedMatchPart(null);
        this.viewModel.setSimilarityTable(null);
        this.viewModel.setPresentableSideBySide(null);

        if(this.viewModel.getToolList() == null) {
            String workingDir = (String) event.getComponent().getAttributes().get("workingDir");
            this.viewModel.setSelectedWorkingDirPath(workingDir);
            controler.initToolAndTechniqueList();
        }
    }

    public void onSimilarityTableRowSelect(SelectEvent event){
        FacesContext context = FacesContext.getCurrentInstance();
        PresentableDetailSimilarityTable similarityTableRow = (PresentableDetailSimilarityTable) event.getObject();
        String selectedRow = similarityTableRow.getTool()+"-"+similarityTableRow.getTechnique();
        FacesMessage msg = new FacesMessage("Pair Selected", selectedRow);
        context.addMessage(null, msg);
    }

    public DetailsReportControler getController() {
        return controler;
    }

    public DetailsReportViewModel getReportViewModel() {
        return viewModel;
    }

    public DetailsReportPresenter getReportPresenter() {
        return presenter;
    }

    public DetailsReportUseCase getDetailsUseCase() {
        return useCase;
    }

    public DetailsReportUseCase getReportUseCase() {
        return useCase;
    }
}
