package org.foi.mpc.executiontools.detectionTools.simgrune;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.foi.mpc.MPCContext;
import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.matches.models.MPCMatchPart;
import org.foi.mpc.executiontools.detectionTools.AdaptersTestTemplate;
import org.foi.mpc.executiontools.factories.SimilarityDetectionToolFactory;
import org.foi.common.filesystem.directory.DirectoryCreator;
import org.foi.common.filesystem.file.TextFileUtility;
import org.junit.Before;
import org.junit.Test;

import static org.foi.common.filesystem.directory.DirectoryCreator.assertDirHasFilesCount;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

@RunWith(HierarchicalContextRunner.class)
public class SimGruneTextAdapterTest extends AdaptersTestTemplate {

    private SimGruneAdapterSettings settings;

    @Before
    @Override
    public void setUp() throws IOException {
        super.setUp();
        adapter = (new SimilarityDetectionToolFactory(MPCContext.MATCHES_DIR)).createTool(SimGruneTextAdapter.NAME);
        settings = (SimGruneAdapterSettings) adapter.getSettings();
    }

    public class withDummySourceDirAndNoRealRun extends AdaptersTestTemplate.withDummySourceDirAndNoRealRun {
        
        @Override
        @Test
        public void defaultOptionsAreOk()  {
            SimGruneTextAdapter simGruneAdapter = new SimGruneTextAdapterStub(" -O");
            simGruneAdapter.runPlagiarsimDetection(sourceDir);
            Process simProcess = simGruneAdapter.getProcess();
            assertDefaultOptionsAreOk(getSimOutput(simProcess));
        }

        private void assertDefaultOptionsAreOk(String outContent) {
            assertTrue(outContent.contains("-O"));
            assertTrue(outContent.contains("-R"));
            assertTrue(outContent.contains("-a"));
            assertTrue(outContent.contains("-e"));
            assertTrue(outContent.contains("-r2"));
            assertTrue(outContent.contains("-s"));
            String OS = System.getProperty("os.name").toLowerCase();
            if(OS.indexOf("win") >= 0) {
                assertTrue(outContent.contains("sim_text.exe"));
            } else {
                assertTrue(outContent.contains("sim_text"));
            }
        }
        
        private String getSimOutput(Process process) {
            String output="";
            String line;
            try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                while ((line = input.readLine()) != null) {
                    output += line;
                }
            } catch (IOException ex) {
                fail("getSimOutput not working!");
            }
            return output;
        }   
        
        @Override
        @Test
        public void isExecutionPhaseAndHasCorrectGetName() {
            super.isExecutionPhaseAndHasCorrectGetName();
            assertEquals(SimGruneTextAdapter.NAME,adapter.getName());
        }
    }

    @Test
    public void noErrorsWhenProgramIsRunWithInvalidSubmissions() throws IOException {
        DirectoryCreator.createTestDirWithNFiles(sourceDir, 2);
        adapter.runPlagiarsimDetection(sourceDir);
    }
    
    public class withValidIdenticalFiles extends AdaptersTestTemplate.withValidIdenticalFiles {
        
        @Override
        protected void assertEndLinesOf100PrecentMatchPart(List<MPCMatchPart> matchPart) {
            assertEquals(17, matchPart.get(0).endLineNumberA);
            assertEquals(17, matchPart.get(0).endLineNumberB);
        }

        @Test
        public void runsPrecentDetectionAfterResultDetection() throws IOException {
            File copyDir = new File(sourceDir+File.separator+settings.precentRunDirName);

            adapter.runPlagiarsimDetection(sourceDir);

            assertNonPrecentDetectionWasRun();
            assertDirWasCreatedCorrectly(copyDir);
            assertResultFileContainsPrecentages();
        }

        @Override
        public void withFilesInSubDirStructure() throws IOException{
            super.withFilesInSubDirStructure();
            assertResultFileContainsPrecentages();
        }

        private void assertNonPrecentDetectionWasRun(){
            File outputFile = new File(sourceDir.getAbsolutePath()+File.separator+settings.resultDirName+File.separator+settings.outputFileName);
            assertTrue(outputFile.exists());
        }

        private void assertResultFileContainsPrecentages() throws IOException {
            File outputFile = new File(sourceDir.getAbsolutePath()
                    +File.separator+settings.resultDirName
                    +File.separator+settings.precentOutputFileName);
            assertTrue(outputFile.exists());

            TextFileUtility tfu = new TextFileUtility(StandardCharsets.UTF_8);
            String contet = tfu.readFileContentToString(outputFile);

            assertTrue(contet.contains("% of "));
            assertTrue(contet.contains(" consists for "));
        }

        private void assertDirWasCreatedCorrectly(File precentDir){
            assertTrue(precentDir.exists());
            assertDirHasFilesCount(2,precentDir);
            assertEquals(javaFile1.getName(),precentDir.listFiles()[0].getName());
            assertEquals(javaFile2.getName(),precentDir.listFiles()[1].getName());
        }
    }
    
    public class withValidNonIdenticalFiles extends AdaptersTestTemplate.withValidNonIdenticalFiles {

        @Override
        protected void assertMatchDataIsOkFileContaingingFileTwice(File matchFile, MPCMatch match) {
            assertEquals(matchFile.getName().split("-")[0], match.fileAName);
            assertEquals(matchFile.getName().split("-")[1], match.fileBName);
            assertEquals(100, match.similarity, 0.0001);
            assertEquals(100, match.similarityA, 0.0001);
            assertEquals(100, match.similarityB, 0.0001);
            assertEquals(2, match.matchParts.size());
        }

        @Override
        protected void assertMatchDataIsOk(File matchFile, MPCMatch match) {
            assertEquals(matchFile.getName().split("-")[0], match.fileAName);
            assertEquals(matchFile.getName().split("-")[1], match.fileBName);
            assertEquals(77.5, match.similarity, 0.0001);
            assertEquals(55, match.similarityA, 0.0001);
            assertEquals(100, match.similarityB, 0.0001);
            assertEquals(2, match.matchParts.size());
        }

        @Override
        protected void assertFirstMatchPartIsOk(List<MPCMatchPart> matchPart) {
            assertEquals(13, matchPart.get(0).startLineNumberA);
            assertEquals(7, matchPart.get(0).startLineNumberB);
            assertEquals(23, matchPart.get(0).endLineNumberA);
            assertEquals(17, matchPart.get(0).endLineNumberB);
            assertEquals(51.51515, matchPart.get(0).similarity, 0.0001);
            assertEquals(36.36363, matchPart.get(0).similarityA, 0.0001);
            assertEquals(66.66666, matchPart.get(0).similarityB, 0.0001);
        }

        @Override
        protected void assertSecondMatchPartIsOk(List<MPCMatchPart> matchPart) {
            assertEquals(1, matchPart.get(1).startLineNumberA);
            assertEquals(1, matchPart.get(1).startLineNumberB);
            assertEquals(6, matchPart.get(1).endLineNumberA);
            assertEquals(6, matchPart.get(1).endLineNumberB);
            assertEquals(25.75757, matchPart.get(1).similarity, 0.0001);
            assertEquals(18.18181, matchPart.get(1).similarityA, 0.0001);
            assertEquals(33.33333, matchPart.get(1).similarityB, 0.0001);
        }

        @Override
        protected void assertCalculatedMatchSimilarityIsOk(MPCMatch match) {
            assertEquals(77.27272, match.calculatedSimilarity, 0.0001);
            assertEquals(54.54545, match.calculatedSimilarityA, 0.0001);
            assertEquals(100, match.calculatedSimilarityB, 0.0001);
        }
    }
    
    private static class SimGruneTextAdapterStub extends SimGruneTextAdapter {

        public SimGruneTextAdapterStub(String params) {
            super(params);
        }

    }
 
    
}
