package org.foi.mpc.executiontools.detectionTools.JPlag;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import java.io.File;
import java.io.IOException;
import java.util.List;
import jplag.options.CommandLineOptions;
import jplag.options.Options;
import org.foi.mpc.MPCContext;
import org.foi.mpc.executiontools.detectionTools.AdaptersTestTemplate;
import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.matches.models.MPCMatchPart;
import org.foi.mpc.executiontools.factories.SimilarityDetectionToolFactory;
import org.foi.common.filesystem.directory.DirectoryCreator;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(HierarchicalContextRunner.class)
public class JPlagJavaAdapterTest extends AdaptersTestTemplate {

    @Before
    public void setUp() throws IOException {
        super.setUp();
        adapter = (new SimilarityDetectionToolFactory(MPCContext.MATCHES_DIR)).createTool(JPlagJavaAdapter.NAME);
    }

    public class withDummySourceDirAndNoRealRun extends AdaptersTestTemplate.withDummySourceDirAndNoRealRun {

        @Before
        public void setUp() throws IOException {
            super.setUp();
            adapter = new JPlagJavaAdapterStub();
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
            String languageName = "java17";
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
            assertEquals("JPlagJava",adapter.getName());
        }
    }

    @Test(expected = JPlagAdapter.JPlagException.class)
    public void programIsRunWithMissingValidSubmissions() throws IOException {
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
            assertEquals(19, matchPart.get(0).endLineNumberA);
            assertEquals(19, matchPart.get(0).endLineNumberB);
        }
    }

    public class withValidNonIdenticalFiles extends AdaptersTestTemplate.withValidNonIdenticalFiles {

        @Override
        protected void assertMatchDataIsOkFileContaingingFileTwice(File matchFile, MPCMatch match) {
            assertEquals(matchFile.getName().split("-")[0], match.fileAName);
            assertEquals(matchFile.getName().split("-")[1], match.fileBName);
            System.out.println(match.similarity+" "+match.similarityA+" "+match.similarityB+" "+ match.matchParts.size());
            assertEquals(68.75, match.similarity, 0.0001);
            assertEquals(100, match.similarityA, 0.0001);
            assertEquals(52.38095, match.similarityB, 0.0001);
            assertEquals(2, match.matchParts.size());
        }

        protected void assertMatchDataIsOk(File matchFile, MPCMatch match) {
            assertEquals(matchFile.getName().split("-")[0], match.fileAName);
            assertEquals(matchFile.getName().split("-")[1], match.fileBName);
            assertEquals(84.210525, match.similarity, 0.0001);
            assertEquals(100, match.similarityA, 0.0001);
            assertEquals(72.727272, match.similarityB, 0.0001);
            assertEquals(2, match.matchParts.size());
        }

        protected void assertFirstMatchPartIsOk(List<MPCMatchPart> matchPart) {
            assertEquals(1, matchPart.get(0).startLineNumberA);
            assertEquals(1, matchPart.get(0).startLineNumberB);
            assertEquals(6, matchPart.get(0).endLineNumberA);
            assertEquals(6, matchPart.get(0).endLineNumberB);
            assertEquals(15.789473, matchPart.get(0).similarity, 0.0001);
            assertEquals(18.75, matchPart.get(0).similarityA, 0.0001);
            assertEquals(13.636363, matchPart.get(0).similarityB, 0.0001);
        }

        protected void assertSecondMatchPartIsOk(List<MPCMatchPart> matchPart) {
            assertEquals(7, matchPart.get(1).startLineNumberA);
            assertEquals(13, matchPart.get(1).startLineNumberB);
            assertEquals(19, matchPart.get(1).endLineNumberA);
            assertEquals(25, matchPart.get(1).endLineNumberB);
            assertEquals(68.421051, matchPart.get(1).similarity, 0.0001);
            assertEquals(81.25, matchPart.get(1).similarityA, 0.0001);
            assertEquals(59.090980, matchPart.get(1).similarityB, 0.0001);
        }

        protected void assertCalculatedMatchSimilarityIsOk(MPCMatch match) {
            assertEquals(match.similarity, match.calculatedSimilarity, 0.0001);
            assertEquals(match.similarityA, match.calculatedSimilarityA, 0.0001);
            assertEquals(match.similarityB, match.calculatedSimilarityB, 0.0001);
        }
    }

    private class JPlagJavaAdapterStub extends JPlagJavaAdapter {

        @Override
        protected void createAndRunJplagProgram() throws JPlagException {
        }
        
    }
}
