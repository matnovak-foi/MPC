package org.foi.mpc;

import org.foi.mpc.executiontools.techniques.sherlock.SherlockNoWhiteSpacesPPTechnique;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagJavaAdapter;
import org.foi.mpc.executiontools.techniques.NoTechniqueOriginal;
import org.foi.mpc.executiontools.techniques.sherlock.SherlockNoCommentsPPTechnique;
import org.foi.mpc.pptestreport.view.PPTestReportBean;
import org.foi.mpc.usecases.combotechnique.view.models.ComboTechniqueViewModel;
import org.foi.mpc.usecases.combotechnique.view.models.ComboTechniqueViewModelBuilder;
import org.foi.mpc.usecases.reports.pptestreport.view.models.PPTestReportViewModelBuilder;
import org.foi.mpc.usecases.reports.pptestreport.view.models.PPTestReportViewModel;
import org.foi.mpc.usecases.reports.pptestreport.view.models.PresentableReport;
import org.foi.mpc.usecases.reports.pptestreport.view.models.PresentableReportPPTechnique;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletContextEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.text.IsEmptyString.isEmptyString;
import static org.junit.Assert.*;

public class ViewAcceptanceTests {
    File workingDir = new File("workingDir");
    File rootDir;
    PPTestReportViewModelBuilder viewModelBuilder;
    ComboTechniqueViewModelBuilder techniqueViewModelBuilder;
    PPTestReportBean bean;
    PPTestReportViewModel viewModel;
    ComboTechniqueViewModel techniqueViewModel;

    @Before
    public void setUp() throws IOException {
        workingDir.mkdir();

        rootDir = new File(System.getProperty("user.dir"));
        if (rootDir.getPath().contains("WebGUI"))
            rootDir = rootDir.getParentFile();

        new ContextListener().contextInitialized(new ServletContextEvent(new TestServletContext()));

        bean = new PPTestReportBean();
        viewModel = bean.getReportViewModel();
        techniqueViewModel = bean.getTechniqueViewModel();

        viewModelBuilder = new PPTestReportViewModelBuilder().updateModel(viewModel)
                .withSourceDir(rootDir.getPath() + File.separator + "testInputData" + File.separator + "ppTestFile")
                .withWorkingDir(workingDir.getPath())
                .withTool(JPlagJavaAdapter.NAME);
        techniqueViewModelBuilder = new ComboTechniqueViewModelBuilder().updateModel(techniqueViewModel);
    }

    @After
    public void tearDown() throws IOException {
        DirectoryFileUtility.deleteDirectoryTree(workingDir);
    }

    @Test
    public void PPreportForTestPairCanBeRunTwice(){
        techniqueViewModelBuilder.withSelectedTechnique(NoTechniqueOriginal.NAME)
                .withSelectedTechnique(SherlockNoCommentsPPTechnique.NAME)
                .build();

        bean.getController().generateReport();
        bean.getController().generateReport();

        PresentableReport report = viewModel.getReport();
        List<PresentableReportPPTechnique> ppTechniques = report.getPpTechniques();
        assertEquals(JPlagJavaAdapter.NAME, report.getToolName());
        assertThat(ppTechniques, hasSize(2));
        assertEquals(NoTechniqueOriginal.NAME, ppTechniques.get(0).getName());
        assertEquals(SherlockNoCommentsPPTechnique.NAME, ppTechniques.get(1).getName());
        assertDataForFirstTechniqueAreOk(report, ppTechniques);
    }

    @Test
    public void PPreportForTestPairWithJPlag() {
        techniqueViewModelBuilder.withSelectedTechnique(NoTechniqueOriginal.NAME)
                        .withSelectedTechnique(SherlockNoCommentsPPTechnique.NAME)
                        .build();

        bean.getController().generateReport();

        PresentableReport report = viewModel.getReport();
        List<PresentableReportPPTechnique> ppTechniques = report.getPpTechniques();
        assertEquals(JPlagJavaAdapter.NAME, report.getToolName());
        assertThat(ppTechniques, hasSize(2));
        assertEquals(NoTechniqueOriginal.NAME, ppTechniques.get(0).getName());
        assertEquals(SherlockNoCommentsPPTechnique.NAME, ppTechniques.get(1).getName());
        assertDataForFirstTechniqueAreOk(report, ppTechniques);
    }

    @Test
    public void runDetectionWithComboTechnique(){
        String comboTechnique1Name = "Test";
        techniqueViewModelBuilder.withSelectedTechnique(NoTechniqueOriginal.NAME)
                .withSelectedTechnique(SherlockNoCommentsPPTechnique.NAME)
                .build();
        bean.getTechniqueController().addTechniqueToCombo();

        techniqueViewModelBuilder.withNewComboTechniqueName(comboTechnique1Name).build();
        bean.getTechniqueController().createComboTechnique();

        String comboTechnique2Name = "Test2";
        techniqueViewModelBuilder.withSelectedTechnique(NoTechniqueOriginal.NAME)
                .withSelectedTechnique(SherlockNoCommentsPPTechnique.NAME)
                .withSelectedTechnique(SherlockNoWhiteSpacesPPTechnique.NAME)
                .build();
        bean.getTechniqueController().addTechniqueToCombo();

        techniqueViewModelBuilder.withNewComboTechniqueName(comboTechnique2Name).build();
        bean.getTechniqueController().createComboTechnique();

        techniqueViewModelBuilder.withNoTechniqueSelected()
                        .withSelectedComboTechnique(comboTechnique1Name)
                        .withSelectedComboTechnique(comboTechnique2Name)
                        .build();
        bean.getController().generateReport();

        PresentableReport report = viewModel.getReport();
        List<PresentableReportPPTechnique> ppTechniques = report.getPpTechniques();
        assertEquals(JPlagJavaAdapter.NAME, report.getToolName());
        assertThat(ppTechniques, hasSize(2));
        assertEquals(comboTechnique1Name, ppTechniques.get(0).getName());
        assertEquals(comboTechnique2Name, ppTechniques.get(1).getName());
        assertDataForFirstTechniqueAreOk(report, ppTechniques);
    }

    private void assertDataForFirstTechniqueAreOk(PresentableReport report, List<PresentableReportPPTechnique> ppTechniques) {
        assertEquals("student1", report.getUsernameA());
        assertEquals("student2", report.getUsernameB());
        assertNotNull(ppTechniques.get(0).getSimilarityA());
        assertNotNull(ppTechniques.get(0).getSimilarityB());
        assertNotNull(ppTechniques.get(0).getSimilarity());
        assertNotNull(ppTechniques.get(1).getSimilarityA());
        assertNotNull(ppTechniques.get(1).getSimilarityB());
        assertNotNull(ppTechniques.get(1).getSimilarity());
        assertThat(viewModel.getErrorMessage(), isEmptyString());
    }
}
