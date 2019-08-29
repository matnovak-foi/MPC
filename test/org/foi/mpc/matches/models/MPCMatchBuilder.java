package org.foi.mpc.matches.models;

import org.foi.mpc.MPCContext;

import java.io.File;
import java.util.ArrayList;

public class MPCMatchBuilder {
    private MPCMatch match;

    public MPCMatchBuilder(){
        match = new MPCMatch();
        match.sourceDir = new File("sourceDir");
        match.matchesDir = new File("testDir"+File.separator+ MPCContext.MATCHES_DIR);
        match.fileAName = "userA.java";
        match.fileBName = "userB.java";
        match.matchParts = new ArrayList<>();
    }

    public MPCMatchBuilder withSourceDir(File sourceDir){
        match.sourceDir = sourceDir;
        return this;
    }

    public MPCMatchBuilder withMatchesDir(File matchesDir){
        match.matchesDir = matchesDir;
        return this;
    }

    public MPCMatchBuilder withMatchesDirForTechnique(String techniqueName){
        match.matchesDir = new File(techniqueName+File.separator+MPCContext.MATCHES_DIR);
        return this;
    }

    public MPCMatchBuilder withFileA(String fileA){
        match.fileAName = fileA;
        return this;
    }

    public MPCMatchBuilder withFileB(String fileB){
        match.fileBName = fileB;
        return this;
    }

    public MPCMatchBuilder with100Similarity(){
        return withSimilarity(100);
    }

    public MPCMatchBuilder withSimilarity(int similarity){
        return withDifferentABSimilarity(similarity,similarity);
    }

    public MPCMatchBuilder withDifferentABSimilarity(int similarityA, int similatirtyB){
        match.similarity = (similarityA+similatirtyB)/2;
        match.similarityA = similarityA;
        match.similarityB = similatirtyB;
        match.calculatedSimilarityA = similarityA;
        match.calculatedSimilarityB = similatirtyB;
        match.calculatedSimilarity = (similarityA+similatirtyB)/2;
        match.matchParts = new ArrayList<>();
        MPCMatchPart part = new MPCMatchPart();
        part.endLineNumberA = similarityA+10;
        part.endLineNumberB = similatirtyB+10;
        part.startLineNumberA = similarityA;
        part.startLineNumberB = similatirtyB;
        part.similarity = (similarityA+similatirtyB)/2;
        part.similarityA = similarityA;
        part.similarityB = similatirtyB;
        match.matchParts.add(part);
        return this;
    }

    public MPCMatch build(){
        return match;
    }

    public MPCMatchBuilder withStartAndEndLine(int atIndex,int start, int end) {
        if(match.matchParts.size()>atIndex) {
            match.matchParts.get(atIndex).startLineNumberA = start;
            match.matchParts.get(atIndex).endLineNumberA = end;
            match.matchParts.get(atIndex).startLineNumberB = start;
            match.matchParts.get(atIndex).endLineNumberB = end;
        } else {
            MPCMatchPart part = new MPCMatchPart();
            part.endLineNumberA = end;
            part.endLineNumberB = end;
            part.startLineNumberA = start;
            part.startLineNumberB = start;
            part.similarity = 200/2;
            part.similarityA = 100;
            part.similarityB = 100;
            match.matchParts.add(part);
        }
        return this;
    }
}
