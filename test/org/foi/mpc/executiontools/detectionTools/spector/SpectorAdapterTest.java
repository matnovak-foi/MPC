/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.mpc.executiontools.detectionTools.spector;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.foi.mpc.MPCContext;
import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.executiontools.detectionTools.AdaptersTestTemplate;
import org.foi.mpc.matches.models.MPCMatchPart;
import org.foi.mpc.executiontools.factories.SimilarityDetectionToolFactory;
import org.foi.common.filesystem.directory.DirectoryCreator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import spector.Presenter;
import spector.Spector;

@RunWith(HierarchicalContextRunner.class)
public class SpectorAdapterTest extends AdaptersTestTemplate {

    @Before
    public void setUp() throws IOException {
        super.setUp();
        adapter = (new SimilarityDetectionToolFactory(MPCContext.MATCHES_DIR)).createTool(SpectorAdapter.NAME);
    }

    public class withDummySourceDirAndNoRealRun extends AdaptersTestTemplate.withDummySourceDirAndNoRealRun {

        @Before
        public void setUp() throws IOException {
            super.setUp();
            adapter = new SpectorAdapterStub();
        }

        @Test
        public void defaultOptionsAreOk() {
            adapter.runPlagiarsimDetection(sourceDir);
            assertDefaultOptionsAreOk();
        }

        private void assertDefaultOptionsAreOk() {
            assertTrue(Spector.recurse);
            assertFalse(Spector.debug);
            assertEquals(Spector.order, Spector.SortOption.Descending);
            assertTrue(Spector.bidirectional);
            assertEquals(Spector.InputOption.File, Spector.input);

            List<Spector.OutputOption> outputOptions = new ArrayList<>();
            outputOptions.add(Spector.OutputOption.Summary);
            assertEquals(outputOptions, Spector.output);

            List<File> include = new ArrayList<>();
            assertEquals(include, Spector.include);
            File resultDir = new File(sourceDir.getPath() + File.separator + "result");
            assertTrue(resultDir.exists());
            assertEquals(resultDir, Spector.place);

            List<String> inspect = new ArrayList<>();
            inspect.add(sourceDir.getPath());
            assertEquals(inspect, Spector.ipath);
        }
        
        @Override
        public void isExecutionPhaseAndHasCorrectGetName() {
            super.isExecutionPhaseAndHasCorrectGetName();
            assertEquals("Spector",adapter.getName());
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
            assertEquals(-1, matchPart.get(0).endLineNumberA);
            assertEquals(-1, matchPart.get(0).endLineNumberB);
        }
    }

    public class withValidNonIdenticalFiles extends AdaptersTestTemplate.withValidNonIdenticalFiles {

        @Override
        protected void assertMatchDataIsOkFileContaingingFileTwice(File matchFile, MPCMatch match) {
            assertEquals(matchFile.getName().split("-")[0], match.fileAName);
            assertEquals(matchFile.getName().split("-")[1], match.fileBName);
            System.out.println(match.similarity+" "+match.similarityA+" "+match.similarityB+" "+ match.matchParts.size());
            assertEquals(100, match.similarity, 0.0001);
            assertEquals(100, match.similarityA, 0.0001);
            assertEquals(100, match.similarityB, 0.0001);
            assertEquals(1, match.matchParts.size());
        }

        @Override
        protected void assertMatchDataIsOk(File matchFile, MPCMatch match) {
            assertEquals(matchFile.getName().split("-")[0], match.fileAName);
            assertEquals(matchFile.getName().split("-")[1], match.fileBName);
            assertEquals(100, match.similarity, 0.0001);
            assertEquals(100, match.similarityA, 0.0001);
            assertEquals(100, match.similarityB, 0.0001);
            assertEquals(1, match.matchParts.size());
        }

        @Override
        protected void assertFirstMatchPartIsOk(List<MPCMatchPart> matchPart) {
            assertEquals(1, matchPart.get(0).startLineNumberA);
            assertEquals(1, matchPart.get(0).startLineNumberB);
            assertEquals(-1, matchPart.get(0).endLineNumberA);
            assertEquals(-1, matchPart.get(0).endLineNumberB);
            assertEquals(100, matchPart.get(0).similarity, 0.0001);
            assertEquals(100, matchPart.get(0).similarityA, 0.0001);
            assertEquals(100, matchPart.get(0).similarityB, 0.0001);
        }

        @Override
        protected void assertSecondMatchPartIsOk(List<MPCMatchPart> matchPart) {
            //THERE IS NO MATCH PART 2 SPECTOR DOES NOT HAVE MATCH PARTS
        }

        @Override
        protected void assertCalculatedMatchSimilarityIsOk(MPCMatch match) {
            assertEquals(match.similarity, match.calculatedSimilarity, 0.0001);
            assertEquals(match.similarityA, match.calculatedSimilarityA, 0.0001);
            assertEquals(match.similarityB, match.calculatedSimilarityB, 0.0001);
        }
    }

    private class SpectorAdapterStub extends SpectorAdapter {

        @Override
        public void runPlagiarsimDetection(File sourceDir) {
            super.runPlagiarsimDetection(sourceDir); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        protected String[] setUpSpectorSettings(File sourceDir) {
            String[] result = super.setUpSpectorSettings(sourceDir); //To change body of generated methods, choose Tools | Templates.
            Spector.out = new PresenterWrapperStub();
            return result;
        }

        private class PresenterWrapperStub extends Presenter {

            @Override
            public void printMatches() {

            }

            @Override
            public void printFullSummary() {

            }

        }
    }
}
