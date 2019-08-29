package org.foi.mpc.executiontools.detectionTools.simgrune;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.foi.mpc.MPCContext;
import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.matches.models.MPCMatchPart;
import org.foi.mpc.executiontools.detectionTools.AdaptersTestTemplate;
import org.foi.mpc.executiontools.factories.SimilarityDetectionToolFactory;
import org.foi.common.filesystem.directory.DirectoryCreator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

@RunWith(HierarchicalContextRunner.class)
public class SimGruneJavaAdapterTest extends AdaptersTestTemplate {
  
    @Before
    @Override
    public void setUp() throws IOException {
        super.setUp();
        adapter = (new SimilarityDetectionToolFactory(MPCContext.MATCHES_DIR)).createTool(SimGruneJavaAdapter.NAME);
    }

    public class withDummySourceDirAndNoRealRun extends AdaptersTestTemplate.withDummySourceDirAndNoRealRun {
        
        @Override
        @Test
        public void defaultOptionsAreOk()  {
            SimGruneJavaAdapter simGruneAdapter = new SimGruneJavaAdapterStub(" -O");
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
                assertTrue(outContent.contains("sim_java.exe"));
            } else {
                assertTrue(outContent.contains("sim_java"));
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
            assertEquals(SimGruneJavaAdapter.NAME,adapter.getName());
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
            assertEquals(19, matchPart.get(0).endLineNumberA);
            assertEquals(19, matchPart.get(0).endLineNumberB);
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
            assertEquals(86, match.similarity, 0.0001);
            assertEquals(72, match.similarityA, 0.0001);
            assertEquals(100, match.similarityB, 0.0001);
            assertEquals(2, match.matchParts.size());
        }

        @Override
        protected void assertFirstMatchPartIsOk(List<MPCMatchPart> matchPart) {
            assertEquals(13, matchPart.get(0).startLineNumberA);
            assertEquals(7, matchPart.get(0).startLineNumberB);
            assertEquals(25, matchPart.get(0).endLineNumberA);
            assertEquals(19, matchPart.get(0).endLineNumberB);
            assertEquals(63.30555, matchPart.get(0).similarity, 0.0001);
            assertEquals(53, matchPart.get(0).similarityA, 0.0001);
            assertEquals(73.61111, matchPart.get(0).similarityB, 0.0001);
        }

        @Override
        protected void assertSecondMatchPartIsOk(List<MPCMatchPart> matchPart) {
            assertEquals(1, matchPart.get(1).startLineNumberA);
            assertEquals(1, matchPart.get(1).startLineNumberB);
            assertEquals(6, matchPart.get(1).endLineNumberA);
            assertEquals(6, matchPart.get(1).endLineNumberB);
            assertEquals(22.69444, matchPart.get(1).similarity, 0.0001);
            assertEquals(19, matchPart.get(1).similarityA, 0.0001);
            assertEquals(26.38888, matchPart.get(1).similarityB, 0.0001);
        }

        @Override
        protected void assertCalculatedMatchSimilarityIsOk(MPCMatch match) {
            assertEquals(86, match.calculatedSimilarity, 0.0001);
            assertEquals(72, match.calculatedSimilarityA, 0.0001);
            assertEquals(100, match.calculatedSimilarityB, 0.0001);
        }
    }
    
    private static class SimGruneJavaAdapterStub extends SimGruneJavaAdapter {

        public SimGruneJavaAdapterStub(String params) {
            super(params);
        }

    }
 
    
}
