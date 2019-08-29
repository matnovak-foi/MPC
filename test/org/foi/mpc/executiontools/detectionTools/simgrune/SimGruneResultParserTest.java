package org.foi.mpc.executiontools.detectionTools.simgrune;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.foi.mpc.MPCContext;
import org.foi.mpc.matches.models.MPCMatchBuilder;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.file.TextFileUtility;
import org.foi.mpc.matches.models.MPCMatch;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;

import org.junit.runner.RunWith;

@RunWith(HierarchicalContextRunner.class)
public class SimGruneResultParserTest {

    File matchesDir;
    SimGruneResultParser parser;
    SimGruneMatchesWriter writer;
    File resultFile;

    @Before
    public void setUp() throws IOException {
        matchesDir = new File(MPCContext.MATCHES_DIR);
        parser = new SimGruneResultParser(matchesDir);
        writer = parser.getWriter();
    }

    @Test(expected = SimGruneResultParser.SimGruneException.class)
    public void wrongLineGiven() {
        String headerLine = "wrongLine";
        parser.parseResultLine(headerLine);
    }

    @Test(expected = SimGruneResultParser.SimGruneException.class)
    public void wrongHeaderLineGiven() {
        String headerLine = "File wrongHeaderLine";
        parser.parseResultLine(headerLine);
    }

    @Test
    public void canParseHeaderLine() {
        String headerLine = "File SIMProcessTestDir/test.txt: 74 tokens, 19 lines (not NL-terminated)";
        parser.parseResultLine(headerLine);
        assertFileNameIsParsedOutCorrectlyFromHeaderLine("test.txt");
        assertEquals(19, writer.getFileLineCount("test.txt"));
        assertEquals(74, writer.getFileTokenCount("test.txt"));
    }

    @Test
    public void canParseHearderLineBug(){
        String headerLine = "File D:\\java\\doktorski_rad\\SOCO\\socoDatasetsWorking\\detection\\SIMGruneJava\\NoPreprocessing\\SubmissionFilesUnifier\\SOCO\\TEST\\A1/A10378: 606 tokens, 1 line (not NL-terminated)";
        parser.parseResultLine(headerLine);
        assertFileNameIsParsedOutCorrectlyFromHeaderLine("A10378");
        assertEquals(1, writer.getFileLineCount("A10378"));
        assertEquals(606, writer.getFileTokenCount("A10378"));
    }

    @Test
    public void canParseMultipleHeaderLines() {
        parseMultipleHeaderLines();
        for (int i = 1; i < 5; i++) {
            assertFileNameIsParsedOutCorrectlyFromHeaderLine("test" + i + ".txt");
            assertEquals((23 + i), writer.getFileLineCount("test" + i + ".txt"));
        }
    }

    private void assertFileNameIsParsedOutCorrectlyFromHeaderLine(String fileName) {
        assertTrue(writer.detectedFilesWithLines.containsKey(fileName));
    }

    @Test(expected = SimGruneResultParser.SimGruneException.class)
    public void wrongTotalLine() {
        String headerLine = "Total wrongHeaderLine";
        parser.parseResultLine(headerLine);
    }

    @Test(expected = SimGruneResultParser.SimGruneException.class)
    public void parseHeaderLinesCountAndTotoalcountDoNotMatchUp() {
        String totalLine = "Total input: 1 files (1 new, 0 old), 250 tokens";
        parser.parseResultLine(totalLine);
    }

    @Test
    public void canParseTotalLineWithZeroFiles() {
        String totalLine = "Total input: 0 files (0 new, 0 old), 250 tokens";
        parser.parseResultLine(totalLine);
        assertEquals(0, writer.getDetectedFilesCount());
    }

    @Test
    public void canParseTotalLineWithHeaderLine() {
        String headerLine = "File SIMProcessTestDir/test1.txt: 74 tokens, 24 lines (not NL-terminated)";
        String totalLine = "Total input: 1 files (0 new, 0 old), 250 tokens";
        parser.parseResultLine(headerLine);
        parser.parseResultLine(totalLine);
        assertEquals(1, writer.getDetectedFilesCount());
    }

    @Test
    public void ignoresEmptyLine() {
        parser.parseResultLine("");
        assertEquals(0, writer.getDetectedFilesCount());
    }

    @Test(expected = SimGruneResultParser.SimGruneException.class)
    public void wrongMatchPartLine() {
        String wrongMatchPartLine = "SIMProcessTestDir/test.txt: line 1-19 SIMProcessTestDir/test2.txt: line 1-19[74]";
        parser.parseResultLine(wrongMatchPartLine);
    }

    @Test(expected = SimGruneResultParser.SimGruneException.class)
    public void wrongPrecentMatchPartLine() {
        String wrongMatchPartLine = "wrongPrecentLine consists for 98 of SubmissionFilesUnifier/test2.java material";
        parser.parseResultLine(wrongMatchPartLine);
    }

    public class MatchPartLineTests {

        @Before
        public void setUp() {
            parseMultipleHeaderLines();
        }

        @Test
        public void parsesMatchPartLine() {
            String matchesPartLine = "SIMProcessTestDir/test1.txt: line 1-19 |SIMProcessTestDir/test2.txt: line 1-19[74]";
            parser.parseResultLine(matchesPartLine);
            assertEquals(1, writer.mpcMatches.size());
            assertTrueMatchExistForFileCombo("test1.txt", "test2.txt");
            assertMatchPartIsParsedOk(writer.findMatch("test1.txt", "test2.txt"));
        }

        @Test
        public void parsesTwoMatchPartLinesDifferentFiles() {
            String matchesPartLine1 = "SIMProcessTestDir/test1.txt: line 1-19 |SIMProcessTestDir/test2.txt: line 1-19[74]";
            String matchesPartLine2 = "SIMProcessTestDir/test3.txt: line 1-19 |SIMProcessTestDir/test4.txt: line 1-19[74]";
            parser.parseResultLine(matchesPartLine1);
            parser.parseResultLine(matchesPartLine2);
            assertEquals(2, writer.mpcMatches.size());
            assertTrueMatchExistForFileCombo("test1.txt", "test2.txt");
            assertTrueMatchExistForFileCombo("test3.txt", "test4.txt");
        }

        @Test
        public void parsesTwoMatchPartLinesSameFiles() {
            String matchesPartLine1 = "SIMProcessTestDir/test1.txt: line 1-19 |SIMProcessTestDir/test2.txt: line 1-19[74]";
            String matchesPartLine2 = "SIMProcessTestDir/test1.txt: line 20-22 |SIMProcessTestDir/test2.txt: line 20-22[74]";
            parser.parseResultLine(matchesPartLine1);
            parser.parseResultLine(matchesPartLine2);
            assertEquals(1, writer.mpcMatches.size());
            assertTrueMatchExistForFileCombo("test1.txt", "test2.txt");
            MPCMatch match = writer.findMatch("test1.txt", "test2.txt");
            assertEquals(2, match.matchParts.size());
        }

        @Test
        public void parsesTwoMatchPartLinesSameFilesReversed() {
            String matchesPartLine1 = "SIMProcessTestDir/test1.txt: line 1-19 |SIMProcessTestDir/test2.txt: line 1-19[74]";
            String matchesPartLine2 = "SIMProcessTestDir/test2.txt: line 20-22 |SIMProcessTestDir/test1.txt: line 20-22[74]";
            parser.parseResultLine(matchesPartLine1);
            parser.parseResultLine(matchesPartLine2);
            assertEquals(1, writer.mpcMatches.size());
            assertTrueMatchExistForFileCombo("test1.txt", "test2.txt");
            MPCMatch match = writer.findMatch("test1.txt", "test2.txt");
            assertEquals(2, match.matchParts.size());
        }

        @Test
        public void parsesTwoEqualMatchPartLinesSameFilesReversed() {
            String matchesPartLine1 = "SIMProcessTestDir/test1.txt: line 1-19 |SIMProcessTestDir/test2.txt: line 1-19[74]";
            String matchesPartLine2 = "SIMProcessTestDir/test2.txt: line 1-19 |SIMProcessTestDir/test1.txt: line 1-19[74]";
            parser.parseResultLine(matchesPartLine1);
            parser.parseResultLine(matchesPartLine2);
            assertEquals(1, writer.mpcMatches.size());
            MPCMatch match = writer.findMatch("test1.txt", "test2.txt");
            assertEquals(1, match.matchParts.size());
        }

        @Test
        public void parsesTwoEqualMatchPartLinesSameFilesReversedDifferentLines() {
            String matchesPartLine1 = "SIMProcessTestDir/test1.txt: line 1-19 |SIMProcessTestDir/test2.txt: line 20-39[74]";
            String matchesPartLine2 = "SIMProcessTestDir/test2.txt: line 20-39 |SIMProcessTestDir/test1.txt: line 1-19[74]";
            parser.parseResultLine(matchesPartLine1);
            parser.parseResultLine(matchesPartLine2);
            assertEquals(1, writer.mpcMatches.size());
            MPCMatch match = writer.findMatch("test1.txt", "test2.txt");
            assertEquals(1, match.matchParts.size());
        }
        
        @Test
        public void parsesTwoEqualMatchPartLinesSameFilesReversedTwiceDifferentLines() {
            String matchesPartLine1 = "SIMProcessTestDir/test1.txt: line 10-20 |SIMProcessTestDir/test2.txt: line 13-25[74]";
            String matchesPartLine2 = "SIMProcessTestDir/test2.txt: line 13-25 |SIMProcessTestDir/test1.txt: line 10-20[74]";
            String matchesPartLine3 = "SIMProcessTestDir/test2.txt: line 1-10 |SIMProcessTestDir/test1.txt: line 1-5[15]";
            String matchesPartLine4 = "SIMProcessTestDir/test1.txt: line 1-5 |SIMProcessTestDir/test2.txt: line 1-10[15]";

            parser.parseResultLine(matchesPartLine1);
            parser.parseResultLine(matchesPartLine2);
            parser.parseResultLine(matchesPartLine3);
            parser.parseResultLine(matchesPartLine4);

            assertEquals(1, writer.mpcMatches.size());
            MPCMatch match = writer.findMatch("test1.txt", "test2.txt");
            assertThat(match.matchParts, hasSize(2));
            assertEquals(((double)74/74)*100,match.matchParts.get(0).similarityA,0.0001);
            assertEquals(((double)74/102)*100,match.matchParts.get(0).similarityB,0.0001);
            assertEquals(((double)15/74)*100,match.matchParts.get(1).similarityA,0.0001);
            assertEquals(((double)15/102)*100,match.matchParts.get(1).similarityB,0.0001);
        }

        @Test
        public void parsesPrecentMatchPartLinesPairs(){
            MPCMatch match = new MPCMatchBuilder().withFileA("test1.java").withFileB("test2.java").with100Similarity().build();
            writer.mpcMatches.put(writer.createMatchKey(match),match);
            String matchesPartLine = "SubmissionFilesUnifier/test1.java consists for 98 % of SubmissionFilesUnifier/test2.java material";
            String matchesPartLineReversed = "SubmissionFilesUnifier/test2.java consists for 96 % of SubmissionFilesUnifier/test1.java material";

            parser.parseResultLine(matchesPartLine);
            parser.parseResultLine(matchesPartLineReversed);

            MPCMatch resultMatch = writer.findMatch("test1.java", "test2.java");
            assertEquals(1, writer.mpcMatches.size());
            assertEquals(97,resultMatch.similarity,0.0001);
            assertEquals(98,resultMatch.similarityA,0.0001);
            assertEquals(96,resultMatch.similarityB,0.0001);
        }

        private void assertMatchPartIsParsedOk(MPCMatch match) {
            assertEquals(1, match.matchParts.size());
            assertEquals(1, match.matchParts.get(0).startLineNumberA);
            assertEquals(19, match.matchParts.get(0).endLineNumberA);
            assertEquals(1, match.matchParts.get(0).startLineNumberB);
            assertEquals(19, match.matchParts.get(0).endLineNumberB);
        }

        private void assertTrueMatchExistForFileCombo(String fileNameA, String fileNameB) {
            assertTrue(writer.mpcMatches.containsKey(fileNameA + "-" + fileNameB));
            MPCMatch match = writer.findMatch(fileNameA, fileNameB);
            assertEquals(fileNameA, match.fileAName);
            assertEquals(fileNameB, match.fileBName);
        }
    }
    
    public class withRealFile {
        TextFileUtility tfu = new TextFileUtility(StandardCharsets.UTF_8);
        File precentResultFile;

        @Before
        public void setUp() throws IOException {
            matchesDir.mkdir();
            resultFile = new File(matchesDir.getPath() + File.separator + "result.txt");
            tfu.createFileWithText(resultFile, getTestFileContent());
            precentResultFile = new File(matchesDir.getPath() + File.separator + "resultPrecent.txt");
            tfu.createFileWithText(precentResultFile, getPrecentTestFileContent());
        }

        @After
        public void tearDown() throws IOException {
            DirectoryFileUtility.deleteDirectoryTree(matchesDir);
        }

        @Test
        public void canReadAllLinesFromResultFilesAndParseThem() throws IOException {
            parser.processResultFile(resultFile);
            parser.processResultFile(precentResultFile);

            assertEquals(3, writer.getDetectedFilesCount());
            assertEquals(3, writer.mpcMatches.size());

            MPCMatch resultMatch = writer.findMatch("test3.txt", "test.txt");
            assertEquals("test3.txt",resultMatch.fileAName);
            assertEquals("test.txt",resultMatch.fileBName);
            assertEquals(97,resultMatch.similarity,0.0001);
            assertEquals(98,resultMatch.similarityA,0.0001);
            assertEquals(96,resultMatch.similarityB,0.0001);
        }
    }
    
    @Test(timeout = 5000)
    public void speedTestOfInMemoryProcessPartParse90300Lines(){
        for(int i=0;i<300;i++){
            String hLine = "File SIMProcessTestDir/test"+i+".txt: 74 tokens, 24 lines (not NL-terminated)";
            parser.parseResultLine(hLine);
        }
        for(int i=0;i<300;i++){
            for(int j=0;j<i;j++){
                String matchesPartLine = "SIMProcessTestDir/test"+i+".txt: line "+10+i+"-20 |SIMProcessTestDir/test"+j+".txt: line "+13+j+"-25[74]";
                parser.parseResultLine(matchesPartLine);
            }
        }
    }
    
    private void parseMultipleHeaderLines() {
        List<String> headerLines = new ArrayList<>();
        headerLines.add("File SIMProcessTestDir/test1.txt: 74 tokens, 24 lines (not NL-terminated)");
        headerLines.add("File SIMProcessTestDir/test2.txt: 102 tokens, 25 lines (not NL-terminated)");
        headerLines.add("File SIMProcessTestDir/test3.txt: 104 tokens, 26 lines (not NL-terminated)");
        headerLines.add("File SIMProcessTestDir/test4.txt: 105 tokens, 27 lines (not NL-terminated)");
        headerLines.forEach((headerLine) -> {
            parser.parseResultLine(headerLine);
        });
    }

    private String getTestFileContent() {
        return "File SIMProcessTestDir/test.txt: 74 tokens, 19 lines (not NL-terminated)\n"
                + "File SIMProcessTestDir/test2.txt: 74 tokens, 19 lines (not NL-terminated)\n"
                + "File SIMProcessTestDir/test3.txt: 102 tokens, 25 lines (not NL-terminated)\n"
                + "Total input: 3 files (3 new, 0 old), 250 tokens\n"
                + "\n"
                + "SIMProcessTestDir/test2.txt: line 1-19 |SIMProcessTestDir/test.txt: line 1-19[74]\n"
                + "\n"
                + "SIMProcessTestDir/test.txt: line 1-19  |SIMProcessTestDir/test2.txt: line 1-19[74]\n"
                + "\n"
                + "SIMProcessTestDir/test3.txt: line 13-25|SIMProcessTestDir/test.txt: line 7-19[53]\n"
                + "\n"
                + "SIMProcessTestDir/test3.txt: line 13-25|SIMProcessTestDir/test2.txt: line 7-19[53]\n"
                + "\n"
                + "SIMProcessTestDir/test.txt: line 7-19  |SIMProcessTestDir/test3.txt: line 13-25[53]\n"
                + "\n"
                + "SIMProcessTestDir/test2.txt: line 7-19 |SIMProcessTestDir/test3.txt: line 13-25[53]\n"
                + "\n"
                + "SIMProcessTestDir/test2.txt: line 1-6  |SIMProcessTestDir/test3.txt: line 1-6[21]\n"
                + "\n"
                + "SIMProcessTestDir/test.txt: line 1-6   |SIMProcessTestDir/test3.txt: line 1-6[21]\n"
                + "\n"
                + "SIMProcessTestDir/test3.txt: line 1-6  |SIMProcessTestDir/test.txt: line 1-6[21]\n"
                + "\n"
                + "SIMProcessTestDir/test3.txt: line 1-6  |SIMProcessTestDir/test2.txt: line 1-6[21]\n"
                + "\n"
                + "";
    }

    private String getPrecentTestFileContent(){
        return "File SubmissionFilesUnifier/test.txt: 74 tokens, 19 lines (not NL-terminated)\n" +
                "File SubmissionFilesUnifier/test2.txt: 74 tokens, 19 lines (not NL-terminated)\n" +
                "File SubmissionFilesUnifier/test3.txt: 102 tokens, 25 lines (not NL-terminated)\n" +
                "Total input: 3 files (3 new, 0 old), 250 tokens\n" +
                "\n" +
                "SubmissionFilesUnifier/test.txt consists for 96 % of SubmissionFilesUnifier/test3.txt material\n" +
                "SubmissionFilesUnifier/test3.txt consists for 98 % of SubmissionFilesUnifier/test.txt material\n";
    }
}
