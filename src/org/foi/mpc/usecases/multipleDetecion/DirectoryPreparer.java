package org.foi.mpc.usecases.multipleDetecion;

import clover.org.apache.commons.collections.map.ListOrderedMap;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

public class DirectoryPreparer {
    public DirectoryPreparer() {
    }

    public List<File> createDirsToPorces(File selectedInputDir, int inputDirDepth) {
        List<File> dirsToProces = new ArrayList<>();

        if (inputDirDepth == 0)
            dirsToProces.add(selectedInputDir);
        else {
            inputDirDepth--;
            for (File dir : selectedInputDir.listFiles()) {
                if (dir.isDirectory() && inputDirDepth == 0) {
                    dirsToProces.add(dir);
                } else if (dir.isDirectory()) {
                    dirsToProces.addAll(createDirsToPorces(dir, inputDirDepth));
                }
            }
        }
        return dirsToProces;
    }

    public Map<String, File> createMapForDisplay(File inputDir, List<File> dirs) {
        Map<String, File> response = new TreeMap<>();

        for(File dir : dirs){
            Path toPresent = inputDir.toPath().relativize(dir.toPath());
            response.put(toPresent.toString(),dir);
        }
        return response;
    }
}