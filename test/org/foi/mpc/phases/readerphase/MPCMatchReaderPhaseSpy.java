package org.foi.mpc.phases.readerphase;

import org.foi.mpc.matches.models.MPCMatch;

import java.io.File;
import java.util.List;

public class MPCMatchReaderPhaseSpy extends MPCMatchReaderPhase {
    public int numberOfMatches = 0;
    public List<MPCMatch> mpcMatches;
    public int callCount = 0;

    public MPCMatchReaderPhaseSpy() {
        super();
    }

    @Override
    public List<File> getOutputDirs() {
        return null;
    }

    @Override
    public void runPhase() {
        callCount++;
        for(MPCMatch match : mpcMatches)
            listener.processMatch(match);
    }
}
