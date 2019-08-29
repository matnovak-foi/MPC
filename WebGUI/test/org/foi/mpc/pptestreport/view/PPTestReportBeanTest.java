package org.foi.mpc.pptestreport.view;

import org.foi.mpc.ContextListener;
import org.foi.mpc.TestContext;
import org.foi.mpc.TestServletContext;
import org.foi.mpc.abstractfactories.FactoryProvider;
import org.foi.mpc.executiontools.factories.ComboTechiniquesLoader;
import org.foi.mpc.executiontools.factories.PreprocessingTechniqueFactory;
import org.foi.mpc.executiontools.factories.SimilarityDetectionToolFactory;
import org.foi.mpc.usecases.combotechnique.ComboTechniqueUseCase;
import org.foi.mpc.usecases.combotechnique.view.ComboTechniqueControler;
import org.foi.mpc.usecases.reports.pptestreport.view.PPTestReportController;
import org.foi.mpc.usecases.reports.pptestreport.view.PPTestReportPresenter;
import org.foi.mpc.usecases.reports.pptestreport.PPTestReportUseCase;
import org.foi.mpc.usecases.reports.pptestreport.view.models.PPTestReportViewModel;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.io.File;

import static org.junit.Assert.*;

public class PPTestReportBeanTest {

    PPTestReportBean bean;

    @Before
    public void setUp(){
        new ContextListener().contextInitialized(new ServletContextEvent(new TestServletContext()));
        bean = new PPTestReportBean();
    }

    @Test
    public void beanCreatesController(){
        assertTrue(bean.getController() instanceof PPTestReportController);
    }

    @Test
    public void beanCreatesViewModel(){
        assertTrue(bean.getReportViewModel() instanceof PPTestReportViewModel);
    }

    @Test
    public void beanCreatesPresenter(){
        assertTrue(bean.getReportPresenter() instanceof PPTestReportPresenter);
    }

    @Test
    public void beanCreatesUseCase(){
        assertTrue(bean.getReportUseCase() instanceof PPTestReportUseCase);
        assertTrue(bean.getComboUseCase() instanceof ComboTechniqueUseCase);
    }

    @Test
    public void beanPassesUseCasesToControllers(){
        PPTestReportController controller = bean.getController();
        ComboTechniqueControler techniqueControler = bean.getTechniqueController();
        assertSame(bean.getReportUseCase(), controller.getTestReportUseCase());
        assertSame(bean.getComboUseCase(), techniqueControler.getUseCase());
    }

    @Test
    public void beanPassesViewModelToController(){
        PPTestReportController controller = bean.getController();
        assertSame(bean.getReportViewModel(),controller.getViewModel());
    }

    @Test
    public void beanPassesPresenterToController(){
        PPTestReportController controller = bean.getController();
        assertSame(bean.getReportPresenter(),controller.getPresenter());
    }

    @Test
    public void everyBeanHasItsOwnInstanceOfFactorieProvider(){
        PPTestReportBean bean2 = new PPTestReportBean();
        assertNotSame(bean.getReportUseCase().getProvider(), bean2.getReportUseCase().getProvider());
    }

    @Test
    public void passesCorrectConfigurationFileToSimilarityDetectionTool(){
        PPTestReportUseCase useCase = bean.getReportUseCase();
        FactoryProvider provider = useCase.getProvider();
        SimilarityDetectionToolFactory toolFactory = (SimilarityDetectionToolFactory) provider.getToolFactory();
        File file = toolFactory.getDetectionToolConfigFileName();

        assertEquals(TestContext.doktorskiRadDir+"MPC"+File.separator+"detectionToolsConfiguration.properties",file.getPath());
    }

    @Test
    public void passesCorrectConfigurationToTemplateExclusionTechnique(){
        PPTestReportUseCase useCase = bean.getReportUseCase();
        FactoryProvider provider = useCase.getProvider();
        PreprocessingTechniqueFactory toolFactory = (PreprocessingTechniqueFactory) provider.getTechniqueFactory();
        File file =  toolFactory.getTemplateExclusionConfigFile();
        assertEquals(TestContext.doktorskiRadDir+"MPC"+File.separator+"templateExclusionConfiguration.properties",file.getPath());
    }

    @Test
    public void loadsComboTechniquesFromFile(){
        ComboTechniqueUseCase useCase = bean.getComboUseCase();
        ComboTechiniquesLoader comboLoader = useCase.getComboTechniqueLoader();

        assertEquals(TestContext.doktorskiRadDir+"MPC"+File.separator+"comboTehniques.properties",comboLoader.getComboTechniqueLoadedFile().getPath());
    }
}
