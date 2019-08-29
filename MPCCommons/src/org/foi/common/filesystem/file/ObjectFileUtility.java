package org.foi.common.filesystem.file;

import sun.misc.IOUtils;
import sun.plugin.util.UIUtil;

import java.io.*;

public class ObjectFileUtility {

    public static class ObjectFileException extends RuntimeException {
        public ObjectFileException(String message) {
            super(message);
        }
    }

    public void writeObjectToFile(File matchFile, Object object) throws IOException {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(matchFile));
            oos.writeObject(object);
            oos.close();
        } finally {
            if (oos != null) {
                oos.close();
            }
        }
    }

    public Object readObjectFromFile(File objectFile) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = null;
        Object object = null;
        try {
            ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(objectFile)));
            object = ois.readObject();
        } finally {
            if(ois != null)
                ois.close();
        }

        return object;
    }
}
