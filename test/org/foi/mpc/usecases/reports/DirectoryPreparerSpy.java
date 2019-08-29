package org.foi.mpc.usecases.reports;

import org.foi.mpc.usecases.multipleDetecion.DirectoryPreparer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoryPreparerSpy extends DirectoryPreparer {
    public boolean wasCalled = false;
    public int inputDirDepth = -1;
    public File selectedInputDir;
    public List<File> returnValue = new ArrayList<>();

    @Override
    public List<File> createDirsToPorces(File selectedInputDir, int inputDirDepth) {
        wasCalled = true;
        this.inputDirDepth = inputDirDepth;
        this.selectedInputDir = selectedInputDir;
        return returnValue;
    }
}
