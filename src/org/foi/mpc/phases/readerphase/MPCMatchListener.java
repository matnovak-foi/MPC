package org.foi.mpc.phases.readerphase;

import org.foi.mpc.matches.models.MPCMatch;

public interface MPCMatchListener {
    public void processMatch(MPCMatch match);
}
