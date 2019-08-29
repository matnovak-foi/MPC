package org.foi.mpc.usecases.reports.avalibleTools.models;

import java.util.List;
import java.util.Objects;

public class AvailableToolsResponseModel {
    public List<String> tools;
    public String errorMessage;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AvailableToolsResponseModel)) return false;
        AvailableToolsResponseModel that = (AvailableToolsResponseModel) o;
        return Objects.equals(tools, that.tools) &&
                Objects.equals(errorMessage, that.errorMessage);
    }

    @Override
    public String toString() {
        return "AvailableToolsResponseModel{" +
                "tools=" + tools +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
