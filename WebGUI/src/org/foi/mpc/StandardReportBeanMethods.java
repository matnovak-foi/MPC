package org.foi.mpc;

import org.foi.mpc.usecases.combotechnique.view.ComboTechniqueControler;
import org.foi.mpc.usecases.combotechnique.view.models.ComboTechniqueViewModel;
import org.foi.mpc.usecases.reports.summaryReport.view.models.DetailsReportViewModel;

public interface StandardReportBeanMethods <T> {
    public void loadProcessedToolsAndTechniques();
    public void loadToolsAndTechniquesFromFile();
    public ComboTechniqueControler getTechniqueController();
    public ComboTechniqueViewModel getTechniqueViewModel();
    public T getReportViewModel();
}
