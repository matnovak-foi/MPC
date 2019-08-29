package org.foi.mpc.executiontools.detectionTools.simgrune;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.matches.models.MPCMatchPart;
import org.foi.mpc.matches.MPCMatchFileUtility;

public class SimGruneMatchesWriter {

    private Matcher matchPart;
    private File matchesDir;
    private MPCMatch mpcMatch;
    private boolean precentUpdateMode = false;
    MPCMatchFileUtility mfu;

    protected Map<String, MPCMatch> mpcMatches = new HashMap<>();
    protected Map<String, Integer> detectedFilesWithLines = new HashMap<>();
    protected Map<String, Integer> detectedFilesWithTokens = new HashMap<>();

    public SimGruneMatchesWriter(File matchesDir) {
        this.matchesDir = matchesDir;
        mfu = new MPCMatchFileUtility();
    }

    public void addDetectedFile(String fileName, int lineCount, int tokenCount) throws NumberFormatException {
        detectedFilesWithLines.put(fileName, lineCount);
        detectedFilesWithTokens.put(fileName, tokenCount);
    }

    public int getDetectedFilesCount() {
        return detectedFilesWithLines.size();
    }

    public void processMatchPartLine(Matcher matchPart) {
        this.matchPart = matchPart;

        createMatch();
        if (isMatchPartNotProcessed()) {
            processMatchPart();
            mpcMatches.put(createMatchKey(mpcMatch), mpcMatch);
        }
    }

    public void writeMatchesToDirectory() {
        mpcMatches.values().forEach((mpcMatch) -> {
            mfu.saveToFile(mpcMatch);
        });
    }

    private void createMatch() {
        if (existsMatch(getFileAName(), getFileBName())) {
            mpcMatch = findMatch(getFileAName(), getFileBName());
        } else {
            mpcMatch = createNewMatch();
        }
    }

    private boolean existsMatch(String fileAName, String fileBName) {
        return mpcMatches.containsKey(fileAName + "-" + fileBName) || mpcMatches.containsKey(fileBName + "-" + fileAName);
    }

    protected MPCMatch findMatch(String fileNameA, String fileNameB) {
        if (mpcMatches.containsKey(fileNameA + "-" + fileNameB)) {
            return mpcMatches.get(fileNameA + "-" + fileNameB);
        } else {
            return mpcMatches.get(fileNameB + "-" + fileNameA);
        }
    }

    private MPCMatch createNewMatch() {
        MPCMatch mpcMatch = new MPCMatch();
        mpcMatch.fileAName = getFileAName();
        mpcMatch.fileBName = getFileBName();
        mpcMatch.similarity = 0;
        mpcMatch.similarityA = 0;
        mpcMatch.similarityB = 0;
        mpcMatch.matchesDir = matchesDir;
        mpcMatch.sourceDir = matchesDir.getParentFile();
        mpcMatch.matchParts = new ArrayList<>();
        return mpcMatch;
    }

    private boolean isMatchPartNotProcessed() {
        for (MPCMatchPart mpcMatchPart : mpcMatch.matchParts) {
            if (isReversedMatch()
                    && isSameMatchPartReversed(mpcMatchPart)) {
                return false;
            } else if (isSameMatchPart(mpcMatchPart)) {
                return false;
            }
        }
        return true;
    }

    private boolean isSameMatchPartReversed(MPCMatchPart mpcMatchPart) {
        return mpcMatchPart.startLineNumberA == getFileBStartLine()
                && mpcMatchPart.startLineNumberB == getFileAStartLine()
                && mpcMatchPart.endLineNumberA == getFileBEndLine()
                && mpcMatchPart.endLineNumberB == getFileAEndLine();
    }

    private boolean isSameMatchPart(MPCMatchPart mpcMatchPart) {
        return mpcMatchPart.startLineNumberA == getFileAStartLine()
                && mpcMatchPart.startLineNumberB == getFileBStartLine()
                && mpcMatchPart.endLineNumberA == getFileAEndLine()
                && mpcMatchPart.endLineNumberB == getFileBEndLine();
    }

    private void processMatchPart() {
        MPCMatchPart mpcMatchPart = new MPCMatchPart();
        mpcMatchPart = prepareMatchPartLines(mpcMatchPart);
        mpcMatchPart = prepareMatchPartSimilarities(mpcMatchPart);
        prepareMatchSimilarities(mpcMatchPart);
    }

    private MPCMatchPart prepareMatchPartLines(MPCMatchPart mpcMatchPart) {
        if (isReversedMatch()) {
            mpcMatchPart.startLineNumberA = getFileBStartLine();
            mpcMatchPart.startLineNumberB = getFileAStartLine();
            mpcMatchPart.endLineNumberA = getFileBEndLine();
            mpcMatchPart.endLineNumberB = getFileAEndLine();
        } else {
            mpcMatchPart.startLineNumberA = getFileAStartLine();
            mpcMatchPart.startLineNumberB = getFileBStartLine();
            mpcMatchPart.endLineNumberA = getFileAEndLine();
            mpcMatchPart.endLineNumberB = getFileBEndLine();
        }
        return mpcMatchPart;
    }

    private boolean isReversedMatch() {
        return mpcMatch.fileAName.equals(getFileBName())
                && mpcMatch.fileBName.equals(getFileAName());
    }

    private MPCMatchPart prepareMatchPartSimilarities(MPCMatchPart mpcMatchPart) {
        int devisorA = getFileTokenCount(mpcMatch.fileAName);//getFileLineCount(mpcMatch.fileAName);
        int devisorB = getFileTokenCount(mpcMatch.fileBName);//getFileLineCount(mpcMatch.fileBName);
        mpcMatchPart.similarityA = ((double) (getMatchPartToken())/devisorA)*100;//((double) (mpcMatchPart.endLineNumberA - mpcMatchPart.startLineNumberA + 1) / devisorA) * 100;
        mpcMatchPart.similarityB = ((double) (getMatchPartToken())/devisorB)*100;//((double) (mpcMatchPart.endLineNumberB - mpcMatchPart.startLineNumberB + 1) / devisorB) * 100;
        mpcMatchPart.similarity = (mpcMatchPart.similarityA + mpcMatchPart.similarityB) / 2;
        return mpcMatchPart;
    }

    private void prepareMatchSimilarities(MPCMatchPart mpcMatchPart) {
        mpcMatch.matchParts.add(mpcMatchPart);
        mpcMatch.similarity += mpcMatchPart.similarity;
        mpcMatch.similarityA += mpcMatchPart.similarityA;
        mpcMatch.similarityB += mpcMatchPart.similarityB;
        mpcMatch.calculatedSimilarity = mpcMatch.similarity;
        mpcMatch.calculatedSimilarityA = mpcMatch.similarityA;
        mpcMatch.calculatedSimilarityB = mpcMatch.similarityB;
    }

    private String getFileAName() {
        return matchPart.group(1);
    }

    private String getFileBName() {
        return matchPart.group(4);
    }

    private int getFileAStartLine() {
        return Integer.parseInt(matchPart.group(2));
    }

    private int getFileBStartLine() {
        return Integer.parseInt(matchPart.group(5));
    }

    private int getFileAEndLine() {
        return Integer.parseInt(matchPart.group(3));
    }

    private int getFileBEndLine() {
        return Integer.parseInt(matchPart.group(6));
    }

    private int getMatchPartToken() {return Integer.parseInt(matchPart.group(7));}

    protected int getFileLineCount(String fileName) {
        return detectedFilesWithLines.get(fileName);
    }

    public String createMatchKey(MPCMatch mpcMatch) {
        return mpcMatch.fileAName + "-" + mpcMatch.fileBName;
    }

    public int getFileTokenCount(String fileName) {
        return detectedFilesWithTokens.get(fileName);
    }

    public void processPrecentMatchPart(Matcher m) {
        matchPart = m;
        MPCMatch match = findMatch(getFileAName(),m.group(3));
        if(match.fileAName.equals(getFileAName())) {
            match.similarityA = getMatchSimilarity();
            match.similarity = (match.similarityA + match.similarityB) / 2;
        } else {
            match.similarityB = getMatchSimilarity();
            match.similarity = (match.similarityA + match.similarityB) / 2;
        }
    }

    private float getMatchSimilarity(){
        return Float.parseFloat(matchPart.group(2));
    }
}
