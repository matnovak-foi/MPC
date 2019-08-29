package org.foi.mpc.executiontools.calibrator.models;

public class CalibratedToolParam {
    public String paramName;
    public int paramValue;

    @Override
    public String toString() {
        return "CalibratedToolParam{" +
                "paramName='" + paramName + '\'' +
                ", paramValue=" + paramValue +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CalibratedToolParam)) return false;

        CalibratedToolParam that = (CalibratedToolParam) o;

        if (paramValue != that.paramValue) return false;
        return paramName.equals(that.paramName);
    }
}
