package org.foi.mpc.executiontools.calibrator.models;

import java.util.List;
import java.util.Map;

public class CalibratedToolCases {
    public List<CalibratedToolCase> calibratedToolCaseList;
    public List<CalibratedToolParam> optimalParams;
    public Map<List<CalibratedToolParam>,Float> diffForAllParamCombos;

    @Override
    public String toString() {
        return "CalibratedToolCases{" +
                "calibratedToolCaseList=" + calibratedToolCaseList +
                ", optimalParams=" + optimalParams +
                ", diffForAllParamCombos=" + diffForAllParamCombos +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CalibratedToolCases)) return false;

        CalibratedToolCases cases = (CalibratedToolCases) o;

        if (!calibratedToolCaseList.equals(cases.calibratedToolCaseList)) return false;
        if (!optimalParams.equals(cases.optimalParams)) return false;
        return diffForAllParamCombos.equals(cases.diffForAllParamCombos);
    }
}
