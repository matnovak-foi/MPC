package org.foi.mpc.executiontools.techniques.sherlock;

public class SherlockNormalisePPTechnique extends SherlockPreprocessingTechnique {
    public static final String NAME = "Normalise";

    public SherlockNormalisePPTechnique() {
        super(SherlockFileType.NORMALISED,NAME);
    }
}
