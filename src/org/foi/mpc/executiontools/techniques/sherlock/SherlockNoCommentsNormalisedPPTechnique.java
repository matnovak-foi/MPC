package org.foi.mpc.executiontools.techniques.sherlock;

public class SherlockNoCommentsNormalisedPPTechnique extends SherlockPreprocessingTechnique {
    public static final String NAME = "RemoveCommentsAndNormalise";

    public SherlockNoCommentsNormalisedPPTechnique() {
        super(SherlockFileType.NOCOMMENTSNORMALISED,NAME);
    }
}
