package org.foi.mpc.usecases.combotechnique.models;

import java.util.List;

public class ComboTechniqueResponseModel {
    public String error;
    public List<String> comboTechniques;
    public List<String> techniques;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComboTechniqueResponseModel)) return false;

        ComboTechniqueResponseModel that = (ComboTechniqueResponseModel) o;

        if (!error.equals(that.error)) return false;
        return comboTechniques.equals(that.comboTechniques);
    }

    @Override
    public String toString() {
        return "ComboTechniqueResponseModel{" +
                "error='" + error + '\'' +
                ", comboTechniques=" + comboTechniques +
                '}';
    }
}
