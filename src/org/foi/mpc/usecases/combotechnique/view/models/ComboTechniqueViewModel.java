package org.foi.mpc.usecases.combotechnique.view.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ComboTechniqueViewModel {
    private List<String> availableTechniques = new ArrayList<>();
    private List<String> avalibleComboTechniques;
    private String errorMessage;
    private List<String> comboTechniqueParts;
    private List<String> selectedComboPartsToDelete;
    private String comboTechniqueName;
    private List<String> selectedTechniques;
    private List<String> selectedComboTechniques;

    public List<String> getAvalibleComboTechniques() {
        return avalibleComboTechniques;
    }

    public void setAvalibleComboTechniques(List<String> avalibleComboTechniques) {
        this.avalibleComboTechniques = avalibleComboTechniques;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setSelectedComboPartsToDelete(List<String> selectedComboPartsToDelete) {
        this.selectedComboPartsToDelete = selectedComboPartsToDelete;
    }

    public List<String> getSelectedComboPartsToDelete() {
        return selectedComboPartsToDelete;
    }

    public void setComboTechniqueParts(List<String> comboTechniqueParts) {
        this.comboTechniqueParts = comboTechniqueParts;
    }

    public List<String> getComboTechniqueParts() {
        return comboTechniqueParts;
    }

    public void setComboTechniqueName(String comboTechniqueName) {
        this.comboTechniqueName = comboTechniqueName;
    }

    public String getComboTechniqueName() {
        return comboTechniqueName;
    }

    public List<String> getSelectedTechniques() {
        return selectedTechniques;
    }

    public void setSelectedTechniques(List<String> selectedTechniques) {
        this.selectedTechniques = selectedTechniques;
    }

    public List<String> getAvailableTechniques() {
        return availableTechniques;
    }

    public void setAvailableTechniques(List<String> availableTechniques) {
        this.availableTechniques = availableTechniques;
    }

    public List<String> getSelectedComboTechniques() {
        return selectedComboTechniques;
    }

    public void setSelectedComboTechniques(List<String> selectedComboTechniques) {
        this.selectedComboTechniques = selectedComboTechniques;
    }

    @Override
    public String toString() {
        return "ComboTechniqueViewModel{" +
                "availableTechniques=" + availableTechniques +
                ", avalibleComboTechniques=" + avalibleComboTechniques +
                ", errorMessage='" + errorMessage + '\'' +
                ", comboTechniqueParts=" + comboTechniqueParts +
                ", selectedComboPartsToDelete=" + selectedComboPartsToDelete +
                ", comboTechniqueName='" + comboTechniqueName + '\'' +
                ", selectedTechniques=" + selectedTechniques +
                ", selectedComboTechniques=" + selectedComboTechniques +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComboTechniqueViewModel)) return false;
        ComboTechniqueViewModel that = (ComboTechniqueViewModel) o;
        return Objects.equals(availableTechniques, that.availableTechniques) &&
                Objects.equals(avalibleComboTechniques, that.avalibleComboTechniques) &&
                Objects.equals(errorMessage, that.errorMessage) &&
                Objects.equals(comboTechniqueParts, that.comboTechniqueParts) &&
                Objects.equals(selectedComboPartsToDelete, that.selectedComboPartsToDelete) &&
                Objects.equals(comboTechniqueName, that.comboTechniqueName) &&
                Objects.equals(selectedTechniques, that.selectedTechniques) &&
                Objects.equals(selectedComboTechniques, that.selectedComboTechniques);
    }
}
