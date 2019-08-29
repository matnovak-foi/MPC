
package org.foi.mpc.executiontools.techniques;

import org.foi.common.JavaCodePartsRemover;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.file.TextFileUtility;
import org.foi.mpc.executiontools.factories.PreprocessingTechnique;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CommonCodeRemoveTechnique implements PreprocessingTechnique {

    public static final String NAME = "RemoveCommonCode";
    private DirectoryFileUtility dc = new DirectoryFileUtility();
    private TextFileUtility tfu = new TextFileUtility(StandardCharsets.UTF_8);
    private File ccrDir;
    private PreprocessDirectoryCleaner cleaner;
    private JavaCodePartsRemover javaCodePartsRemover;

    public CommonCodeRemoveTechnique() {
        cleaner = new PreprocessDirectoryCleaner(NAME);
        javaCodePartsRemover = new JavaCodePartsRemover();
    }

    public void setDc(DirectoryFileUtility dc) {
        this.dc = dc;
    }

    public void setTfu(TextFileUtility tfu) {
        this.tfu = tfu;
    }

    public void setCleaner(PreprocessDirectoryCleaner cleaner) {
        this.cleaner = cleaner;
    }

    @Override
    public void runTool(File dirToProcess) {
        runPreporcess(dirToProcess);
        cleaner.clean(dirToProcess);
    }

    @Override
    public void runPreporcess(File dirToProcess) {
        ccrDir = dc.createSubDir(dirToProcess, getName());
        processDir(dirToProcess);
    }

    private void processDir(File dirToProcess) {
        for (File file : dirToProcess.listFiles()) {
            if (file.isDirectory()) {
                if (isNotCCRDir(file))
                    processDir(file);
            } else {
                processFile(file);
            }
        }
    }

    private void processFile(File file) {
        //System.err.println(file.getAbsolutePath().toString());
        String content;
        try {
            content = tfu.readFileContentToString(file);
            if (content == null)
                return;
        } catch (IOException e) {
            e.printStackTrace();
            throw new TextFileUtility.FileReadWriteException(file.getPath()+e.getMessage());
        }

        content = removeCommonCode(content);
        File newFile = new File(ccrDir.getPath() + File.separator + file.getName());
        try {
            tfu.createFileWithText(newFile, content);
        } catch (IOException e) {
            throw new TextFileUtility.FileReadWriteException(newFile.getPath()+e.getMessage());
        }
    }

    private String removeCommonCode(String content) {
        content = javaCodePartsRemover.removePackageStatements(content);
        //System.err.println("package");
        content = javaCodePartsRemover.removeImportStatements(content);
        //System.err.println("improt");
        content = javaCodePartsRemover.removeAnnotations(content);
        //System.err.println("anotation");
        content = javaCodePartsRemover.removeSetMethods(content);
        //System.err.println("setMethods");
        content = javaCodePartsRemover.removeGetMethods(content);
        //System.err.println("getter");
        content = javaCodePartsRemover.removeEmptyVoidMethodsAndConstructors(content);
        //System.err.println("emptyvoid");
        content = javaCodePartsRemover.removeSimpleFieldsSetConstructorsOrFunctions(content);
        //System.err.println("siplefiled");
        content = javaCodePartsRemover.removeNotInitializedClassFields(content);

        content = javaCodePartsRemover.removeEmptyLoopBlockAndSimilar(content);
        content = javaCodePartsRemover.removeEmptyElseFinallyStatement(content);
        content = javaCodePartsRemover.removeAnyEmptyMethod(content);

        content = javaCodePartsRemover.removeEmptyClasses(content);
        //System.err.println("emptyClass");
        content = javaCodePartsRemover.removeLeftoverWhiteSpaces(content);
        //System.err.println("leftoverwhitespace");
        content = content.replaceAll("///\\*","//\n/*");
        //System.err.println("lastquery");
        return content;
    }

    private boolean isNotCCRDir(File file) {
        return !file.getName().equals(ccrDir.getName());
    }

    @Override
    public String getName() {
        return NAME;
    }

}