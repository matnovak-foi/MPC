package org.foi.mpc.executiontools.detectionTools.learningTests;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import jplag.AllMatches;
import jplag.ExitException;
import jplag.Match;
import jplag.Program;
import jplag.SortedVector;
import jplag.Token;
import jplag.clustering.Cluster;
import jplag.clustering.ThemeGenerator;
import jplag.options.CommandLineOptions;
import jplag.options.Options;
import jplagUtils.PropertiesLoader;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.file.TextFileUtility;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class JPlagProcessTest {

    File testDir;
    String language = "text";//java17
    int minTokenMatch = 2;
    int store_matches = 2;
    String outputLog;
    String resultDir;
    String includedExtensions = ".txt,.java";

    @Before
    public void setUp() throws IOException {
        TextFileUtility tfu = new TextFileUtility(StandardCharsets.UTF_8);
        testDir = new File("JplagProcessTestDir");
        outputLog = testDir.getAbsolutePath() + File.separator + "JPlag.log";
        resultDir = testDir.getAbsolutePath() + File.separator + "result";
        //testDir.mkdir();
        File file1 = new File(testDir.getPath() + File.separator + "test.txt");
        File file2 = new File(testDir.getPath() + File.separator + "test2.txt");
        //tfu.createFileWithText(file1, TextCreator.validMiniCodeExcample());
        //tfu.createFileWithText(file2, TextCreator.validMiniCodeExcample());
        //testDir.mkdir();
    }

    @After
    public void tearDown() throws IOException {
        DirectoryFileUtility.deleteDirectoryTree(new File(testDir.getPath() + File.separator + "temp"));
        File result = new File(resultDir);
        if(result.exists()){
            DirectoryFileUtility.deleteDirectoryTree(result);
        }
        //DirectoryFileUtility.deleteDirectoryTree(testDir);
    }

    @Test
    public void runJpagNoParmsPrintsUsage() throws ExitException {
        String[] args = new String[]{};
        CommandLineOptions options = new CommandLineOptions(args);
    }

    @Test
    public void defaultCMDParams() throws ExitException {
        String args = testDir.getPath() + " -vq -external -p " + includedExtensions + " -o " + outputLog + " -t " + minTokenMatch + " -m " + store_matches + "% -r " + resultDir + " -l " + language + " ";
        CommandLineOptions options = new CommandLineOptions(args.split(" "));
        assertOptions(options, args);
        Program p = new JPlagTestAdapter(options);
        p.run();
    }

    @Test
    public void runJPlagOnSomeDir() throws ExitException {
        minTokenMatch = 9;
        store_matches = 0;
        language = "java17";
        testDir = new File("D:\\java\\doktorski_rad\\ppTest\\optOutputData4\\preprocess\\RemoveComments\\SubmissionFilesUnifier");
        outputLog = testDir.getAbsolutePath() + File.separator + "JPlag.log";
        resultDir = testDir.getAbsolutePath() + File.separator + "result";
        String args = testDir.getPath() + " -vd -external -p " + includedExtensions + " -o " + outputLog + " -t " + minTokenMatch + " -m " + store_matches + "% -r " + resultDir + " -l " + language + " ";
        CommandLineOptions options = new CommandLineOptions(args.split(" "));
        Program p = new JPlagTestAdapter(options);
        p.run();
    }

    @Test
    public void defaultCMDParamsManual() throws ExitException {
        String[] args = new String[]{};
        CommandLineOptions options = new CommandLineOptions(args);
        options.languageIsFound = true;
        options.languageName = language;
        options.min_token_match = minTokenMatch;
        options.store_matches = store_matches;
        options.store_percent = true;
        options.output_file = outputLog;
        options.result_dir = resultDir;
        options.root_dir = testDir.getPath();
        options.suffixes = includedExtensions.split(",");
        options.verbose_quiet = true;
        options.verbose_long = false;
        options.verbose_parser = false;
        options.verbose_details = false;
        options.externalSearch = true;
        assertOptions(options, "");
        Program p = new JPlagTestAdapter(options);
        options.suffixes = includedExtensions.split(",");
        options.min_token_match = minTokenMatch;
        options.debugParser = true;
        p.run();
    }

    private void assertOptions(CommandLineOptions options, String args) {
        int disabled = 0;
        assertEquals("", options.basecode);
        assertEquals(disabled, options.clusterType);
        assertFalse(options.clustering);
        assertEquals(args, options.commandLine);
        assertEquals(disabled, options.compare);//speacial compariosn
        assertEquals(Options.COMPMODE_NORMAL, options.comparisonMode);
        assertEquals("en", options.countryTag);
        assertFalse(options.debugParser);
        assertFalse(options.diff_report);
        assertEquals(null, options.exclude_file);
        assertFalse(options.exp);//experiment
        assertTrue(options.externalSearch);
        /*
            "-external" option to activate External Search. This options allows
to have thousands of submissions by not loading them all into memory at
the same time. During parsing temporary files are created and stored in
the sub directory "temp". These temporary files store the Structure
objects which will be loaded in blocks and compared.
- "-external" also activates the creation of a new HTML file containing the
token length distribution as of request be Ronald Kostoff. 
         */
        assertEquals(null, options.filter);
        assertEquals(".", options.filtername);
        assertArrayEquals(new String[]{null}, options.helper);
        assertEquals(null, options.include_file);
        assertEquals(null, options.language);
        assertTrue(options.languageIsFound);
        assertEquals(language, options.languageName);
        assertEquals(minTokenMatch, options.min_token_match);
        assertEquals(null, options.original_dir);
        assertEquals(outputLog, options.output_file);
        assertFalse(options.read_subdirs);
        assertEquals(resultDir, options.result_dir);
        assertEquals(testDir.getPath(), options.root_dir);
        assertFalse(options.skipParse);
        /*
        Additional option "-skipparse" is meant to be used in connection with
        external to skip parsing if the temporary files have already been created. 
         */
        assertEquals(store_matches, options.store_matches);
        assertTrue(options.store_percent);
        assertEquals(null, options.sub_dir);
        assertArrayEquals(includedExtensions.split(","), options.suffixes);
        assertArrayEquals(new int[]{15}, options.themewords);
        assertEquals(null, options.threshold);
        assertEquals("", options.title);
        assertFalse(options.useBasecode);
        assertFalse(options.verbose_details);
        assertFalse(options.verbose_long);
        assertFalse(options.verbose_parser);
        assertTrue(options.verbose_quiet);
    }

    @Test
    public void intializeJplagWithClusters() throws ExitException {
        String args = testDir.getPath() + " -clustertype avr -vq -external -p " + includedExtensions + " -o " + outputLog + " -t " + minTokenMatch + " -m " + store_matches + "% -r " + resultDir + " -l " + language + " ";
        CommandLineOptions options = new CommandLineOptions(args.split(" "));
        Program p = new JPlagTestAdapter(options);
        p.run();

        Properties props = PropertiesLoader.loadProps("jplag/version.properties");
        for (Object k : props.keySet()) {
            System.out.println("Param:" + k.toString() + "=>" + props.getProperty((String) k));
        }
    }

    private class JPlagTestAdapter extends Program {

        public JPlagTestAdapter(Options options) throws ExitException {
            super(options);
        }

        @Override
        protected void writeResults(int[] dist, SortedVector<AllMatches> avgmatches, SortedVector<AllMatches> maxmatches, SortedVector<AllMatches> minmatches, Cluster clustering) throws ExitException {
            super.writeResults(dist, avgmatches, maxmatches, minmatches, clustering);
//            System.out.println("AVG");
            if (avgmatches != null) {
                saveMatches(avgmatches);
            }
        }

        private void saveMatches(SortedVector<AllMatches> matches) throws ExitException {
            for (AllMatches runmatch : matches) {
                int divisorA = runmatch.subA.size() - runmatch.subA.files.length;
                int divisorB = runmatch.subB.size() - runmatch.subB.files.length;

                System.out.println(runmatch.subA.name + " vs " + runmatch.subB.name);
                if (use_externalSearch()) {
                    ThemeGenerator.loadStructure(runmatch.subA, get_root_dir());
                    ThemeGenerator.loadStructure(runmatch.subB, get_root_dir());
                }

                Token[] A = runmatch.subA.struct.tokens;
                Token[] B = runmatch.subB.struct.tokens;
                runmatch.sort();
                float similarTotalA = 0;
                float similarTotalB = 0;
                float similarTotal = 0;
                for (Match match : runmatch.matches) {
                    if (match.length == 0) {
                        continue;
                    }
                    Token startA = A[match.startA];
                    Token endeA = A[match.startA + match.length - 1];

                    Token startB = B[match.startB];
                    Token endeB = B[match.startB + match.length - 1];

                    similarTotalA = similarTotalA + (float) ((float) (match.length * 100) / (float) divisorA);
                    similarTotalB = similarTotalB + (float) ((float) (match.length * 100) / (float) divisorB);
                    similarTotal = similarTotal + (float) ((float) (match.length * 200) / (float) (divisorB + divisorA));
                    System.out.println("Sim" + similarTotalA);

                    Path file1 = Paths.get(runmatch.subA.name);
                    Path file2 = Paths.get(runmatch.subB.name);

                    int start_line_f1 = startA.getLine();
                    int end_line_f1 = endeA.getLine();
                    int start_line_f2 = startB.getLine();
                    int end_line_f2 = endeB.getLine();
                    double similarity = ((float) (match.length * 200) / (float) (divisorB + divisorA));
                    double similarity_f1 = ((float) (match.length * 100) / (float) divisorA);
                    double similarity_f2 = ((float) (match.length * 100) / (float) divisorB);
                    System.out.println("File1 " + file1.toString());
                    System.out.println("File2 " + file2.toString());
                    System.out.println("start_line_f1 " + start_line_f1);
                    System.out.println("end_line_f1 " + end_line_f1);
                    System.out.println("start_line_f2 " + start_line_f2);
                    System.out.println("end_line_f2 " + end_line_f2);
                    System.out.println("similarity " + similarity);
                    System.out.println("similarity_f1 " + similarity_f1);
                    System.out.println("similarity_f2 " + similarity_f2);
                }
                System.out.println("SimTotal " + similarTotal + " check " + runmatch.percent());
                System.out.println("SimTotal A" + similarTotalA + " check " + runmatch.percentA());
                System.out.println("SimTotal B" + similarTotalB + " check " + runmatch.percentB());
            }
        }
    }
}
