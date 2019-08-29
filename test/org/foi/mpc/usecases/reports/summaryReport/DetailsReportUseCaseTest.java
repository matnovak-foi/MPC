package org.foi.mpc.usecases.reports.summaryReport;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.file.TextFileUtility;
import org.foi.mpc.MPCContext;
import org.foi.mpc.matches.MPCMatchFileUtility;
import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.matches.models.MPCMatchBuilder;
import org.foi.mpc.matches.models.MPCMatchPart;
import org.foi.mpc.usecases.UseCaseResponseErrorMessages;
import org.foi.mpc.usecases.reports.summaryReport.models.DetailsReportMatchInfoRequestModel;
import org.foi.mpc.usecases.reports.summaryReport.models.DetailsReportMatchInfoResponseModel;
import org.foi.mpc.usecases.reports.summaryReport.models.DetailsReportMatchInfoResponseModel.DetailsReportToolTechniquesSimilatiry;
import org.foi.mpc.usecases.reports.summaryReport.models.DetailsReportMatchInfoResponseModel.DetailsReportToolTechniquesSimilatiry.DetailsReportMatchParts;
import org.foi.mpc.usecases.reports.summaryReport.models.SideBySideCompariosnRequestModel;
import org.foi.mpc.usecases.reports.summaryReport.models.SideBySideCompariosnRequestModel.SideBySideType;
import org.foi.mpc.usecases.reports.summaryReport.models.SideBySideComparisonResponseModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;

@RunWith(HierarchicalContextRunner.class)
public class DetailsReportUseCaseTest {
    File selectedWorkingDirPath = new File("testInputDirPath");
    DetailsReportInputBoundary useCase;
    DetailsReportOutputBoundarySpy presenterSpy;

    @Before
    public void setUp() throws Exception {
        this.presenterSpy = new DetailsReportOutputBoundarySpy();
        this.useCase = new DetailsReportUseCase();
    }

    @After
    public void tearDown() throws Exception {
        DirectoryFileUtility.deleteDirectoryTree(selectedWorkingDirPath);
    }

    @Test
    public void isDetailsReportInputBoudary() {
        assertTrue(useCase instanceof DetailsReportInputBoundary);
    }

    public class generateDetailInfoForMatchTest {
        DetailsReportMatchInfoRequestModel requestModel;
        MPCMatchFileUtility mpcMatchFileUtility = new MPCMatchFileUtility();
        MPCMatchBuilder matchBuilder;

        @Before
        public void setUp() throws Exception {
            selectedWorkingDirPath.mkdirs();

            requestModel = new DetailsReportMatchInfoRequestModel();
            requestModel.selectedTool = "Tool1";
            requestModel.selectedTechnique = "Technique1";
            requestModel.selectedMatchesDir = createMatchesDirForToolAndTechnique(selectedWorkingDirPath.getPath(),requestModel.selectedTool,requestModel.selectedTechnique);
            requestModel.selectedWorkingDirPath = selectedWorkingDirPath;
            requestModel.selectedStudentA = "studentA";
            requestModel.selectedFileAName = "studentA.java";
            requestModel.selectedStudentB = "studentB";
            requestModel.selectedFileBName = "studentB.java";
            requestModel.toolList = new ArrayList<>();
            requestModel.techniqueList = new ArrayList<>();
            requestModel.toolList.add(requestModel.selectedTool);
            requestModel.techniqueList.add(requestModel.selectedTechnique);

            requestModel.selectedMatchesDir.mkdirs();
            matchBuilder = new MPCMatchBuilder()
                    .with100Similarity()
                    .withSourceDir(requestModel.selectedWorkingDirPath)
                    .withMatchesDir(requestModel.selectedMatchesDir)
                    .withFileA(requestModel.selectedFileAName)
                    .withFileB(requestModel.selectedFileBName);
        }





        @Test
        public void withNoToolSelected(){
            requestModel.selectedTool = "";
            useCase.generateDetailInfoForMatch(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.noToolIsSelected,presenterSpy.detailsInfoResponseModel.errorMessages);
        }

        @Test
        public void withNoTechniqueSelected(){
            requestModel.selectedTechnique = "";
            useCase.generateDetailInfoForMatch(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.noTechniqueIsSelected,presenterSpy.detailsInfoResponseModel.errorMessages);
        }

        @Test
        public void withNoWorkingDirSelected(){
            requestModel.selectedWorkingDirPath = null;
            useCase.generateDetailInfoForMatch(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.invalidWorkingDir,presenterSpy.detailsInfoResponseModel.errorMessages);

            requestModel.selectedWorkingDirPath = new File("wrongInputDir");
            useCase.generateDetailInfoForMatch(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.invalidWorkingDir,presenterSpy.detailsInfoResponseModel.errorMessages);
        }

        @Test
        public void withMatchDirWithNoDetecion(){
            requestModel.selectedMatchesDir = new File("wrongMatchDir");
            useCase.generateDetailInfoForMatch(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.invalidMatchesDir+":"+requestModel.selectedMatchesDir,presenterSpy.detailsInfoResponseModel.errorMessages);
        }

        @Test
        public void withMissigMatchDir(){
            requestModel.selectedMatchesDir = new File(File.separator+"detection"+File.separator+"wrongMatchDir");
            useCase.generateDetailInfoForMatch(requestModel, presenterSpy);

            MPCMatch mpcMatch = matchBuilder.withSimilarity(0).build();
            mpcMatch.matchParts = new ArrayList<>();
            DetailsReportToolTechniquesSimilatiry toolTechniquesSimilatiry = presenterSpy.detailsInfoResponseModel.toolsTechniquesList.get(0);
            assertEquals(requestModel.selectedTool, toolTechniquesSimilatiry.tool);
            assertEquals(requestModel.selectedTechnique, toolTechniquesSimilatiry.technique);
            assertEquals(mpcMatch.similarity, toolTechniquesSimilatiry.similarity,0.001);
            assertEquals(mpcMatch.similarityB, toolTechniquesSimilatiry.similarityB,0.001);
            assertEquals(mpcMatch.similarityA, toolTechniquesSimilatiry.similarityA,0.001);
            assertEquals(mpcMatch.matchParts.size(), toolTechniquesSimilatiry.matchParts.size());

            assertEquals(UseCaseResponseErrorMessages.invalidMatchesDir+":testInputDirPath"+File.separator+"detection"+File.separator+"wrongMatchDir",presenterSpy.detailsInfoResponseModel.errorMessages);
        }

        @Test
        public void withNoStudentPairMatchSelected(){
            requestModel.selectedStudentA = "";
            useCase.generateDetailInfoForMatch(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.noSelectedaStudentPairMatch,presenterSpy.detailsInfoResponseModel.errorMessages );
        }

        @Test
        public void generatesDetailInfoForMatch(){
            MPCMatch mpcMatch = matchBuilder.build();
            mpcMatchFileUtility.saveToFile(mpcMatch);

            useCase.generateDetailInfoForMatch(requestModel,presenterSpy);

            assertSimilarityReportForSelectedToolAndTechnique(mpcMatch);
        }

        @Test
        public void generatesDetailInfoForMatchWithMatchesDirFromLinux() throws IOException {
            MPCMatch mpcMatch = matchBuilder.build();
            mpcMatchFileUtility.saveToFile(mpcMatch);
            requestModel.selectedMatchesDir = new File(File.separator+"shared"+File.separator+"mnovak"+File.separator+"studentDatasetsWorking"+File.separator+"detection"+File.separator+requestModel.selectedTool+File.separator+requestModel.selectedTechnique+File.separator+MPCContext.MATCHES_DIR);

            useCase.generateDetailInfoForMatch(requestModel,presenterSpy);

            assertSimilarityReportForSelectedToolAndTechnique(mpcMatch);
        }

        @Test
        public void toolListMustContainSelectedTool(){
            requestModel.toolList.remove(requestModel.selectedTool);
            MPCMatch mpcMatch = matchBuilder.build();
            mpcMatchFileUtility.saveToFile(mpcMatch);

            useCase.generateDetailInfoForMatch(requestModel,presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.noToolIsSelected+":"+requestModel.selectedTool,presenterSpy.detailsInfoResponseModel.errorMessages);
        }

        @Test
        public void techniqueListMustContainSelectedTechnique(){
            requestModel.techniqueList.remove(requestModel.selectedTechnique);
            MPCMatch mpcMatch = matchBuilder.build();
            mpcMatchFileUtility.saveToFile(mpcMatch);

            useCase.generateDetailInfoForMatch(requestModel,presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.noTechniqueIsSelected+":"+requestModel.selectedTechnique,presenterSpy.detailsInfoResponseModel.errorMessages);
        }

        @Test
        public void generatesReportWithMultipleToolsAndTechniques(){
            requestModel.toolList.add("Tool2");
            requestModel.toolList.add("Tool3");
            requestModel.techniqueList.add("Technique2");
            requestModel.techniqueList.add("Technique3");

            int sim = 100;
            for(String tool : requestModel.toolList) {
                for (String technique : requestModel.techniqueList) {
                    File matchDir = createMatchesDirForToolAndTechnique(selectedWorkingDirPath.getPath(), tool, technique);
                    matchDir.mkdirs();
                    MPCMatch mpcMatch = matchBuilder.withSimilarity(sim).withMatchesDir(matchDir).build();
                    mpcMatchFileUtility.saveToFile(mpcMatch);
                    sim--;
                }
            }
            MPCMatch mpcMatch1 = matchBuilder.build();
            mpcMatchFileUtility.saveToFile(mpcMatch1);

            useCase.generateDetailInfoForMatch(requestModel,presenterSpy);

            assertThat(presenterSpy.detailsInfoResponseModel.toolsTechniquesList, hasSize(9));
            int i = 0;
            for(String tool : requestModel.toolList) {
                for (String technique : requestModel.techniqueList) {
                    assertEquals(tool, presenterSpy.detailsInfoResponseModel.toolsTechniquesList.get(i).tool);
                    assertEquals(technique, presenterSpy.detailsInfoResponseModel.toolsTechniquesList.get(i).technique);
                    assertEquals(100-i, presenterSpy.detailsInfoResponseModel.toolsTechniquesList.get(i).similarity, 0.001);

                    i++;
                }
            }
        }

        @Test
        public void genereatesCorrectLineCountWithOverlap(){
            MPCMatch mpcMatch = matchBuilder
                    .withStartAndEndLine(0,5,12)
                    .withStartAndEndLine(1,3,10)
                    .build();
            mpcMatchFileUtility.saveToFile(mpcMatch);

            useCase.generateDetailInfoForMatch(requestModel,presenterSpy);

            DetailsReportToolTechniquesSimilatiry toolTechniquesSimilatiry = presenterSpy.detailsInfoResponseModel.toolsTechniquesList.get(0);
            assertEquals(10, toolTechniquesSimilatiry.totalLineCountA);
            assertEquals(10, toolTechniquesSimilatiry.totalLineCountB);
        }

        @Test
        public void generatesReportWithMatchOfReversedStudentNames(){
            MPCMatch mpcMatch = matchBuilder.withDifferentABSimilarity(99,98).build();
            mpcMatchFileUtility.saveToFile(mpcMatch);
            String studentAfile = requestModel.selectedFileAName;
            requestModel.selectedFileAName = requestModel.selectedFileBName;
            requestModel.selectedFileBName = studentAfile;

            useCase.generateDetailInfoForMatch(requestModel,presenterSpy);

            MPCMatch reversedMPCMatch = matchBuilder.withDifferentABSimilarity(98,99).build();
            assertSimilarityReportForSelectedToolAndTechnique(reversedMPCMatch);
        }

        @Test
        public void generatesReportWith0MatchIfNoMatchFileExists(){
            useCase.generateDetailInfoForMatch(requestModel,presenterSpy);

            MPCMatch mpcMatch = matchBuilder.withSimilarity(0).build();
            mpcMatch.matchParts = new ArrayList<>();
            assertSimilarityReportForSelectedToolAndTechnique(mpcMatch);
        }

        private void assertSimilarityReportForSelectedToolAndTechnique(MPCMatch mpcMatch) {
            DetailsReportToolTechniquesSimilatiry toolTechniquesSimilatiry = presenterSpy.detailsInfoResponseModel.toolsTechniquesList.get(0);
            assertEquals(requestModel.selectedTool, toolTechniquesSimilatiry.tool);
            assertEquals(requestModel.selectedTechnique, toolTechniquesSimilatiry.technique);
            String expectedMatchesDir = selectedWorkingDirPath.getPath()+File.separator+"detection"+File.separator+requestModel.selectedTool+File.separator+requestModel.selectedTechnique+File.separator+MPCContext.MATCHES_DIR;
            assertEquals(expectedMatchesDir,toolTechniquesSimilatiry.matchesDir.getPath());
            assertEquals(mpcMatch.similarity, toolTechniquesSimilatiry.similarity,0.001);
            assertEquals(mpcMatch.similarityB, toolTechniquesSimilatiry.similarityB,0.001);
            assertEquals(mpcMatch.similarityA, toolTechniquesSimilatiry.similarityA,0.001);
            assertEquals(mpcMatch.calculatedSimilarity, toolTechniquesSimilatiry.calculatedSimilarity,0.001);
            assertEquals(mpcMatch.calculatedSimilarityA, toolTechniquesSimilatiry.calculatedSimilarityA,0.001);
            assertEquals(mpcMatch.calculatedSimilarityB, toolTechniquesSimilatiry.calculatedSimilarityB,0.001);
            assertEquals(mpcMatch.matchParts.size(), toolTechniquesSimilatiry.matchParts.size());

            int expectedTotalA = 0;
            int expectedTotalB = 0;
            if(mpcMatch.matchParts.size()>0) {
                MPCMatchPart expectedMatchPart = mpcMatch.matchParts.get(0);
                DetailsReportMatchParts matchParts = toolTechniquesSimilatiry.matchParts.get(0);
                //assertEquals(requestModel.selectedTools, matchParts.tool);
                //assertEquals(requestModel.selectedTechnique, matchParts.technique);
                assertEquals(expectedMatchPart.similarity, matchParts.similarity, 0.1);
                assertEquals(expectedMatchPart.similarityA, matchParts.similarityA, 0.1);
                assertEquals(expectedMatchPart.similarityB, matchParts.similarityB, 0.1);
                assertEquals(expectedMatchPart.startLineNumberA, matchParts.startLineNumberA);
                assertEquals(expectedMatchPart.startLineNumberB, matchParts.startLineNumberB);
                assertEquals(expectedMatchPart.endLineNumberA, matchParts.endLineNumberA);
                assertEquals(expectedMatchPart.endLineNumberB, matchParts.endLineNumberB);
                assertEquals(expectedMatchPart.endLineNumberA-expectedMatchPart.startLineNumberA+1, matchParts.lineCountA);
                assertEquals(expectedMatchPart.endLineNumberB-expectedMatchPart.startLineNumberB+1, matchParts.lineCountB);
                expectedTotalA = (expectedMatchPart.endLineNumberA-expectedMatchPart.startLineNumberA+1)*mpcMatch.matchParts.size();
                expectedTotalB = (expectedMatchPart.endLineNumberB-expectedMatchPart.startLineNumberB+1)*mpcMatch.matchParts.size();
            }

            assertEquals(expectedTotalA, toolTechniquesSimilatiry.totalLineCountA);
            assertEquals(expectedTotalB, toolTechniquesSimilatiry.totalLineCountB);
        }
    }

    public class generateDetailInfoMatchPartSideBySideViewTest {
        SideBySideCompariosnRequestModel requestModel = new SideBySideCompariosnRequestModel();

        @Before
        public void setUp() throws Exception {
            requestModel.matchesDir = new File(selectedWorkingDirPath.getPath()+File.separator+"matches");
            requestModel.matchesDir.mkdirs();

            requestModel.startLineA = 1;
            requestModel.endLineA = 3;
            requestModel.startLineB = 1;
            requestModel.endLineB = 3;
            requestModel.studentAfileName = "studentA.java";
            requestModel.studentBfileName = "studentB.java";
            requestModel.sideBySideType = SideBySideType.MarkingWumpz;

            TextFileUtility textFileUtility = new TextFileUtility(StandardCharsets.UTF_8);
            String testCodeA = "line 1\nline 2\nline 3\nline 4\nline 5";
            String testCodeB = "line 1\nlinee 2\nline 3\nline 4\nline 5";

            textFileUtility.createFileWithText(new File(selectedWorkingDirPath.getPath()+File.separator+requestModel.studentAfileName),testCodeA);
            textFileUtility.createFileWithText(new File(selectedWorkingDirPath.getPath()+File.separator+requestModel.studentBfileName),testCodeB);
        }

        @Test
        public void withNoMatchesDir(){
            requestModel.matchesDir = null;
            useCase.generateMatchPartSidBySideComparion(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.invalidMatchesDir, presenterSpy.sideBySideResponseModel.errorMessages);

            requestModel.matchesDir = new File("");
            useCase.generateMatchPartSidBySideComparion(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.invalidMatchesDir,presenterSpy.sideBySideResponseModel.errorMessages);

            requestModel.matchesDir = new File("nonExistingMatchesDir");
            useCase.generateMatchPartSidBySideComparion(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.invalidMatchesDir+":"+requestModel.matchesDir.getPath(),presenterSpy.sideBySideResponseModel.errorMessages);
        }

        @Test
        public void withStartLineEndLineAProblem(){
            requestModel.startLineA = 1;
            requestModel.endLineA = 0;
            useCase.generateMatchPartSidBySideComparion(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.invalidLineNumber,presenterSpy.sideBySideResponseModel.errorMessages);

            requestModel.startLineA = -1;
            requestModel.endLineA = 0;
            useCase.generateMatchPartSidBySideComparion(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.invalidLineNumber,presenterSpy.sideBySideResponseModel.errorMessages);
        }

        @Test
        public void withStartLineEndLineBProblem(){
            requestModel.startLineB = 1;
            requestModel.endLineB = 0;
            useCase.generateMatchPartSidBySideComparion(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.invalidLineNumber,presenterSpy.sideBySideResponseModel.errorMessages);

            requestModel.startLineB = -1;
            requestModel.endLineB = 0;
            useCase.generateMatchPartSidBySideComparion(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.invalidLineNumber,presenterSpy.sideBySideResponseModel.errorMessages);
        }

        @Test
        public void withStartAndEndLineZero(){
            requestModel.startLineA = 0;
            requestModel.endLineA = 0;
            requestModel.startLineB = 0;
            requestModel.endLineB = 0;

            useCase.generateMatchPartSidBySideComparion(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.invalidLineNumber,presenterSpy.sideBySideResponseModel.errorMessages);
        }

        @Test
        public void withMissingStudentAFile(){
            requestModel.studentAfileName = "missingStudentA";
            useCase.generateMatchPartSidBySideComparion(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.misingStudentFile+":"+
                    requestModel.matchesDir.getParent()+File.separator+requestModel.studentAfileName,
                    presenterSpy.sideBySideResponseModel.errorMessages);
        }



        @Test
        public void withMissingStudentBFile(){
            requestModel.studentBfileName = "missingStudentB";
            useCase.generateMatchPartSidBySideComparion(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.misingStudentFile+":"+
                    requestModel.matchesDir.getParent()+File.separator+requestModel.studentBfileName,
                    presenterSpy.sideBySideResponseModel.errorMessages);
        }

        @Test
        public void canReadCorrectLineFromFilesMarkingWumpz(){
            useCase.generateMatchPartSidBySideComparion(requestModel,presenterSpy);
            String expectedTestCodeA = "Line 1:line 1<br/>Line 2:~~~line~~~ 2<br/>Line 3:line 3<br/>";
            String expectedTestCodeB = "Line 1:line 1<br/>Line 2:~~~linee~~~ 2<br/>Line 3:line 3<br/>";
            assertEquals(expectedTestCodeA,presenterSpy.sideBySideResponseModel.matchPartContentA);
            assertEquals(expectedTestCodeB,presenterSpy.sideBySideResponseModel.matchPartContentB);
        }

        @Test
        public void canReadCorrectLineFromFilesNoMarking(){
            requestModel.sideBySideType = SideBySideType.NoMarking;
            useCase.generateMatchPartSidBySideComparion(requestModel,presenterSpy);
            String expectedTestCodeA = "Line 1:line 1<br/>Line 2:line 2<br/>Line 3:line 3";
            String expectedTestCodeB = "Line 1:line 1<br/>Line 2:linee 2<br/>Line 3:line 3";
            assertEquals(expectedTestCodeA,presenterSpy.sideBySideResponseModel.matchPartContentA);
            assertEquals(expectedTestCodeB,presenterSpy.sideBySideResponseModel.matchPartContentB);
        }

        @Test
        public void canReadCorrectLineFromFilesMarkigJYCR(){
            requestModel.sideBySideType = SideBySideType.MarkingJYCR;
            useCase.generateMatchPartSidBySideComparion(requestModel,presenterSpy);
            String expectedTestCodeA = "Line 1:line 1<br/>Line 2:line 2<br/>Line 3:line 3<br/>";
            String expectedTestCodeB = "Line 1:line 1<br/>Line 2:line<ins class=\"good\">e</ins> 2<br/>Line 3:line 3<br/>";
            assertEquals(expectedTestCodeA,presenterSpy.sideBySideResponseModel.matchPartContentA);
            assertEquals(expectedTestCodeB,presenterSpy.sideBySideResponseModel.matchPartContentB);
        }


    }

    public class fullFilesSideBySide {

        SideBySideCompariosnRequestModel requestModel = new SideBySideCompariosnRequestModel();

        @Before
        public void setUp() throws Exception {
            requestModel.matchesDir = new File(selectedWorkingDirPath.getPath()+File.separator+"matches");
            requestModel.matchesDir.mkdirs();

            requestModel.studentAfileName = "studentA.java";
            requestModel.studentBfileName = "studentB.java";

            TextFileUtility textFileUtility = new TextFileUtility(StandardCharsets.UTF_8);
            String testCodeA = "line 1\nline 2\nline 3\nline 4\nline 5";
            String testCodeB = "line 1\nlinee 2\nline 3\nline 4\nline 5";

            textFileUtility.createFileWithText(new File(selectedWorkingDirPath.getPath()+File.separator+requestModel.studentAfileName),testCodeA);
            textFileUtility.createFileWithText(new File(selectedWorkingDirPath.getPath()+File.separator+requestModel.studentBfileName),testCodeB);
        }

        @Test
        public void withNoMatchesDir(){
            requestModel.matchesDir = null;
            useCase.generateFullFileSidBySideComparion(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.invalidMatchesDir, presenterSpy.sideBySideResponseModel.errorMessages);

            requestModel.matchesDir = new File("");
            useCase.generateFullFileSidBySideComparion(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.invalidMatchesDir,presenterSpy.sideBySideResponseModel.errorMessages);

            requestModel.matchesDir = new File("nonExistingMatchesDir");
            useCase.generateFullFileSidBySideComparion(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.invalidMatchesDir+":"+requestModel.matchesDir.getPath(),presenterSpy.sideBySideResponseModel.errorMessages);
        }

        @Test
        public void withMissingStudentAFile(){
            requestModel.studentAfileName = "missingStudentA";
            useCase.generateFullFileSidBySideComparion(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.misingStudentFile+":"+
                            requestModel.matchesDir.getParent()+File.separator+requestModel.studentAfileName,
                    presenterSpy.sideBySideResponseModel.errorMessages);
        }

        @Test
        public void withMissingStudentBFile(){
            requestModel.studentBfileName = "missingStudentB";
            useCase.generateFullFileSidBySideComparion(requestModel, presenterSpy);

            assertEquals(UseCaseResponseErrorMessages.misingStudentFile+":"+
                            requestModel.matchesDir.getParent()+File.separator+requestModel.studentBfileName,
                    presenterSpy.sideBySideResponseModel.errorMessages);
        }

        @Test
        public void canReadCorrectLineFromFilesNoMarking(){
            useCase.generateFullFileSidBySideComparion(requestModel,presenterSpy);

            String expectedTestCodeA = "Line 1:line 1<br/>Line 2:line 2<br/>Line 3:line 3<br/>Line 4:line 4<br/>Line 5:line 5";
            String expectedTestCodeB = "Line 1:line 1<br/>Line 2:linee 2<br/>Line 3:line 3<br/>Line 4:line 4<br/>Line 5:line 5";
            assertEquals(expectedTestCodeA,presenterSpy.sideBySideResponseModel.matchPartContentA);
            assertEquals(expectedTestCodeB,presenterSpy.sideBySideResponseModel.matchPartContentB);
        }

        @Test
        public void canReadCorrectLineFromFilesMatchPartMarking(){
            MPCMatchFileUtility mpcMatchFileUtility = new MPCMatchFileUtility();
            MPCMatchBuilder matchBuilder;
            matchBuilder = new MPCMatchBuilder()
                    .withStartAndEndLine(0,1,5)
                    .withSourceDir(selectedWorkingDirPath)
                    .withMatchesDir(requestModel.matchesDir)
                    .withFileA(requestModel.studentAfileName)
                    .withFileB(requestModel.studentBfileName);

            mpcMatchFileUtility.saveToFile(matchBuilder.build());
            useCase.generateFullFileSidBySideComparion(requestModel,presenterSpy);

            String expectedTestCodeA = "<span class=\"bad\" id=\"partAM1\"> (M1-S100-L5)Line 1:line 1<br/>Line 2:line 2<br/>Line 3:line 3<br/>Line 4:line 4<br/>Line 5:line 5</span>";
            String expectedTestCodeB = "<span class=\"bad\" id=\"partBM1\"> (M1-S100-L5)Line 1:line 1<br/>Line 2:linee 2<br/>Line 3:line 3<br/>Line 4:line 4<br/>Line 5:line 5</span>";
            assertEquals(expectedTestCodeA,presenterSpy.sideBySideResponseModel.matchPartContentA);
            assertEquals(expectedTestCodeB,presenterSpy.sideBySideResponseModel.matchPartContentB);
        }

        @Test
        public void canReadCorrectLineFromFilesMatchPartMarking2Parts(){
            MPCMatchFileUtility mpcMatchFileUtility = new MPCMatchFileUtility();
            MPCMatchBuilder matchBuilder;
            matchBuilder = new MPCMatchBuilder()
                    .withStartAndEndLine(0,1,1)
                    .withStartAndEndLine(1,3,4)
                    .withSourceDir(selectedWorkingDirPath)
                    .withMatchesDir(requestModel.matchesDir)
                    .withFileA(requestModel.studentAfileName)
                    .withFileB(requestModel.studentBfileName);

            mpcMatchFileUtility.saveToFile(matchBuilder.build());
            useCase.generateFullFileSidBySideComparion(requestModel,presenterSpy);

            String expectedTestCodeA = "<span class=\"bad\" id=\"partAM1\"> (M1-S100-L1)Line 1:line 1</span><br/>Line 2:line 2<br/>Line<span class=\"neutral\" id=\"partAM2\"> (M2-S100-L4) 3:line 3<br/>Line 4:line 4</span><br/>Line 5:line 5";
            String expectedTestCodeB = "<span class=\"bad\" id=\"partBM1\"> (M1-S100-L1)Line 1:line 1</span><br/>Line 2:linee 2<br/>Line<span class=\"neutral\" id=\"partBM2\"> (M2-S100-L4) 3:line 3<br/>Line 4:line 4</span><br/>Line 5:line 5";
            assertEquals(expectedTestCodeA,presenterSpy.sideBySideResponseModel.matchPartContentA);
            assertEquals(expectedTestCodeB,presenterSpy.sideBySideResponseModel.matchPartContentB);
        }

        @Test
        public void canReadCorrectLineFromFilesMatchPartMarking4Parts(){
            MPCMatchFileUtility mpcMatchFileUtility = new MPCMatchFileUtility();
            MPCMatchBuilder matchBuilder;
            matchBuilder = new MPCMatchBuilder()
                    .withStartAndEndLine(0,1,1)
                    .withStartAndEndLine(1,3,3)
                    .withStartAndEndLine(2,4,4)
                    .withStartAndEndLine(3,5,5)
                    .withSourceDir(selectedWorkingDirPath)
                    .withMatchesDir(requestModel.matchesDir)
                    .withFileA(requestModel.studentAfileName)
                    .withFileB(requestModel.studentBfileName);

            mpcMatchFileUtility.saveToFile(matchBuilder.build());
            useCase.generateFullFileSidBySideComparion(requestModel,presenterSpy);

            String expectedTestCodeA = "<span class=\"bad\" id=\"partAM1\"> (M1-S100-L1)Line 1:line 1</span><br/>Line 2:line 2<br/>Line<span class=\"neutral\" id=\"partAM2\"> (M2-S100-L3) 3:line 3</span><br/>Line<span class=\"bad\" id=\"partAM3\"> (M3-S100-L4) 4:line 4</span><br/>Line<span class=\"neutral\" id=\"partAM4\"> (M4-S100-L5) 5:line 5</span>";
            String expectedTestCodeB = "<span class=\"bad\" id=\"partBM1\"> (M1-S100-L1)Line 1:line 1</span><br/>Line 2:linee 2<br/>Line<span class=\"neutral\" id=\"partBM2\"> (M2-S100-L3) 3:line 3</span><br/>Line<span class=\"bad\" id=\"partBM3\"> (M3-S100-L4) 4:line 4</span><br/>Line<span class=\"neutral\" id=\"partBM4\"> (M4-S100-L5) 5:line 5</span>";
            assertEquals(expectedTestCodeA,presenterSpy.sideBySideResponseModel.matchPartContentA);
            assertEquals(expectedTestCodeB,presenterSpy.sideBySideResponseModel.matchPartContentB);
        }
    }

    private File createMatchesDirForToolAndTechnique(String workingDirPath, String tool, String technique) {
        return new File(workingDirPath+File.separator+"detection"+File.separator+tool+File.separator+technique+File.separator+MPCContext.MATCHES_DIR);
    }

    private class DetailsReportOutputBoundarySpy implements DetailsReportOutputBoundary {
        public DetailsReportMatchInfoResponseModel detailsInfoResponseModel;
        public SideBySideComparisonResponseModel sideBySideResponseModel;

        @Override
        public void updateDetailsMatchInfo(DetailsReportMatchInfoResponseModel responseModel) {
            this.detailsInfoResponseModel = responseModel;
        }

        @Override
        public void updateDetailsSideBySideComparison(SideBySideComparisonResponseModel responseModel) {
            this.sideBySideResponseModel = responseModel;
        }
    }
}