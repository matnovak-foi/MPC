package org.foi.mpc.main;

import org.foi.common.filesystem.file.ConfigurationReader;
import org.foi.common.filesystem.file.PropertiesFileUtility;
import org.foi.mpc.MPCContext;

import java.io.File;
import java.util.Objects;
import java.util.Properties;

public class MultipleDetectionConfigReader implements ConfigurationReader<MultipleDetectionConfiguration>{
    private static String COMBO_TECHNIQUES_CONFIG_FILE = "ComboTechniquesConfigFile";
    private static String TEMPLATE_EXCLUSION_CONFIG_FILE = "TemplateExclusionConfigFile";
    private static String DETECTION_TOOOLS_CONFIG_FILE = "DetectionToolsConfigFile";
    private static String SELECTED_INPUT_DIR = "InputDir";
    private static String SELECTED_WORKING_DIR = "WorkingDir";
    private static String SELECTED_DETECTION_TOOLS = "DetectionTools";
    private static String SELECTED_PP_TECHNIQUES = "PreprocessingTechniques";
    private static String SELECTED_INPUT_DIR_DEPTH = "InputDirDepth";

    @Override
    public MultipleDetectionConfiguration read(File configurationFile) {
        Properties properties = PropertiesFileUtility.readProperties(configurationFile);

        MultipleDetectionConfiguration configuration = new MultipleDetectionConfiguration();
        configuration.selectedTechniques = properties.getProperty(SELECTED_PP_TECHNIQUES);
        configuration.selectedTool = properties.getProperty(SELECTED_DETECTION_TOOLS);
        configuration.selectedWorkingDir = properties.getProperty(SELECTED_WORKING_DIR);
        configuration.selectedInputDir = properties.getProperty(SELECTED_INPUT_DIR);
        configuration.inputDirDepth = Integer.parseInt(properties.getProperty(SELECTED_INPUT_DIR_DEPTH));
        configuration.comboTechniquesConfigFile = properties.getProperty(COMBO_TECHNIQUES_CONFIG_FILE);
        configuration.templateExclusionConfigFile = properties.getProperty(TEMPLATE_EXCLUSION_CONFIG_FILE);
        configuration.detectionToolsConfigFile = properties.getProperty(DETECTION_TOOOLS_CONFIG_FILE);

        return configuration;
    }

    public void write(File configurationFile, MultipleDetectionConfiguration configuration) {
        Properties properties = new Properties();

        properties.setProperty(SELECTED_INPUT_DIR,configuration.selectedInputDir);
        properties.setProperty(SELECTED_WORKING_DIR,configuration.selectedWorkingDir);
        properties.setProperty(SELECTED_DETECTION_TOOLS,configuration.selectedTool);
        properties.setProperty(SELECTED_PP_TECHNIQUES,configuration.selectedTechniques);
        properties.setProperty(SELECTED_INPUT_DIR_DEPTH,String.valueOf(configuration.inputDirDepth));
        properties.setProperty(COMBO_TECHNIQUES_CONFIG_FILE,configuration.comboTechniquesConfigFile);
        properties.setProperty(TEMPLATE_EXCLUSION_CONFIG_FILE,configuration.templateExclusionConfigFile);
        properties.setProperty(DETECTION_TOOOLS_CONFIG_FILE,configuration.detectionToolsConfigFile);

        PropertiesFileUtility.createNewPropertiesFile(properties,configurationFile);
    }
}
