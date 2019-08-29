package org.foi.mpc.summaryReport.view;

import org.foi.common.cookie.CookieHelper;
import org.foi.mpc.MPCContext;
import org.foi.mpc.StandardReportBeanMethods;
import org.foi.mpc.abstractfactories.FactoryProvider;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagJavaAdapter;
import org.foi.mpc.executiontools.factories.PreprocessingTechniqueFactory;
import org.foi.mpc.executiontools.techniques.CommonCodeRemoveTechnique;
import org.foi.mpc.executiontools.techniques.NoTechniqueOriginal;
import org.foi.mpc.usecases.combotechnique.ComboTechniqueUseCase;
import org.foi.mpc.usecases.combotechnique.view.ComboTechniqueControler;
import org.foi.mpc.usecases.combotechnique.view.ComboTechniquePresenter;
import org.foi.mpc.usecases.combotechnique.view.models.ComboTechniqueViewModel;
import org.foi.mpc.usecases.reports.summaryReport.SummaryReportUseCase;
import org.foi.mpc.usecases.reports.summaryReport.view.SummaryReportControler;
import org.foi.mpc.usecases.reports.summaryReport.view.SummaryReportPresenter;
import org.foi.mpc.usecases.reports.summaryReport.view.models.PresentableReportSummaryTableRow;
import org.foi.mpc.usecases.reports.summaryReport.view.models.SummaryReportViewModel;
import org.primefaces.event.SelectEvent;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ManagedBean(name = "summaryReportBean")
@SessionScoped
public class SummaryReportBean implements StandardReportBeanMethods {
    private ComboTechniqueControler techniqueController;
    private ComboTechniqueViewModel techniqueViewModel;
    private ComboTechniquePresenter techniquePresenter;
    private ComboTechniqueUseCase comboUseCase;

    private SummaryReportControler controller;
    private SummaryReportViewModel reportViewModel;
    private SummaryReportPresenter reportPresenter;
    private SummaryReportUseCase reportUseCase;

    private CookieHelper cookieHelper;

    public SummaryReportBean() {
        techniqueViewModel = new ComboTechniqueViewModel();
        reportViewModel = new SummaryReportViewModel();

        techniquePresenter = new ComboTechniquePresenter(techniqueViewModel);
        reportPresenter = new SummaryReportPresenter(reportViewModel);

        FactoryProvider factoryProvider = new FactoryProvider(MPCContext.MATCHES_DIR);
        factoryProvider.createSimilarityDetectionToolFactory(MPCContext.DETECTION_TOOLS_PROPERITES_FILE);
        factoryProvider.createPreprocessingTechniqueFactory(MPCContext.TEMPLATE_EXCLUSION_PROPERTIES_FILE);
        reportUseCase = new SummaryReportUseCase(factoryProvider);

        comboUseCase = new ComboTechniqueUseCase((PreprocessingTechniqueFactory) factoryProvider.getTechniqueFactory());

        controller = new SummaryReportControler(reportViewModel, reportPresenter, techniqueViewModel);
        controller.setSummaryReportUseCase(reportUseCase);

        techniqueController = new ComboTechniqueControler(techniquePresenter, techniqueViewModel);
        techniqueController.setComboTechniqueUseCase(comboUseCase);
        techniqueController.loadComboTehniquesFromFile(MPCContext.COMBO_TECHNIQUES_PROPERTIES_FILE);

        if (MPCContext.DEFAULT_USE_SOCO_SUMMARY_REPORT)
            setDefaultDataSOCO();
        else
            setDefaultData();

        cookieHelper = new CookieHelper();
        loadCookies();
    }


    public void loadCookies(){

        Cookie cookie = cookieHelper.getCookie("mpcData");
        if(cookie != null){
            String value = cookie.getValue();
            String[] data = value.split(":");
            List tool = new ArrayList();
            tool.add(data[0]);
            List technique = new ArrayList();
            technique.add(data[2]);
            techniqueViewModel.setSelectedTechniques(technique);
            reportViewModel.setSelectedInputDirPath(data[1]);
            reportViewModel.setSelectedTools(tool);
        }
    }

    private void setDefaultData() {
        List list = new ArrayList();
        list.add(NoTechniqueOriginal.NAME);
        techniqueViewModel.setSelectedTechniques(list);
        reportViewModel.setWorkingDirPath(MPCContext.DEFAULT_WORKING_DIR);
        reportViewModel.setSourceDirPath(MPCContext.DEFAULT_SOURCE_DIR);
        reportViewModel.setInputDirDepth(MPCContext.DEFAULT_INPUT_DIR_DEPTH);
        controller.loadDirList();
        controller.clearNonProcessedTools();
        reportViewModel.setSelectedInputDirPath(MPCContext.DEFAULT_INPUT_DIR_PATH);
        List list2 = new ArrayList();
        list2.add(JPlagJavaAdapter.NAME);
        reportViewModel.setSelectedTools(list2);
        //controller.generateReport();
        loadProcessedToolsAndTechniques();
    }

    private void setDefaultDataSOCO() {
        List list = new ArrayList();
        list.add(NoTechniqueOriginal.NAME);
        techniqueViewModel.setSelectedTechniques(list);
        reportViewModel.setWorkingDirPath(MPCContext.DEFAULT_WORKING_DIR_SOCO);
        reportViewModel.setSourceDirPath(MPCContext.DEFAULT_SOURCE_DIR_SOCO);
        reportViewModel.setInputDirDepth(MPCContext.DEFAULT_INPUT_DIR_DEPTH);
        controller.loadDirList();
        controller.clearNonProcessedTools();
        reportViewModel.setSelectedInputDirPath(MPCContext.DEFAULT_INPUT_DIR_PATH_SOCO);
        List list2 = new ArrayList();
        list2.add(JPlagJavaAdapter.NAME);
        reportViewModel.setSelectedTools(list2);
        //controller.generateReport();
        loadProcessedToolsAndTechniques();
    }

    public SummaryReportViewModel getReportViewModel() {
        return reportViewModel;
    }

    public SummaryReportControler getController() {
        return this.controller;
    }

    public void setController(SummaryReportControler controller) {
        this.controller = controller;
    }

    public void generateReport(){
        cookieHelper = new CookieHelper();
        setCookiesAndGenerateReport();
    }

    public void setCookiesAndGenerateReport(){
        String data = reportViewModel.getSelectedTools().get(0)+":"+reportViewModel.getSelectedInputDirPath()+":"+techniqueViewModel.getSelectedTechniques().get(0);
        cookieHelper.setCookie("mpcData",data,31536000);
        controller.generateReport();
    }


    public void setCookieHelper(CookieHelper cookieHelper) {
        this.cookieHelper = cookieHelper;
    }

    public SummaryReportPresenter getReportPresenter() {
        return this.reportPresenter;
    }

    public SummaryReportUseCase getReportUseCase() {
        return reportUseCase;
    }

    public ComboTechniqueUseCase getComboUseCase() {
        return comboUseCase;
    }

    public ComboTechniqueControler getTechniqueController() {
        return techniqueController;
    }

    public ComboTechniqueViewModel getTechniqueViewModel() {
        return techniqueViewModel;
    }

    public void onPairSelect(SelectEvent event) {

        PresentableReportSummaryTableRow pair = (PresentableReportSummaryTableRow) event.getObject();

        PresentableReportSummaryTableRow oldPair = null;
        if(reportViewModel.getSelectedPair() != null)
            oldPair = reportViewModel.getSelectedPair();

        List<PresentableReportSummaryTableRow> reportSummaryTable = reportViewModel.getSummaryReport().getReportTable();
        if(!pair.getPlagiarized()) pair.setProcessedDisabled(false);
        pair.setPlagiarizedDisabled(false);
        if(oldPair != null && oldPair!=pair) {
            oldPair.setProcessedDisabled(true);
            oldPair.setPlagiarizedDisabled(true);
            reportSummaryTable.remove(oldPair);
            reportSummaryTable.add(oldPair);
        }

        reportSummaryTable.remove(pair);
        reportSummaryTable.add(pair);
        reportViewModel.getSummaryReport().setReportTable(reportSummaryTable);
        reportViewModel.setSelectedPair(pair);
    }

    public void onChangeProcessed() {
        PresentableReportSummaryTableRow pair = reportViewModel.getSelectedPair();
        String message = reportViewModel.getSelectedPair().getProcessed() ? "Processed" : "Not processed";
        printPopupMesage(pair.getStudentA()+"-"+pair.getStudentB()+" marked as "+message);
        controller.updatePlagInfo();
    }

    public void onChangePlagiarized() {
        PresentableReportSummaryTableRow pair = reportViewModel.getSelectedPair();
        String message = reportViewModel.getSelectedPair().getPlagiarized() ? "Plagiarized" : "Not plagiarized";
        printPopupMesage(pair.getStudentA()+"-"+pair.getStudentB()+" marked as "+message);
        controller.updatePlagInfo();
    }

    private void printPopupMesage(String message){
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage msg = new FacesMessage(message);
        context.addMessage(null, msg);
    }

    public void loadProcessedToolsAndTechniques() {
        techniqueController.loadProcessedTechniques(new File(reportViewModel.getWorkingDirPath()));
        controller.clearNonProcessedTools();
        techniqueViewModel.setAvalibleComboTechniques(new ArrayList<>());
    }

    @Override
    public void loadToolsAndTechniquesFromFile() {
        techniqueController.loadComboTehniquesFromFile(MPCContext.COMBO_TECHNIQUES_PROPERTIES_FILE);
        techniqueController.setComboTechniqueUseCase(comboUseCase);
        controller.setSummaryReportUseCase(reportUseCase);
    }
}
