package org.foi.mpc.executiontools.techniques.sherlock;

import org.foi.mpc.executiontools.detectionTools.sherlock.SherlockAdapter;

public class SherlockNoCommentsNoWhiteSpacesPPTechnique extends SherlockPreprocessingTechnique {
    public static final String NAME = "RemoveCommentsAndWhiteSpaces";

    public SherlockNoCommentsNoWhiteSpacesPPTechnique() {
        super(SherlockAdapter.SherlockFileType.NOCOMMENTSNOWHITESPACES,NAME);
    }
}
