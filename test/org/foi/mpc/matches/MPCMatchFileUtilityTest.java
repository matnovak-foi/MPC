package org.foi.mpc.matches;

import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.matches.models.MPCMatchPart;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class MPCMatchFileUtilityTest {

    @Test
    public void calculateSimilarityFromMatchesLines1(){
        MPCMatch match = new MPCMatch();
        match.matchParts = new ArrayList<>();

        match.similarity = 50f;
        match.similarityA = 50f;
        match.similarityB = 50f;

        MPCMatchPart matchPart1 = new MPCMatchPart();
        matchPart1.startLineNumberA = 0;
        matchPart1.endLineNumberA = 10;
        matchPart1.similarityA = 50;
        matchPart1.startLineNumberB = 0;
        matchPart1.endLineNumberB = 10;
        matchPart1.similarityB = 50;
        match.matchParts.add(matchPart1);


        MPCMatch resultMatch = MPCMatchFileUtility.calculateSimilarityBasedOnLines(match);
        assertEquals(50,resultMatch.similarityA,0.01);
        assertEquals(50,resultMatch.similarityA,0.01);
        assertEquals(50,resultMatch.similarityB,0.01);
    }

    @Test
    public void calculateSimilarityFromMatchesLines2(){
        MPCMatch match = new MPCMatch();
        match.matchParts = new ArrayList<>();

        match.similarity = 100f;
        match.similarityA = 100f;
        match.similarityB = 100f;

        MPCMatchPart matchPart1 = new MPCMatchPart();
        matchPart1.startLineNumberA = 0;
        matchPart1.endLineNumberA = 10;
        matchPart1.similarityA = 50;
        matchPart1.startLineNumberB = 0;
        matchPart1.endLineNumberB = 10;
        matchPart1.similarityB = 50;
        match.matchParts.add(matchPart1);

        MPCMatchPart matchPart2 = new MPCMatchPart();
        matchPart2.startLineNumberA = 0;
        matchPart2.endLineNumberA = 10;
        matchPart2.similarityA = 50;
        matchPart2.startLineNumberB = 0;
        matchPart2.endLineNumberB = 10;
        matchPart2.similarityB = 50;
        match.matchParts.add(matchPart2);

        MPCMatch resultMatch = MPCMatchFileUtility.calculateSimilarityBasedOnLines(match);
        assertEquals(100,resultMatch.similarity,0.01);
        assertEquals(100,resultMatch.similarityA,0.01);
        assertEquals(100,resultMatch.similarityB,0.01);
        assertEquals(50,resultMatch.calculatedSimilarity,0.01);
        assertEquals(50,resultMatch.calculatedSimilarityA,0.01);
        assertEquals(50,resultMatch.calculatedSimilarityB,0.01);
    }

    @Test
    public void calculateSimilarityFromMatchesLines3(){
        MPCMatch match = new MPCMatch();
        match.matchParts = new ArrayList<>();

        match.similarity = 75f;
        match.similarityA = 75f;
        match.similarityB = 75f;

        MPCMatchPart matchPart1 = new MPCMatchPart();
        matchPart1.startLineNumberA = 1;
        matchPart1.endLineNumberA = 10;
        matchPart1.similarityA = 50;
        matchPart1.startLineNumberB = 1;
        matchPart1.endLineNumberB = 10;
        matchPart1.similarityB = 50;
        match.matchParts.add(matchPart1);

        MPCMatchPart matchPart2 = new MPCMatchPart();
        matchPart2.startLineNumberA = 6;
        matchPart2.endLineNumberA = 10;
        matchPart2.similarityA = 25;
        matchPart2.startLineNumberB = 6;
        matchPart2.endLineNumberB = 10;
        matchPart2.similarityB = 25;
        match.matchParts.add(matchPart2);

        MPCMatch resultMatch = MPCMatchFileUtility.calculateSimilarityBasedOnLines(match);
        assertEquals(75,resultMatch.similarity,0.01);
        assertEquals(75,resultMatch.similarityA,0.01);
        assertEquals(75,resultMatch.similarityB,0.01);

        assertEquals(50,resultMatch.calculatedSimilarity,0.01);
        assertEquals(50,resultMatch.calculatedSimilarityA,0.01);
        assertEquals(50,resultMatch.calculatedSimilarityB,0.01);
    }
}