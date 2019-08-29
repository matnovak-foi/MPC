package org.foi.mpc.executiontools.detectionTools.JPlag;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import java.io.File;
import java.io.IOException;
import java.util.List;
import jplag.options.CommandLineOptions;
import jplag.options.Options;
import org.foi.mpc.MPCContext;
import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.executiontools.detectionTools.AdaptersTestTemplate;
import org.foi.mpc.matches.models.MPCMatchPart;
import org.foi.mpc.executiontools.factories.SimilarityDetectionToolFactory;
import org.foi.common.filesystem.directory.DirectoryCreator;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(HierarchicalContextRunner.class)
public class JPlagTextAdapterTest extends AdaptersTestTemplate {

    
    @Before
    public void setUp() throws IOException {
        super.setUp();
        
        adapter = (new SimilarityDetectionToolFactory(MPCContext.MATCHES_DIR)).createTool(JPlagTextAdapter.NAME);
    }

    public class withDummySourceDirAndNoRealRun extends AdaptersTestTemplate.withDummySourceDirAndNoRealRun {

        @Before
        public void setUp() throws IOException {
            super.setUp();
            adapter = new JPlagTextAdapterStub();
        }

        @Test
        public void defaultOptionsAreOk() {
            adapter.runPlagiarsimDetection(sourceDir);
            JPlagAdapter jpa = (JPlagAdapter) adapter;
            CommandLineOptions options = jpa.getJPlagOptions();
            assertDefaultOptionsAreOk(options);
        }

        private void assertDefaultOptionsAreOk(CommandLineOptions options) {
            int disabled = 0;
            int minStoreMatchAbove = 0;
            int minTokenMatch = 1;
            String languageName = "text";
            String outputLog = sourceDir.getAbsolutePath() + File.separator + "JPlag.log";
            String resultDir = sourceDir.getAbsolutePath() + File.separator + "result";
            String includeFileExtensions = ".java";
            String args = sourceDir
                    + " -external"
                    + " -l " + languageName
                    + " -t " + minTokenMatch
                    + " -o " + outputLog
                    + " -r " + resultDir
                    + " -s"
                    + " -m " + minStoreMatchAbove + "%"
                    + " -p " + includeFileExtensions
                    + " -vq ";

            assertEquals("", options.basecode);
            assertEquals(disabled, options.clusterType);
            assertFalse(options.clustering);
            assertEquals(disabled, options.compare);
            assertEquals(Options.COMPMODE_NORMAL, options.comparisonMode);
            assertEquals("en", options.countryTag);
            assertFalse(options.debugParser);
            assertFalse(options.diff_report);
            assertEquals(null, options.exclude_file);
            assertFalse(options.exp);//experiment
            assertTrue(options.externalSearch);
            assertEquals(null, options.filter);
            assertEquals(".", options.filtername);
            assertArrayEquals(new String[]{null}, options.helper);
            assertEquals(null, options.include_file);
            assertNull(options.language);
            assertTrue(options.languageIsFound);
            assertEquals(languageName, options.languageName);
            assertEquals(minTokenMatch, options.min_token_match);
            assertEquals(null, options.original_dir);
            assertEquals(outputLog, options.output_file);
            assertTrue(options.read_subdirs);
            assertEquals(resultDir, options.result_dir);
            assertEquals(sourceDir.getPath(), options.root_dir);
            assertFalse(options.skipParse);
            assertEquals(minStoreMatchAbove, options.store_matches);
            assertTrue(options.store_percent);
            assertEquals(null, options.sub_dir);
            assertArrayEquals(includeFileExtensions.split(","), options.suffixes);
            assertArrayEquals(new int[]{15}, options.themewords);
            assertNull(options.threshold);
            assertEquals("", options.title);
            assertFalse(options.useBasecode);
            assertFalse(options.verbose_details);
            assertFalse(options.verbose_long);
            assertFalse(options.verbose_parser);
            assertTrue(options.verbose_quiet);
            assertEquals(args, options.commandLine);
        }
        
        @Override
        public void isExecutionPhaseAndHasCorrectGetName() {
            super.isExecutionPhaseAndHasCorrectGetName();
            assertEquals("JPlagText",adapter.getName());
        }
    }

    @Test
    public void noErrorsWhenProgramIsRunWithInvalidSubmissions() throws IOException {
        DirectoryCreator.createTestDirWithNFiles(sourceDir, 2);
        adapter.runPlagiarsimDetection(sourceDir);
    }

    public class withValidIdenticalFiles extends AdaptersTestTemplate.withValidIdenticalFiles {

        @Test
        public void programIsCreatedAndRuned() throws IOException, InterruptedException {
            super.programIsCreatedAndRuned();
            JPlagAdapter jpa = (JPlagAdapter) adapter;
            assertNotNull(jpa.getJPlagProgram());
        }

        @Override
        protected void assertEndLinesOf100PrecentMatchPart(List<MPCMatchPart> matchPart) {
            assertEquals(17, matchPart.get(0).endLineNumberA);
            assertEquals(17, matchPart.get(0).endLineNumberB);
        }

        @Override
        protected void assert100PrecentMatchesAreOk(File matchesDir) throws IOException, ClassNotFoundException {
             for (File matchFile : matchesDir.listFiles()) {
                MPCMatch match = matchFileUtility.readFromFile(matchFile);
                assertEquals(matchFile.getName().split("-")[0], match.fileAName);
                assertEquals(matchFile.getName().split("-")[1], match.fileBName);
                assertEquals(97.91666, match.similarity, 0.0001);
                assertEquals(97.91666, match.similarityA, 0.0001);
                assertEquals(97.91666, match.similarityB, 0.0001);
                assertEquals(1, match.matchParts.size());
                List<MPCMatchPart> matchPart = match.matchParts;
                assertEquals(1, matchPart.get(0).startLineNumberA);
                assertEquals(1, matchPart.get(0).startLineNumberB);
                assertEndLinesOf100PrecentMatchPart(matchPart);
                assertEquals(97.91666, matchPart.get(0).similarity, 0.0001);
                assertEquals(97.91666, matchPart.get(0).similarityA, 0.0001);
                assertEquals(97.91666, matchPart.get(0).similarityB, 0.0001);
            }
        }

    }

    public class withValidNonIdenticalFiles extends AdaptersTestTemplate.withValidNonIdenticalFiles {

        @Override
        protected void assertMatchDataIsOkFileContaingingFileTwice(File matchFile, MPCMatch match) {
            assertEquals(matchFile.getName().split("-")[0], match.fileAName);
            assertEquals(matchFile.getName().split("-")[1], match.fileBName);
            System.out.println(match.similarity+" "+match.similarityA+" "+match.similarityB+" "+ match.matchParts.size());
            assertEquals(65.90909, match.similarity, 0.0001);
            assertEquals(98.86364, match.similarityA, 0.0001);
            assertEquals(49.43182, match.similarityB, 0.0001);
            assertEquals(1, match.matchParts.size());
        }

        protected void assertMatchDataIsOk(File matchFile, MPCMatch match) {
            assertEquals(matchFile.getName().split("-")[0], match.fileAName);
            assertEquals(matchFile.getName().split("-")[1], match.fileBName);
            assertEquals(69.11764, match.similarity, 0.0001);
            assertEquals(97.91666, match.similarityA, 0.0001);
            assertEquals(53.40909, match.similarityB, 0.0001);
            assertEquals(2, match.matchParts.size());
        }

        protected void assertFirstMatchPartIsOk(List<MPCMatchPart> matchPart) {
            assertEquals(1, matchPart.get(0).startLineNumberA);
            assertEquals(1, matchPart.get(0).startLineNumberB);
            assertEquals(6, matchPart.get(0).endLineNumberA);
            assertEquals(6, matchPart.get(0).endLineNumberB);
            assertEquals(22.058822, matchPart.get(0).similarity, 0.0001);
            assertEquals(31.25, matchPart.get(0).similarityA, 0.0001);
            assertEquals(17.045454, matchPart.get(0).similarityB, 0.0001);
        }

        protected void assertSecondMatchPartIsOk(List<MPCMatchPart> matchPart) {
            assertEquals(7, matchPart.get(1).startLineNumberA);
            assertEquals(13, matchPart.get(1).startLineNumberB);
            assertEquals(17, matchPart.get(1).endLineNumberA);
            assertEquals(23, matchPart.get(1).endLineNumberB);
            assertEquals(47.058822, matchPart.get(1).similarity, 0.0001);
            assertEquals(66.666664, matchPart.get(1).similarityA, 0.0001);
            assertEquals(36.363636, matchPart.get(1).similarityB, 0.0001);
        }

        protected void assertCalculatedMatchSimilarityIsOk(MPCMatch match) {
            assertEquals(match.similarity, match.calculatedSimilarity, 0.0001);
            assertEquals(match.similarityA, match.calculatedSimilarityA, 0.0001);
            assertEquals(match.similarityB, match.calculatedSimilarityB, 0.0001);
        }
    }

    private class JPlagTextAdapterStub extends JPlagTextAdapter {

        @Override
        protected void createAndRunJplagProgram() throws JPlagException {
        }
        
    }
}
