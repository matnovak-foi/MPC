package org.foi.common.filesystem.file;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;

import org.foi.common.MPCexcpetions;

public class TextFileUtility {


    public static class FileDoesNotExistException extends RuntimeException {}
    public static class FileAlreadyExistException extends RuntimeException {}
    public static class FileCreateException extends RuntimeException {
        public FileCreateException(String message) {
            super(message);
        }
    }
    public static class FileReadWriteException extends RuntimeException {
        public FileReadWriteException(String message) {
            super(message);
        }

    }
    public static class FileBufferedReaderException extends RuntimeException {

        public FileBufferedReaderException(String message) {
            super(message);
        }
    
    }

    private static String lineSeparator = System.getProperty("line.separator");
    private Charset charset;

    public static String getLineSeparator() {
        return lineSeparator;
    }

    public TextFileUtility(Charset charset) {
        this.charset = charset;
    }

    public File createFileWithText(File dir, String fileName, String text) throws IOException {
        if(dir == null || fileName == null)
            throw new MPCexcpetions.IsNullException();
        if(fileName=="")
            throw new FileCreateException("File name can not be Empty string!");
        File file = new File(dir.getPath()+File.separator+fileName);
        createFileWithText(file, text);
        return file;
    }

    public void createFileWithText(File file, String text) throws IOException {
        if(file == null || text == null)
            throw new MPCexcpetions.IsNullException();
        createFileIfNotExists(file);
        writeTextToFile(file, text, StandardOpenOption.WRITE);
    }
    
    public String readFileContentToString(File file) throws IOException {
        if(file == null)
            throw new MPCexcpetions.IsNullException();
        BufferedReader reader = createBufferedReader(file);
        return readFileContent(reader);
    }

    public String readFileContentToString(File file, int startLine, int endLine)  throws IOException {
        if(file == null)
            throw new MPCexcpetions.IsNullException();
        BufferedReader reader = createBufferedReader(file);
        return readFileContent(reader,startLine,endLine);
    }

    public void appendTextToFile(File file, String text) throws IOException {
        if(file == null || text == null)
            throw new MPCexcpetions.IsNullException();
        else if(!file.exists())
            throw new FileDoesNotExistException();
        
        writeTextToFile(file, text, StandardOpenOption.APPEND);
    }

    private void createFileIfNotExists(File file) {
        if (!file.exists()) {
            createEmptyFile(file);
        } else {
            throw new FileAlreadyExistException();
        }
    }

    private void createEmptyFile(File file) {
        try {
            Files.createFile(file.toPath(), new FileAttribute[]{});
        } catch (IOException ex) {
            throw new FileCreateException(ex.getMessage());
        }
    }

    private void writeTextToFile(File file, String text, OpenOption... options) throws IOException {
        BufferedWriter bf = null;
        try {
            bf = Files.newBufferedWriter(file.toPath(), charset, options);
            bf.write(text);
        } finally {
            closeCreatedBufferedWriter(bf);
        }
    }

    private void closeCreatedBufferedWriter(BufferedWriter bf) throws IOException {
        if (bf != null) {
            bf.close();
        }
    }

    public BufferedReader createBufferedReader(File file) {
        try {
            return new BufferedReader(new InputStreamReader(new FileInputStream(file),charset.name()));
            //return Files.newBufferedReader(file.toPath(), charset); //causes error when reading test file fileWithReadError
        } catch (IOException ex) {
            throw new FileDoesNotExistException();
        }
    }
    
    private String readFileContent(BufferedReader reader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

            
        try {
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                stringBuilder.append(lineSeparator);
                line = reader.readLine();
            }
        } finally {
            closeBufferedReader(reader);
        }
        
        String content = stringBuilder.toString();
        content = removeArtificalLineFeed(content, lineSeparator);
        return content;
    }

    private String readFileContent(BufferedReader reader, int startLine, int endLine)  throws IOException  {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            int i = 1;
            String line = reader.readLine();
            while (line != null) {
                if(i>=startLine && i<=endLine) {
                    stringBuilder.append(line);
                    stringBuilder.append(lineSeparator);
                } else if(i>endLine){
                    break;
                }
                line = reader.readLine();
                i++;
            }
        } finally {
            closeBufferedReader(reader);
        }

        String content = stringBuilder.toString();
        content = removeArtificalLineFeed(content, lineSeparator);
        return content;
    }

    private static String removeArtificalLineFeed(String content, String ls) {
        return content.substring(0, content.length()-ls.length());
    }

    public void closeBufferedReader(BufferedReader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException ex) {
                throw new FileBufferedReaderException(ex.getMessage());
            }
        }
    }

    
}
