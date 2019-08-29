package org.foi.mpc.usecases.reports.summaryReport;


import difflib.DiffRow;
import difflib.DiffRowGenerator;
import difflib.myers.DiffException;
import org.foi.common.filesystem.file.TextFileUtility;
import org.foi.mpc.matches.MPCMatchFileUtility;
import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.matches.models.MPCMatchPart;
import org.foi.mpc.usecases.UseCaseResponseErrorMessages;
import org.foi.mpc.usecases.reports.summaryReport.models.DetailsReportMatchInfoRequestModel;
import org.foi.mpc.usecases.reports.summaryReport.models.DetailsReportMatchInfoResponseModel;
import org.foi.mpc.usecases.reports.summaryReport.models.DetailsReportMatchInfoResponseModel.DetailsReportToolTechniquesSimilatiry;
import org.foi.mpc.usecases.reports.summaryReport.models.DetailsReportMatchInfoResponseModel.DetailsReportToolTechniquesSimilatiry.DetailsReportMatchParts;
import org.foi.mpc.usecases.reports.summaryReport.models.SideBySideCompariosnRequestModel;
import org.foi.mpc.usecases.reports.summaryReport.models.SideBySideComparisonResponseModel;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DetailsReportUseCase implements DetailsReportInputBoundary {
    @Override
    public void generateDetailInfoForMatch(DetailsReportMatchInfoRequestModel requestModel, DetailsReportOutputBoundary presenter) {
        DetailsInfo detailsInfo = new DetailsInfo();
        detailsInfo.generateDetailInfoForMatch(requestModel, presenter);
    }

    @Override
    public void generateMatchPartSidBySideComparion(SideBySideCompariosnRequestModel requestModel, DetailsReportOutputBoundary presenter) {
        SideBySideInfo sideBySideInfo = new SideBySideInfo();
        sideBySideInfo.generateSidBySideComparion(requestModel, presenter);
    }

    @Override
    public void generateFullFileSidBySideComparion(SideBySideCompariosnRequestModel requestModel, DetailsReportOutputBoundary presenter) {
        SideBySideInfo sideBySideInfo = new SideBySideInfo();
        sideBySideInfo.generateFullFilesSidBySideComparion(requestModel, presenter);
    }

    private class DetailsInfo {
        DetailsReportMatchInfoResponseModel responseModel;

        private void generateDetailInfoForMatch(DetailsReportMatchInfoRequestModel requestModel, DetailsReportOutputBoundary presenter) {
            createEmptyResponseModel();

            if (isRequestMissingData(requestModel)) {
                presenter.updateDetailsMatchInfo(responseModel);
                return;
            }

            String relativeMatchesDirSelectedTool = relativeMatchesDirPath(requestModel);
            if (relativeMatchesDirSelectedTool == null) {
                responseModel.errorMessages = UseCaseResponseErrorMessages.invalidMatchesDir + ":" + requestModel.selectedMatchesDir;
                presenter.updateDetailsMatchInfo(responseModel);
                return;
            }

            MPCMatchFileUtility mfu = new MPCMatchFileUtility();
            for (String tool : requestModel.toolList) {
                for (String technique : requestModel.techniqueList) {
                    File matchDir = getMatchDir(requestModel, relativeMatchesDirSelectedTool, tool, technique);

                    MPCMatch match = mfu.readFromFile(matchDir, requestModel.selectedFileAName, requestModel.selectedFileBName);
                    if (!matchDir.exists()) {
                        responseModel.errorMessages = UseCaseResponseErrorMessages.invalidMatchesDir + ":" + matchDir;
                    }

                    DetailsReportToolTechniquesSimilatiry toolTechniquesSimilatiry = new DetailsReportToolTechniquesSimilatiry();

                    toolTechniquesSimilatiry.tool = tool;
                    toolTechniquesSimilatiry.technique = technique;
                    toolTechniquesSimilatiry.matchesDir = matchDir;
                    toolTechniquesSimilatiry = prepareMatchDataForResponse(match, toolTechniquesSimilatiry);

                    responseModel.toolsTechniquesList.add(toolTechniquesSimilatiry);
                }
            }

            presenter.updateDetailsMatchInfo(responseModel);
        }

        private File getMatchDir(DetailsReportMatchInfoRequestModel requestModel, String relativeMatchesDirSelectedTool, String tool, String technique) {
            String relativeMatchesDir = relativeMatchesDirSelectedTool.replaceAll(requestModel.selectedTool, tool);
            relativeMatchesDir = relativeMatchesDir.replaceAll(requestModel.selectedTechnique, technique);

            return new File(requestModel.selectedWorkingDirPath.getPath() + relativeMatchesDir);
        }

        private DetailsReportToolTechniquesSimilatiry prepareMatchDataForResponse(MPCMatch match, DetailsReportToolTechniquesSimilatiry toolTechniquesSimilatiry) {
            toolTechniquesSimilatiry.similarity = match.similarity;
            toolTechniquesSimilatiry.similarityA = match.similarityA;
            toolTechniquesSimilatiry.similarityB = match.similarityB;
            toolTechniquesSimilatiry.calculatedSimilarity = match.calculatedSimilarity;
            toolTechniquesSimilatiry.calculatedSimilarityA = match.calculatedSimilarityA;
            toolTechniquesSimilatiry.calculatedSimilarityB = match.calculatedSimilarityB;
            toolTechniquesSimilatiry.totalLineCountA = 0;
            toolTechniquesSimilatiry.totalLineCountB = 0;

            toolTechniquesSimilatiry.matchParts = prepareMatchPartsForResponse(match, toolTechniquesSimilatiry);

            return toolTechniquesSimilatiry;
        }

        private List<DetailsReportMatchParts> prepareMatchPartsForResponse(MPCMatch match, DetailsReportToolTechniquesSimilatiry toolTechniquesSimilatiry) {
            toolTechniquesSimilatiry.matchParts = new ArrayList<>();
            List<Integer> totalLineCountA = new ArrayList<>();
            List<Integer> totalLineCountB = new ArrayList<>();
            for (MPCMatchPart matchPart : match.matchParts) {
                DetailsReportMatchParts reportMatchPart = new DetailsReportMatchParts();
                reportMatchPart.similarity = matchPart.similarity;
                reportMatchPart.similarityA = matchPart.similarityA;
                reportMatchPart.similarityB = matchPart.similarityB;
                reportMatchPart.startLineNumberA = matchPart.startLineNumberA;
                reportMatchPart.startLineNumberB = matchPart.startLineNumberB;
                reportMatchPart.endLineNumberA = matchPart.endLineNumberA;
                reportMatchPart.endLineNumberB = matchPart.endLineNumberB;
                reportMatchPart.lineCountA = matchPart.endLineNumberA - matchPart.startLineNumberA+1;
                reportMatchPart.lineCountB = matchPart.endLineNumberB - matchPart.startLineNumberB+1;
                toolTechniquesSimilatiry.matchParts.add(reportMatchPart);

                totalLineCountA = MPCMatchFileUtility.addLinesToTotalLineCount(matchPart.startLineNumberA, matchPart.endLineNumberA,totalLineCountA);
                totalLineCountB = MPCMatchFileUtility.addLinesToTotalLineCount(matchPart.startLineNumberB, matchPart.endLineNumberB,totalLineCountB);
            }
            toolTechniquesSimilatiry.totalLineCountA = totalLineCountA.size();
            toolTechniquesSimilatiry.totalLineCountB = totalLineCountB.size();
            return toolTechniquesSimilatiry.matchParts;
        }



        private String relativeMatchesDirPath(DetailsReportMatchInfoRequestModel requestModel) {
            int i = requestModel.selectedMatchesDir.getPath().indexOf("detection") - 1;
            if (i < 0)
                return null;
            int j = requestModel.selectedMatchesDir.getPath().length();
            return requestModel.selectedMatchesDir.getPath().substring(i, j);
        }

        private void addSelectedToolTechniqueToReport(String tool, String technique, MPCMatch match) {
            DetailsReportToolTechniquesSimilatiry toolTechniquesSimilatiry = new DetailsReportToolTechniquesSimilatiry();
            toolTechniquesSimilatiry.tool = tool;
            toolTechniquesSimilatiry.technique = technique;
            toolTechniquesSimilatiry.similarity = match.similarity;
            responseModel.toolsTechniquesList.add(toolTechniquesSimilatiry);
        }

        private void createEmptyResponseModel() {
            responseModel = new DetailsReportMatchInfoResponseModel();
            responseModel.toolsTechniquesList = new ArrayList<DetailsReportToolTechniquesSimilatiry>();
            responseModel.errorMessages = "";
        }

        private boolean isRequestMissingData(DetailsReportMatchInfoRequestModel requestModel) {
            if (requestModel.selectedWorkingDirPath == null || !requestModel.selectedWorkingDirPath.exists()) {
                responseModel.errorMessages = UseCaseResponseErrorMessages.invalidWorkingDir;
                return true;
            }

            if (requestModel.selectedTool.isEmpty()) {
                responseModel.errorMessages = UseCaseResponseErrorMessages.noToolIsSelected;
                return true;
            }

            if (requestModel.selectedTechnique.isEmpty()) {
                responseModel.errorMessages = UseCaseResponseErrorMessages.noTechniqueIsSelected;
                return true;
            }

            if (requestModel.selectedStudentA == null || requestModel.selectedStudentA.isEmpty() || requestModel.selectedStudentB.isEmpty()) {
                responseModel.errorMessages = UseCaseResponseErrorMessages.noSelectedaStudentPairMatch;
                return true;
            }

            if (!requestModel.toolList.contains(requestModel.selectedTool)) {
                responseModel.errorMessages = UseCaseResponseErrorMessages.noToolIsSelected + ":" + requestModel.selectedTool;
                return true;
            }

            if (!requestModel.techniqueList.contains(requestModel.selectedTechnique)) {
                responseModel.errorMessages = UseCaseResponseErrorMessages.noTechniqueIsSelected + ":" + requestModel.selectedTechnique;
                return true;
            }

            return false;
        }
    }

    private class SideBySideInfo {
        TextFileUtility tfu = new TextFileUtility(StandardCharsets.UTF_8);
        SideBySideComparisonResponseModel responseModel;
        String contentA="";
        String contentB="";

        public void generateFullFilesSidBySideComparion(SideBySideCompariosnRequestModel requestModel, DetailsReportOutputBoundary presenter) {
            createEmptyResponseModel();

            if (isRequestMissingData(requestModel)) {
                presenter.updateDetailsSideBySideComparison(responseModel);
                return;
            }

            try {
                contentA = tfu.readFileContentToString(getStudentDir(requestModel.matchesDir,requestModel.studentAfileName));
                contentB = tfu.readFileContentToString(getStudentDir(requestModel.matchesDir,requestModel.studentBfileName));
            } catch (IOException e) {
                e.printStackTrace();
            }

            requestModel.startLineA++;
            requestModel.startLineB++;
            responseContentWithNoMarking(requestModel);
            responseContnetAddMatchPartMarkings(requestModel);

            presenter.updateDetailsSideBySideComparison(responseModel);
        }

        private void responseContnetAddMatchPartMarkings(SideBySideCompariosnRequestModel requestModel) {
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            MPCMatchFileUtility fileUtility = new MPCMatchFileUtility();
            MPCMatch match = fileUtility.readFromFile(requestModel.matchesDir,requestModel.studentAfileName,requestModel.studentBfileName);

            String[] contentA = responseModel.matchPartContentA.split("<br/>Line");
            String[] contentB = responseModel.matchPartContentB.split("<br/>Line");

            boolean redColor = true;
            int i = 1;
            for(MPCMatchPart part : match.matchParts){
                  String color = (redColor) ? "bad" : "neutral";

                 contentA[part.startLineNumberA-1] = "<span class=\""+color+"\" id=\"partAM"+i+"\"> (M"+i+"-S"+df.format(part.similarityA)+"-L"+part.endLineNumberA+")"+contentA[part.startLineNumberA-1];
                 contentA[part.endLineNumberA-1] = contentA[part.endLineNumberA-1]+"</span>";
                 contentB[part.startLineNumberB-1] = "<span class=\""+color+"\" id=\"partBM"+i+"\"> (M"+i+"-S"+df.format(part.similarityB)+"-L"+part.endLineNumberB+")"+contentB[part.startLineNumberB-1];
                 contentB[part.endLineNumberB-1] = contentB[part.endLineNumberB-1]+"</span>";

                 redColor = (redColor) ? false : true;
                 i++;
            }

            String resultContentA = mergeLines(contentA);
            String resultContentB = mergeLines(contentB);


            responseModel.matchPartContentA = resultContentA.substring(0,resultContentA.length()-("<br/>Line").length());
            responseModel.matchPartContentB = resultContentB.substring(0,resultContentB.length()-("<br/>Line").length());


        }

        private String mergeLines(String[] content) {
            StringBuilder resultContent = new StringBuilder();
            for(String line : content){
                resultContent.append(line);
                resultContent.append("<br/>Line");
            }
            return resultContent.toString();
        }

        public void generateSidBySideComparion(SideBySideCompariosnRequestModel requestModel, DetailsReportOutputBoundary presenter) {
            createEmptyResponseModel();

            if (isRequestMissingData(requestModel) || isRequestMissingLines(requestModel)) {
                presenter.updateDetailsSideBySideComparison(responseModel);
                return;
            }

            try {
                contentA = tfu.readFileContentToString(
                        getStudentDir(requestModel.matchesDir,requestModel.studentAfileName),
                        requestModel.startLineA,requestModel.endLineA);
                contentB = tfu.readFileContentToString(
                        getStudentDir(requestModel.matchesDir,requestModel.studentBfileName),
                        requestModel.startLineB,requestModel.endLineB);
            } catch (IOException e) {
                e.printStackTrace();
            }


            responseContentWithCorrentMarkings(requestModel);

            presenter.updateDetailsSideBySideComparison(responseModel);
        }

        protected void responseContentWithCorrentMarkings(SideBySideCompariosnRequestModel requestModel) {
            if(requestModel.sideBySideType.equals(SideBySideCompariosnRequestModel.SideBySideType.MarkingJYCR))
                responseContentWithJYCRMarkings(requestModel);
            else if(requestModel.sideBySideType.equals(SideBySideCompariosnRequestModel.SideBySideType.MarkingWumpz))
                responseContentWithWumpzMarkings(requestModel);
            else if(requestModel.sideBySideType.equals(SideBySideCompariosnRequestModel.SideBySideType.NoMarking))
                responseContentWithNoMarking(requestModel);
        }


        protected void responseContentWithNoMarking(SideBySideCompariosnRequestModel requestModel) {
            responseModel.matchPartContentA = prepareContentForPrintout(requestModel.startLineA,contentA);
            responseModel.matchPartContentB = prepareContentForPrintout(requestModel.startLineB,contentB);
        }

        private String prepareContentForPrintout(int startLine, String content){
            StringBuilder builder = new StringBuilder();
            int i = startLine;
            builder.append("Line "+i+":");
            i++;
            String response = "";
            char c = ' ';
            for(int x=0;x<content.length();x++) {
                c = content.charAt(x);
                builder.append(c);
                if(c == '\n'){
                    response += builder.toString().replaceAll(TextFileUtility.getLineSeparator(),"<br/>Line "+i+":");
                    builder = new StringBuilder();
                    i++;
                }
            }
            if(c != '\n'){
                response += builder.toString();
            } else {
                i--;
                //if (response.endsWith("Line " + i + ":")) {
                    response = response.substring(0, response.length() - ("Line " + i + ":").length());
                //}
            }

            return response;
        }

        protected void responseContentWithWumpzMarkings(SideBySideCompariosnRequestModel requestModel) {
            com.github.difflib.text.DiffRowGenerator generator = com.github.difflib.text.DiffRowGenerator.create()
                    .showInlineDiffs(true)
                    .inlineDiffByWord(true)
                    .oldTag(f -> "~~~")
                    .newTag(f -> "~~~")
                    .build();
            List<String> original = Arrays.asList(contentA.split(TextFileUtility.getLineSeparator()));
            List<String> revised =  Arrays.asList(contentB.split(TextFileUtility.getLineSeparator()));
            List<com.github.difflib.text.DiffRow> rows = null;

            try {
                rows = generator.generateDiffRows(original,revised);
            } catch (com.github.difflib.algorithm.DiffException e) {
                e.printStackTrace();
            }

            int i = requestModel.startLineA;
            int j = requestModel.startLineB;
            responseModel.matchPartContentA = "";
            responseModel.matchPartContentB = "";
            for (com.github.difflib.text.DiffRow row : rows) {
                responseModel.matchPartContentA += "Line "+i+":"+row.getOldLine()+"<br/>";
                responseModel.matchPartContentB += "Line "+j+":"+row.getNewLine()+"<br/>";
                i++; j++;
            }
        }

        protected void responseContentWithJYCRMarkings(SideBySideCompariosnRequestModel requestModel) {
            DiffRowGenerator generator = new DiffRowGenerator.Builder()
                    .showInlineDiffs(true)
                    .inlineOriginDeleteCssClass("good")
                    .inlineRevisedInsertCssClass("good")
                    .build();
            List<String> original = Arrays.asList(contentA.split(TextFileUtility.getLineSeparator()));
            List<String> revised =  Arrays.asList(contentB.split(TextFileUtility.getLineSeparator()));
            List<DiffRow> rows = null;

            rows = generator.generateDiffRows(original,revised);

            int i = requestModel.startLineA;
            int j = requestModel.startLineB;
            responseModel.matchPartContentA = "";
            responseModel.matchPartContentB = "";
            for (DiffRow row : rows) {
                responseModel.matchPartContentA += "Line "+i+":"+row.getOldLine()+"<br/>";
                responseModel.matchPartContentB += "Line "+j+":"+row.getNewLine()+"<br/>";
                i++; j++;
            }
        }

        private boolean isRequestMissingData(SideBySideCompariosnRequestModel requestModel) {
            if(requestModel.matchesDir == null || requestModel.matchesDir.getPath().isEmpty()){
                responseModel.errorMessages = UseCaseResponseErrorMessages.invalidMatchesDir;
                return true;
            } else if(!requestModel.matchesDir.exists()){
                responseModel.errorMessages = UseCaseResponseErrorMessages.invalidMatchesDir+":"+requestModel.matchesDir.getPath();
                return true;
            }

            File studentA = getStudentDir(requestModel.matchesDir,requestModel.studentAfileName);
            File studentB = getStudentDir(requestModel.matchesDir,requestModel.studentBfileName);

            if(!studentA.exists()){
                responseModel.errorMessages = UseCaseResponseErrorMessages.misingStudentFile+":"+studentA.getPath();
                return true;
            } else if(!studentB.exists()){
                responseModel.errorMessages = UseCaseResponseErrorMessages.misingStudentFile+":"+studentB.getPath();
                return true;
            }

            return false;
        }

        private boolean isRequestMissingLines(SideBySideCompariosnRequestModel requestModel) {
            if(requestModel.startLineA > requestModel.endLineA || requestModel.startLineA < 0 ||
                    (requestModel.startLineA == 0 && requestModel.endLineA == 0 && requestModel.startLineB == 0 && requestModel.endLineB == 0)
                    || requestModel.startLineB > requestModel.endLineB || requestModel.startLineB < 0){
                responseModel.errorMessages = UseCaseResponseErrorMessages.invalidLineNumber;
                return true;
            }
            return false;
        }

        private File getStudentDir(File matchesDir, String studentFileName) {
            return new File(matchesDir.getParentFile().getPath()+File.separator+studentFileName);
        }


        private void createEmptyResponseModel() {
            responseModel = new SideBySideComparisonResponseModel();
            responseModel.errorMessages = "";
        }


    }
}
