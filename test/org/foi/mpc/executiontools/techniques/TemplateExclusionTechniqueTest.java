package org.foi.mpc.executiontools.techniques;

import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.file.ConfigurationReader;
import org.foi.common.filesystem.file.TextFileUtility;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import plagiarsimchecker.Configuration;
import plagiarsimchecker.PlagiarsimChecker;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;

public class TemplateExclusionTechniqueTest {
    private DirectoryFileUtility dfu = new DirectoryFileUtility();
    private File workingDir = new File("workingDir");
    private File templateDir = new File("templateDir");
    private TemplateExclusionTechnique technique;
    private String templateExlusionTestInputData = "testInputData"+File.separator+"templateExclusion";
    private PlagiarsimChecker plagiarismCheckerDummy = new PlagiarsimChecker(new Configuration()){
        @Override
        public void runExclusionOfTemplate(String source_dir, String prof_files_dir, String excludeDirName) {

        }
    };
    private TemplateExclusionConfiguration configuration;

    @Before
    public void setUp() throws Exception {
        workingDir.mkdir();
        templateDir.mkdir();

        copyTestData(workingDir);
        dfu.copyDirectoryTree(new File(templateExlusionTestInputData+File.separator+"my_code"),templateDir);

        technique = new TemplateExclusionTechnique();
        configuration = new TemplateExclusionConfiguration();
        configuration.templateRootDir = templateDir.getPath();

        technique.configure(configuration);
    }

    private void copyTestData(File workingDir) throws IOException {
        List<String> exclude = new ArrayList<>();
        exclude.add("my_code");
        exclude.add("expected");
        dfu.copyDirectoryTree(new File(templateExlusionTestInputData),workingDir, exclude);
    }

    @After
    public void tearDown() throws Exception {
        DirectoryFileUtility.deleteDirectoryTree(workingDir);
        DirectoryFileUtility.deleteDirectoryTree(templateDir);
    }

    @Test
    public void copiesExclusionDirToWorkingDir(){
        technique.setPlagiarsimChecker(plagiarismCheckerDummy);
        technique.runPreporcess(workingDir);

        File copiedTemplateDir = new File(workingDir+File.separator+TemplateExclusionTechnique.TEMPLATE_DIR_NAME);

        assertTrue(copiedTemplateDir.exists());
        assertEquals("grupa1.java",copiedTemplateDir.listFiles()[0].getName());
    }

    @Test
    public void copiesExclusionDirToWorkingDirForCorrectAssignement() throws IOException {
        dfu.deleteDirectoryTree(templateDir);
        templateDir.mkdir();
        String afterDris = File.separator+"preprocess"+File.separator+"TemplateExclusion"+File.separator+"SubmissionFilesUnifier";
        File assignementDir = new File(workingDir+File.separator+"NWTiS"+File.separator+"2017_2018"+File.separator+"DZ1"+afterDris);
        assignementDir.mkdirs();
        copyTestData(assignementDir);

        File assignementTemplateDir = new File(templateDir+File.separator+"DZ1"+File.separator+"2017_2018"+File.separator+"NWTiS");
        assignementTemplateDir.mkdirs();
        dfu.copyFile(new File(templateExlusionTestInputData+File.separator+"my_code"+File.separator+"grupa1.java"),assignementTemplateDir);

        technique.setPlagiarsimChecker(plagiarismCheckerDummy);
        technique.runPreporcess(assignementDir);

        File copiedTemplateDir = new File(assignementDir+File.separator+TemplateExclusionTechnique.TEMPLATE_DIR_NAME);
        assertTrue(copiedTemplateDir.exists());
        assertEquals("grupa1.java",copiedTemplateDir.listFiles()[0].getName());
    }

    @Test
    public void runsCleaner() {
        technique.setPlagiarsimChecker(plagiarismCheckerDummy);
        final boolean[] cleanIsRun = {false};
        technique.setCleaner(new PreprocessDirectoryCleaner(technique.getName()){
            @Override
            public void clean(File preprocessedDir) {
                cleanIsRun[0] = true;
            }
        });

        technique.runTool(workingDir);

        assertTrue(cleanIsRun[0]);
    }

    @Test
    public void runsPlagiarismCheckerCorrectly() throws IOException {
        configuration.amalgamate = true;
        configuration.concatanate = false;
        configuration.maxBackwardJump = 1;
        configuration.maxForwardJump = 3;
        configuration.maxJumpDiff = 3;
        configuration.minRunLenght = 3;
        configuration.minStringLength = 8;
        configuration.strictness = 2;
        technique.configure(configuration);
        technique.runTool(workingDir);

        File file1 = new File(workingDir+File.separator+"file1.java");
        File file1expected = new File(templateExlusionTestInputData+File.separator+"expected"+File.separator+"TextFileUtility.java");
        File file2 = new File(workingDir+File.separator+"file2.java");
        File file2expected = new File(templateExlusionTestInputData+File.separator+"expected"+File.separator+"TextFileUtilityCopy.java");
        TextFileUtility tfu = new TextFileUtility(StandardCharsets.UTF_8);

        assertEquals(tfu.readFileContentToString(file1expected),tfu.readFileContentToString(file1));
        assertEquals(tfu.readFileContentToString(file2expected),tfu.readFileContentToString(file2));
        assertEquals(2,workingDir.listFiles().length);
    }

    @Test(expected = TemplateExclusionTechnique.TemplateDirEmptyException.class)
    public void runsPlagiarismCheckerWithEmptyTemplateDir() throws IOException {
        dfu.deleteDirectoryTree(templateDir);
        templateDir.mkdir();

        technique.setPlagiarsimChecker(plagiarismCheckerDummy);
        technique.runTool(workingDir);
    }

    @Test(expected = TemplateExclusionTechnique.TemplateDirectoyCopyException.class)
    public void runsPlagiarsimCheckerWithWrongWorkingDir() throws IOException {
        templateDir.mkdir();

        technique.setPlagiarsimChecker(plagiarismCheckerDummy);
        technique.runTool(new File("nonExistingFile"));
    }

    @Test
    public void  canReadConfigurationFromFile(){
        File configFile = new File("configFile");
        technique.setReader(new ConfigurationReader<TemplateExclusionConfiguration>(){
            @Override
            public TemplateExclusionConfiguration read(File configurationFile) {
                TemplateExclusionConfiguration configuration = new TemplateExclusionConfiguration();
                configuration.templateRootDir = configFile.getPath();
                return configuration;
            }
        });

        technique.configure(configFile);
        assertEquals(configFile, technique.getTemplateDirectoryRoot());
    }
}