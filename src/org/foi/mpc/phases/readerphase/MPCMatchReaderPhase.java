package org.foi.mpc.phases.readerphase;

import org.foi.mpc.MPCContext;
import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.matches.MPCMatchFileUtility;
import org.foi.mpc.phases.executionphases.ExecutionLogger;
import org.foi.mpc.phases.runner.Phase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MPCMatchReaderPhase extends Thread implements Phase  {
    private String matches_dir;
    protected MPCMatchListener listener;
    private List<File> dirsToProcess;
    private MPCMatchFileUtility mfu;

    public MPCMatchReaderPhase() {
        mfu = new MPCMatchFileUtility();
    }

    public void setMPCMatchFileUtility(MPCMatchFileUtility mfu) {
        this.mfu = mfu;
    }

    public void setListener(MPCMatchListener listener){
        this.listener = listener;
    }

    public void setMatches_dir(String matches_dir) {
        this.matches_dir = matches_dir;
    }

    @Override
    public void run() {
        runPhase();
    }

    @Override
    public List<File> getOutputDirs() {
        return null;
    }

    @Override
    public void setDirsToProcess(List<File> dirsToProcess) {
        this.dirsToProcess = dirsToProcess;
    }

    @Override
    public void runPhase() {
        for(File dir : dirsToProcess) {
            for (File subDir : dir.listFiles()) {
                if (subDir.getName().equals(matches_dir)) {
                    int threadNum = 0;

                    ExecutionLogger logger1 = new ExecutionLogger();
                    logger1.setStartTime(subDir.getPath());
                    if(MPCContext.MATCHES_THREAD_READ) {
                        for (File file : subDir.listFiles()) {
                            ReadFileThread readFileThread = new ReadFileThread();
                            readFileThread.threadNum = threadNum++;
                            readFileThread.file = file;
                            addRemoveSizeThread(readFileThread, ThreadAction.add);
                            readFileThread.start();
                        }
                    } else {
                        for (File file : subDir.listFiles()) {
                            MPCMatch match = mfu.readFromFile(file);
                            listener.processMatch(match);
                        }
                    }
                    logger1.setEndTime(subDir.getPath());
                    logger1.printTimeElapsed("Threads start: "+subDir.getPath());

                    while (addRemoveSizeThread(null,ThreadAction.size)>0){
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    logger1.setEndTime(subDir.getPath());
                    logger1.printTimeElapsed("Finished with match dir:"+subDir.getPath());
                }
            }
        }
    }

    private List<ReadFileThread> threads = new ArrayList<>();
    private enum ThreadAction {
        size, add, remove
    }
    private synchronized int addRemoveSizeThread(ReadFileThread thread, ThreadAction action){
        switch (action){
            case size: return threads.size();
            case add: threads.add(thread); break;
            case remove: threads.remove(thread); break;
        }
        return 0;
    }
    private class ReadFileThread extends Thread {
        public File file;
        public int threadNum;

        @Override
        public void run() {
            //ExecutionLogger logger2 = new ExecutionLogger();
            //logger2.setStartTime("");
            MPCMatch match = mfu.readFromFile(file);
            //logger2.setEndTime("");
            //logger2.printTimeElapsed("MPCMatchFileUtility.readFromFile:"+threadNum+":");
            callListenter(match);
            //logger2.setEndTime("");
            //logger2.printTimeElapsed("MPCMatchFileUtility.calledListener:"+threadNum+":");
            addRemoveSizeThread(this,ThreadAction.remove);
            //logger2.setEndTime("");
            //logger2.printTimeElapsed("MPCMatchFileUtility.threadEnd:"+threadNum+":");
        }
    }

    private synchronized void callListenter(MPCMatch match){
        ExecutionLogger logger2 = new ExecutionLogger();
        logger2.setStartTime("");
        listener.processMatch(match);
        logger2.setEndTime("");
    }


}
