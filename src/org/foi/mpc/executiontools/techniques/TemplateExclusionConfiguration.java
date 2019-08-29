package org.foi.mpc.executiontools.techniques;

public class TemplateExclusionConfiguration {
    public String templateRootDir;
    public boolean amalgamate;
    public boolean concatanate;
    public int maxBackwardJump;
    public int maxForwardJump;
    public int minRunLenght;
    public int minStringLength;
    public int maxJumpDiff;
    public int strictness;

    @Override
    public String toString() {
        return "TemplateExclusionConfiguration{" +
                "templateRootDir='" + templateRootDir + '\'' +
                ", amalgamate=" + amalgamate +
                ", concatanate=" + concatanate +
                ", maxBackwardJump=" + maxBackwardJump +
                ", maxForwardJump=" + maxForwardJump +
                ", minRunLenght=" + minRunLenght +
                ", minStringLength=" + minStringLength +
                ", maxJumpDiff=" + maxJumpDiff +
                ", strictness=" + strictness +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TemplateExclusionConfiguration)) return false;

        TemplateExclusionConfiguration that = (TemplateExclusionConfiguration) o;

        if (amalgamate != that.amalgamate) return false;
        if (concatanate != that.concatanate) return false;
        if (maxBackwardJump != that.maxBackwardJump) return false;
        if (maxForwardJump != that.maxForwardJump) return false;
        if (minRunLenght != that.minRunLenght) return false;
        if (minStringLength != that.minStringLength) return false;
        if (maxJumpDiff != that.maxJumpDiff) return false;
        if (strictness != that.strictness) return false;
        return templateRootDir.equals(that.templateRootDir);
    }
}
