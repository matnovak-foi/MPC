package org.foi.mpc.executiontools.techniques;

import org.foi.common.filesystem.file.ConfigurationReader;
import org.foi.common.filesystem.file.PropertiesFileUtility;
import org.foi.mpc.MPCContext;
import org.foi.mpc.MPCContext.ToolsConfigProperties;

import java.io.File;
import java.util.Properties;

public class TemplateExclusionConfigurationReader implements ConfigurationReader<TemplateExclusionConfiguration> {

    public TemplateExclusionConfiguration read(File configurationFile) {
        TemplateExclusionConfiguration configuration = new TemplateExclusionConfiguration();
        Properties properties = PropertiesFileUtility.readProperties(configurationFile);
        configuration.templateRootDir = properties.getProperty(MPCContext.TEMPLATE_ROOT_DIR);
        configuration.minStringLength = Integer.parseInt(properties.getProperty(ToolsConfigProperties.SHERLOCK_TEXT_MSL));
        configuration.minRunLenght = Integer.parseInt(properties.getProperty(ToolsConfigProperties.SHERLOCK_TEXT_MRL));
        configuration.maxJumpDiff = Integer.parseInt(properties.getProperty(ToolsConfigProperties.SHERLOCK_TEXT_MJD));
        configuration.maxForwardJump = Integer.parseInt(properties.getProperty(ToolsConfigProperties.SHERLOCK_TEXT_MFJ));
        configuration.maxBackwardJump = Integer.parseInt(properties.getProperty(ToolsConfigProperties.SHERLOCK_TEXT_MBJ));
        configuration.strictness = Integer.parseInt(properties.getProperty(ToolsConfigProperties.SHERLOCK_TEXT_STR));
        configuration.amalgamate = Boolean.parseBoolean(properties.getProperty(ToolsConfigProperties.SHERLOCK_TEXT_AMALGAMATE));
        configuration.concatanate = Boolean.parseBoolean(properties.getProperty(ToolsConfigProperties.SHERLOCK_TEXT_CONCATENATE));
        return configuration;
    }
}
