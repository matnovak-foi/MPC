package org.foi.mpc.usecases.combotechnique;

import java.io.File;
import java.util.List;

public interface ComboTechniqueInputBoundary {
    void getAvailableTechniques(ComboTechniqueOutputBoundary outputBoundary);
    void addTechniquesToComboParts(ComboTechniqueOutputBoundary presenter, List<String> selectedTechniques);
    void removeTechniquesFromComboParts(ComboTechniqueOutputBoundary presenter, List<String> comboParts);
    void createNewComboTechnique(ComboTechniqueOutputBoundary presenter, String comboTechniqueName);
    void loadComboTehniquesFromFile(ComboTechniqueOutputBoundary presenter, String inputFile);
    void clearSelectedTechniques(ComboTechniqueOutputBoundary outputBoundary);
    void getProcessedTechniques(ComboTechniqueOutputBoundary presenterSpy, File workingDir);
}
