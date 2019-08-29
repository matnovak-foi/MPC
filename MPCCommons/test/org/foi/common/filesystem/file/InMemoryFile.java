package org.foi.common.filesystem.file;

import org.foi.common.filesystem.directory.InMemoryDir;

import java.io.File;

public class InMemoryFile extends File {

    String content = null;
    InMemoryDir myDir;

    public InMemoryFile(String pathname) {
        super(pathname);
        this.myDir = null;
    }

    public void setMyDir(InMemoryDir dir){
        myDir = dir;
    }

    @Override
    public boolean delete() {
        myDir.deleteFile(this);
        return true;
    }

    public void setContent(String content) {
        this.content = content;
    }

    void appendContent(String content) {
        this.content += content;
    }

    String readContent() {
        return content;
    }

    @Override
    public boolean exists() {
        return this.content != null;
    }
}
