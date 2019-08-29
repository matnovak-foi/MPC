package org.foi.mpc;

import org.foi.mpc.matches.MPCMatchFileUtility;
import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.matches.models.MPCMatchBuilder;
import org.foi.mpc.phases.readerphase.MPCMatchReaderPhase;
import org.foi.mpc.phases.readerphase.MPCMatchReaderPhaseTest;
import org.junit.Test;

import java.io.File;


public class QuickMPCReader {
    @Test
    public void readMPCFile(){
        MPCMatchFileUtility mfu  =new MPCMatchFileUtility();
        MPCMatch match = mfu.readFromFile(new File("/media/matnovak/HomeData/java/doktorski_rad/MPC/testInputData/statisticsReportWorkingDir/detection/JPlagJava/NoPreprocessing/SubmissionFilesUnifier/NWTiS/2017-2018/DZ1/matches/C20001.java-C20000.java-match"));
        System.out.println(match);
    }
}
