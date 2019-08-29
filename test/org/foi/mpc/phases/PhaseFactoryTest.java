package org.foi.mpc.phases;

import org.foi.mpc.MPCContext;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.mpc.phases.executionphases.ExecutionPhase;
import org.foi.mpc.executiontools.prepareTools.ArchiveExtractor;
import org.foi.mpc.executiontools.prepareTools.Renamer;
import org.foi.mpc.executiontools.prepareTools.SubmissionFilesUnifier;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.*;

public class PhaseFactoryTest {
    private static File workingDir = new File("workingDir");
    private static  File inputDir = new File("inputDir");
    private PhaseFactory phaseFactory = new PhaseFactory(MPCContext.MATCHES_DIR);

    @BeforeClass
    public static void setUpClass(){
        workingDir.mkdir();
        inputDir.mkdir();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        DirectoryFileUtility.deleteDirectoryTree(workingDir);
        DirectoryFileUtility.deleteDirectoryTree(inputDir);
    }

    @Test
    public void canCreateDefaultSetUpForPreparePhase(){
        List<Class> expectedTools = new ArrayList<>();
        expectedTools.add(ArchiveExtractor.class);
        expectedTools.add(Renamer.class);
        expectedTools.add(SubmissionFilesUnifier.class);

        ExecutionPhase defaultPreparer = phaseFactory.createDefaultSetupAssignmentPreparer(workingDir, inputDir);
        assertListHasListOfClassesInOrder(expectedTools,defaultPreparer.getExecutionTools());
        assertEquals(workingDir,defaultPreparer.getPhaseWorkingDir().getParentFile());
        assertEquals(inputDir, defaultPreparer.getSourceDir());
        assertTrue(defaultPreparer.getSequential());
    }

    public void assertListHasListOfClassesInOrder(List<Class> expectedClasses, List objects){
        int i =0;
        for(Class classType : expectedClasses){
            assertThat(objects.get(i),instanceOf(classType));
            i++;
        }
    }
}
