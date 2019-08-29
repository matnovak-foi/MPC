package org.foi.common.filesystem.file;

import java.io.File;
import java.io.IOException;

public class InMemoryObjectFileUtility extends ObjectFileUtility {
    @Override
    public void writeObjectToFile(File matchFile, Object object) throws IOException {

    }

    @Override
    public Object readObjectFromFile(File objectFile) throws IOException, ClassNotFoundException {
        return null;
    }
}
