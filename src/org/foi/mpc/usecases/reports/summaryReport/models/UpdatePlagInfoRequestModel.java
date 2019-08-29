package org.foi.mpc.usecases.reports.summaryReport.models;

import java.io.File;

public class UpdatePlagInfoRequestModel {
    public String fileAname;
    public String fileBname;
    public File sourceDirPath;
    public File selectedInputDirPath;
    public File workingDir;
    public boolean processed;
    public boolean plagiarized;
}
