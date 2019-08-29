package org.foi.mpc.usecases.reports.summaryReport.models;

import java.io.File;
import java.util.Objects;

public class SideBySideCompariosnRequestModel {
    public static enum SideBySideType {
        NoMarking, MarkingWumpz, MarkingJYCR
    }

    public File matchesDir;
    public int startLineA;
    public int endLineA;
    public int startLineB;
    public int endLineB;
    public String studentAfileName;
    public String studentBfileName;
    public SideBySideType sideBySideType;

    @Override
    public String toString() {
        return "SideBySideCompariosnRequestModel{" +
                "matchesDir=" + matchesDir +
                ", startLineA=" + startLineA +
                ", endLineA=" + endLineA +
                ", startLineB=" + startLineB +
                ", endLineB=" + endLineB +
                ", studentAfileName='" + studentAfileName + '\'' +
                ", studentBfileName='" + studentBfileName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SideBySideCompariosnRequestModel)) return false;
        SideBySideCompariosnRequestModel that = (SideBySideCompariosnRequestModel) o;
        return startLineA == that.startLineA &&
                endLineA == that.endLineA &&
                startLineB == that.startLineB &&
                endLineB == that.endLineB &&
                Objects.equals(matchesDir, that.matchesDir) &&
                Objects.equals(studentAfileName, that.studentAfileName) &&
                Objects.equals(studentBfileName, that.studentBfileName);
    }

}
