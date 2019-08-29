package org.foi.common.filesystem.file;

import org.foi.common.filesystem.directory.InMemoryDir;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class InMemoryTextFileUtility extends TextFileUtility {

    public InMemoryTextFileUtility(Charset charset) {
        super(charset);
    }

    @Override
    public void appendTextToFile(File file, String text) throws IOException {
        InMemoryFile imfile = (InMemoryFile) file;
        imfile.appendContent(text);
    }

    @Override
    public void createFileWithText(File file, String text) throws IOException {
        if(file instanceof InMemoryFile){
            InMemoryFile imfile = (InMemoryFile) file;
            imfile.setContent(text);
        }
        else{
            String dirName = file.getParentFile().getPath();
            InMemoryDir dir = InMemoryDir.getInMemoryDirByName(dirName);
            InMemoryFile imfile = new InMemoryFile(file.getPath());
            imfile.setContent(text);
            dir.addFile(imfile);
        }
    }

    @Override
    public File createFileWithText(File dir, String fileName, String text) throws IOException {
        InMemoryDir dir2 = InMemoryDir.getInMemoryDirByName(dir.getPath());
        InMemoryFile imFile = new InMemoryFile(dir.getPath()+File.separator+fileName);
        imFile.setContent(text);
        dir2.addFile(imFile);
        return imFile;
    }

    @Override
    public String readFileContentToString(File file) throws IOException {
        InMemoryFile imfile = null;
        if(file instanceof InMemoryFile)
            imfile = (InMemoryFile) file;
        else{
            imfile = InMemoryDir.getInMemoryFileByName(file.getPath());
        }
        return imfile.readContent();
    }
}
