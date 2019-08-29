package org.foi.mpc.usecases.reports.summaryReport.view;

import com.sun.org.glassfish.external.statistics.Statistic;
import com.sun.org.glassfish.external.statistics.impl.StatisticImpl;
import org.foi.common.MathHelper;
import org.foi.mpc.usecases.reports.summaryReport.DetailsReportOutputBoundary;
import org.foi.mpc.usecases.reports.summaryReport.models.DetailsReportMatchInfoResponseModel;
import org.foi.mpc.usecases.reports.summaryReport.models.DetailsReportMatchInfoResponseModel.DetailsReportToolTechniquesSimilatiry.DetailsReportMatchParts;
import org.foi.mpc.usecases.reports.summaryReport.models.SideBySideComparisonResponseModel;
import org.foi.mpc.usecases.reports.summaryReport.view.models.DetailsReportViewModel;
import org.foi.mpc.usecases.reports.summaryReport.view.models.PresentableDetailSimilarityTable;
import org.foi.mpc.usecases.reports.summaryReport.view.models.PresentableDetailsMatchPart;
import org.foi.mpc.usecases.reports.summaryReport.view.models.PresentableSideBySide;

import java.util.*;

public class DetailsReportPresenter implements DetailsReportOutputBoundary {
    private DetailsReportViewModel viewModel;

    public DetailsReportPresenter(DetailsReportViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void updateDetailsMatchInfo(DetailsReportMatchInfoResponseModel responseModel) {
        if(responseModel.errorMessages.isEmpty()) {
            List<PresentableDetailSimilarityTable> similarityTableList = new ArrayList<>();

            for(DetailsReportMatchInfoResponseModel.DetailsReportToolTechniquesSimilatiry similatiry :responseModel.toolsTechniquesList) {
                PresentableDetailSimilarityTable similarityTableRow = new PresentableDetailSimilarityTable();

                similarityTableRow.setTool(String.valueOf(similatiry.tool));
                similarityTableRow.setTechnique(String.valueOf(similatiry.technique));
                similarityTableRow.setMatchesDir(similatiry.matchesDir.getPath());
                similarityTableRow.setSimilarity(similatiry.similarity);
                similarityTableRow.setSimilarityA(similatiry.similarityA);
                similarityTableRow.setSimilarityB(similatiry.similarityB);
                similarityTableRow.setCalculatedSimilarity(similatiry.calculatedSimilarity);
                similarityTableRow.setCalculatedSimilarityA(similatiry.calculatedSimilarityA);
                similarityTableRow.setCalculatedSimilarityB(similatiry.calculatedSimilarityB);
                similarityTableRow.setTotalLineCountA(similatiry.totalLineCountA);
                similarityTableRow.setTotalLineCountB(similatiry.totalLineCountB);

                List<PresentableDetailsMatchPart> presentableMatchParts = new ArrayList<>();
                for(DetailsReportMatchParts matchPart : similatiry.matchParts){
                    PresentableDetailsMatchPart presentableDetailsMatchPart = new PresentableDetailsMatchPart();
                    presentableDetailsMatchPart.setSimilarity(matchPart.similarity);
                    presentableDetailsMatchPart.setSimilarityA(matchPart.similarityA);
                    presentableDetailsMatchPart.setSimilarityB(matchPart.similarityB);
                    presentableDetailsMatchPart.setStartLineNumberA(matchPart.startLineNumberA);
                    presentableDetailsMatchPart.setStartLineNumberB(matchPart.startLineNumberB);
                    presentableDetailsMatchPart.setEndLineNumberA(matchPart.endLineNumberA);
                    presentableDetailsMatchPart.setEndLineNumberB(matchPart.endLineNumberB);
                    presentableDetailsMatchPart.setLineCountA(matchPart.lineCountA);
                    presentableDetailsMatchPart.setLineCountB(matchPart.lineCountB);
                    presentableDetailsMatchPart.setMatchesDir(similatiry.matchesDir.getPath());

                    presentableMatchParts.add(presentableDetailsMatchPart);
                }
                similarityTableRow.setMatchParts(presentableMatchParts);

                similarityTableList.add(similarityTableRow);
            }
            this.viewModel.setSimilarityTable(similarityTableList);
        }

        this.viewModel.setErrorMessage(responseModel.errorMessages);
    }

    @Override
    public void updateDetailsSideBySideComparison(SideBySideComparisonResponseModel responseModel) {
        PresentableSideBySide presentableSideBySide = new PresentableSideBySide();

        if(responseModel.errorMessages.isEmpty()) {
            presentableSideBySide.setContentA(colorChanges(responseModel.matchPartContentA));
            presentableSideBySide.setContentB(colorChanges(responseModel.matchPartContentB));
            presentableSideBySide.setMatchPartLinks(createLinks(responseModel.matchPartContentA));
        }

        this.viewModel.setPresentableSideBySide(presentableSideBySide);
        this.viewModel.setErrorMessage(responseModel.errorMessages);
    }

    private String createLinks(String content){
        List<String> links = new ArrayList<>();
        String[] matchParts = content.split("id=\"partAM");

        if(matchParts.length > 1) {
            boolean skipFirst = true;
            for (String part : matchParts) {
                if(skipFirst) {
                    skipFirst = false;
                    continue;
                }
                String[] partNumbers = part.split("\"> \\(M");
                int partNumber = Integer.parseInt(partNumbers[0]);
                links.add("<a href=\"#form:detailsReport\" " +
                        "onclick=\"scrollToMatchPartsSideBySide('#partAM"+partNumber+"','#partBM"+partNumber+"')\">M"
                        + partNumber + "</a> ");
            }
        }
        Collections.sort(links, (a, b) -> {
            int numberA = Integer.parseInt(a.split(">M")[1].split("</a>")[0]);
            int numberB = Integer.parseInt(b.split(">M")[1].split("</a>")[0]);
            return numberA > numberB ? 1 : -1;
        });
        String result = "";
        for(String link : links)
            result+=link;
        return result;
    }


    protected String colorChanges(String contentA) {
        String resultA = "";
        int startRead = 0;
        boolean openTag = true;
        for (int i = -1; (i = contentA.indexOf("~~~", i + 1)) != -1; i++) {
            if(openTag) {
                resultA += contentA.substring(startRead, i) + "<span class=\"bad\">";
                openTag = false;
            } else {
                resultA += contentA.substring(startRead, i) + "</span>";
                openTag = true;
            }
            startRead = i + "~~~".length();
        }
        resultA += contentA.substring(startRead,contentA.length());
        return resultA;
    }
}
