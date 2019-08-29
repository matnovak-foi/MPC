package org.foi.mpc.matches;

import java.util.Objects;

public class MPCPlagMatch {
    public String fileA;
    public String fileB;
    public boolean plagiarized = false;

    @Override
    public String toString() {
        return "MPCPlagMatch{" +
                "fileA='" + fileA + '\'' +
                ", fileB='" + fileB + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MPCPlagMatch)) return false;
        MPCPlagMatch that = (MPCPlagMatch) o;
        return (Objects.equals(fileA, that.fileA) &&
                Objects.equals(fileB, that.fileB))
                || (Objects.equals(fileB, that.fileA) &&
                Objects.equals(fileA, that.fileB));
    }
}
