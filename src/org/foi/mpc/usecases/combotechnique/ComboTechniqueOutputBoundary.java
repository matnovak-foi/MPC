package org.foi.mpc.usecases.combotechnique;

import org.foi.mpc.usecases.combotechnique.models.AvailableTechniquesResponseModel;
import org.foi.mpc.usecases.combotechnique.models.ComboTechniqueResponseModel;

import java.util.List;

public interface ComboTechniqueOutputBoundary {
    public void presentComboParts(List<String> comboParts);
    public void presentComboTechniques(ComboTechniqueResponseModel responseModel);
    public void clearSelectedTechniques();
    public void presentAvailableTechniques(AvailableTechniquesResponseModel responseModel);

}
