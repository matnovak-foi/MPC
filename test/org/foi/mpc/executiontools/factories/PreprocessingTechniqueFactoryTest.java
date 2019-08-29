package org.foi.mpc.executiontools.factories;

import org.foi.mpc.abstractfactories.ExecutionToolFactory;
import org.foi.mpc.executiontools.techniques.*;
import org.foi.mpc.executiontools.techniques.sherlock.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.*;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.*;

public class PreprocessingTechniqueFactoryTest {

    PreprocessingTechniqueFactory techniqueFactory = new PreprocessingTechniqueFactory();

    @Test
    public void factoryReturnsAvailableTechniques() {
        List<String> availableTools = new ArrayList<>();
        availableTools.add("NoPreprocessing");
        availableTools.add("Normalise");
        availableTools.add("RemoveComments");
        availableTools.add("RemoveCommentsAndNormalise");
        availableTools.add("RemoveCommentsAndWhiteSpaces");
        availableTools.add("RemoveCommentsSherlock");
        availableTools.add("RemoveCommonCode");
        availableTools.add("RemoveWhiteSpaces");
        availableTools.add("TemplateExclusion");

        List<String> resultList = techniqueFactory.getAvailableTools();
        Assert.assertThat(resultList, contains(availableTools.toArray()));
    }

    @Test
    public void createsToolObjectBasedOnName() {
        Map<String, Class> tools = new HashMap<>();
        tools.put(NoTechniqueOriginal.NAME, NoTechniqueOriginal.class);
        tools.put(CommonCodeRemoveTechnique.NAME, CommonCodeRemoveTechnique.class);
        tools.put(SherlockNormalisePPTechnique.NAME, SherlockNormalisePPTechnique.class);
        tools.put(SherlockNoCommentsPPTechnique.NAME, SherlockNoCommentsPPTechnique.class);
        tools.put(SherlockNoCommentsNormalisedPPTechnique.NAME, SherlockNoCommentsNormalisedPPTechnique.class);
        tools.put(SherlockNoCommentsNoWhiteSpacesPPTechnique.NAME, SherlockNoCommentsNoWhiteSpacesPPTechnique.class);
        tools.put(SherlockNoWhiteSpacesPPTechnique.NAME, SherlockNoWhiteSpacesPPTechnique.class);

        for (String toolName : tools.keySet()) {
            PreprocessingTechnique tool = techniqueFactory.createTool(toolName);
            Class expectedClass = tools.get(toolName);
            assertThat(tool, instanceOf(expectedClass));
        }
    }

    @Test(expected = ExecutionToolFactory.ClassNameException.class)
    public void throwExceptionIfToolNameIsWrongWhenCreating() {
        techniqueFactory.createTool("wrongName");
    }

    @Test(expected = ExecutionToolFactory.ClassNameException.class)
    public void throwExceptionIfToolNameIsEmpty() {
        techniqueFactory.createTool("");
    }

    @Test(expected = PreprocessingTechniqueFactory.TechniqueCreationParametersException.class)
    public void throwExceptionWhenCreatingTemplateExlusionIfExclusionDirIsMissing() {
        techniqueFactory.createTool(TemplateExclusionTechnique.NAME);
    }

    @Test
    public void createsTemplateExclusionWithExlusionDirectory() {
        File templateConfigFile = new File("templateExclusionConfiguration.properties");
        techniqueFactory = new PreprocessingTechniqueFactory(templateConfigFile);
        TemplateExclusionTechnique exclusionTechnique = (TemplateExclusionTechnique) techniqueFactory.createTool(TemplateExclusionTechnique.NAME);

        assertNotNull(exclusionTechnique.getTemplateDirectoryRoot());
    }
}