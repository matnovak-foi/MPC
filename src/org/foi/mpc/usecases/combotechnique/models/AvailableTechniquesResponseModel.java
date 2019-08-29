package org.foi.mpc.usecases.combotechnique.models;

import java.util.List;
import java.util.Objects;

public class AvailableTechniquesResponseModel {
    public List<String> techniques;
    public String errorMessage;

    @Override
    public String toString() {
        return "AvailableTechniquesResponseModel{" +
                "techniques=" + techniques +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AvailableTechniquesResponseModel)) return false;
        AvailableTechniquesResponseModel that = (AvailableTechniquesResponseModel) o;
        return Objects.equals(techniques, that.techniques) &&
                Objects.equals(errorMessage, that.errorMessage);
    }

}
