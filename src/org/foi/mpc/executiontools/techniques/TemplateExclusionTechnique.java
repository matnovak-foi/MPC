package org.foi.mpc.executiontools.techniques;

import com.atlassian.clover.remote.Config;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.file.ConfigurationReader;
import org.foi.common.filesystem.file.TextFileUtility;
import org.foi.mpc.executiontools.factories.PreprocessingTechnique;
import plagiarsimchecker.Configuration;
import plagiarsimchecker.PlagiarsimChecker;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class TemplateExclusionTechnique implements PreprocessingTechnique {
    public class TemplateDirectoyCopyException extends RuntimeException {
        public TemplateDirectoyCopyException(String message) {
            super(message);
        }
    }

    public class TemplateDirEmptyException extends RuntimeException {
        public TemplateDirEmptyException(String message) {
            super(message);
        }
    }

    public class TemplateJPlagFixException extends RuntimeException {
        public TemplateJPlagFixException(String message) {
            super(message);
        }
    }

    public static final String NAME = "TemplateExclusion";
    public static final String TEMPLATE_DIR_NAME = "templateDir";

    private File templateDirectoryRoot;
    private PreprocessDirectoryCleaner cleaner;
    private PlagiarsimChecker plagiarsimChecker;
    private ConfigurationReader<TemplateExclusionConfiguration> reader;

    public TemplateExclusionTechnique() {
        cleaner = new PreprocessDirectoryCleaner(NAME);
        reader = new TemplateExclusionConfigurationReader();
    }

    public void setCleaner(PreprocessDirectoryCleaner cleaner) {
        this.cleaner = cleaner;
    }

    public void setReader(ConfigurationReader reader) {
        this.reader = reader;
    }

    public void setPlagiarsimChecker(PlagiarsimChecker plagiarsimChecker) {
        this.plagiarsimChecker = plagiarsimChecker;
    }

    public File getTemplateDirectoryRoot() {
        return templateDirectoryRoot;
    }

    public void configure(File configurationFile) {
        TemplateExclusionConfiguration teConfiguration = reader.read(configurationFile);
        configure(teConfiguration);
    }

    void configure(TemplateExclusionConfiguration teConfiguration) {
        Configuration configuration = new Configuration();
        configuration.maxJumpDiff = teConfiguration.maxJumpDiff;
        configuration.minStringLength = teConfiguration.minStringLength;
        configuration.strictness = teConfiguration.strictness;
        configuration.minRunLenght = teConfiguration.minRunLenght;
        configuration.maxForwardJump = teConfiguration.maxForwardJump;
        configuration.maxBackwardJump = teConfiguration.maxBackwardJump;
        configuration.concatanate = teConfiguration.concatanate;
        configuration.amalgamate = teConfiguration.amalgamate;

        plagiarsimChecker = new PlagiarsimChecker(configuration);

        templateDirectoryRoot = new File(teConfiguration.templateRootDir);
    }

    @Override
    public void runPreporcess(File dirToProcess) {
        File workingTemplatedir = prepareDirectoryForTechnique(dirToProcess);

        plagiarsimChecker.runExclusionOfTemplate(dirToProcess.getPath(), workingTemplatedir.getName(), NAME);
    }

    private File prepareDirectoryForTechnique(File dirToProcess) {
        File workingTemplatedir = new File(dirToProcess + File.separator + TEMPLATE_DIR_NAME);
        workingTemplatedir.mkdir();

        String templateDirectoryName = createTemplateDirectoryPathForAssignment(dirToProcess, templateDirectoryRoot);
        File templateDir = new File(templateDirectoryName);

        copyTemplateCodeToWorkingTemplateDir(workingTemplatedir, templateDir);
        return workingTemplatedir;
    }

    private String createTemplateDirectoryPathForAssignment(File dirToProcess, File templateDirectoryRoot) {
        String templateDir = templateDirectoryRoot.getPath();

        String compareDir = dirToProcess.getName();
        boolean foundDir = false;
        for (File dir : templateDirectoryRoot.listFiles()) {
            if (dir.getName().equalsIgnoreCase(compareDir)) {
                templateDir = createTemplateDirectoryPathForAssignment(dirToProcess.getParentFile(), dir);
                foundDir = true;
            }
        }
        if (!foundDir && dirToProcess.getParentFile() != null) {
            templateDir = createTemplateDirectoryPathForAssignment(dirToProcess.getParentFile(), templateDirectoryRoot);
        }

        return templateDir;
    }

    private void copyTemplateCodeToWorkingTemplateDir(File workingTemplatedir, File templateDir) {
        DirectoryFileUtility dfu = new DirectoryFileUtility();
        if (noTemplateExistsIn(templateDir)) {
            throw new TemplateDirEmptyException(templateDir.getAbsolutePath() + " IS EMPTY!");
        }

        try {
            dfu.copyDirectoryTree(templateDir, workingTemplatedir);
        } catch (Exception e) {
            throw new TemplateDirectoyCopyException("Directory copy failed from " + templateDir.getPath() + " to " + workingTemplatedir.getPath());
        }
    }

    private boolean noTemplateExistsIn(File templateDir) {
        if (templateDir.listFiles().length == 0)
            return true;

        for (File files : templateDir.listFiles()) {
            if (files.isDirectory())
                return true;
        }

        return false;
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
}
