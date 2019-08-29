package org.foi.mpc.summaryReport.view;

import org.foi.mpc.abstractfactories.FactoryProvider;
import org.foi.mpc.usecases.combotechnique.ComboTechniqueUseCase;
import org.foi.mpc.usecases.combotechnique.view.ComboTechniqueControler;
import org.foi.mpc.usecases.reports.summaryReport.DetailsReportUseCase;
import org.foi.mpc.usecases.reports.summaryReport.SummaryReportUseCase;
import org.foi.mpc.usecases.reports.summaryReport.view.DetailsReportControler;
import org.foi.mpc.usecases.reports.summaryReport.view.DetailsReportPresenter;
import org.foi.mpc.usecases.reports.summaryReport.view.SummaryReportControler;
import org.foi.mpc.usecases.reports.summaryReport.view.SummaryReportPresenter;
import org.foi.mpc.usecases.reports.summaryReport.view.models.DetailsReportViewModel;
import org.foi.mpc.usecases.reports.summaryReport.view.models.SummaryReportViewModel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SummaryReportDetailBeanTest {
    SummaryReportDetailBean bean;

    @Before
    public void setUp(){
        bean = new SummaryReportDetailBean();
    }

    @Test
    public void beanCreatesController(){
        assertTrue(bean.getController() instanceof DetailsReportControler);
    }

    @Test
    public void beanCreatesViewModel(){
        assertTrue(bean.getReportViewModel() instanceof DetailsReportViewModel);
    }

    @Test
    public void beanCreatesPresenter(){
        assertTrue(bean.getReportPresenter() instanceof DetailsReportPresenter);
    }

    @Test
    public void beanCreatesUseCase(){
        assertTrue(bean.getReportUseCase() instanceof DetailsReportUseCase);
    }

    @Test
    public void beanPassesUseCaseToController(){
        DetailsReportControler controller = bean.getController();
        assertSame(bean.getReportUseCase(), controller.getUseCase());
    }

    @Test
    public void beanPassesViewModelToController(){
        DetailsReportControler controller = bean.getController();
        assertSame(bean.getReportViewModel(),controller.getViewModel());
    }

    @Test
    public void beanPassesPresenterToController(){
        DetailsReportControler controller = bean.getController();
        assertSame(bean.getReportPresenter(),controller.getPresenter());
    }
}