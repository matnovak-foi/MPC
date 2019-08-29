import org.foi.common.filesystem.file.TextFileUtility;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagJavaAdapter;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagTextAdapter;
import org.foi.mpc.executiontools.detectionTools.sherlock.SherlockOriginalAdapter;
import org.foi.mpc.executiontools.detectionTools.sherlock.SherlockTokenisedAdapter;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneJavaAdapter;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneTextAdapter;
import org.foi.mpc.executiontools.techniques.*;
import org.foi.mpc.main.MultipleDetectionConfigReader;
import org.foi.mpc.main.MultipleDetectionConfiguration;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Not a test class used to generate configuration files to  run detection on cluster
 */
public class ToolConfigClusterGenerator {
    TextFileUtility tfu = new TextFileUtility(StandardCharsets.UTF_8);
    MultipleDetectionConfigReader multipleDetectionConfigReader = new MultipleDetectionConfigReader();
    List<String> years = new ArrayList<>();
    List<String> tools = new ArrayList<>();
    List<String> techiques = new ArrayList<>();
    List<String> soco = new ArrayList<>();
    public static String BASE_DIR;

    @Before
    public void setUp() throws IOException {
        //BASE_DIR = "/shared/mnovak/"; //cluster
        BASE_DIR = "/home/matnovak/phd/";//webdip_server
        //soco.add("T1");
        //soco.add("C1");
        //soco.add("C2");
        //soco.add("A1");
        //soco.add("A2");
        //soco.add("B1");
        //soco.add("B2");
        soco.add("D1");
        soco.add("D2");
        soco.add("D3");
        soco.add("D4");


        years.add("12-13");
        years.add("13-14");
        years.add("14-15");
        years.add("15-16");
        years.add("16-17");
        years.add("17-18");

        techiques.add(NoTechniqueOriginal.NAME);
        techiques.add(RemoveCommentsTechnique.NAME);
        techiques.add(CommonCodeRemoveTechnique.NAME);
        //techiques.add(TemplateExclusionTechnique.NAME);
        techiques.add("Allv3");
        techiques.add("Allv4");


        //tools.add(JPlagJavaAdapter.NAME);
        tools.add(JPlagTextAdapter.NAME);
        tools.add(SimGruneJavaAdapter.NAME);
        tools.add(SimGruneTextAdapter.NAME);
        tools.add(SherlockOriginalAdapter.NAME);
        tools.add(SherlockTokenisedAdapter.NAME);
    }

    @Test
    @Ignore
    public void generateStudents() throws IOException {
        File configFilesDir = new File("D:\\java\\doktorski_rad\\realRunTest\\fromCluster\\runConfigs\\studentDatasetsByDZ\\TOOLS_RUN");
        File shellScript = new File(configFilesDir.getAbsolutePath()+File.separator+"runAllToolsInParalel.sh");
        tfu.createFileWithText(shellScript,"");

        for (String tool: tools) {
            for (String technique: techiques) {
                for (String year : years) {
                    for(int dz=1;dz<5;dz++) {
                        File configFile = new File(configFilesDir.getAbsolutePath() + File.separator + "config_" + tool + "_" + technique + "_" + year+"-"+dz+ ".properties");
                        MultipleDetectionConfiguration mdc = createConfigurationStudent(tool, technique, year+"-"+dz);
                        multipleDetectionConfigReader.write(configFile, mdc);

                        ClusterRunConfig crc = new ClusterRunConfig(configFile);
                        crc.prepareStudentConfig(tool, technique, year+"-"+dz);
                        File runConfigFile = new File(configFilesDir.getAbsolutePath() + File.separator + "runWithStudentConfig_" + tool + "_" + technique + "_" + year+"-"+dz);
                        crc.createFile(runConfigFile);

                        tfu.appendTextToFile(shellScript, "qsub " + runConfigFile.getName() + "\n");
                    }
                }
            }
        }
        tfu.appendTextToFile(shellScript,"qstat -u mnovak");
    }

    @Test
    @Ignore
    public void generateSOCO() throws IOException {
        //File configFilesDir = new File("D:\\java\\doktorski_rad\\SOCO\\runsConfig\\onlyJPlagJava\\");
        //File configFilesDir = new File("D:\\java\\doktorski_rad\\SOCO\\runsConfig\\allToolsExceptJPlagJava\\");
        File configFilesDir = new File("D:\\java\\doktorski_rad\\SOCO\\runsConfig\\allOtherToolsNewD1-D4\\");

        File shellScriptCluster = new File(configFilesDir.getAbsolutePath() + File.separator + "runSOCOAllToolsInParalel.sh");
        File shellScriptWebDiPServer = new File(configFilesDir.getAbsolutePath() + File.separator + "runSOCOAllToolsInParalelWebDiP.sh");

        tfu.createFileWithText(shellScriptCluster, "");
        tfu.createFileWithText(shellScriptWebDiPServer, "");

        for (String tool : tools) {
            for (String assign : soco) {
                for (String technique : techiques) {

                    File configFile = new File(configFilesDir.getAbsolutePath() + File.separator + "config_" + tool + "_" + technique + "_" + assign + ".properties");
                    MultipleDetectionConfiguration mdc = createConfigurationSOCO(tool, technique, assign);
                    multipleDetectionConfigReader.write(configFile, mdc);

                    ClusterRunConfig crc = new ClusterRunConfig(configFile);
                    crc.prepareSocoConfig(tool, technique, assign);
                    File runConfigFile = new File(configFilesDir.getAbsolutePath() + File.separator + "runWithSocoConfig_" + tool + "_" + technique + "_" + assign);
                    crc.createFile(runConfigFile);

                    tfu.appendTextToFile(shellScriptCluster, "qsub " + runConfigFile.getName() + "\n");
                    tfu.appendTextToFile(shellScriptWebDiPServer,createRunCommand(tool,assign,technique,configFile));
                }
            }
        }
        tfu.appendTextToFile(shellScriptCluster,"qstat -u mnovak");
    }

    @Test
    @Ignore
    public void generateSOCOForToolsAllAsingments() throws IOException {
        //File configFilesDir = new File("D:\\java\\doktorski_rad\\SOCO\\runsConfig\\onlyJPlagJava\\");
        File configFilesDir = new File("D:\\java\\doktorski_rad\\SOCO\\runsConfig\\allOtherToolsDn1-30\\");

        //File shellScript = new File(configFilesDir.getAbsolutePath() + File.separator + "runSOCOAllToolsInParalel.sh");
        File shellScriptWebDiPServer = new File(configFilesDir.getAbsolutePath() + File.separator + "runSOCOAllToolsInParalelWebDiP.sh");

        //tfu.createFileWithText(shellScript, "");
        tfu.createFileWithText(shellScriptWebDiPServer, "");

            for (String tool : tools) {
                for (String technique : techiques) {

                    File configFile = new File(configFilesDir.getAbsolutePath() + File.separator + "config_" + tool + "_" + technique + "_ALL" + ".properties");
                    MultipleDetectionConfiguration mdc = createConfigurationSOCO(tool, technique);
                    multipleDetectionConfigReader.write(configFile, mdc);

                    ClusterRunConfig crc = new ClusterRunConfig(configFile);
                    crc.prepareSocoConfig(tool, technique);
                    //File runConfigFile = new File(configFilesDir.getAbsolutePath() + File.separator + "runWithSocoConfig_" + tool + "_" + technique + "_ALL");
                    //crc.createFile(runConfigFile);

                    //tfu.appendTextToFile(shellScript, "qsub " + runConfigFile.getName() + "\n");
                    tfu.appendTextToFile(shellScriptWebDiPServer,createRunCommand(tool,"ALL",technique,configFile));
                }
            }
        //tfu.appendTextToFile(shellScript,"qstat -u mnovak");
    }

    private String createRunCommand(String tool, String assign, String technique, File configFile){
        return "java -Xss16M -Djava.awt.headless=true -jar "+BASE_DIR+"MPC/MPC.jar "+configFile.getName()+" " +
                "> "+BASE_DIR+"runs/socoRuns/results/results_"+tool+"_soco_"+technique+"_"+assign+".out " +
                "2> "+BASE_DIR+"runs/socoRuns/results/results_"+tool+"_soco_"+technique+"_"+assign+".err &\n";
    }

    private MultipleDetectionConfiguration createConfigurationStudent(String tool, String technique, String year){
        MultipleDetectionConfiguration mdc = createConfigStandradPart(tool, technique);

        mdc.selectedWorkingDir = BASE_DIR+"studentDatasetsWorking";
        mdc.selectedInputDir = BASE_DIR+"studentDatasetsByDZ/studentDatasets-"+year;

        return mdc;
    }

    private MultipleDetectionConfiguration createConfigurationSOCO(String tool, String technique, String assign){
        MultipleDetectionConfiguration mdc = createConfigStandradPart(tool, technique);

        mdc.selectedWorkingDir = BASE_DIR+"socoDatasetsWorking";
        mdc.selectedInputDir = BASE_DIR+"socoDatasetsByDZ/socoDatasets"+assign;

        return mdc;
    }

    private MultipleDetectionConfiguration createConfigurationSOCO(String tool, String technique){
        MultipleDetectionConfiguration mdc = createConfigStandradPart(tool, technique);

        mdc.selectedWorkingDir = BASE_DIR+"socoDatasetsWorking";
        mdc.selectedInputDir = BASE_DIR+"socoDatasets";

        return mdc;
    }

    private MultipleDetectionConfiguration createConfigStandradPart(String tool, String technique) {
        MultipleDetectionConfiguration mdc = new MultipleDetectionConfiguration();

        mdc.inputDirDepth = 3;
        mdc.comboTechniquesConfigFile = BASE_DIR+"runs/comboTehniques.properties";
        mdc.detectionToolsConfigFile = BASE_DIR+"runs/detectionToolsConfiguration.properties";
        mdc.templateExclusionConfigFile = BASE_DIR+"runs/templateExclusionConfiguration.properties";
        mdc.selectedTool = tool;
        mdc.selectedTechniques = technique;

        return mdc;
    }

    public class ClusterRunConfig {

        private String name;
        private String mail;
        private String out;
        private String err;
        private String configFile;

        public ClusterRunConfig(File configFile) {
            this.mail = "matija.novak@foi.hr";
            this.configFile = configFile.getName();
        }

        public void prepareStudentConfig(String tool, String technique, String year) {
            this.name = "RUN-"+tool+"-"+technique+"-"+year;
            this.out = BASE_DIR+"runs/toolRuns/results/results_"+tool+"_"+technique+"_"+year+".out";
            this.err = BASE_DIR+"runs/toolRuns/results/results_"+tool+"_"+technique+"_"+year+".err";
        }

        public void prepareSocoConfig(String tool, String technique, String assign) {
            this.name = "SOCO-"+assign+"-"+technique+"-"+tool;
            this.out = BASE_DIR+"runs/socoRuns/results/results_"+tool+"_soco_"+technique+"_"+assign+".out";
            this.err = BASE_DIR+"runs/socoRuns/results/results_"+tool+"_soco_"+technique+"_"+assign+".err";
        }

        public void prepareSocoConfig(String tool, String technique) {
            this.name = "SOCO-"+technique+"-"+tool+"-ALL";
            this.out = BASE_DIR+"runs/socoRuns/results/results_"+tool+"_soco_"+technique+"_ALL.out";
            this.err = BASE_DIR+"runs/socoRuns/results/results_"+tool+"_soco_"+technique+"_ALL.err";
        }

        public void createFile (File runConfigFile) throws IOException {
            StringBuilder sb = new StringBuilder();
            sb.append("#!/bin/bash\n");
            sb.append("#$ -N "+name+"\n");
            sb.append("#$ -M "+mail+"\n");
            sb.append("#$ -m abe\n");
            sb.append("#$ -o "+out+"\n");
            sb.append("#$ -e "+err+"\n");
            sb.append("#$ -cwd\n");
            sb.append("\n");
            sb.append("java -Xss16M -Djava.awt.headless=true -jar /shared/mnovak/MPC/MPC.jar "+configFile);
            //sb.append("java -Xss16M -Djava.awt.headless=true -jar /shared/mnovak/MPC2/MPC.jar "+configFile);
            sb.append("\n");

            tfu.createFileWithText(runConfigFile,sb.toString());
        }
    }
}
