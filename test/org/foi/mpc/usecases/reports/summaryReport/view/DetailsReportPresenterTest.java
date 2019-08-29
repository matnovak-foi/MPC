package org.foi.mpc.usecases.reports.summaryReport.view;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.foi.mpc.usecases.reports.summaryReport.DetailsReportOutputBoundary;
import org.foi.mpc.usecases.reports.summaryReport.models.DetailsReportMatchInfoResponseModel;
import org.foi.mpc.usecases.reports.summaryReport.models.DetailsReportMatchInfoResponseModel.DetailsReportToolTechniquesSimilatiry;
import org.foi.mpc.usecases.reports.summaryReport.models.DetailsReportMatchInfoResponseModel.DetailsReportToolTechniquesSimilatiry.DetailsReportMatchParts;
import org.foi.mpc.usecases.reports.summaryReport.models.SideBySideComparisonResponseModel;
import org.foi.mpc.usecases.reports.summaryReport.view.models.DetailsReportViewModel;
import org.foi.mpc.usecases.reports.summaryReport.view.models.PresentableDetailSimilarityTable;
import org.foi.mpc.usecases.reports.summaryReport.view.models.PresentableDetailsMatchPart;
import org.foi.mpc.usecases.reports.summaryReport.view.models.PresentableSideBySide;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(HierarchicalContextRunner.class)
public class DetailsReportPresenterTest  {

    DetailsReportPresenter presenter;
    DetailsReportViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new DetailsReportViewModel();
        viewModel.setSelectedWorkingDirPath("selectedWorkingDirPath");
        presenter = new DetailsReportPresenter(viewModel);
    }
    @Test
    public void isPPTestReportOutputBoundary() {
        assertTrue(presenter instanceof DetailsReportOutputBoundary);
    }

    public class DetailsInfoTest {
        @Test
        public void ifErrorMessageIsPresentDisplayIt() {
            DetailsReportMatchInfoResponseModel responseModel = new DetailsReportMatchInfoResponseModel();
            responseModel.errorMessages = "someMessage";

            presenter.updateDetailsMatchInfo(responseModel);

            assertEquals(responseModel.errorMessages, viewModel.getErrorMessage());
        }

        @Test
        public void translatesResponseModelToViewModel() {
            DetailsReportToolTechniquesSimilatiry toolTechniquesSimilatiry = new DetailsReportToolTechniquesSimilatiry();
            toolTechniquesSimilatiry.tool = "Tool";
            toolTechniquesSimilatiry.technique = "Technique";
            toolTechniquesSimilatiry.matchesDir = new File("matchesDir");
            toolTechniquesSimilatiry.similarity = 99.1555f;
            toolTechniquesSimilatiry.similarityA = 98.1444f;
            toolTechniquesSimilatiry.similarityB = 97.2344f;
            toolTechniquesSimilatiry.totalLineCountA = 100;
            toolTechniquesSimilatiry.totalLineCountB = 99;
            toolTechniquesSimilatiry.calculatedSimilarity = 100.0011f;
            toolTechniquesSimilatiry.calculatedSimilarityA = 101.543f;
            toolTechniquesSimilatiry.calculatedSimilarityB = 102.987f;
            toolTechniquesSimilatiry.matchParts = new ArrayList<>();
            DetailsReportMatchParts reportMatchParts = new DetailsReportMatchParts();
            reportMatchParts.similarity = 89.3453;
            reportMatchParts.similarityA = 89.6453;
            reportMatchParts.similarityB = 89.3653;
            reportMatchParts.startLineNumberA = 1;
            reportMatchParts.startLineNumberB = 2;
            reportMatchParts.endLineNumberA = 3;
            reportMatchParts.endLineNumberB = 4;
            reportMatchParts.lineCountA = 10;
            reportMatchParts.lineCountB = 9;
            toolTechniquesSimilatiry.matchParts.add(reportMatchParts);
            toolTechniquesSimilatiry.matchParts.add(reportMatchParts);

            DetailsReportMatchInfoResponseModel responseModel = new DetailsReportMatchInfoResponseModel();
            responseModel.toolsTechniquesList = new ArrayList<>();
            responseModel.toolsTechniquesList.add(toolTechniquesSimilatiry);
            responseModel.toolsTechniquesList.add(toolTechniquesSimilatiry);
            responseModel.errorMessages = "";

            presenter.updateDetailsMatchInfo(responseModel);

            assertPresentableDetailsReportSimilarityTable(responseModel, 0);
            assertPresentableDetailsReportSimilarityTable(responseModel, 1);

            assertEquals("", viewModel.getErrorMessage());
        }
    }

    public class SideBySideTest {
        @Test
        public void ifErrorMessageIsPresentDisplayIt() {
            SideBySideComparisonResponseModel responseModel = new SideBySideComparisonResponseModel();
            responseModel.errorMessages = "someMessage";

            presenter.updateDetailsSideBySideComparison(responseModel);

            assertEquals(responseModel.errorMessages, viewModel.getErrorMessage());
        }

        @Test
        public void updateDetailsSideBySideComparison() {
            SideBySideComparisonResponseModel responseModel = new SideBySideComparisonResponseModel();
            responseModel.errorMessages = "";
            responseModel.matchPartContentA = "contentA";
            responseModel.matchPartContentB = "contentB";

            presenter.updateDetailsSideBySideComparison(responseModel);

            assertEquals(responseModel.matchPartContentA,viewModel.getPresentableSideBySide().getContentA());
            assertEquals(responseModel.matchPartContentB,viewModel.getPresentableSideBySide().getContentB());
        }

        @Test
        public void updateDetailsSideBySideComparisonWithColor() {
            SideBySideComparisonResponseModel responseModel = new SideBySideComparisonResponseModel();
            responseModel.errorMessages = "";
            responseModel.matchPartContentA = "contentA ~~~colored~~~ some text";
            responseModel.matchPartContentB = "contentB ~~~colored~~~";

            presenter.updateDetailsSideBySideComparison(responseModel);

            assertEquals("contentA <span class=\"bad\">colored</span> some text",viewModel.getPresentableSideBySide().getContentA());
            assertEquals("contentB <span class=\"bad\">colored</span>",viewModel.getPresentableSideBySide().getContentB());
        }

        @Test
        public void updateDetailsSideBySideComparisonWithMatchNamesLinks() {
            SideBySideComparisonResponseModel responseModel = new SideBySideComparisonResponseModel();
            responseModel.errorMessages = "";
            responseModel.matchPartContentA = "<span class=\"bad\" id=\"partAM0\"> (M0)Line 1:line 1</span><br/>Line 2:line 2<br/>Line<span class=\"neutral\" id=\"partAM1\"> (M1) 3:line 3</span><br/>Line<span class=\"bad\" id=\"partAM2\"> (M2) 4:line 4</span><br/>Line<span class=\"neutral\" id=\"partAM3\"> (M3) 5:line 5</span>";
            responseModel.matchPartContentB = responseModel.matchPartContentA;

            presenter.updateDetailsSideBySideComparison(responseModel);

            assertThatLinksAreInAscendingOrder();
        }

        @Test
        public void updateDetailsSideBySideComparisonWithMatchNamesLinksReversed() {
            SideBySideComparisonResponseModel responseModel = new SideBySideComparisonResponseModel();
            responseModel.errorMessages = "";
            responseModel.matchPartContentA = "<span class=\"bad\" id=\"partAM3\"> (M3)Line 1:line 1</span><br/>Line 2:line 2<br/>Line<span class=\"neutral\" id=\"partAM2\"> (M2) 3:line 3</span><br/>Line<span class=\"bad\" id=\"partAM1\"> (M1) 4:line 4</span><br/>Line<span class=\"neutral\" id=\"partAM0\"> (M0) 5:line 5</span>";
            responseModel.matchPartContentB = responseModel.matchPartContentA;

            presenter.updateDetailsSideBySideComparison(responseModel);

            assertThatLinksAreInAscendingOrder();
        }

        @Test
        public void updateDetailsSideBySideComparisonWithMatchNamesLinksMixed() {
            SideBySideComparisonResponseModel responseModel = new SideBySideComparisonResponseModel();
            responseModel.errorMessages = "";
            responseModel.matchPartContentA = "<span class=\"bad\" id=\"partAM2\"> (M2)Line 1:line 1</span><br/>Line 2:line 2<br/>Line<span class=\"neutral\" id=\"partAM3\"> (M3) 3:line 3</span><br/>Line<span class=\"bad\" id=\"partAM0\"> (M0) 4:line 4</span><br/>Line<span class=\"neutral\" id=\"partAM1\"> (M1) 5:line 5</span>";
            responseModel.matchPartContentB = responseModel.matchPartContentA;

            presenter.updateDetailsSideBySideComparison(responseModel);

            assertThatLinksAreInAscendingOrder();
        }

        protected void assertThatLinksAreInAscendingOrder() {
            assertEquals("<a href=\"#form:detailsReport\" onclick=\"scrollToMatchPartsSideBySide('#partAM0','#partBM0')\">M0</a> " +
                            "<a href=\"#form:detailsReport\" onclick=\"scrollToMatchPartsSideBySide('#partAM1','#partBM1')\">M1</a> " +
                            "<a href=\"#form:detailsReport\" onclick=\"scrollToMatchPartsSideBySide('#partAM2','#partBM2')\">M2</a> " +
                            "<a href=\"#form:detailsReport\" onclick=\"scrollToMatchPartsSideBySide('#partAM3','#partBM3')\">M3</a> "
                    ,
                    viewModel.getPresentableSideBySide().getMatchPartLinks());
        }
    }

    protected void assertPresentableDetailsReportSimilarityTable(DetailsReportMatchInfoResponseModel responseModel, int atIndex) {
        DetailsReportToolTechniquesSimilatiry toolTechniquesSimilatiry = responseModel.toolsTechniquesList.get(atIndex);
        PresentableDetailSimilarityTable similarityTable = viewModel.getSimilarityTable().get(atIndex);
        assertEquals(toolTechniquesSimilatiry.calculatedSimilarity, similarityTable.getCalculatedSimilarity(),0.1);
        assertEquals(toolTechniquesSimilatiry.calculatedSimilarityA, similarityTable.getCalculatedSimilarityA(),0.1);
        assertEquals(toolTechniquesSimilatiry.calculatedSimilarityB, similarityTable.getCalculatedSimilarityB(),0.1);
        assertEquals(toolTechniquesSimilatiry.similarity, similarityTable.getSimilarity(),0.1);
        assertEquals(toolTechniquesSimilatiry.similarityA, similarityTable.getSimilarityA(),0.1);
        assertEquals(toolTechniquesSimilatiry.similarityB, similarityTable.getSimilarityB(),0.1);
        assertEquals(toolTechniquesSimilatiry.totalLineCountA, similarityTable.getTotalLineCountA());
        assertEquals(toolTechniquesSimilatiry.totalLineCountB, similarityTable.getTotalLineCountB());
        assertEquals(toolTechniquesSimilatiry.tool, similarityTable.getTool());
        assertEquals(toolTechniquesSimilatiry.technique, similarityTable.getTechnique());
        assertEquals(toolTechniquesSimilatiry.matchesDir.getPath(), similarityTable.getMatchesDir());
        assertEquals(toolTechniquesSimilatiry.matchParts.size(), similarityTable.getMatchParts().size());

        List<DetailsReportMatchParts> matchParts = toolTechniquesSimilatiry.matchParts;
        List<PresentableDetailsMatchPart> presentableMatchParts = similarityTable.getMatchParts();
        assertDetailsMatchPart(0, matchParts, presentableMatchParts);
        assertEquals(toolTechniquesSimilatiry.matchesDir.getPath(),presentableMatchParts.get(atIndex).getMatchesDir());
    }

    private void assertDetailsMatchPart(int atIndex, List<DetailsReportMatchParts> matchParts, List<PresentableDetailsMatchPart> presentableMatchParts) {
        assertEquals(matchParts.get(atIndex).similarity,presentableMatchParts.get(atIndex).getSimilarity(),0.1);
        assertEquals(matchParts.get(atIndex).similarityA,presentableMatchParts.get(atIndex).getSimilarityA(),0.1);
        assertEquals(matchParts.get(atIndex).similarityB,presentableMatchParts.get(atIndex).getSimilarityB(),0.1);
        assertEquals(matchParts.get(atIndex).startLineNumberA,presentableMatchParts.get(atIndex).getStartLineNumberA());
        assertEquals(matchParts.get(atIndex).startLineNumberB,presentableMatchParts.get(atIndex).getStartLineNumberB());
        assertEquals(matchParts.get(atIndex).endLineNumberA,presentableMatchParts.get(atIndex).getEndLineNumberA());
        assertEquals(matchParts.get(atIndex).endLineNumberB,presentableMatchParts.get(atIndex).getEndLineNumberB());
        assertEquals(matchParts.get(atIndex).lineCountA,presentableMatchParts.get(atIndex).getLineCountA());
        assertEquals(matchParts.get(atIndex).lineCountB,presentableMatchParts.get(atIndex).getLineCountB());

    }
}