package org.foi.mpc.matches.models;

import java.io.File;
import java.io.Serializable;
import java.util.List;


public class MPCMatch implements Serializable {
    public String fileAName;
    public String fileBName;
    public float similarity;
    public float similarityA;
    public float similarityB;
    public List<MPCMatchPart> matchParts;
    public float calculatedSimilarity;
    public float calculatedSimilarityA;
    public float calculatedSimilarityB;
    public File sourceDir;
    public File matchesDir;

    @Override
    public String toString() {
        return "MPCMatch{" +
                "fileAName='" + fileAName + '\'' +
                ", fileBName='" + fileBName + '\'' +
                ", similarity=" + similarity +
                ", similarityA=" + similarityA +
                ", similarityB=" + similarityB +
                ", matchParts=" + matchParts +
                ", calculatedSimilarity=" + calculatedSimilarity +
                ", calculatedSimilarityA=" + calculatedSimilarityA +
                ", calculatedSimilarityB=" + calculatedSimilarityB +
                ", sourceDir=" + sourceDir +
                ", matchesDir=" + matchesDir +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MPCMatch){
            MPCMatch that = (MPCMatch) obj;
            return that.fileAName.equalsIgnoreCase(fileAName)
                    && that.fileBName.equalsIgnoreCase(fileBName)
                    && that.matchesDir.equals(matchesDir)
                    && that.sourceDir.equals(sourceDir)
                    && that.similarity == similarity
                    && that.similarityB == similarityB
                    && that.similarityA == similarityA
                    && that.calculatedSimilarity == calculatedSimilarity
                    && that.calculatedSimilarityA == calculatedSimilarityA
                    && that.calculatedSimilarityB == calculatedSimilarityB
                    && matchParts.equals(that.matchParts);
        }

        return false;
    }

}