package org.foi.mpc.executiontools.techniques;

import org.foi.common.filesystem.file.PropertiesFileUtility;
import org.foi.mpc.MPCContext;
import org.foi.mpc.executiontools.techniques.TemplateExclusionConfiguration;
import org.foi.mpc.executiontools.techniques.TemplateExclusionConfigurationReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Properties;

import static org.junit.Assert.*;

public class TemplateExclusionConfigurationReaderTest {
    File configFile = new File("configurationFileTest");

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        configFile.delete();
    }

    @Test
    public void canReadTemplateExclusionConfiguration(){
        Properties properties = new Properties();
        properties.put(MPCContext.ToolsConfigProperties.SHERLOCK_TEXT_MBJ, "1");
        properties.put(MPCContext.ToolsConfigProperties.SHERLOCK_TEXT_MFJ, "2");
        properties.put(MPCContext.ToolsConfigProperties.SHERLOCK_TEXT_MJD, "3");
        properties.put(MPCContext.ToolsConfigProperties.SHERLOCK_TEXT_MRL, "4");
        properties.put(MPCContext.ToolsConfigProperties.SHERLOCK_TEXT_MSL, "5");
        properties.put(MPCContext.ToolsConfigProperties.SHERLOCK_TEXT_STR, "6");
        properties.put(MPCContext.ToolsConfigProperties.SHERLOCK_TEXT_AMALGAMATE,"true");
        properties.put(MPCContext.ToolsConfigProperties.SHERLOCK_TEXT_CONCATENATE,"false");
        properties.put(MPCContext.TEMPLATE_ROOT_DIR, "D:\\java\\doktorski_rad\\ppTest\\templateDirectory");

        PropertiesFileUtility.createNewPropertiesFile(properties, configFile);

        TemplateExclusionConfigurationReader reader = new TemplateExclusionConfigurationReader();
        TemplateExclusionConfiguration configuration = reader.read(configFile);

        assertEquals(1, configuration.maxBackwardJump);
        assertEquals(2, configuration.maxForwardJump);
        assertEquals(3, configuration.maxJumpDiff);
        assertEquals(4, configuration.minRunLenght);
        assertEquals(5, configuration.minStringLength);
        assertEquals(6, configuration.strictness);
        assertEquals(true,configuration.amalgamate);
        assertEquals(false,configuration.concatanate);
        assertEquals("D:\\java\\doktorski_rad\\ppTest\\templateDirectory", configuration.templateRootDir);
    }

}