package org.foi.common.filesystem.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

public class PropertiesFileUtility {
    public static class PropertiesWriteException extends RuntimeException {
        public PropertiesWriteException(String message) {
            super(message);
        }
    }
    public static class PropertiesReadException extends RuntimeException {
        public PropertiesReadException(String message) {
            super(message);
        }
    }
    public static Properties readProperties(File file){
        Properties properties = new Properties();
        try {
            InputStream is = Files.newInputStream(file.toPath(), StandardOpenOption.READ);
            properties.load(is);
            is.close();
        } catch (IOException e) {
            throw new PropertiesWriteException(file.getPath()+":"+e.getStackTrace());
        }
        return properties;
    }

    public static void createNewPropertiesFile(Properties properties, File file){
        StandardOpenOption[] openOption = new StandardOpenOption[]{StandardOpenOption.CREATE_NEW};
        try {
            OutputStream os = Files.newOutputStream(file.toPath(), openOption);
            properties.store(os,file.getName());
            os.close();
        } catch (IOException e) {
            throw new PropertiesWriteException(file.getPath()+":"+e.getStackTrace());
        }
    }
}
