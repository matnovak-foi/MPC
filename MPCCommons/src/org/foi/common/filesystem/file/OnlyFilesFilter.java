package org.foi.common.filesystem.file;

import java.io.File;
import java.io.FileFilter;

public class OnlyFilesFilter implements FileFilter {
    private String extension;
    public OnlyFilesFilter(String extension) {
        this.extension = extension;
    }

    @Override
    public boolean accept(File pathname) {
        return !pathname.isDirectory() && pathname.getName().endsWith(extension);
    }
}
