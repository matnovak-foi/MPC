package org.foi.mpc.executiontools.detectionTools.JPlag;

import org.foi.mpc.matches.models.MPCMatch;

import java.io.File;
import java.util.ArrayList;
import jplag.AllMatches;
import jplag.Match;
import jplag.SortedVector;
import jplag.Token;
import jplag.clustering.ThemeGenerator;
import org.foi.mpc.matches.models.MPCMatchPart;
import org.foi.mpc.matches.MPCMatchFileUtility;

public class JplagMatchesWriter {

    private MPCMatchFileUtility mfu;
    File matchesDir;
    String rootFilesDir;
    MPCMatch mpcMatch;
    boolean isExternalSearch;

    public JplagMatchesWriter(File matchesDir, String rootFilesDir, boolean externalSearch) {
        this.matchesDir = matchesDir;
        this.rootFilesDir = rootFilesDir;
        this.isExternalSearch = externalSearch;
        this.mfu = new MPCMatchFileUtility();
    }
    
    protected void writeAllMatchesToDirectory(SortedVector<AllMatches> matches) {
        for (AllMatches match : matches)
            writeMatchToDirectory(match);
    }

    private void writeMatchToDirectory(AllMatches match) {
        mpcMatch = new MPCMatch();
        processMatchData(match);
        processMatchPartsData(match);
        mfu.saveToFile(mpcMatch);
    }
    
    private void processMatchData(AllMatches match) {
        mpcMatch.fileAName = match.subA.name;
        mpcMatch.fileBName = match.subB.name;
        mpcMatch.sourceDir = matchesDir.getParentFile();
        mpcMatch.matchesDir = matchesDir;
        mpcMatch.similarity = match.percent();
        mpcMatch.similarityA = match.percentA();
        mpcMatch.similarityB = match.percentB();
    }
    
    private void processMatchPartsData(AllMatches match) {
        initializeCalculatedSimilarities();
        prepareForJPlagExternalSearch(match);

        mpcMatch.matchParts = new ArrayList<>();
        for (Match matchPart : match.matches) {
            if (matchPart.length == 0)
                continue;

            processMatchPart(matchPart, match);
            
        }
    }
    
    private void initializeCalculatedSimilarities() {
        mpcMatch.calculatedSimilarity = 0;
        mpcMatch.calculatedSimilarityA = 0;
        mpcMatch.calculatedSimilarityB = 0;
    }

    private void prepareForJPlagExternalSearch(AllMatches match) {
        if (isExternalSearch) {
            ThemeGenerator.loadStructure(match.subA, rootFilesDir);
            ThemeGenerator.loadStructure(match.subB, rootFilesDir);
        }
        match.sort();
    }
    
    private void processMatchPart(Match matchPart, AllMatches match) {
        MPCMatchPart mpcMatchPart = new MPCMatchPart();
        
        Token[] A = match.subA.struct.tokens;
        int divisorA = match.subA.size() - match.subA.files.length;
        mpcMatchPart.startLineNumberA = A[matchPart.startA].getLine();
        Token endA = A[matchPart.startA + matchPart.length - 1];
        mpcMatchPart.endLineNumberA = endA.getLine();
        
        Token[] B = match.subB.struct.tokens;
        int divisorB = match.subB.size() - match.subB.files.length;
        mpcMatchPart.startLineNumberB = B[matchPart.startB].getLine();
        Token endB = B[matchPart.startB + matchPart.length - 1];
        mpcMatchPart.endLineNumberB = endB.getLine();
        
        double similarityMatchPart = (float) (matchPart.length * 200) / (float) (divisorB + divisorA);
        mpcMatchPart.similarity = similarityMatchPart;
        
        double similarityMatchPartA = (float) (matchPart.length * 100) / (float) divisorA;
        mpcMatchPart.similarityA = similarityMatchPartA;
        
        double similarityMatchPartB = (float) (matchPart.length * 100) / (float) divisorB;
        mpcMatchPart.similarityB = similarityMatchPartB;
        
        processMatchCalculatedSimilarities(mpcMatchPart);
        
        mpcMatch.matchParts.add(mpcMatchPart);
    }

    private void processMatchCalculatedSimilarities(MPCMatchPart matchPart) {
        mpcMatch.calculatedSimilarity += matchPart.similarity;
        mpcMatch.calculatedSimilarityA += matchPart.similarityA;
        mpcMatch.calculatedSimilarityB += matchPart.similarityB;
    }
}