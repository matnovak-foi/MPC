package org.foi.mpc.statisticsReport.view;

import org.foi.mpc.MPCContext;
import org.foi.mpc.StandardReportBeanMethods;
import org.foi.mpc.abstractfactories.FactoryProvider;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagJavaAdapter;
import org.foi.mpc.executiontools.factories.PreprocessingTechniqueFactory;
import org.foi.mpc.executiontools.techniques.NoTechniqueOriginal;
import org.foi.mpc.phases.readerphase.MPCMatchReaderPhase;
import org.foi.mpc.usecases.combotechnique.ComboTechniqueUseCase;
import org.foi.mpc.usecases.combotechnique.view.ComboTechniqueControler;
import org.foi.mpc.usecases.combotechnique.view.ComboTechniquePresenter;
import org.foi.mpc.usecases.combotechnique.view.models.ComboTechniqueViewModel;
import org.foi.mpc.usecases.reports.statisticsReport.StatisticsReportUseCase;
import org.foi.mpc.usecases.reports.statisticsReport.view.StatisticsReportControler;
import org.foi.mpc.usecases.reports.statisticsReport.view.StatisticsReportPresenter;
import org.foi.mpc.usecases.reports.statisticsReport.view.model.StatisticsReportViewModel;
import org.primefaces.event.SelectEvent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "statisticsReportBean")
@SessionScoped
public class StatisticsReportBean implements StandardReportBeanMethods {
    private StatisticsReportViewModel reportViewModel;
    private StatisticsReportControler controller;
    private StatisticsReportPresenter presenter;
    private StatisticsReportUseCase useCase;

    private ComboTechniqueUseCase techniqueUseCase;
    private ComboTechniqueControler techniqueControler;
    private ComboTechniqueViewModel techniqueViewModel;
    private ComboTechniquePresenter techniquePresenter;

    public StatisticsReportBean() {
        reportViewModel = new StatisticsReportViewModel();
        techniqueViewModel = new ComboTechniqueViewModel();

        presenter = new StatisticsReportPresenter(reportViewModel);
        techniquePresenter = new ComboTechniquePresenter(techniqueViewModel);

        FactoryProvider factoryProvider = new FactoryProvider(MPCContext.MATCHES_DIR);
        useCase = new StatisticsReportUseCase((MPCMatchReaderPhase) factoryProvider.getPhaseFactory().createMPCMatchReadPhase());
        techniqueUseCase = new ComboTechniqueUseCase((PreprocessingTechniqueFactory) factoryProvider.getTechniqueFactory());

        controller = new StatisticsReportControler(reportViewModel, presenter, techniqueViewModel);
        controller.setUseCase(useCase);

        techniqueControler = new ComboTechniqueControler(techniquePresenter, techniqueViewModel);
        techniqueControler.setComboTechniqueUseCase(techniqueUseCase);

        reportViewModel.setDisabledLoadToolsAndTechniquesFromFile(true);
        setDefaultData();
        if(MPCContext.DEFAULT_USE_SOCO_STATISTIC_REPORT)
            setDefaultDataSOCO();
        loadProcessedToolsAndTechniques();
        controller.loadDirList();
    }

    private void setDefaultDataSOCO(){
        reportViewModel.setWorkingDirPath(MPCContext.DEFAULT_WORKING_DIR_SOCO);
        reportViewModel.setSourceDirPath(MPCContext.DEFAULT_SOURCE_DIR_SOCO);
        reportViewModel.setSelectedInputDirPath(MPCContext.DEFAULT_INPUT_DIR_PATH_SOCO);
    }
    private void setDefaultData() {
        List list = new ArrayList();
        list.add(NoTechniqueOriginal.NAME);
        techniqueViewModel.setSelectedTechniques(list);
        reportViewModel.setWorkingDirPath(MPCContext.DEFAULT_WORKING_DIR);
        reportViewModel.setSourceDirPath(MPCContext.DEFAULT_SOURCE_DIR);
        reportViewModel.setInputDirDepth(MPCContext.DEFAULT_INPUT_DIR_DEPTH);
        reportViewModel.setSelectedInputDirPath(MPCContext.DEFAULT_INPUT_DIR_PATH);
        List list2 = new ArrayList();
        list2.add(JPlagJavaAdapter.NAME);
        reportViewModel.setSelectedTools(list2);
    }

    public StatisticsReportViewModel getReportViewModel() {
        return reportViewModel;
    }

    public StatisticsReportControler getController() {
        return controller;
    }

    public StatisticsReportPresenter getReportPresenter() {
        return presenter;
    }

    public StatisticsReportUseCase getReportUseCase() {
        return useCase;
    }

    public ComboTechniqueUseCase getComboUseCase() {
        return techniqueUseCase;
    }

    public ComboTechniqueControler getTechniqueController() {
        return techniqueControler;
    }

    public ComboTechniqueViewModel getTechniqueViewModel() {
        return techniqueViewModel;
    }

    public void loadProcessedToolsAndTechniques() {
        controller.loadProcessedTools();
        techniqueControler.loadProcessedTechniques(new File(reportViewModel.getWorkingDirPath()));
    }

    @Override
    public void loadToolsAndTechniquesFromFile() {

    }

    public void onStatReportRowSelect(SelectEvent event) {

    }
}
