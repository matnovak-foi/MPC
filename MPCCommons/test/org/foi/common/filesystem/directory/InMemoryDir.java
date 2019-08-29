package org.foi.common.filesystem.directory;

import org.foi.common.filesystem.file.InMemoryFile;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.*;

public class InMemoryDir extends File {

    private static Map<String,File> allDirs = new HashMap<>();
    private Map<String,File> files = new TreeMap<>();
    private InMemoryDir parrentDir;

    public InMemoryDir(String pathname) {
        super(pathname);
        allDirs.put(pathname,this);
    }

    public void addFile(File file){
        files.put(file.getPath(),file);
        if(file instanceof InMemoryFile)
            ((InMemoryFile) file).setMyDir(this);
        else if(file instanceof InMemoryDir)
            ((InMemoryDir) file).setParrentDir(this);

        allDirs.put(file.getPath(), file);
    }

    public File readFile(int index) {
        return files.get(index);
    }

    public void deleteFile(File file) {
        files.remove(file.getPath());
    }

    public void setParrentDir(InMemoryDir parrentDir) {
        this.parrentDir = parrentDir;
    }

    @Override
    public String[] list() {
        List<String> names = new ArrayList<>();
        for(File file : files.values())
            names.add(file.getName());
        return names.toArray(new String[names.size()]);
    }

    @Override
    public File[] listFiles(FilenameFilter filter) {
        ArrayList<File> files = new ArrayList<>();
        for (File file : this.files.values())
            if ((filter == null) || filter.accept(this, file.getName()))
                files.add(file);
        return files.toArray(new File[files.size()]);
    }

    @Override
    public File[] listFiles(FileFilter filter) {
        ArrayList<File> files = new ArrayList<>();
        for (File file : this.files.values())
            if ((filter == null) || filter.accept(file))
                files.add(file);
        return files.toArray(new File[files.size()]);
    }

    @Override
    public boolean exists() {
        return true;
    }

    @Override
    public boolean renameTo(File dest) {
        return true;
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    @Override
    public boolean delete(){
        parrentDir.deleteFile(this);
        return true;
    }

    @Override
    public File[] listFiles() {
        return files.values().toArray(new File[files.size()]);
    }


    public static InMemoryDir getInMemoryDirByName(String dirName) {
        return (InMemoryDir) allDirs.get(dirName);
    }

    public static InMemoryFile getInMemoryFileByName(String fileName) {
        return (InMemoryFile) allDirs.get(fileName);
    }

    public static void formatSystem(){
        allDirs.clear();
    }
}
