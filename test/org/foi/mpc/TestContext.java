package org.foi.mpc;

import org.foi.mpc.abstractfactories.ExecutionToolFactory;
import org.foi.mpc.abstractfactories.FactoryProvider;
import org.foi.mpc.phases.*;
import org.foi.mpc.phases.executionphases.ExecutionPhase;
import org.foi.mpc.phases.executionphases.ExecutionTool;
import org.foi.mpc.phases.readerphase.MPCMatchReaderPhaseSpy;
import org.foi.mpc.phases.runner.Phase;
import org.foi.mpc.executiontools.spies.PrepareToolSpy;
import org.foi.mpc.executiontools.spies.PreprocessingTechniqueSpy;
import org.foi.mpc.executiontools.spies.SimilarityDetectionToolSpy;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestContext {
    //public String testRootDir = "D:\\java\\doktorski_rad\\";
    public static String doktorskiRadDir = "/media/matnovak/HomeData/java/doktorski_rad/";

    private SimilarityDetectionToolSpy toolSpy;
    private PrepareToolSpy prepareToolSpy;

    public PhaseFactorySpy testPhaseFactorySpy;
    public FactoryProvider factoryProvider;
    public MPCMatchReaderPhaseSpy matchReaderPhaseSpy;
    public Map<String,PreprocessingTechniqueSpy> techniqueSpies = new HashMap<>();

    public TestContext(){
        setToolSpy(new SimilarityDetectionToolSpy());
        setTechniqueSpy(new PreprocessingTechniqueSpy());
        setPrepareToolSpy(new PrepareToolSpy());
        testPhaseFactorySpy = new PhaseFactorySpy(MPCContext.MATCHES_DIR);
        matchReaderPhaseSpy = new MPCMatchReaderPhaseSpy();
        matchReaderPhaseSpy.mpcMatches = new ArrayList<>();

        factoryProvider = new FactoryProvider(MPCContext.MATCHES_DIR);

        factoryProvider.setToolFactory(new ExecutionToolFactory() {
            @Override
            public ExecutionTool createTool(String selectedTool) {
                return getToolSpy();
            }

            @Override
            public List<String> getAvailableTools() {
                return null;
            }
        });
        factoryProvider.setTechniqueFactory(new ExecutionToolFactory() {
            @Override
            public ExecutionTool createTool(String selectedTool) {
                return techniqueSpies.get(selectedTool);
            }

            @Override
            public List<String> getAvailableTools() {
                return null;
            }
        });

        factoryProvider.setPhaseFactory(testPhaseFactorySpy);
    }


    public void setTechniqueSpy(PreprocessingTechniqueSpy techniqueSpy) {
        this.techniqueSpies.put(techniqueSpy.getName(),techniqueSpy);
    }

    public SimilarityDetectionToolSpy getToolSpy() {
        toolSpy.resetRunTimeCounter();
        return toolSpy;
    }

    public void setToolSpy(SimilarityDetectionToolSpy toolSpy) {
        this.toolSpy = toolSpy;
    }

    public PrepareToolSpy getPrepareToolSpy() {
        return prepareToolSpy;
    }

    public void setPrepareToolSpy(PrepareToolSpy prepareToolSpy) {
        this.prepareToolSpy = prepareToolSpy;
    }

    public class PhaseFactorySpy extends PhaseFactory {

        public boolean defaultPreparePhaseIsUsed;
        public File sourceInputDir;

        public PhaseFactorySpy(String matchesDir) {
            super(matchesDir);
        }

        @Override
        public ExecutionPhase createDefaultSetupAssignmentPreparer(File workingDir, File testInputDir) {
            defaultPreparePhaseIsUsed = true;
            this.sourceInputDir = testInputDir;
            ExecutionPhase preparer = createAssignmentPreparer(workingDir, testInputDir);
            List<ExecutionTool> prepareToolsList = new ArrayList<>();
            prepareToolsList.add(getPrepareToolSpy());
            preparer.setExecutionTools(prepareToolsList);
            return preparer;
        }

        @Override
        public Phase createMPCMatchReadPhase() {
            return matchReaderPhaseSpy;
        }
    }
}
