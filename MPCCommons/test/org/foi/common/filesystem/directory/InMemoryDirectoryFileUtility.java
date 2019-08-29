package org.foi.common.filesystem.directory;

import java.io.File;

public class InMemoryDirectoryFileUtility extends DirectoryFileUtility {

    @Override
    public File createSubDir(File parentDir, String name) {
        if(parentDir instanceof InMemoryDir){
            InMemoryDir imd = new InMemoryDir(parentDir.getPath()+File.separator+name);
            ((InMemoryDir) parentDir).addFile(imd);
            return imd;
        }
        return null;
    }
}
