package org.foi.mpc.matches;

import org.foi.common.filesystem.file.ObjectFileUtility;
import org.foi.mpc.MPCContext;
import org.foi.mpc.executiontools.detectionTools.sherlock.SherlockOriginalAdapter;
import org.foi.mpc.executiontools.detectionTools.sherlock.SherlockTokenisedAdapter;
import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.matches.models.MPCMatchPart;
import org.foi.mpc.phases.executionphases.ExecutionLogger;
import uk.ac.warwick.dcs.cobalt.sherlock.Sherlock;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MPCMatchFileUtility {

    private static String sufix = "-match";
    ObjectFileUtility ofu;

    public MPCMatchFileUtility() {
        ofu = new ObjectFileUtility();
    }

    public void setObjectFileUtility(ObjectFileUtility ofu) {
        this.ofu = ofu;
    }

    public void saveToFile(MPCMatch match) throws ObjectFileUtility.ObjectFileException {
        String matchFileName = match.fileAName + "-" + match.fileBName + sufix;
        File matchFile = new File(match.matchesDir.getPath() + File.separator + matchFileName);
        try {
            ofu.writeObjectToFile(matchFile, match);
        } catch (IOException ex) {
            throw new ObjectFileUtility.ObjectFileException(ex.getMessage());
        }
    }

    public MPCMatch readFromFile(File file) {
        MPCMatch match = null;

        try {
            match = (MPCMatch) ofu.readObjectFromFile(file);
        } catch (Exception e) {
            throw new ObjectFileUtility.ObjectFileException(
                    "file: " + file.getPath() + "\n" + e.getMessage());
        }

        match = calculateSimilarityBasedOnLinesForSherlock(match);
        return match;
    }

    public MPCMatch readFromFile(File matchesDir, String fileAName, String fileBName) {

        String matchFileName = matchesDir.getPath()+File.separator+fileAName + "-" + fileBName + sufix;
        String matchFileNameReversed = matchesDir.getPath()+File.separator+fileBName + "-" + fileAName + sufix;
        File matchFile = new File(matchFileName);
        File matchFileReversed = new File(matchFileNameReversed);

        if(matchFile.exists())
            return readFromFile(matchFile);
        else if(matchFileReversed.exists())
            return revereseABifNecesary(readFromFile(matchFileReversed),fileAName);

        MPCMatch match = createDummyMatch();
        return match;
    }



    public MPCMatch revereseABifNecesary(MPCMatch match, String fileAName){
        if(match.fileBName.equalsIgnoreCase(fileAName)){
            MPCMatch reversedMatch = new MPCMatch();
            reversedMatch.fileAName = match.fileBName;
            reversedMatch.fileBName = match.fileAName;
            reversedMatch.similarity = match.similarity;
            reversedMatch.similarityA = match.similarityB;
            reversedMatch.similarityB = match.similarityA;
            reversedMatch.calculatedSimilarity = match.calculatedSimilarity;
            reversedMatch.calculatedSimilarityA = match.calculatedSimilarityB;
            reversedMatch.calculatedSimilarityB = match.calculatedSimilarityA;
            reversedMatch.matchesDir = match.matchesDir;
            reversedMatch.sourceDir = match.sourceDir;

            reversedMatch.matchParts = new ArrayList<>();
            for(MPCMatchPart matchPart : match.matchParts){
                MPCMatchPart reversedMatchPart = new MPCMatchPart();
                reversedMatchPart.similarity = matchPart.similarity;
                reversedMatchPart.similarityA = matchPart.similarityB;
                reversedMatchPart.similarityB = matchPart.similarityA;
                reversedMatchPart.startLineNumberA = matchPart.startLineNumberB;
                reversedMatchPart.startLineNumberB = matchPart.startLineNumberA;
                reversedMatchPart.endLineNumberA = matchPart.endLineNumberB;
                reversedMatchPart.endLineNumberB = matchPart.endLineNumberA;
                reversedMatch.matchParts.add(reversedMatchPart);
            }

            return reversedMatch;
        }

        return match;
    }

    protected MPCMatch createDummyMatch() {
        MPCMatch match = new MPCMatch();
        match.matchParts = new ArrayList<>();
        return match;
    }

    private static MPCMatch calculateSimilarityBasedOnLinesForSherlock(MPCMatch match){
        if(match != null && (match.matchesDir.getAbsolutePath().contains(SherlockOriginalAdapter.NAME)
                || match.matchesDir.getAbsolutePath().contains(SherlockTokenisedAdapter.NAME))){
            //match.matchesDir = matchesDir;
            return calculateSimilarityBasedOnLines(match);
        }
        return match;
    }

    public static MPCMatch calculateSimilarityBasedOnLines(MPCMatch match) {
        if(match != null) {
            List<Integer> usedUpLinesA = new ArrayList<>();
            List<Integer> usedUpLinesB = new ArrayList<>();
            List<Integer> allLinesA = new ArrayList<>();
            List<Integer> allLinesB = new ArrayList<>();

            for (MPCMatchPart part : match.matchParts) {
                //System.out.println(part.startLineNumberA+"-"+part.endLineNumberA);
                //System.out.println(part.startLineNumberB+"-"+part.endLineNumberB);
                for (int i = part.startLineNumberA; i <= part.endLineNumberA; i++) {
                    allLinesA.add(i);
                    if (!usedUpLinesA.contains(i)) {
                        usedUpLinesA.add(i);
                    }
                }
                for (int i = part.startLineNumberB; i <= part.endLineNumberB; i++) {
                    allLinesB.add(i);
                    if (!usedUpLinesB.contains(i)) {
                        usedUpLinesB.add(i);
                    }
                }
            }
/* TO calculate similarity based on lines
            int tootalLinesA = 0;
            int tootalLinesB = 0;
            try {
                tootalLinesA = countLinesNew(match.matchesDir.getParentFile()+File.separator+match.fileAName);
                tootalLinesB = countLinesNew(match.matchesDir.getParentFile()+File.separator+match.fileBName);
            } catch (IOException e) {
                e.printStackTrace();
            }
*/
            double lineWeightA = match.similarityA / allLinesA.size();
            double lineWeightB = match.similarityB / allLinesB.size();

            match.calculatedSimilarity = match.similarity;
            match.calculatedSimilarityA = match.similarityA;
            match.calculatedSimilarityB = match.similarityB;
/* TO calculate similarity based on lines
            System.out.println("Total:"+tootalLinesA+"-"+tootalLinesB);
            float simA = (usedUpLinesA.size()/Float.valueOf(tootalLinesA))*100;
            float simB = (usedUpLinesB.size()/Float.valueOf(tootalLinesB))*100;
            System.out.println("A:"+simA);
            System.out.println("B:"+simB);
            (simA+simB)/2f;
            */
            match.calculatedSimilarity = Float.valueOf(Double.toString((usedUpLinesA.size() * lineWeightA + usedUpLinesB.size() * lineWeightB) / 2));
            match.calculatedSimilarityA = Float.valueOf(Double.toString(usedUpLinesA.size() * lineWeightA));
            match.calculatedSimilarityB = Float.valueOf(Double.toString(usedUpLinesB.size() * lineWeightB));

            return match;
        } else {
            return match;
        }
    }

    public static List<Integer> addLinesToTotalLineCount(int start, int end, List<Integer> totalLines){
        for (int i = start; i <= end; i++) {
            if (!totalLines.contains(i)) {
                totalLines.add(i);
            }
        }
        return totalLines;
    }

    //https://stackoverflow.com/questions/453018/number-of-lines-in-a-file-in-java
    public static int countLinesNew(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];

            int readChars = is.read(c);
            if (readChars == -1) {
                // bail out if nothing to read
                return 0;
            }

            // make it easy for the optimizer to tune this loop
            int count = 0;
            int i = 0;
            while (readChars == 1024) {
                for (i=0; i<1024;) {
                    if (c[i++] == '\n') {
                        ++count;
                    }
                }
                readChars = is.read(c);
            }

            // count remaining characters
            while (readChars != -1) {
                //System.out.println(readChars);
                for (i=0; i<readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
                readChars = is.read(c);
            }

            if(c[--i] != '\n')
                ++count;

            return count == 0 ? 1 : count;
        } finally {
            is.close();
        }
    }
}
