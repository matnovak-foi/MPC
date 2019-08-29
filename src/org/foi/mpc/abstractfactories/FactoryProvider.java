package org.foi.mpc.abstractfactories;

import org.foi.mpc.phases.PhaseFactory;
import org.foi.mpc.executiontools.factories.SimilarityDetectionToolFactory;
import org.foi.mpc.executiontools.factories.PreprocessingTechniqueFactory;

import java.io.File;

public class FactoryProvider {
    private ExecutionToolFactory toolFactory;
    private ExecutionToolFactory techniqueFactory;
    private PhaseFactory phaseFactory;
    private String matchesDir;

    public FactoryProvider(String matchesDir) {
        this.matchesDir = matchesDir;
        toolFactory = new SimilarityDetectionToolFactory(matchesDir);
        techniqueFactory = new PreprocessingTechniqueFactory();
        phaseFactory = new PhaseFactory(matchesDir);
    }

    public ExecutionToolFactory getToolFactory() {
        return toolFactory;
    }

    public void setToolFactory(ExecutionToolFactory toolFactory) {
        this.toolFactory = toolFactory;
    }

    public ExecutionToolFactory getTechniqueFactory() {
        return techniqueFactory;
    }

    public void setTechniqueFactory(ExecutionToolFactory techniqueFactory) {
        this.techniqueFactory = techniqueFactory;
    }

    public void setPhaseFactory(PhaseFactory phaseFactory) {
        this.phaseFactory = phaseFactory;
    }

    public PhaseFactory getPhaseFactory() {
        return phaseFactory;
    }

    public void createSimilarityDetectionToolFactory(String configFileName) {
        toolFactory = new SimilarityDetectionToolFactory(matchesDir, new File(configFileName));
    }

    public void createPreprocessingTechniqueFactory(String templatesExclusionConfigFileName) {
        techniqueFactory = new PreprocessingTechniqueFactory(new File(templatesExclusionConfigFileName));
    }
}
