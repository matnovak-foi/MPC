package org.foi.mpc.executiontools.calibrator.models;

public class BaseToolCase {
    public String caseName;
    public float similarity;

    @Override
    public String toString() {
        return "BaseToolCase{" +
                "caseName='" + caseName + '\'' +
                ", similarity=" + similarity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseToolCase)) return false;

        BaseToolCase that = (BaseToolCase) o;

        if (Float.compare(that.similarity, similarity) != 0) return false;
        return caseName.equals(that.caseName);
    }
}
