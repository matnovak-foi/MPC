package org.foi.mpc.statisticsReport.view;

import org.foi.mpc.ContextListener;
import org.foi.mpc.TestServletContext;
import org.foi.mpc.usecases.UseCaseResponseErrorMessages;
import org.foi.mpc.usecases.combotechnique.ComboTechniqueUseCase;
import org.foi.mpc.usecases.combotechnique.view.ComboTechniqueControler;
import org.foi.mpc.usecases.reports.statisticsReport.StatisticsReportUseCase;
import org.foi.mpc.usecases.reports.statisticsReport.view.StatisticsReportControler;
import org.foi.mpc.usecases.reports.statisticsReport.view.StatisticsReportPresenter;
import org.foi.mpc.usecases.reports.statisticsReport.view.model.StatisticsReportViewModel;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletContextEvent;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class StatisticsReportBeanTest {
    StatisticsReportBean bean;

    @Before
    public void setUp(){
        new ContextListener().contextInitialized(new ServletContextEvent(new TestServletContext()));
        bean = new StatisticsReportBean();
    }

    @Test
    public void beanCreatesController(){
        assertTrue(bean.getController() instanceof StatisticsReportControler);
    }

    @Test
    public void beanCreatesViewModel(){
        assertTrue(bean.getReportViewModel() instanceof StatisticsReportViewModel);
    }

    @Test
    public void beanCreatesPresenter(){
        assertTrue(bean.getReportPresenter() instanceof StatisticsReportPresenter);
    }

    @Test
    public void beanCreatesUseCase(){
        assertTrue(bean.getReportUseCase() instanceof StatisticsReportUseCase);
        assertTrue(bean.getComboUseCase() instanceof ComboTechniqueUseCase);
    }

    @Test
    public void beanPassesUseCaseToController(){
        StatisticsReportControler controller = bean.getController();
        ComboTechniqueControler techniqueControler = bean.getTechniqueController();
        assertSame(bean.getReportUseCase(), controller.getStatisticsReportUseCase());
        assertSame(bean.getComboUseCase(), techniqueControler.getUseCase());
    }

    @Test
    public void beanPassesViewModelToController(){
        StatisticsReportControler controller = bean.getController();
        assertSame(bean.getReportViewModel(),controller.getViewModel());
        assertSame(bean.getTechniqueViewModel(),controller.getTechniquesViewModel());
    }

    @Test
    public void beanPassesPresenterToController(){
        StatisticsReportControler controller = bean.getController();
        assertSame(bean.getReportPresenter(),controller.getPresenter());
    }

    @Test
    public void beanPassesViewModelToPresenter(){
        StatisticsReportPresenter presenter = bean.getReportPresenter();
        assertSame(bean.getReportViewModel(),presenter.getViewModel());
    }

    @Test
    public void beanCallsLoadsProcessedToolsAndTechniques(){
        bean.getReportViewModel().setWorkingDirPath("invalidDirPath");
        bean.loadProcessedToolsAndTechniques();
        assertEquals(UseCaseResponseErrorMessages.invalidWorkingDir,bean.getReportViewModel().getErrorMessage());
        assertEquals(UseCaseResponseErrorMessages.invalidWorkingDir,bean.getTechniqueViewModel().getErrorMessage());

    }
}