package org.foi.mpc.matches;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.file.TextFileUtility;
import org.foi.mpc.MPCContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(HierarchicalContextRunner.class)
public class PlagMatchesReaderTest {
    File dir =  new File("plagMatchesDir");
    PlagMatchesReader plagMatchesReader = new PlagMatchesReader(dir);
    TextFileUtility textFileUtility = new TextFileUtility(StandardCharsets.UTF_8);
    File processedFile = new File(dir+File.separator+MPCContext.PROCESSED_MATCH_FILE);
    File plagFile = new File(dir+File.separator+MPCContext.PLAG_MATCH_FILE);

    @Before
    public void setUp() throws Exception {
        dir.mkdirs();
    }

    @After
    public void tearDown() throws IOException {
        DirectoryFileUtility.deleteDirectoryTree(dir);
    }

    @Test
    public void readNonExistingDir(){
        dir.delete();
        plagMatchesReader.read();
        assertEquals(0, plagMatchesReader.matchesCount());
        assertEquals(0,plagMatchesReader.plagMatchesCount());
    }

    @Test
    public void readNonExistingFile(){
        plagMatchesReader.read();
        assertEquals(0, plagMatchesReader.matchesCount());
        assertEquals(0,plagMatchesReader.plagMatchesCount());
    }

    public class plagiarizedCases {


        @Test
        public void readEmptyFile() throws IOException {
            textFileUtility.createFileWithText(plagFile, "");
            plagMatchesReader.read();

            assertEquals(0, plagMatchesReader.matchesCount());
            assertEquals(0,plagMatchesReader.plagMatchesCount());
        }

        @Test
        public void readFileWithSkipWrongLine1() throws IOException {
            textFileUtility.createFileWithText(plagFile, "003.java 004.java\n");
            plagMatchesReader.read();
            assertEquals(0, plagMatchesReader.matchesCount());
            assertEquals(0,plagMatchesReader.plagMatchesCount());
        }

        @Test
        public void readUnexistingMatch() throws IOException {
            textFileUtility.createFileWithText(plagFile, "003.java|004.java\n");
            assertFalse(plagMatchesReader.containsPlagPair("002.java", "004.java"));
        }

        @Test
        public void readFileWithOneLine() throws IOException {
            textFileUtility.createFileWithText(plagFile, "003.java|004.java\n");
            plagMatchesReader.read();

            assertEquals(1, plagMatchesReader.matchesCount());
            assertEquals(1,plagMatchesReader.plagMatchesCount());

            List<MPCPlagMatch> matches = plagMatchesReader.getMatches();
            assertEquals("003.java", matches.get(0).fileA);
            assertEquals("004.java", matches.get(0).fileB);

            assertTrue(plagMatchesReader.containsPlagPair("003.java", "004.java"));
            assertTrue(plagMatchesReader.containsPlagPair("004.java", "003.java"));
        }

        @Test
        public void readLineWithoutNewLineAtTheEnd() throws IOException {
            textFileUtility.createFileWithText(plagFile, "003.java|004.java");
            plagMatchesReader.read();
            assertEquals(1, plagMatchesReader.matchesCount());
            assertEquals(1,plagMatchesReader.plagMatchesCount());

            List<MPCPlagMatch> matches = plagMatchesReader.getMatches();
            assertEquals("003.java", matches.get(0).fileA);
            assertEquals("004.java", matches.get(0).fileB);

            assertTrue(plagMatchesReader.containsPlagPair("003.java", "004.java"));
        }

        @Test
        public void readfileWithMultipleMatches() throws IOException {
            textFileUtility.createFileWithText(plagFile, "003.java|004.java\n005.java|006.java\n008.java|010.java\n014.java|021.java\n");
            plagMatchesReader.read();
            assertEquals(4, plagMatchesReader.matchesCount());
            assertEquals(4,plagMatchesReader.plagMatchesCount());
            assertTrue(plagMatchesReader.containsPlagPair("003.java", "004.java"));
            assertTrue(plagMatchesReader.containsPlagPair("005.java", "006.java"));
            assertTrue(plagMatchesReader.containsPlagPair("008.java", "010.java"));
            assertTrue(plagMatchesReader.containsPlagPair("014.java", "021.java"));

            assertTrue(plagMatchesReader.containsPlagPair("004.java", "003.java"));
            assertTrue(plagMatchesReader.containsPlagPair("006.java", "005.java"));
            assertTrue(plagMatchesReader.containsPlagPair("010.java", "008.java"));
            assertTrue(plagMatchesReader.containsPlagPair("021.java", "014.java"));
        }

        @Test
        public void readfileWithMultipleMatchesWithEmptyLines() throws IOException {
            textFileUtility.createFileWithText(plagFile, "003.java|004.java\n\n005.java|006.java\n\n008.java|010.java\n\n014.java|021.java");
            plagMatchesReader.read();
            assertEquals(4, plagMatchesReader.matchesCount());
            assertEquals(4,plagMatchesReader.plagMatchesCount());
        }

        @Test
        public void readfileWithMultipleSamePlagMatchesLines() throws IOException {
            textFileUtility.createFileWithText(plagFile, "003.java|004.java\n\n003.java|004.java\n\n008.java|010.java\n\n014.java|021.java");
            plagMatchesReader.read();
            assertEquals(3, plagMatchesReader.matchesCount());
            assertEquals(3,plagMatchesReader.plagMatchesCount());
        }

        @Test
        public void addPlagiarizedMatch() throws IOException {
            textFileUtility.createFileWithText(plagFile, "003.java|004.java\n\n005.java|006.java\n\n008.java|010.java\n\n014.java|021.java");
            plagMatchesReader.read();
            plagMatchesReader.addPlagiarizedMatch("new1.java", "new2.java");
            assertEquals(5, plagMatchesReader.matchesCount());
            assertEquals(5,plagMatchesReader.plagMatchesCount());
            assertTrue(plagMatchesReader.containsPlagPair("new1.java","new2.java"));
        }

        @Test
        public void addDuplicatePlagiarizedMatch() throws IOException {
            textFileUtility.createFileWithText(plagFile, "003.java|004.java\n\n005.java|006.java\n\n008.java|010.java\n\n014.java|021.java");
            plagMatchesReader.read();
            plagMatchesReader.addPlagiarizedMatch("new1.java", "new2.java");
            plagMatchesReader.addPlagiarizedMatch("new1.java", "new2.java");
            assertEquals(5, plagMatchesReader.matchesCount());
            assertEquals(5,plagMatchesReader.plagMatchesCount());
            assertTrue(plagMatchesReader.containsPlagPair("new1.java","new2.java"));
        }

        @Test
        public void addPlagiarizedMatchFirstAdd() throws IOException {
            textFileUtility.createFileWithText(plagFile, "003.java|004.java\n\n005.java|006.java\n\n008.java|010.java\n\n014.java|021.java");
            plagMatchesReader = new PlagMatchesReader(dir);

            plagMatchesReader.addPlagiarizedMatch("new1.java", "new2.java");
            plagMatchesReader.read();

            assertEquals(5, plagMatchesReader.matchesCount());
            assertEquals(5,plagMatchesReader.plagMatchesCount());
            assertTrue(plagMatchesReader.containsPlagPair("new1.java","new2.java"));
        }

        @Test
        public void removeMatchWhichIsProcessed() throws IOException {
            textFileUtility.createFileWithText(processedFile, "003.java|004.java\n\n005.java|006.java\n\n008.java|010.java\n\n014.java|021.java");
            plagMatchesReader = new PlagMatchesReader(dir);

            plagMatchesReader.removePlagMatch("003.java", "004.java");
            plagMatchesReader.read();

            assertEquals(4, plagMatchesReader.matchesCount());
            assertEquals(0,plagMatchesReader.plagMatchesCount());
            assertTrue(plagMatchesReader.containsPair("003.java","004.java"));
            assertFalse(plagMatchesReader.containsPlagPair("003.java","004.java"));
        }

        @Test
        public void removeMatchWhichIsPlagiarized() throws IOException {
            textFileUtility.createFileWithText(plagFile, "003.java|004.java\n\n005.java|006.java\n\n008.java|010.java\n\n014.java|021.java");
            plagMatchesReader = new PlagMatchesReader(dir);

            plagMatchesReader.removePlagMatch("003.java", "004.java");
            plagMatchesReader.read();

            assertEquals(4, plagMatchesReader.matchesCount());
            assertEquals(3,plagMatchesReader.plagMatchesCount());
            assertTrue(plagMatchesReader.containsPair("003.java","004.java"));
            assertFalse(plagMatchesReader.containsPlagPair("003.java","004.java"));
        }

        @Test
        public void removeMatchWhichIsPlagiarizedNoReadAgain() throws IOException {
            textFileUtility.createFileWithText(plagFile, "003.java|004.java\n\n005.java|006.java\n\n008.java|010.java\n\n014.java|021.java");
            plagMatchesReader = new PlagMatchesReader(dir);

            plagMatchesReader.removePlagMatch("003.java", "004.java");

            assertEquals(4, plagMatchesReader.matchesCount());
            assertEquals(3,plagMatchesReader.plagMatchesCount());
            assertTrue(plagMatchesReader.containsPair("003.java","004.java"));
            assertFalse(plagMatchesReader.containsPlagPair("003.java","004.java"));
        }

        @Test
        public void removeMatchWhichIsNotPresent() throws IOException {
            textFileUtility.createFileWithText(plagFile, "n005.java|006.java\n\n008.java|010.java\n\n014.java|021.java");
            plagMatchesReader = new PlagMatchesReader(dir);

            plagMatchesReader.removePlagMatch("003.java", "004.java");
            plagMatchesReader.read();

            assertEquals(3, plagMatchesReader.matchesCount());
            assertEquals(3,plagMatchesReader.plagMatchesCount());
            assertFalse(plagMatchesReader.containsPair("003.java","004.java"));
            assertFalse(plagMatchesReader.containsPlagPair("003.java","004.java"));
        }
    }

    public class processedCases {
        @Test
        public void readEmptyFile() throws IOException {
            textFileUtility.createFileWithText(processedFile, "");
            plagMatchesReader.read();

            assertEquals(0, plagMatchesReader.matchesCount());
            assertEquals(0,plagMatchesReader.plagMatchesCount());
        }

        @Test
        public void readFileWithSkipWrongLine1() throws IOException {
            textFileUtility.createFileWithText(processedFile, "003.java 004.java\n");
            plagMatchesReader.read();
            assertEquals(0, plagMatchesReader.matchesCount());
            assertEquals(0,plagMatchesReader.plagMatchesCount());
        }

        @Test
        public void readUnexistingMatch() throws IOException {
            textFileUtility.createFileWithText(processedFile, "003.java|004.java\n");
            assertFalse(plagMatchesReader.containsPair("002.java", "004.java"));
        }

        @Test
        public void readFileWithOneLine() throws IOException {
            textFileUtility.createFileWithText(processedFile, "003.java|004.java\n");
            plagMatchesReader.read();

            assertEquals(1, plagMatchesReader.matchesCount());
            assertEquals(0,plagMatchesReader.plagMatchesCount());

            List<MPCPlagMatch> matches = plagMatchesReader.getMatches();
            assertEquals("003.java", matches.get(0).fileA);
            assertEquals("004.java", matches.get(0).fileB);

            assertTrue(plagMatchesReader.containsPair("003.java", "004.java"));
            assertTrue(plagMatchesReader.containsPair("004.java", "003.java"));
            assertFalse(plagMatchesReader.containsPlagPair("003.java", "004.java"));
            assertFalse(plagMatchesReader.containsPlagPair("004.java", "003.java"));
        }

        @Test
        public void readfileWithMultipleMatches() throws IOException {
            textFileUtility.createFileWithText(processedFile, "003.java|004.java\n005.java|006.java\n008.java|010.java\n014.java|021.java\n");
            plagMatchesReader.read();
            assertEquals(4, plagMatchesReader.matchesCount());
            assertEquals(0,plagMatchesReader.plagMatchesCount());
            assertTrue(plagMatchesReader.containsPair("003.java", "004.java"));
            assertTrue(plagMatchesReader.containsPair("005.java", "006.java"));
            assertTrue(plagMatchesReader.containsPair("008.java", "010.java"));
            assertTrue(plagMatchesReader.containsPair("014.java", "021.java"));

            assertTrue(plagMatchesReader.containsPair("004.java", "003.java"));
            assertTrue(plagMatchesReader.containsPair("006.java", "005.java"));
            assertTrue(plagMatchesReader.containsPair("010.java", "008.java"));
            assertTrue(plagMatchesReader.containsPair("021.java", "014.java"));

            assertFalse(plagMatchesReader.containsPlagPair("003.java", "004.java"));
            assertFalse(plagMatchesReader.containsPlagPair("005.java", "006.java"));
            assertFalse(plagMatchesReader.containsPlagPair("008.java", "010.java"));
            assertFalse(plagMatchesReader.containsPlagPair("014.java", "021.java"));
        }

        @Test
        public void addMatch() throws IOException {
            textFileUtility.createFileWithText(processedFile, "003.java|004.java\n\n005.java|006.java\n\n008.java|010.java\n\n014.java|021.java");
            plagMatchesReader.read();
            plagMatchesReader.addProcessedMatch("new1.java", "new2.java");
            assertEquals(5, plagMatchesReader.matchesCount());
            assertEquals(0,plagMatchesReader.plagMatchesCount());
            assertTrue(plagMatchesReader.containsPair("new1.java","new2.java"));
            assertFalse(plagMatchesReader.containsPlagPair("new1.java","new2.java"));
        }

        @Test
        public void addDuplicateMatch() throws IOException {
            textFileUtility.createFileWithText(processedFile, "003.java|004.java\n\n005.java|006.java\n\n008.java|010.java\n\n014.java|021.java");
            plagMatchesReader.read();
            plagMatchesReader.addProcessedMatch("new1.java", "new2.java");
            plagMatchesReader.addProcessedMatch("new1.java", "new2.java");
            assertEquals(5, plagMatchesReader.matchesCount());
            assertEquals(0,plagMatchesReader.plagMatchesCount());
            assertTrue(plagMatchesReader.containsPair("new1.java","new2.java"));
            assertFalse(plagMatchesReader.containsPlagPair("new1.java","new2.java"));
        }

        @Test
        public void addPMatchFirstAdd() throws IOException {
            textFileUtility.createFileWithText(processedFile, "003.java|004.java\n\n005.java|006.java\n\n008.java|010.java\n\n014.java|021.java");
            plagMatchesReader = new PlagMatchesReader(dir);

            plagMatchesReader.addProcessedMatch("new1.java", "new2.java");
            plagMatchesReader.read();

            assertEquals(5, plagMatchesReader.matchesCount());
            assertEquals(0,plagMatchesReader.plagMatchesCount());
            assertTrue(plagMatchesReader.containsPair("new1.java","new2.java"));
            assertFalse(plagMatchesReader.containsPlagPair("new1.java","new2.java"));
        }

        @Test
        public void removeMatchWhichIsProcessed() throws IOException {
            textFileUtility.createFileWithText(processedFile, "003.java|004.java\n\n005.java|006.java\n\n008.java|010.java\n\n014.java|021.java");
            plagMatchesReader = new PlagMatchesReader(dir);

            plagMatchesReader.removeProcessedMatch("003.java", "004.java");
            plagMatchesReader.read();

            assertEquals(3, plagMatchesReader.matchesCount());
            assertEquals(0,plagMatchesReader.plagMatchesCount());
            assertFalse(plagMatchesReader.containsPair("003.java","004.java"));
            assertFalse(plagMatchesReader.containsPlagPair("003.java","004.java"));
        }

        @Test
        public void removeMatchWhichIsProcessedNoReadAgian() throws IOException {
            textFileUtility.createFileWithText(processedFile, "003.java|004.java\n\n005.java|006.java\n\n008.java|010.java\n\n014.java|021.java");
            plagMatchesReader = new PlagMatchesReader(dir);

            plagMatchesReader.removeProcessedMatch("003.java", "004.java");

            assertEquals(3, plagMatchesReader.matchesCount());
            assertEquals(0,plagMatchesReader.plagMatchesCount());
            assertFalse(plagMatchesReader.containsPair("003.java","004.java"));
            assertFalse(plagMatchesReader.containsPlagPair("003.java","004.java"));
        }

        @Test
        public void removeMatchWhichIsPlagiarized() throws IOException {
            textFileUtility.createFileWithText(plagFile, "003.java|004.java\n\n005.java|006.java\n\n008.java|010.java\n\n014.java|021.java");
            plagMatchesReader = new PlagMatchesReader(dir);

            plagMatchesReader.removeProcessedMatch("003.java", "004.java");
            plagMatchesReader.read();

            assertEquals(4, plagMatchesReader.matchesCount());
            assertEquals(4,plagMatchesReader.plagMatchesCount());
            assertTrue(plagMatchesReader.containsPair("003.java","004.java"));
            assertTrue(plagMatchesReader.containsPlagPair("003.java","004.java"));
        }

        @Test
        public void removeMatchWhichIsNotPresent() throws IOException {
            textFileUtility.createFileWithText(processedFile, "n005.java|006.java\n\n008.java|010.java\n\n014.java|021.java");
            plagMatchesReader = new PlagMatchesReader(dir);

            plagMatchesReader.removeProcessedMatch("003.java", "004.java");
            plagMatchesReader.read();

            assertEquals(3, plagMatchesReader.matchesCount());
            assertEquals(0,plagMatchesReader.plagMatchesCount());
            assertFalse(plagMatchesReader.containsPair("003.java","004.java"));
            assertFalse(plagMatchesReader.containsPlagPair("003.java","004.java"));
        }
    }

    @Test
    public void readfileWithMultipleMatchesPlagiarizedAndNot() throws IOException {
        textFileUtility.createFileWithText(processedFile, "003.java|004.java\n005.java|006.java");
        textFileUtility.createFileWithText(plagFile, "005.java|006.java\n008.java|010.java\n014.java|021.java\n");

        plagMatchesReader.read();
        assertEquals(4, plagMatchesReader.matchesCount());
        assertEquals(3,plagMatchesReader.plagMatchesCount());
        assertTrue(plagMatchesReader.containsPair("003.java", "004.java"));
        assertTrue(plagMatchesReader.containsPair("005.java", "006.java"));
        assertTrue(plagMatchesReader.containsPair("008.java", "010.java"));
        assertTrue(plagMatchesReader.containsPair("014.java", "021.java"));

        assertTrue(plagMatchesReader.containsPair("004.java", "003.java"));
        assertTrue(plagMatchesReader.containsPair("006.java", "005.java"));
        assertTrue(plagMatchesReader.containsPair("010.java", "008.java"));
        assertTrue(plagMatchesReader.containsPair("021.java", "014.java"));
    }

    @Test
    public void addPlagiarizedMatchWithExistingProcessedMatch() throws IOException {
        textFileUtility.createFileWithText(processedFile, "003.java|004.java\n\n005.java|006.java\n\n008.java|010.java\n\n014.java|021.java");

        plagMatchesReader.read();
        plagMatchesReader.addProcessedMatch("new1.java", "new2.java");
        plagMatchesReader.addPlagiarizedMatch("new1.java", "new2.java");

        assertEquals(5, plagMatchesReader.matchesCount());
        assertEquals(1,plagMatchesReader.plagMatchesCount());
        assertTrue(plagMatchesReader.containsPair("new1.java","new2.java"));
        assertTrue(plagMatchesReader.containsPlagPair("new1.java","new2.java"));
    }
}