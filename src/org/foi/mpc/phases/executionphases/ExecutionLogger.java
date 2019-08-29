package org.foi.mpc.phases.executionphases;

import org.foi.mpc.MPCContext;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagAdapter;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagAdapterSettings;
import org.foi.mpc.executiontools.detectionTools.sherlock.SherlockAdapter;
import org.foi.mpc.executiontools.detectionTools.sherlock.SherlockAdapterSettings;
import org.foi.mpc.executiontools.detectionTools.sherlock.SherlockOriginalAdapter;
import org.foi.mpc.executiontools.detectionTools.sherlock.SherlockTokenisedAdapter;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneAdapter;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneAdapterSettings;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExecutionLogger {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private long startTime;
    private long endTime;

    public void setStartTime(String message){
        startTime = System.currentTimeMillis();
        //System.out.println(message+":START:"+dateFormat.format(new Date()));
    }

    public void setEndTime(String message){
        endTime = System.currentTimeMillis();
        //System.out.println(message+":END"+dateFormat.format(new Date()));
    }

    public void printTimeElapsed(String message) {
        if(MPCContext.CONSOLE_PRINT)
            System.out.println(message+":Time passed:"+(endTime-startTime));
    }

    public void endRun(int processedDirsCount, File dirToProces, ExecutionTool tool){
        if(MPCContext.CONSOLE_PRINT) {
            System.out.println("END");
            System.out.println("ITERATION: " + processedDirsCount);
            System.out.println("AT: " + dateFormat.format(new Date()));
            System.out.println("Processing dir: " + dirToProces.getAbsolutePath());
            System.out.println("With tool: " + tool.getName());
        }
    }

    public void beginRun(int processedDirsCount, File dirToProces, ExecutionTool tool) {
        if(MPCContext.CONSOLE_PRINT) {
            System.out.println("\n\n\nBEGIN");
            System.out.println("ITERATION: " + processedDirsCount);
            System.out.println("AT: " + dateFormat.format(new Date()));
            System.out.println("Processing dir: " + dirToProces.getAbsolutePath());
            System.out.println("With tool: " + tool.getName());
            if (tool instanceof JPlagAdapter) {
                JPlagAdapterSettings settings = ((JPlagAdapter) tool).getSettings();
                System.out.println("Param: " + settings.minTokenMatch);
            } else if (tool instanceof SimGruneAdapter) {
                SimGruneAdapterSettings settings = ((SimGruneAdapter) tool).getSettings();
                System.out.println("Param: " + settings.minRunLength);
            } else if (tool instanceof SherlockAdapter) {
                SherlockAdapterSettings settings = null;
                if (tool instanceof SherlockTokenisedAdapter)
                    settings = ((SherlockTokenisedAdapter) tool).getSettings();
                else if (tool instanceof SherlockOriginalAdapter)
                    settings = ((SherlockOriginalAdapter) tool).getSettings();

                if (settings != null)
                    System.out.println("Param MBJ: " + settings.maxBackwardJump + " MFJ: " + settings.maxForwardJump +
                            " MJD: " + settings.maxJumpDiff + " MRL: " + settings.minRunLenght +
                            " MSL: " + settings.minStringLength + " STR: " + settings.strictness +
                            " AMALGAMATE: " + settings.amalgamate + " CONCATENATE: " + settings.concatanate);
            }
            System.out.println("\n");
        }
    }
}
