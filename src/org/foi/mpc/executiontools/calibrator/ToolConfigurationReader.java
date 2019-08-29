package org.foi.mpc.executiontools.calibrator;

import org.foi.common.filesystem.file.ConfigurationReader;
import org.foi.common.filesystem.file.PropertiesFileUtility;
import org.foi.mpc.MPCContext;

import java.io.File;
import java.util.Properties;

public class ToolConfigurationReader implements ConfigurationReader<ToolsConfiguration> {

    @Override
    public ToolsConfiguration read(File configurationFile) {
        Properties toolConfigs = PropertiesFileUtility.readProperties(configurationFile);
        ToolsConfiguration configuration = new ToolsConfiguration();

        configuration.minRunLengthSimJava = Integer.parseInt(toolConfigs.getProperty(MPCContext.ToolsConfigProperties.SIM_JAVA_MIN_RUN_LENGTH));
        configuration.minRunLengthSimText = Integer.parseInt(toolConfigs.getProperty(MPCContext.ToolsConfigProperties.SIM_TEXT_MIN_RUN_LENGTH));

        configuration.minTokenMatchJPlagJava = Integer.parseInt(toolConfigs.getProperty(MPCContext.ToolsConfigProperties.JPLAG_JAVA_MIN_TOKEN_MATCH));
        configuration.minTokenMatchJPlagText = Integer.parseInt(toolConfigs.getProperty(MPCContext.ToolsConfigProperties.JPLAG_TEXT_MIN_TOKEN_MATCH));

        configuration.maxJumpDiffSherlockJava = Integer.parseInt(toolConfigs.getProperty(MPCContext.ToolsConfigProperties.SHERLOCK_JAVA_MJD));
        configuration.maxBackwardJumpSherlockJava = Integer.parseInt(toolConfigs.getProperty(MPCContext.ToolsConfigProperties.SHERLOCK_JAVA_MBJ));
        configuration.maxForwardJumpSherlockJava = Integer.parseInt(toolConfigs.getProperty(MPCContext.ToolsConfigProperties.SHERLOCK_JAVA_MFJ));
        configuration.minStringLengthSherlockJava = Integer.parseInt(toolConfigs.getProperty(MPCContext.ToolsConfigProperties.SHERLOCK_JAVA_MSL));
        configuration.minRunLenghtSherlockJava = Integer.parseInt(toolConfigs.getProperty(MPCContext.ToolsConfigProperties.SHERLOCK_JAVA_MRL));
        configuration.strictnessSherlockJava = Integer.parseInt(toolConfigs.getProperty(MPCContext.ToolsConfigProperties.SHERLOCK_JAVA_STR));

        configuration.maxJumpDiffSherlockText = Integer.parseInt(toolConfigs.getProperty(MPCContext.ToolsConfigProperties.SHERLOCK_TEXT_MJD));
        configuration.maxBackwardJumpSherlockText = Integer.parseInt(toolConfigs.getProperty(MPCContext.ToolsConfigProperties.SHERLOCK_TEXT_MBJ));
        configuration.maxForwardJumpSherlockText = Integer.parseInt(toolConfigs.getProperty(MPCContext.ToolsConfigProperties.SHERLOCK_TEXT_MFJ));
        configuration.minStringLengthSherlockText = Integer.parseInt(toolConfigs.getProperty(MPCContext.ToolsConfigProperties.SHERLOCK_TEXT_MSL));
        configuration.minRunLenghtSherlockText = Integer.parseInt(toolConfigs.getProperty(MPCContext.ToolsConfigProperties.SHERLOCK_TEXT_MRL));
        configuration.strictnessSherlockText = Integer.parseInt(toolConfigs.getProperty(MPCContext.ToolsConfigProperties.SHERLOCK_TEXT_STR));

        return configuration;
    }
}
