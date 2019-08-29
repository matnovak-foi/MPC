package org.foi.mpc.executiontools.techniques.sherlock;

public class SherlockNoCommentsPPTechnique extends SherlockPreprocessingTechnique{
    public static final String NAME = "RemoveCommentsSherlock";

    public SherlockNoCommentsPPTechnique() {
        super(SherlockFileType.NOCOMMENTS,NAME);
    }
}
