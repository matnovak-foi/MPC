package org.foi.mpc.executiontools.detectionTools.spector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.matches.models.MPCMatchPart;
import org.foi.mpc.matches.MPCMatchFileUtility;
import spector.Comparison;

public class SpectorMatchesWriter {

    public class SpectorMatchesWriterException extends RuntimeException {
    }

    File matchesDir;
    MPCMatch mpcMatch;
    MPCMatchFileUtility mfu;

    public SpectorMatchesWriter(File matchesDir) {
        this.matchesDir = matchesDir;
        mfu = new MPCMatchFileUtility();
    }

    protected void writeAllMatchesToDirectory(List<Comparison> comparisons) {
        for (Comparison match : comparisons) {
            
            if (isMatchAlreadyProcessed(match.getFirst().getName(), match.getSecond().getName())) {
                continue;
            }
            
            mpcMatch = new MPCMatch();
            processMatch(match, comparisons);
            prepareDefaultMatchPart();
            mfu.saveToFile(mpcMatch);
        }
    }

    private void processMatch(Comparison match, List<Comparison> comparisons) throws SpectorMatchesWriterException {
        File fileA = match.getFirst().getFile();
        File fileB = match.getSecond().getFile();
        
        mpcMatch.fileAName = fileA.getName();
        mpcMatch.fileBName = fileB.getName();
        mpcMatch.sourceDir = matchesDir.getParentFile();
        mpcMatch.matchesDir = matchesDir;

        Comparison viceVersaMatch = findComparisonFromFileBtoA(comparisons, fileA, fileB);
        
        processMatchSimilarity(viceVersaMatch, match);
    }

    private void processMatchSimilarity(Comparison viceVersaMatch, Comparison match) throws SpectorMatchesWriterException {
        if (viceVersaMatch != null) {
            mpcMatch.similarityA = (float) match.getTotal();
            mpcMatch.similarityB = (float) viceVersaMatch.getTotal();
            mpcMatch.similarity = (mpcMatch.similarityA + mpcMatch.similarityB) / 2;
            mpcMatch.calculatedSimilarity = mpcMatch.similarity;
            mpcMatch.calculatedSimilarityA = mpcMatch.similarityA;
            mpcMatch.calculatedSimilarityB = mpcMatch.similarityB;  
        } else {
            throw new SpectorMatchesWriterException();
        }
    }

    private Comparison findComparisonFromFileBtoA(List<Comparison> comparisons, File fileA, File fileB) {
        Comparison viceVersa = null;
        for (Comparison comp : comparisons) {
            if (comp.getFirst().getFile().equals(fileB)
                    && comp.getSecond().getFile().equals(fileA)) {
                viceVersa = comp;
                break;
            }
        }
        return viceVersa;
    }

    private boolean isMatchAlreadyProcessed(String fileAName, String fileBName) {
        String matchFileNameExist = fileBName + "-" + fileAName + "-match";
        File matchFileExist = new File(matchesDir.getPath() + File.separator + matchFileNameExist);
        if (matchFileExist.exists()) {
            return true;
        }
        return false;
    }

    private void prepareDefaultMatchPart() {
        MPCMatchPart matchPart = new MPCMatchPart();
        matchPart.startLineNumberA = 1;
        matchPart.startLineNumberB = 1;
        matchPart.endLineNumberA = -1;
        matchPart.endLineNumberB = -1;
        matchPart.similarity = mpcMatch.similarity;
        matchPart.similarityA = mpcMatch.similarityA;
        matchPart.similarityB = mpcMatch.similarityB;

        mpcMatch.matchParts = new ArrayList<>();
        mpcMatch.matchParts.add(matchPart);
    }
}
