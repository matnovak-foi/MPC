package org.foi.mpc.phases.runner;

import org.foi.mpc.MPCContext;
import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.phases.executionphases.ExecutionTool;
import org.foi.mpc.phases.readerphase.MPCMatchListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhaseRunnerBuilderDummy extends PhaseRunnerBuilder {

    public PhaseRunnerBuilderDummy(String matchesDir) {
        super(matchesDir);
    }

    @Override
    public PhaseRunner build() {
        return new PhaseRunner(new PhaseRunnerSettings());
    }
}
