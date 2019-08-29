package org.foi.mpc.phases.runner;

import org.foi.mpc.phases.executionphases.ExecutionPhase;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

@RunWith(HierarchicalContextRunner.class)
public class PhaseRunnerTest {
    
    @Rule
    public ExpectedException exceptionThrown = ExpectedException.none();

    PhaseRunner phaseRunner;
    PhaseRunnerSettings phaseRunnerSettings;
    PhaseSpy preparerSpy;
    PhaseSpy preprocesserSpy;
    PhaseSpy detectionSpy;
    PhaseSpy readerSpy;

    @Before
    public void setUp() throws Exception {
        phaseRunnerSettings = new PhaseRunnerSettings();
        phaseRunnerSettings.runPreparePhase = false;
        phaseRunnerSettings.runPreprocessPhase = false;
        phaseRunnerSettings.runDetectionPhase = false;
        phaseRunnerSettings.runMPCMatchReadPhase = false;

        phaseRunner = new PhaseRunner(phaseRunnerSettings);

        preparerSpy = new PhaseSpy();
        preprocesserSpy = new PhaseSpy();
        detectionSpy = new PhaseSpy();
        readerSpy = new PhaseSpy();
    }

    @After
    public void tearDown() throws Exception {

    }

    public class phasesInitalizationTests {
        
        @Test(expected = ExecutionPhase.PhaseNotInstanciatedException.class)
        public void throwsPhaseErrorForPreparePhase() {
            phaseRunnerSettings.runPreparePhase = true;
            phaseRunner.runPhases();
        }    
        
        @Test(expected = ExecutionPhase.PhaseNotInstanciatedException.class)
        public void throwsPhaseErrorForPreprocessPhase() {
            phaseRunnerSettings.runPreprocessPhase = true;
            phaseRunner.runPhases();
        } 
        
        @Test(expected = ExecutionPhase.PhaseNotInstanciatedException.class)
        public void throwsPhaseErrorForDetectionPhase() {
            phaseRunnerSettings.runDetectionPhase = true;
            phaseRunner.runPhases();
        }

        @Test(expected = ExecutionPhase.PhaseNotInstanciatedException.class)
        public void throwsPhaseErrorForMPCReadPhase(){
            phaseRunnerSettings.runMPCMatchReadPhase = true;
            phaseRunner.runPhases();
        }

        @Test
        public void noPhaseShouldRunWithoutSetUp() {
            phaseRunner.runPhases();
            
            assertFalse(preparerSpy.runPhaseCalled);
            assertFalse(preprocesserSpy.runPhaseCalled);
            assertFalse(detectionSpy.runPhaseCalled);
            assertFalse(readerSpy.runPhaseCalled);
        }
        
        @Test 
        public void noPhaseShoudRunWithSetUp() {
            phaseRunner.setPreparePhase(preparerSpy);
            phaseRunner.setPreprocessPhase(preprocesserSpy);
            phaseRunner.setDetectionPhase(detectionSpy);
            phaseRunner.setMPCMatchReadPhase(readerSpy);
            
            phaseRunner.runPhases();
            
            assertFalse(preparerSpy.runPhaseCalled);
            assertFalse(preprocesserSpy.runPhaseCalled);
            assertFalse(detectionSpy.runPhaseCalled);
            assertFalse(readerSpy.runPhaseCalled);
        }
        
        @Test
        public void preparePhaseWasRun() {
            phaseRunnerSettings.runPreparePhase = true;

            phaseRunner.setPreparePhase(preparerSpy);
            phaseRunner.runPhases();

            assertTrue(preparerSpy.runPhaseCalled);
        } 
        
        @Test
        public void preprocessPhaseWasRun() {
            phaseRunnerSettings.runPreprocessPhase = true;

            phaseRunner.setPreprocessPhase(preprocesserSpy);
            phaseRunner.runPhases();

            assertTrue(preprocesserSpy.runPhaseCalled);
        } 
        
        @Test
        public void prepareAndPreprocessPhaseWereRun() {
            phaseRunnerSettings.runPreparePhase = true;
            phaseRunnerSettings.runPreprocessPhase = true;
            
            phaseRunner.setPreparePhase(preparerSpy);
            phaseRunner.setPreprocessPhase(preprocesserSpy);
            phaseRunner.runPhases();

            assertTrue(preparerSpy.runPhaseCalled);
            assertTrue(preprocesserSpy.runPhaseCalled);
            assertFalse(detectionSpy.runPhaseCalled);
        }
        
        @Test
        public void preparePreprocessDetectionPhasesWereRun() throws InterruptedException {
            phaseRunnerSettings.runPreparePhase = true;
            phaseRunnerSettings.runPreprocessPhase = true;
            phaseRunnerSettings.runDetectionPhase = true;
            
            phaseRunner.setPreparePhase(preparerSpy);
            phaseRunner.setPreprocessPhase(preprocesserSpy);
            phaseRunner.setDetectionPhase(detectionSpy);
            phaseRunner.runPhases();
            assertTrue(preparerSpy.runPhaseCalled);
            assertTrue(preprocesserSpy.runPhaseCalled);
            assertTrue(detectionSpy.runPhaseCalled);
        }

        @Test
        public void allPhasesWereRun() throws InterruptedException {
            phaseRunnerSettings.runPreparePhase = true;
            phaseRunnerSettings.runPreprocessPhase = true;
            phaseRunnerSettings.runDetectionPhase = true;
            phaseRunnerSettings.runMPCMatchReadPhase = true;

            phaseRunner.setPreparePhase(preparerSpy);
            phaseRunner.setPreprocessPhase(preprocesserSpy);
            phaseRunner.setDetectionPhase(detectionSpy);
            phaseRunner.setMPCMatchReadPhase(readerSpy);

            phaseRunner.runPhases();

            assertTrue(preparerSpy.runPhaseCalled);
            assertTrue(preprocesserSpy.runPhaseCalled);
            assertTrue(detectionSpy.runPhaseCalled);
            assertTrue(readerSpy.runPhaseCalled);
        }
    }

    public class phasesFolderExchangeTests {
        List<File> dirs;

        @Before
        public void setUp(){
            dirs = new ArrayList<>();
            dirs.add(new File("dir1"));
            preparerSpy.setDirsToProcess(dirs);

            phaseRunner.setPreparePhase(preparerSpy);
            phaseRunner.setPreprocessPhase(preprocesserSpy);
            phaseRunner.setDetectionPhase(detectionSpy);
            phaseRunner.setMPCMatchReadPhase(readerSpy);
        }

        @Test
        public void whenRunPreparePhaseProcessedDirsAreSaved(){
            phaseRunnerSettings.runPreparePhase = true;

            phaseRunner.runPhases();

            assertEquals(dirs,phaseRunnerSettings.prepareOutputDirs);
        }

        @Test
        public void whenRunPreprocessPhaseOutputDirsFormDeactivatedPreparePhaseAreUsed(){
            phaseRunnerSettings.runPreprocessPhase = true;
            phaseRunnerSettings.prepareOutputDirs = dirs;

            phaseRunner.runPhases();

            assertEquals(dirs,preprocesserSpy.getDirsToProcess());
            assertEquals(dirs,phaseRunnerSettings.preprocessOutputDirs);
        }

        @Test
        public void whenRunPreprocessPhaseOutputDirsFormPreparePhaseAreUsed(){
            phaseRunnerSettings.runPreparePhase = true;
            phaseRunnerSettings.runPreprocessPhase = true;

            phaseRunner.runPhases();

            assertEquals(dirs,preprocesserSpy.getDirsToProcess());
            assertEquals(dirs,phaseRunnerSettings.preprocessOutputDirs);
        }

        @Test
        public void whenRunDetectionPhaseOutputDirsFormDeactivatedPreprocessPhaseAreUsed(){
            phaseRunnerSettings.runDetectionPhase = true;
            phaseRunnerSettings.preprocessOutputDirs = dirs;

            phaseRunner.runPhases();

            assertEquals(dirs,detectionSpy.getDirsToProcess());
            assertEquals(dirs,phaseRunnerSettings.detectionOutputDirs);
        }

        @Test
        public void whenRunDetectionPhaseOutputDirsFormPreprocessPhaseAreUsed(){
            phaseRunnerSettings.runPreparePhase = true;
            phaseRunnerSettings.runPreprocessPhase = true;
            phaseRunnerSettings.runDetectionPhase = true;

            phaseRunner.runPhases();

            assertEquals(dirs,detectionSpy.getDirsToProcess());
            assertEquals(dirs,phaseRunnerSettings.detectionOutputDirs);
        }

        @Test
        public void whenRunReadPhaseOutputDirsFormDeactivatedDetectionPhaseAreUsed(){
            phaseRunnerSettings.runMPCMatchReadPhase = true;
            phaseRunnerSettings.detectionOutputDirs = dirs;

            phaseRunner.runPhases();

            assertEquals(dirs,readerSpy.getDirsToProcess());
        }

        @Test
        public void whenRunReadPhaseOutputDirsFormDetectionPhaseAreUsed(){
            phaseRunnerSettings.runPreparePhase = true;
            phaseRunnerSettings.runPreprocessPhase = true;
            phaseRunnerSettings.runDetectionPhase = true;
            phaseRunnerSettings.runMPCMatchReadPhase = true;

            phaseRunner.runPhases();

            assertEquals(dirs,readerSpy.getDirsToProcess());
        }
    }

}
