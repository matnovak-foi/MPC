package org.foi.mpc;

import org.foi.mpc.abstractfactories.FactoryProvider;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagJavaAdapter;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagTextAdapter;
import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.matches.models.MPCMatchPart;
import org.foi.mpc.executiontools.detectionTools.sherlock.SherlockOriginalAdapter;
import org.foi.mpc.executiontools.detectionTools.sherlock.SherlockTokenisedAdapter;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneJavaAdapter;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneTextAdapter;
import org.foi.mpc.executiontools.detectionTools.spector.SpectorAdapter;
import org.foi.mpc.executiontools.factories.PreprocessingTechnique;
import org.foi.mpc.executiontools.factories.PreprocessingTechniqueFactory;
import org.foi.mpc.executiontools.factories.SimilarityDetectionToolFactory;
import org.foi.mpc.executiontools.techniques.CommonCodeRemoveTechnique;
import org.foi.mpc.executiontools.techniques.NoTechniqueOriginal;
import org.foi.mpc.executiontools.techniques.sherlock.*;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.mpc.matches.MPCMatchFileUtility;
import org.foi.mpc.phases.PhaseFactory;
import org.foi.mpc.phases.executionphases.ExecutionPhase;
import org.foi.mpc.phases.executionphases.ExecutionTool;
import org.foi.mpc.phases.runner.PhaseRunner;
import org.foi.mpc.phases.runner.PhaseRunnerBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class FullSystemTest {
    File workingDir;
    File testInputDir;
    List<File> dirsToProcess;
    File[] dirsToProcessClone;
    static PreprocessingTechniqueFactory techniqueFactory;
    static SimilarityDetectionToolFactory toolFactory;
    static PhaseFactory phaseFactory;

    static {
        FactoryProvider factoryProvider = new FactoryProvider(MPCContext.MATCHES_DIR);
        techniqueFactory = (PreprocessingTechniqueFactory) factoryProvider.getTechniqueFactory();
        toolFactory = (SimilarityDetectionToolFactory) factoryProvider.getToolFactory();
        phaseFactory = factoryProvider.getPhaseFactory();
    }

    @Before
    public void setUp(){
        workingDir = new File("testWorkignDir");
        workingDir.mkdir();

        testInputDir = new File("testInputData" +File.separator+"fullSystemTest");
        dirsToProcess = new ArrayList<>();
        dirsToProcess.add(new File(testInputDir+File.separator+"NWTiS"+File.separator+"2010-2011"+File.separator+"DZ1"));
        dirsToProcess.add(new File(testInputDir+File.separator+"NWTiS"+File.separator+"2010-2011"+File.separator+"DZ2"));
        dirsToProcess.add(new File(testInputDir+File.separator+"NWTiS"+File.separator+"2011-2012"+File.separator+"DZ1"));
        dirsToProcessClone = dirsToProcess.toArray(new File[dirsToProcess.size()]);
    }

    @After
    public void tearDown() throws IOException {
        DirectoryFileUtility.deleteDirectoryTree(workingDir);
    }

    @Test
    public void canRunAllToolsOnAllTehcniques() throws IOException, ClassNotFoundException {
        List<ExecutionTool> techniques = new ArrayList<>();
        techniques.add(techniqueFactory.createTool(SherlockNoCommentsNormalisedPPTechnique.NAME));
        techniques.add(techniqueFactory.createTool(SherlockNoCommentsNoWhiteSpacesPPTechnique.NAME));
        techniques.add(techniqueFactory.createTool(SherlockNoCommentsPPTechnique.NAME));
        techniques.add(techniqueFactory.createTool(SherlockNoWhiteSpacesPPTechnique.NAME));
        techniques.add(techniqueFactory.createTool(SherlockNormalisePPTechnique.NAME));
        techniques.add(techniqueFactory.createTool(CommonCodeRemoveTechnique.NAME));
        techniques.add(techniqueFactory.createTool(NoTechniqueOriginal.NAME));

        List<ExecutionTool> tools = new ArrayList<>();
        tools.add(toolFactory.createTool(SherlockOriginalAdapter.NAME));
        tools.add(toolFactory.createTool(SherlockTokenisedAdapter.NAME));
        tools.add(toolFactory.createTool(JPlagJavaAdapter.NAME));
        tools.add(toolFactory.createTool(JPlagTextAdapter.NAME));
        //tools.add(toolFactory.createTool(SpectorAdapter.NAME));
        tools.add(toolFactory.createTool(SimGruneJavaAdapter.NAME));
        tools.add(toolFactory.createTool(SimGruneTextAdapter.NAME));

        PhaseRunner phaseRunner = new PhaseRunnerBuilder(phaseFactory).
                withSourceDir(testInputDir).
                withWorkingDir(workingDir).
                forToolList(tools).
                forTechiqueList(techniques).
                toProcessDirs(dirsToProcess).
                withMatchReadPhaseDisabled().
                build();

        phaseRunner.runPhases();

        ExecutionPhase preparer = (ExecutionPhase) phaseRunner.getPreparePhase();
        List<ExecutionTool> prepareToolsList = preparer.getExecutionTools();

        assertPrepareToolsRunOk(prepareToolsList);
        assertTechniquesRunOK(techniques);
        assertDetectionRunOK(techniques, tools);
    }

    @Test
    public void canRunCombinationsOfTechniques() {
        List<ExecutionTool> techniques = new ArrayList<>();
        PreprocessingTechnique ncTechnique = techniqueFactory.createTool(SherlockNoCommentsPPTechnique.NAME);
        PreprocessingTechnique nwsTechnique = techniqueFactory.createTool(SherlockNoWhiteSpacesPPTechnique.NAME);
        PreprocessingTechnique[] ncNwsTechniqueCombo = new PreprocessingTechnique[2];
        ncNwsTechniqueCombo[0] = ncTechnique;
        ncNwsTechniqueCombo[1] = nwsTechnique;
        PreprocessingTechnique ncNwsTechnique = techniqueFactory.createComboTechniqueInstance("Name",ncNwsTechniqueCombo);
        techniques.add(ncTechnique);
        techniques.add(nwsTechnique);
        techniques.add(ncNwsTechnique);

        PhaseRunner phaseRunner = new PhaseRunnerBuilder(phaseFactory).
                withSourceDir(testInputDir).
                withWorkingDir(workingDir).
                forTechiqueList(techniques).
                toProcessDirs(dirsToProcess).
                withMatchReadAndDetectionPhaseDisabled().
                build();
        phaseRunner.runPhases();

        ExecutionPhase preparer = (ExecutionPhase) phaseRunner.getPreparePhase();
        List<ExecutionTool> prepareToolsList = preparer.getExecutionTools();

        assertPrepareToolsRunOk(prepareToolsList);
        assertTechniquesRunOK(techniques);
    }

    @Test
    public void spectorStackOverflowTest() throws IOException, ClassNotFoundException {
        testInputDir = new File("testInputData"+File.separator+"spectorStackOverflow");
        dirsToProcess = new ArrayList<>();
        dirsToProcess.add(testInputDir);
        dirsToProcessClone = dirsToProcess.toArray(new File[dirsToProcess.size()]);
        List<ExecutionTool> techniques = new ArrayList<>();
        techniques.add(techniqueFactory.createTool(NoTechniqueOriginal.NAME));
        List<ExecutionTool> tools = new ArrayList<>();
        tools.add(toolFactory.createTool(SpectorAdapter.NAME));

        PhaseRunner phaseRunner = new PhaseRunnerBuilder(phaseFactory).
                withSourceDir(testInputDir).
                withWorkingDir(workingDir).
                forTechiqueList(techniques).
                forToolList(tools).
                toProcessDirs(dirsToProcess).
                withMatchReadPhaseDisabled().
                build();
        phaseRunner.runPhases();

        ExecutionPhase preparer = (ExecutionPhase) phaseRunner.getPreparePhase();
        List<ExecutionTool> prepareToolsList = preparer.getExecutionTools();

        assertPrepareToolsRunOk(prepareToolsList);
        assertTechniquesRunOK(techniques);
        assertDetectionRunOK(techniques, tools);
    }

    private void assertPrepareToolsRunOk(List<ExecutionTool> prepareToolsList) {
        for(ExecutionTool prepareTool : prepareToolsList) {
            for (File dir : dirsToProcessClone) {
                File targetRootDir = new File(workingDir.getPath() + File.separator + "prepared" + File.separator + prepareTool.getName());
                Path targetPath = DirectoryFileUtility.createTargetPath(testInputDir.toPath(), targetRootDir.toPath(), dir.toPath());
                //System.out.println(targetPath.toString());
                assertEquals(2, targetPath.toFile().list().length);
                assertEquals(1, targetPath.toFile().listFiles()[0].list().length);
                assertEquals(1, targetPath.toFile().listFiles()[1].list().length);
            }
        }
    }

    private void assertTechniquesRunOK(List<ExecutionTool> techniques) {
        for(ExecutionTool technique : techniques) {
            for (File dir : dirsToProcessClone) {
                File targetRootDir = new File(workingDir.getPath() + File.separator + "preprocess" + File.separator + technique.getName() + File.separator + "SubmissionFilesUnifier");
                Path targetPath = DirectoryFileUtility.createTargetPath(testInputDir.toPath(), targetRootDir.toPath(), dir.toPath());
                //System.out.println(targetPath.toString());
                assertEquals(2, targetPath.toFile().list().length);
                assertEquals("student1.java",targetPath.toFile().listFiles()[1].getName());
                assertEquals("student2.java", targetPath.toFile().listFiles()[0].getName());
            }
        }
    }

    private void assertDetectionRunOK(List<ExecutionTool> techniques, List<ExecutionTool> tools) throws IOException, ClassNotFoundException {
        for(ExecutionTool tool : tools) {
            for(ExecutionTool technique : techniques) {
                for (File dir : dirsToProcessClone) {
                    File targetRootDir = new File(workingDir.getPath() + File.separator + "detection" + File.separator + tool.getName() + File.separator + technique.getName() + File.separator + "SubmissionFilesUnifier");
                    Path targetPath = DirectoryFileUtility.createTargetPath(testInputDir.toPath(), targetRootDir.toPath(), dir.toPath());
                    File matches = new File(targetPath.toString()+File.separator+ MPCContext.MATCHES_DIR);
                    if(tool instanceof SherlockTokenisedAdapter) {
                        File f1 = matches.listFiles()[0];
                        //System.out.println(matches.getPath());
                        MPCMatch mathc = new MPCMatchFileUtility().readFromFile(f1);
                        for(MPCMatchPart part : mathc.matchParts){
                            //System.out.println(part.startLineNumberA+" "+part.endLineNumberA);
                            //System.out.println(part.startLineNumberB+" "+part.endLineNumberB);
                        }

                    }
                    assertEquals(1, matches.list().length);
                    assertEqualsToOneOf(new String[]{"student1.java-student2.java-match",
                                    "student2.java-student1.java-match"},
                            matches.listFiles()[0].getName());
                }
            }
        }
    }

    public void assertEqualsToOneOf(Object[] expected, String actual){
        for(Object one : expected)
            if(one.equals(actual))
                return;

        fail(actual+" does not match any of the given elements "+expected.toString());
    }
}
