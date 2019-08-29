package org.foi.mpc.phases.executionphases;

import org.foi.mpc.phases.executionphases.spies.ExecutionToolSpy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ExecutionToolAssertHelper {

    public static void assertToolWasRunNTimes(ExecutionToolSpy executionToolSpy, int numberOfRuns) {
        assertTrue(executionToolSpy.runMethodWasCalled());
        assertEquals(numberOfRuns, executionToolSpy.wasRunHowManyTimes());
        executionToolSpy.resetRunTimeCounter();
    }
}
