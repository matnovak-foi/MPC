package org.foi.mpc.summaryReport.view;

import org.foi.common.cookie.CookieHelper;
import org.foi.mpc.*;
import org.foi.mpc.abstractfactories.FactoryProvider;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagAdapter;
import org.foi.mpc.executiontools.factories.ComboTechiniquesLoader;
import org.foi.mpc.executiontools.factories.PreprocessingTechniqueFactory;
import org.foi.mpc.executiontools.factories.SimilarityDetectionToolFactory;
import org.foi.mpc.usecases.UseCaseResponseErrorMessages;
import org.foi.mpc.usecases.combotechnique.ComboTechniqueUseCase;
import org.foi.mpc.usecases.combotechnique.view.ComboTechniqueControler;
import org.foi.mpc.usecases.combotechnique.view.models.ComboTechniqueViewModel;
import org.foi.mpc.usecases.reports.summaryReport.SummaryReportUseCase;
import org.foi.mpc.usecases.reports.summaryReport.view.SummaryReportControler;
import org.foi.mpc.usecases.reports.summaryReport.view.SummaryReportPresenter;
import org.foi.mpc.usecases.reports.summaryReport.view.models.SummaryReportViewModel;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletContextEvent;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class SummaryReportBeanTest {
    SummaryReportBean bean;
    CookieHelper cookieHelper;
    @Before
    public void setUp(){
        CookieHelper.testMode = true;
        cookieHelper = new CookieHelper();
        HttpServletRequestMock requestMock = new HttpServletRequestMock();
        HttpServletResponseMock responseMock = new HttpServletResponseMock();
        cookieHelper.setRequest(requestMock);
        cookieHelper.setResponse(responseMock);

        new ContextListener().contextInitialized(new ServletContextEvent(new TestServletContext()));
        bean = new SummaryReportBean();
        bean.setCookieHelper(cookieHelper);
    }

    @Test
    public void beanCreatesController(){
        assertTrue(bean.getController() instanceof SummaryReportControler);
    }

    @Test
    public void beanCreatesViewModel(){
        assertTrue(bean.getReportViewModel() instanceof SummaryReportViewModel);
    }

    @Test
    public void beanCreatesPresenter(){
        assertTrue(bean.getReportPresenter() instanceof SummaryReportPresenter);
    }

    @Test
    public void beanCreatesUseCase(){
        assertTrue(bean.getReportUseCase() instanceof SummaryReportUseCase);
        assertTrue(bean.getComboUseCase() instanceof ComboTechniqueUseCase);
    }

    @Test
    public void beanPassesUseCaseToController(){
        SummaryReportControler controller = bean.getController();
        ComboTechniqueControler techniqueControler = bean.getTechniqueController();
        assertSame(bean.getReportUseCase(), controller.getSummaryReportUseCase());
        assertSame(bean.getComboUseCase(), techniqueControler.getUseCase());
    }

    @Test
    public void beanPassesViewModelToController(){
        SummaryReportControler controller = bean.getController();
        assertSame(bean.getReportViewModel(),controller.getViewModel());
    }

    @Test
    public void beanOnCallGenerateReportCallsControllerGenerateReport(){
        SummaryReportControllerSpy controler = new SummaryReportControllerSpy(null, null, null);
        bean.setController(controler);
        bean.setCookiesAndGenerateReport();
        assertTrue(controler.wasCalled);

        HttpServletResponseMock response = (HttpServletResponseMock) cookieHelper.getResponse();
        assertNotNull(response.cookie);
        assertEquals("mpcData",response.cookie.getName());
        assertEquals(bean.getReportViewModel().getSelectedTools().get(0)+":"+bean.getReportViewModel().getSelectedInputDirPath()+":"+bean.getTechniqueViewModel().getSelectedTechniques().get(0),response.cookie.getValue());
    }

    @Test
    public void loadDataIsDone(){
        HttpServletRequestMock request = (HttpServletRequestMock) cookieHelper.getRequest();
        request.cookie = new Cookie[1];
        request.cookie[0] = new Cookie("mpcData","Tool:Path:Technique");
        bean.loadCookies();
        assertEquals("Technique",bean.getTechniqueViewModel().getSelectedTechniques().get(0));
        assertEquals("Tool",bean.getReportViewModel().getSelectedTools().get(0));
        assertEquals("Path",bean.getReportViewModel().getSelectedInputDirPath());
    }


    public class SummaryReportControllerSpy extends SummaryReportControler {
        public boolean wasCalled = false;

        public SummaryReportControllerSpy (SummaryReportViewModel viewModel, SummaryReportPresenter presenter, ComboTechniqueViewModel techniqueViewModel){
            super(viewModel,presenter,techniqueViewModel);
        }

        @Override
        public void generateReport(){
            wasCalled = true;
        }
    }

    @Test
    public void beanPassesPresenterToController(){
        SummaryReportControler controller = bean.getController();
        assertSame(bean.getReportPresenter(),controller.getPresenter());
    }

    @Test
    public void everyBeanHasItsOwnInstanceOfFactorieProvider(){
        SummaryReportBean bean2 = new SummaryReportBean();
        assertNotSame(bean.getReportUseCase().getProvider(), bean2.getReportUseCase().getProvider());
    }

    @Test
    public void passesCorrectConfigurationFileToSimilarityDetectionTool(){
        SummaryReportUseCase useCase = bean.getReportUseCase();
        FactoryProvider provider = useCase.getProvider();
        SimilarityDetectionToolFactory toolFactory = (SimilarityDetectionToolFactory) provider.getToolFactory();
        File file = toolFactory.getDetectionToolConfigFileName();
        assertEquals(TestContext.doktorskiRadDir+"MPC"+File.separator+"detectionToolsConfiguration.properties",file.getPath());
    }

    @Test
    public void passesCorrectConfigurationToTemplateExclusionTechnique(){
        SummaryReportUseCase useCase = bean.getReportUseCase();
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

    @Test
    public void beanCallsLoadsProcessedToolsAndTechniques(){
        bean.getReportViewModel().setWorkingDirPath("invalidDirPath");
        bean.loadProcessedToolsAndTechniques();
        assertEquals(UseCaseResponseErrorMessages.invalidWorkingDir,bean.getReportViewModel().getErrorMessage());
        assertEquals(UseCaseResponseErrorMessages.invalidWorkingDir,bean.getTechniqueViewModel().getErrorMessage());
        assertEquals(new ArrayList<>(),bean.getTechniqueViewModel().getAvalibleComboTechniques());
    }

}
