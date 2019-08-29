package org.foi.mpc.matches.models;

import java.io.Serializable;

public class MPCMatchPart implements Serializable {
    public int startLineNumberA;
    public int startLineNumberB;
    public int endLineNumberA;
    public int endLineNumberB;
    public double similarity;
    public double similarityA;
    public double similarityB;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MPCMatchPart){
            MPCMatchPart that = (MPCMatchPart) obj;
            return that.similarityA == similarityA
                    && that.similarityB == similarityB
                    && that.similarity == similarity
                    && that.startLineNumberA == startLineNumberA
                    && that.startLineNumberB == startLineNumberB
                    && that.endLineNumberA == endLineNumberA
                    && that.endLineNumberB == endLineNumberB;
        }

        return false;
    }

    @Override
    public String toString() {
        return "MPCMatchPart{" +
                "startLineNumberA=" + startLineNumberA +
                ", startLineNumberB=" + startLineNumberB +
                ", endLineNumberA=" + endLineNumberA +
                ", endLineNumberB=" + endLineNumberB +
                ", similarity=" + similarity +
                ", similarityA=" + similarityA +
                ", similarityB=" + similarityB +
                '}';
    }
}
