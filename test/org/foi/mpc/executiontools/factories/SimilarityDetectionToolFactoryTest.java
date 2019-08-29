package org.foi.mpc.executiontools.factories;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.foi.common.filesystem.file.PropertiesFileUtility;
import org.foi.mpc.MPCContext;
import org.foi.mpc.abstractfactories.ExecutionToolFactory;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagAdapter;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneAdapter;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagJavaAdapter;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagTextAdapter;
import org.foi.mpc.executiontools.detectionTools.sherlock.SherlockOriginalAdapter;
import org.foi.mpc.executiontools.detectionTools.sherlock.SherlockTokenisedAdapter;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneJavaAdapter;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneTextAdapter;
import org.foi.mpc.executiontools.detectionTools.spector.SpectorAdapter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.*;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.*;

@RunWith(HierarchicalContextRunner.class)
public class SimilarityDetectionToolFactoryTest {
    SimilarityDetectionToolFactory toolFactory = new SimilarityDetectionToolFactory(MPCContext.MATCHES_DIR);

    @Test
    public void factoryReturnsAvailableTools(){
        List<String> availableTools = new ArrayList<>();
        availableTools.add("JPlagJava");
        availableTools.add("JPlagText");
        availableTools.add("SIMGruneJava");
        availableTools.add("SIMGruneText");
        availableTools.add("SherlockJava");
        availableTools.add("SherlockText");
        availableTools.add("Spector");

        List<String> resultList = toolFactory.getAvailableTools();
        Assert.assertThat(resultList, contains(availableTools.toArray()));
    }

    @Test
    public void createsToolObjectBasedOnName(){
        Map<String, Class> tools = new HashMap<>();
        tools.put(JPlagJavaAdapter.NAME, JPlagJavaAdapter.class);
        tools.put(JPlagTextAdapter.NAME, JPlagTextAdapter.class);
        tools.put(SherlockTokenisedAdapter.NAME, SherlockTokenisedAdapter.class);
        tools.put(SherlockOriginalAdapter.NAME, SherlockOriginalAdapter.class);
        tools.put(SimGruneTextAdapter.NAME, SimGruneTextAdapter.class);
        tools.put(SimGruneJavaAdapter.NAME, SimGruneJavaAdapter.class);
        tools.put(SpectorAdapter.NAME, SpectorAdapter.class);

        for (String toolName : tools.keySet()) {
            SimilarityDetectionTool tool = toolFactory.createTool(toolName);
            Class expectedClass = tools.get(toolName);
            assertThat(tool, instanceOf(expectedClass));
        }
    }

    public class createsToolsWithConfigFromFile {
        File configFile;

        @Before
        public void setUp(){
            configFile = new File("testConfigFileTools.properties");
            Properties properties = new Properties();
            properties.put(MPCContext.ToolsConfigProperties.SIM_JAVA_MIN_RUN_LENGTH, "22");
            properties.put(MPCContext.ToolsConfigProperties.SIM_TEXT_MIN_RUN_LENGTH, "23");
            properties.put(MPCContext.ToolsConfigProperties.JPLAG_JAVA_MIN_TOKEN_MATCH, "24");
            properties.put(MPCContext.ToolsConfigProperties.JPLAG_TEXT_MIN_TOKEN_MATCH, "25");
            properties.put(MPCContext.ToolsConfigProperties.SHERLOCK_JAVA_MBJ, "1");
            properties.put(MPCContext.ToolsConfigProperties.SHERLOCK_JAVA_MFJ, "2");
            properties.put(MPCContext.ToolsConfigProperties.SHERLOCK_JAVA_MJD, "3");
            properties.put(MPCContext.ToolsConfigProperties.SHERLOCK_JAVA_MRL, "4");
            properties.put(MPCContext.ToolsConfigProperties.SHERLOCK_JAVA_MSL, "5");
            properties.put(MPCContext.ToolsConfigProperties.SHERLOCK_JAVA_STR, "6");
            properties.put(MPCContext.ToolsConfigProperties.SHERLOCK_TEXT_MBJ, "7");
            properties.put(MPCContext.ToolsConfigProperties.SHERLOCK_TEXT_MFJ, "8");
            properties.put(MPCContext.ToolsConfigProperties.SHERLOCK_TEXT_MJD, "9");
            properties.put(MPCContext.ToolsConfigProperties.SHERLOCK_TEXT_MRL, "10");
            properties.put(MPCContext.ToolsConfigProperties.SHERLOCK_TEXT_MSL, "11");
            properties.put(MPCContext.ToolsConfigProperties.SHERLOCK_TEXT_STR, "12");
            PropertiesFileUtility.createNewPropertiesFile(properties, configFile);
        }

        @After
        public void tearDown(){
            configFile.delete();
        }

        @Test
        public void createsSIMJavaWithConfigFromFile() {
            toolFactory = new SimilarityDetectionToolFactory(MPCContext.MATCHES_DIR, configFile);
            SimGruneAdapter adapter = (SimGruneAdapter) toolFactory.createTool(SimGruneJavaAdapter.NAME);
            assertEquals(22, adapter.getSettings().minRunLength);
        }

        @Test
        public void createsSIMTextWithConfigFromFile() {
            toolFactory = new SimilarityDetectionToolFactory(MPCContext.MATCHES_DIR, configFile);
            SimGruneAdapter adapter = (SimGruneAdapter) toolFactory.createTool(SimGruneTextAdapter.NAME);
            assertEquals(23, adapter.getSettings().minRunLength);
        }

        @Test
        public void createsJPlagJavaWithConfigFromFile() {
            toolFactory = new SimilarityDetectionToolFactory(MPCContext.MATCHES_DIR, configFile);
            JPlagAdapter adapter = (JPlagAdapter) toolFactory.createTool(JPlagJavaAdapter.NAME);
            assertEquals(24, adapter.getSettings().minTokenMatch);
        }

        @Test
        public void createsJPlagTextWithConfigFromFile() {
            toolFactory = new SimilarityDetectionToolFactory(MPCContext.MATCHES_DIR, configFile);
            JPlagAdapter adapter = (JPlagAdapter) toolFactory.createTool(JPlagTextAdapter.NAME);
            assertEquals(25, adapter.getSettings().minTokenMatch);
        }

        @Test
        public void createsSherlockJavaWithConfigFromFile() {
            toolFactory = new SimilarityDetectionToolFactory(MPCContext.MATCHES_DIR, configFile);
            SherlockTokenisedAdapter adapter = (SherlockTokenisedAdapter) toolFactory.createTool(SherlockTokenisedAdapter.NAME);
            assertEquals(3, adapter.getSettings().maxJumpDiff);
            assertEquals(1, adapter.getSettings().maxBackwardJump);
            assertEquals(2, adapter.getSettings().maxForwardJump);
            assertEquals(4, adapter.getSettings().minRunLenght);
            assertEquals(5, adapter.getSettings().minStringLength);
            assertEquals(6, adapter.getSettings().strictness);
        }

        @Test
        public void createsSherlockTextWithConfigFromFile() {
            toolFactory = new SimilarityDetectionToolFactory(MPCContext.MATCHES_DIR, configFile);
            SherlockOriginalAdapter adapter = (SherlockOriginalAdapter) toolFactory.createTool(SherlockOriginalAdapter.NAME);
            assertEquals(9, adapter.getSettings().maxJumpDiff);
            assertEquals(7, adapter.getSettings().maxBackwardJump);
            assertEquals(8, adapter.getSettings().maxForwardJump);
            assertEquals(10, adapter.getSettings().minRunLenght);
            assertEquals(11, adapter.getSettings().minStringLength);
            assertEquals(12, adapter.getSettings().strictness);
        }
    }

    @Test(expected = NullPointerException.class)
    public void throwExceptionIfToolNameIsWrongWhenCreating(){
        toolFactory.createTool("wrongName");
    }

    //@Test(expected = ExecutionToolFactory.ClassNameException.class)
    @Test(expected = NullPointerException.class)
    public void throwExceptionIfToolNameIsEmpty(){
        toolFactory.createTool("");
    }
}
