package org.foi.mpc.usecases.toolCalibration.models;

public class ToolParam {
    public String paramName;
    public int paramValue;

    @Override
    public String toString() {
        return "ToolParam{" +
                "paramName='" + paramName + '\'' +
                ", paramValue=" + paramValue +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ToolParam)) return false;

        ToolParam param = (ToolParam) o;

        if (paramValue != param.paramValue) return false;
        return paramName.equals(param.paramName);
    }
}
