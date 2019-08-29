package org.foi.mpc.executiontools.factories;

import org.foi.mpc.abstractfactories.ExecutionToolFactory;
import org.foi.mpc.executiontools.techniques.NoTechniqueOriginal;
import org.foi.mpc.executiontools.techniques.sherlock.SherlockNoCommentsPPTechnique;
import org.foi.mpc.phases.executionphases.spies.ExecutionToolSpy;
import org.foi.common.filesystem.directory.InMemoryDir;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class PreprocessingTechniqueFactoryComboTest {
    PreprocessingTechniqueSpy technique1spy;
    PreprocessingTechniqueSpy technique2spy;
    PreprocessingTechnique newTechnique;
    PreprocessingTechniqueFactory ptf;

    @Before
    public void setUp(){
        ptf = new PreprocessingTechniqueFactory();
        technique1spy = new PreprocessingTechniqueSpy();
        technique2spy = new PreprocessingTechniqueSpy();

        PreprocessingTechnique[] comboTechniques = new PreprocessingTechnique[2];
        comboTechniques[0] = technique1spy;
        comboTechniques[1] = technique2spy;

        newTechnique = ptf.createComboTechniqueInstance("Name",comboTechniques);
    }

    @Test
    public void createdComboTechniqueHasTheRightName(){
        assertEquals("Name",newTechnique.getName());
    }

    @Test
    public void createdComboTechniqueHasRunIndividualTechniques(){
        newTechnique.runPreporcess(new InMemoryDir("testDir"));
        assertEquals(1,technique1spy.wasRunHowManyTimes());
        assertEquals(1,technique2spy.wasRunHowManyTimes());
    }

    @Test
    public void createdComboTechniqueHasRunIndividualTechniques2(){
        newTechnique.runTool(new InMemoryDir("testDir"));
        assertEquals(1,technique1spy.wasRunHowManyTimes());
        assertEquals(1,technique2spy.wasRunHowManyTimes());
    }

    @Test
    public void createComboTechniqueFromStrings(){
        Set<String> strings = new LinkedHashSet<>();
        strings.add(NoTechniqueOriginal.NAME);
        strings.add(SherlockNoCommentsPPTechnique.NAME);

        ptf.createComboTechnique("Name",strings);

        ComboPreprocessingTechnique newTechnique = ptf.getComboTechnique("Name");
        assertEquals("Name",newTechnique.name);

        List<PreprocessingTechnique> newTechniqueParts = ptf.getComboTechniqueParts("Name");
        assertEquals(NoTechniqueOriginal.NAME, newTechniqueParts.get(0).getName());
        assertEquals(SherlockNoCommentsPPTechnique.NAME, newTechniqueParts.get(1).getName());
    }

    @Test
    public void ifComboTechniqueNameIsGivenToCreateToolReturnComboTechniqueIfExists(){
        Set<String> strings = new LinkedHashSet<>();
        strings.add(NoTechniqueOriginal.NAME);
        strings.add(SherlockNoCommentsPPTechnique.NAME);

        ptf.createComboTechnique("Name",strings);

        PreprocessingTechnique comboTechniqueInstance = ptf.createTool("Name");
        assertEquals(ptf.getComboTechnique("Name").comboTechniqueInstance, comboTechniqueInstance);
    }

    @Test(expected = ExecutionToolFactory.ClassNameException.class)
    public void ifNonExistingComboTechniqueNameIsGivenToCreateToolThrowException(){
        ptf.createTool("UnexistingComboTechniqueName");
    }

    private class PreprocessingTechniqueSpy extends ExecutionToolSpy implements PreprocessingTechnique {

        @Override
        public void runPreporcess(File dirToProcess) {

        }
    }
}
