package org.foi.mpc.executiontools.techniques.sherlock;

public class SherlockNoWhiteSpacesPPTechnique extends SherlockPreprocessingTechnique{
    public static final String NAME = "RemoveWhiteSpaces";

    public SherlockNoWhiteSpacesPPTechnique() {
        super(SherlockFileType.NOWHITESPACES,NAME);
    }
}
