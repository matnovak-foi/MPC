package org.foi.mpc.executiontools.factories;

import java.util.ArrayList;
import java.util.List;

public class ComboPreprocessingTechnique {
    public String name;
    public PreprocessingTechnique comboTechniqueInstance;
    public List<PreprocessingTechnique> comboTechniqueParts;

    public ComboPreprocessingTechnique() {
        comboTechniqueParts = new ArrayList<>();
    }
}
