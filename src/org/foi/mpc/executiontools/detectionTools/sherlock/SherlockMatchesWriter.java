package org.foi.mpc.executiontools.detectionTools.sherlock;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import org.foi.common.filesystem.directory.DirectoryTreeWalker;
import org.foi.common.filesystem.file.ObjectFileUtility;
import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.matches.models.MPCMatchPart;
import org.foi.mpc.matches.MPCMatchFileUtility;
import uk.ac.warwick.dcs.cobalt.sherlock.Match;
import uk.ac.warwick.dcs.cobalt.sherlock.Settings;

public class SherlockMatchesWriter implements DirectoryTreeWalker {

    private MPCMatchFileUtility mfu;
    private File matchesDir;
    private File sherlockMatchDir;
    private MPCMatch mpcMatch;

    public SherlockMatchesWriter(File matchesDir, File sherlockMatchDir) {
        this.matchesDir = matchesDir;
        this.sherlockMatchDir = sherlockMatchDir;
        mfu = new MPCMatchFileUtility();
    }

    @Override
    public FileVisitResult visitFile(Path matchFile, BasicFileAttributes attrs) throws IOException {
        Match match = readSherlockMatchFromFile(matchFile);
        String fileNameStart = getFirstPartOfMatchFileName(match, matchFile);
        
        if(isMatchAlreadyProcessed(fileNameStart))
            return FileVisitResult.CONTINUE;
        
        processMatch(match, fileNameStart);
        mfu.saveToFile(mpcMatch);
        
        return FileVisitResult.CONTINUE;
    }
    
    
    private Match readSherlockMatchFromFile(Path matchFile) throws IOException, ObjectFileUtility.ObjectFileException {
        Match match = readOneMatchPartFromFile(matchFile.toFile());
        return match;
    }

    private String getFirstPartOfMatchFileName(Match match, Path matchFile) {
        String fileNameStart = getFileName(match.getFile1()) + "-" + getFileName(match.getFile2());
        if (matchFile.startsWith(getFileName(match.getFile2()))) {
            fileNameStart = getFileName(match.getFile2()) + "-" + getFileName(match.getFile1());
        }
        return fileNameStart;
    }
    
    private boolean isMatchAlreadyProcessed(String fileNameStart) {
        File[] existingMatches = matchesDir.listFiles(new MatchPartsFilter(fileNameStart));
        if (existingMatches.length > 0) {
            return true;
        }
        return false;
    }
    
    private void processMatch(Match match, String fileNameStart) throws IOException, NumberFormatException, ObjectFileUtility.ObjectFileException {
        mpcMatch = new MPCMatch();
        processBaseMatchData(match);
        File[] matchParts = readAllMatchPartsFiles(fileNameStart);
        processMatchParts(matchParts);
        makeAllSimilaritiesEqual();
    }
    
    private void processBaseMatchData(Match match) {
        mpcMatch.fileAName = getFileName(match.getFile1());
        mpcMatch.fileBName = getFileName(match.getFile2());
        mpcMatch.sourceDir = matchesDir.getParentFile();
        mpcMatch.matchesDir = matchesDir;
    }
    
    private File[] readAllMatchPartsFiles(String fileNameStart) {
        File[] matchParts = sherlockMatchDir.listFiles(new MatchPartsFilter(fileNameStart));
        return matchParts;
    }

    private void processMatchParts(File[] matchParts) throws ObjectFileUtility.ObjectFileException, NumberFormatException, IOException {
        mpcMatch.calculatedSimilarity = 0;
        mpcMatch.matchParts = new ArrayList<>();
        
        for (File matchPartFile : matchParts) {
            Match matchPart = readOneMatchPartFromFile(matchPartFile);
            processMatchPart(matchPart);
            mpcMatch.calculatedSimilarity += Float.parseFloat(String.valueOf(matchPart.getSimilarity()));
        }
    }

    private Match readOneMatchPartFromFile(File matchPartFile) throws IOException, ObjectFileUtility.ObjectFileException {
        Match matchPart = null;
        try {
            matchPart = (Match) new ObjectFileUtility().readObjectFromFile(matchPartFile);
        } catch (ClassNotFoundException ex) {
            throw new ObjectFileUtility.ObjectFileException(ex.getMessage());
        }
        return matchPart;
    }

    private void processMatchPart(Match matchPart) throws NumberFormatException {
        MPCMatchPart mpcMatchPart = new MPCMatchPart();
        mpcMatchPart.startLineNumberA = matchPart.getRun().getStartCoordinates().getLineNoInFile1();
        mpcMatchPart.startLineNumberB = matchPart.getRun().getStartCoordinates().getLineNoInFile2();
        mpcMatchPart.endLineNumberA = matchPart.getRun().getEndCoordinates().getLineNoInFile1();
        mpcMatchPart.endLineNumberB = matchPart.getRun().getEndCoordinates().getLineNoInFile2();
        mpcMatchPart.similarity = Float.parseFloat(String.valueOf(matchPart.getSimilarity()));
        mpcMatchPart.similarityA = mpcMatchPart.similarity;
        mpcMatchPart.similarityB = mpcMatchPart.similarity;
        mpcMatch.matchParts.add(mpcMatchPart);
    }

    private void makeAllSimilaritiesEqual() {
        mpcMatch.similarity = mpcMatch.calculatedSimilarity > 100 ? 100 : mpcMatch.calculatedSimilarity;
        mpcMatch.similarityA = mpcMatch.similarity;
        mpcMatch.similarityB = mpcMatch.similarity;
        mpcMatch.calculatedSimilarityA = mpcMatch.calculatedSimilarity;
        mpcMatch.calculatedSimilarityB = mpcMatch.calculatedSimilarity;
    }

    public static String getFileName(String f) {
        int dotindex = f.lastIndexOf('.');
        int slashindex = f.lastIndexOf(Settings.getFileSep());
        String fname = f.substring(slashindex + 1, dotindex);

        return fname;
    }

    private class MatchPartsFilter implements FilenameFilter {

        private String fileNameStart;

        public MatchPartsFilter(String fileNameStart) {
            this.fileNameStart = fileNameStart;
        }

        @Override
        public boolean accept(File dir, String name) {
            return name.startsWith(fileNameStart);
        }

    }
}
