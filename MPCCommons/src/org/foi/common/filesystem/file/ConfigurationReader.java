package org.foi.common.filesystem.file;

import java.io.File;

public interface ConfigurationReader<T> {
    public T read(File configurationFile);
}
