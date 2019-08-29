package org.foi.mpc.executiontools.factories;

import org.foi.common.filesystem.file.TextFileUtility;
import org.foi.mpc.abstractfactories.ExecutionToolFactory;
import org.foi.mpc.executiontools.techniques.CommonCodeRemoveTechnique;
import org.foi.mpc.executiontools.techniques.sherlock.SherlockNoCommentsPPTechnique;
import org.foi.mpc.executiontools.techniques.sherlock.SherlockNoWhiteSpacesPPTechnique;
import org.foi.mpc.executiontools.techniques.sherlock.SherlockNormalisePPTechnique;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.*;

public class ComboTechiniquesLoaderTest {

    private ComboTechiniquesLoader ctl;
    private PreprocessingTechniqueFactory factory;
    private String testComboParts;
    File comboTechniquesFile = new File("testComboTehniques");

    @Before
    public void setUp(){
        factory = new PreprocessingTechniqueFactory();
        ctl = new ComboTechiniquesLoader(factory);

        testComboParts = CommonCodeRemoveTechnique.NAME+","+SherlockNoCommentsPPTechnique.NAME;
    }

    @After
    public void tearDown(){
        if(comboTechniquesFile.exists()){
            comboTechniquesFile.delete();
        }
    }

    @Test
    public void technqiueNotCreatedIfThereIsNoName() {
        ctl.loadMultipleTechniquesFromText("="+testComboParts);
        assertThat(factory.getAvailableComboTechniques(),hasSize(0));
        assertFalse(ctl.isLoadSuccesfull());
    }

    @Test
    public void technqiueNotCreatedIfThereIsNoPart() {
        ctl.loadMultipleTechniquesFromText("NewTechniqueName=");
        assertThat(factory.getAvailableComboTechniques(),hasSize(0));
        assertFalse(ctl.isLoadSuccesfull());
    }

    @Test(expected = ExecutionToolFactory.ClassNameException.class)
    public void techniqueNotCreateIfWrongNameIsGivenForPart() {
        testComboParts+=",wrongName";
        ctl.loadMultipleTechniquesFromText("NewTechniqueName="+testComboParts);
    }

    @Test
    public void ifTechniqueWithSameNameOverwrite() {
        String newComboPars = SherlockNoWhiteSpacesPPTechnique.NAME+","+SherlockNormalisePPTechnique.NAME;

        ctl.loadMultipleTechniquesFromText("NewTechniqueName="+testComboParts);
        ctl.loadMultipleTechniquesFromText("NewTechniqueName="+newComboPars);

        assertThat(factory.getAvailableComboTechniques(),hasItem("NewTechniqueName"));
        List<PreprocessingTechnique> createdParts = factory.getComboTechnique("NewTechniqueName").comboTechniqueParts;
        assertEquals(2,createdParts.size());
        assertEquals(createdParts.get(0).getName(),SherlockNoWhiteSpacesPPTechnique.NAME);
        assertEquals(createdParts.get(1).getName(),SherlockNormalisePPTechnique.NAME);
        assertTrue(ctl.isLoadSuccesfull());
    }

    @Test
    public void givenWronInputTechniqueNotLoaded(){
        String missingEqual = "NewTechniqueName"+SherlockNoWhiteSpacesPPTechnique.NAME+","+SherlockNormalisePPTechnique.NAME;
        ctl.loadMultipleTechniquesFromText(missingEqual);
        assertFalse(ctl.isLoadSuccesfull());
        assertThat(factory.getAvailableComboTechniques(),hasSize(0));

        String missingAtLeastTwoParts = "NewTechniqueName="+SherlockNoWhiteSpacesPPTechnique.NAME;
        ctl.loadMultipleTechniquesFromText(missingAtLeastTwoParts);
        assertFalse(ctl.isLoadSuccesfull());
        assertThat(factory.getAvailableComboTechniques(),hasSize(0));
    }

    @Test
    public void canLoadTechniqueFromTextWithTwoParts(){
        String comboTechnique = "NewTechniqueName="+testComboParts;
        ctl.loadMultipleTechniquesFromText(comboTechnique);
        assertThat(factory.getAvailableComboTechniques(),hasItem("NewTechniqueName"));
        assertTrue(ctl.isLoadSuccesfull());
    }

    @Test
    public void canLoadTechniqueFromTextWithFourPartsInCorrectOrder(){
        String comboTechnique = "NewTechniqueName="+SherlockNoWhiteSpacesPPTechnique.NAME+","+SherlockNormalisePPTechnique.NAME+","+CommonCodeRemoveTechnique.NAME+","+SherlockNoCommentsPPTechnique.NAME;
        ctl.loadMultipleTechniquesFromText(comboTechnique);
        assertThat(factory.getAvailableComboTechniques(),hasItem("NewTechniqueName"));

        List<PreprocessingTechnique> createdParts = factory.getComboTechnique("NewTechniqueName").comboTechniqueParts;
        assertEquals(4,createdParts.size());
        assertEquals(createdParts.get(0).getName(),SherlockNoWhiteSpacesPPTechnique.NAME);
        assertEquals(createdParts.get(1).getName(),SherlockNormalisePPTechnique.NAME);
        assertEquals(createdParts.get(2).getName(),CommonCodeRemoveTechnique.NAME);
        assertEquals(createdParts.get(3).getName(),SherlockNoCommentsPPTechnique.NAME);
        assertTrue(ctl.isLoadSuccesfull());
    }

    @Test
    public void canLoadMultipleTechniqueFromText(){
        String comboTechnique = "NewTechniqueName1="+testComboParts+"\n";
        comboTechnique += "NewTechniqueName2="+testComboParts+"\n";
        comboTechnique += "NewTechniqueName3="+testComboParts+"\n";
        comboTechnique += "NewTechniqueName4="+testComboParts+"\n";
        ctl.loadMultipleTechniquesFromText(comboTechnique);
        assertThat(factory.getAvailableComboTechniques(),hasItems("NewTechniqueName1","NewTechniqueName2",
                "NewTechniqueName3","NewTechniqueName4"));
        assertTrue(ctl.isLoadSuccesfull());
    }

    @Test
    public void ifOneTechniqueIsNotOkLoadOthersAndReportLoadFailure(){
        String comboTechnique = "NewTechniqueName1="+testComboParts+"\n";
        comboTechnique += "="+testComboParts+"\n";
        comboTechnique += "NewTechniqueName3="+testComboParts+"\n";
        comboTechnique += "NewTechniqueName4="+testComboParts+"\n";
        ctl.loadMultipleTechniquesFromText(comboTechnique);
        assertThat(factory.getAvailableComboTechniques(),hasItems("NewTechniqueName1",
                "NewTechniqueName3","NewTechniqueName4"));
        assertFalse(ctl.isLoadSuccesfull());
    }

    @Test
    public void canLoadMultipleComboTechniquesFromFile() throws IOException {
        String comboTechnique = "NewTechniqueName1="+testComboParts+"\n";
        comboTechnique += "NewTechniqueName2="+testComboParts+"\n";
        comboTechnique += "NewTechniqueName3="+testComboParts+"\n";
        comboTechnique += "NewTechniqueName4="+testComboParts+"\n";
        TextFileUtility tfu = new TextFileUtility(StandardCharsets.UTF_8);
        tfu.createFileWithText(comboTechniquesFile, comboTechnique);

        ctl.loadMultipleTechniquesFromFile(comboTechniquesFile);
        assertTrue(ctl.isLoadSuccesfull());
        assertThat(factory.getAvailableComboTechniques(),hasItems("NewTechniqueName1","NewTechniqueName2",
                "NewTechniqueName3","NewTechniqueName4"));

    }
}
