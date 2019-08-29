package org.foi.mpc.executiontools.calibrator;

public class ToolsConfiguration {
    public int minTokenMatchJPlagText;
    public int minRunLengthSimJava;
    public int minRunLengthSimText;
    public int minTokenMatchJPlagJava;
    public int maxJumpDiffSherlockJava;
    public int maxBackwardJumpSherlockJava;
    public int maxForwardJumpSherlockJava;
    public int minStringLengthSherlockJava;
    public int minRunLenghtSherlockJava;
    public int strictnessSherlockJava;
    public int maxJumpDiffSherlockText;
    public int maxBackwardJumpSherlockText;
    public int maxForwardJumpSherlockText;
    public int minStringLengthSherlockText;
    public int minRunLenghtSherlockText;
    public int strictnessSherlockText;

    @Override
    public String toString() {
        return "ToolsConfiguration{" +
                "minTokenMatchJPlagText=" + minTokenMatchJPlagText +
                ", minRunLengthSimJava=" + minRunLengthSimJava +
                ", minRunLengthSimText=" + minRunLengthSimText +
                ", minTokenMatchJPlagJava=" + minTokenMatchJPlagJava +
                ", maxJumpDiffSherlockJava=" + maxJumpDiffSherlockJava +
                ", maxBackwardJumpSherlockJava=" + maxBackwardJumpSherlockJava +
                ", maxForwardJumpSherlockJava=" + maxForwardJumpSherlockJava +
                ", minStringLengthSherlockJava=" + minStringLengthSherlockJava +
                ", minRunLenghtSherlockJava=" + minRunLenghtSherlockJava +
                ", strictnessSherlockJava=" + strictnessSherlockJava +
                ", maxJumpDiffSherlockText=" + maxJumpDiffSherlockText +
                ", maxBackwardJumpSherlockText=" + maxBackwardJumpSherlockText +
                ", maxForwardJumpSherlockText=" + maxForwardJumpSherlockText +
                ", minStringLengthSherlockText=" + minStringLengthSherlockText +
                ", minRunLenghtSherlockText=" + minRunLenghtSherlockText +
                ", strictnessSherlockText=" + strictnessSherlockText +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ToolsConfiguration)) return false;

        ToolsConfiguration that = (ToolsConfiguration) o;

        if (minTokenMatchJPlagText != that.minTokenMatchJPlagText) return false;
        if (minRunLengthSimJava != that.minRunLengthSimJava) return false;
        if (minRunLengthSimText != that.minRunLengthSimText) return false;
        if (minTokenMatchJPlagJava != that.minTokenMatchJPlagJava) return false;
        if (maxJumpDiffSherlockJava != that.maxJumpDiffSherlockJava) return false;
        if (maxBackwardJumpSherlockJava != that.maxBackwardJumpSherlockJava) return false;
        if (maxForwardJumpSherlockJava != that.maxForwardJumpSherlockJava) return false;
        if (minStringLengthSherlockJava != that.minStringLengthSherlockJava) return false;
        if (minRunLenghtSherlockJava != that.minRunLenghtSherlockJava) return false;
        if (strictnessSherlockJava != that.strictnessSherlockJava) return false;
        if (maxJumpDiffSherlockText != that.maxJumpDiffSherlockText) return false;
        if (maxBackwardJumpSherlockText != that.maxBackwardJumpSherlockText) return false;
        if (maxForwardJumpSherlockText != that.maxForwardJumpSherlockText) return false;
        if (minStringLengthSherlockText != that.minStringLengthSherlockText) return false;
        if (minRunLenghtSherlockText != that.minRunLenghtSherlockText) return false;
        return strictnessSherlockText == that.strictnessSherlockText;
    }
}
