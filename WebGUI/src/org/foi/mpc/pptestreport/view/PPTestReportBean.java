package org.foi.mpc.pptestreport.view;

import org.foi.mpc.MPCContext;
import org.foi.mpc.StandardReportBeanMethods;
import org.foi.mpc.abstractfactories.FactoryProvider;
import org.foi.mpc.executiontools.factories.PreprocessingTechniqueFactory;
import org.foi.mpc.usecases.combotechnique.ComboTechniqueUseCase;
import org.foi.mpc.usecases.combotechnique.view.ComboTechniqueControler;
import org.foi.mpc.usecases.combotechnique.view.ComboTechniquePresenter;
import org.foi.mpc.usecases.combotechnique.view.models.ComboTechniqueViewModel;
import org.foi.mpc.usecases.reports.pptestreport.view.PPTestReportController;
import org.foi.mpc.usecases.reports.pptestreport.view.PPTestReportPresenter;
import org.foi.mpc.usecases.reports.pptestreport.PPTestReportUseCase;
import org.foi.mpc.usecases.reports.pptestreport.view.models.PPTestReportViewModel;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "ppTestReportBean")
@SessionScoped
public class PPTestReportBean implements StandardReportBeanMethods {
    private ComboTechniqueControler techniqueControler;
    private ComboTechniqueViewModel techniqueViewModel;
    private ComboTechniquePresenter techniquePresenter;
    private ComboTechniqueUseCase comboUseCase;

    private PPTestReportController controller;
    private PPTestReportViewModel reportViewModel;
    private PPTestReportPresenter reportPresenter;
    private PPTestReportUseCase reportUseCase;


    public PPTestReportBean() {
         techniqueViewModel = new ComboTechniqueViewModel();
         reportViewModel = new PPTestReportViewModel();
         reportViewModel.setWorkingDirPath("D:\\java\\doktorski_rad\\ppTest\\outputData");
         reportViewModel.setSourceDirPath("D:\\java\\doktorski_rad\\ppTest\\inputData");

         reportPresenter = new PPTestReportPresenter(reportViewModel);
         techniquePresenter = new ComboTechniquePresenter(techniqueViewModel);

         FactoryProvider factoryProvider = new FactoryProvider(MPCContext.MATCHES_DIR);
         factoryProvider.createSimilarityDetectionToolFactory(MPCContext.DETECTION_TOOLS_PROPERITES_FILE);
         factoryProvider.createPreprocessingTechniqueFactory(MPCContext.TEMPLATE_EXCLUSION_PROPERTIES_FILE);
         reportUseCase = new PPTestReportUseCase(factoryProvider, MPCContext.MATCHES_DIR);

         comboUseCase = new ComboTechniqueUseCase((PreprocessingTechniqueFactory) factoryProvider.getTechniqueFactory());

         controller = new PPTestReportController(reportPresenter, reportViewModel, techniqueViewModel);
         controller.setTestReportUseCase(reportUseCase);

         techniqueControler = new ComboTechniqueControler(techniquePresenter,techniqueViewModel);
         techniqueControler.setComboTechniqueUseCase(comboUseCase);
         techniqueControler.loadComboTehniquesFromFile(MPCContext.COMBO_TECHNIQUES_PROPERTIES_FILE);

         reportViewModel.setDisabledLoadProcessedToolsAndTechniques(true);
    }

    public PPTestReportController getController() {
        return controller;
    }

    public PPTestReportViewModel getReportViewModel(){
        return reportViewModel;
    }

    protected PPTestReportPresenter getReportPresenter() {return reportPresenter;}

    protected PPTestReportUseCase getReportUseCase() {
        return reportUseCase;
    }

    public void setReportViewModel(PPTestReportViewModel reportViewModel) {
        this.reportViewModel = reportViewModel;
    }

    public ComboTechniqueUseCase getComboUseCase() {
        return comboUseCase;
    }

    public ComboTechniqueControler getTechniqueController() {
        return techniqueControler;
    }

    public ComboTechniqueViewModel getTechniqueViewModel() {
        return techniqueViewModel;
    }

    public void loadProcessedToolsAndTechniques() {

    }

    @Override
    public void loadToolsAndTechniquesFromFile() {
        techniqueControler.loadComboTehniquesFromFile(MPCContext.COMBO_TECHNIQUES_PROPERTIES_FILE);
        techniqueControler.setComboTechniqueUseCase(comboUseCase);
        controller.setTestReportUseCase(reportUseCase);
    }
}
