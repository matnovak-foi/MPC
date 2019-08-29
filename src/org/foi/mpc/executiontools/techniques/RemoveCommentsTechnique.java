package org.foi.mpc.executiontools.techniques;

import org.foi.common.JavaCodePartsRemover;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.file.TextFileUtility;
import org.foi.mpc.executiontools.factories.PreprocessingTechnique;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RemoveCommentsTechnique implements PreprocessingTechnique {

    public static final String NAME = "RemoveComments";

    private DirectoryFileUtility dc = new DirectoryFileUtility();
    private TextFileUtility tfu = new TextFileUtility(StandardCharsets.UTF_8);
    private PreprocessDirectoryCleaner cleaner;
    private File rcDir;
    private JavaCodePartsRemover codePartsRemover = new JavaCodePartsRemover();

    public void setDc(DirectoryFileUtility dc) {
        this.dc = dc;
    }

    public void setTfu(TextFileUtility tfu) {
        this.tfu = tfu;
    }

    public RemoveCommentsTechnique() {
        cleaner = new PreprocessDirectoryCleaner(NAME);
    }

    @Override
    public void runPreporcess(File dirToProcess) {
        rcDir = dc.createSubDir(dirToProcess, getName());
        processDir(dirToProcess);
    }

    private void processDir(File dirToProcess) {
        for (File file : dirToProcess.listFiles()) {
            if (file.isDirectory()) {
                if (isNotRCDir(file))
                    processDir(file);
            } else {
                processFile(file);
            }
        }
    }

    private void processFile(File file) {
        String content;
        try {
            content = tfu.readFileContentToString(file);
            if (content == null)
                return;
        } catch (IOException e) {
            e.printStackTrace();
            throw new TextFileUtility.FileReadWriteException(file.getPath()+e.getMessage());
        }

        content = codePartsRemover.removeComments(content);

        File newFile = new File(rcDir.getPath() + File.separator + file.getName());
        try {
            tfu.createFileWithText(newFile, content);
        } catch (IOException e) {
            throw new TextFileUtility.FileReadWriteException(newFile.getPath()+e.getMessage());
        }
    }

    private boolean isNotRCDir(File file) {
        return !file.getName().equals(rcDir.getName());
    }




    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void runTool(File dirToProcess) {
        runPreporcess(dirToProcess);
        cleaner.clean(dirToProcess);
    }

    public void setCleaner(PreprocessDirectoryCleaner cleaner) {
        this.cleaner = cleaner;
    }
}
