package org.foi.mpc.executiontools.prepareTools;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.mpc.executiontools.factories.PrepareTools;
import org.foi.common.filesystem.directory.InMemoryDir;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(HierarchicalContextRunner.class)
public class RenamerTest {
    Renamer renamer;

    @Before
    public void setUp() throws Exception {
        renamer = new Renamer();
    }

    @Test
    public void isPreparerToolAndHasCorecctName() {
        assertTrue(renamer instanceof PrepareTools);
        assertEquals(Renamer.NAME, renamer.getName());
    }

    @Test
    public void runWithFile() throws IOException {
        renamer.runPrepareTool(new File("Name Username.java"));
        assertEquals(0,renamer.getUnrenamedDirs().size());
    }

    @Test
    public void canRename(){
        assertEquals("username",renamer.rename(new InMemoryDir("username")).getName());
        assertEquals("username",renamer.rename(new InMemoryDir("username_zadaca_1")).getName());
        assertEquals("username",renamer.rename(new InMemoryDir("username_zadaca_2")).getName());
        assertEquals("username",renamer.rename(new InMemoryDir("Name Surname_2465256_assignsubmission_file_username_zadaca_1")).getName());
        assertEquals("username",renamer.rename(new InMemoryDir("Name Surname_2465256_assignsubmission_file_username_zadaca_2")).getName());
        assertEquals("Name Username_2587928_assignsubmission_file_", renamer.rename(new InMemoryDir("Name Username_2587928_assignsubmission_file_")).getName());
    }

    @Test
    public void invalidNamesStored() {
        File dir = new File("Name Username_2587928_assignsubmission_file_");
        renamer.rename(dir);
        assertEquals(dir.getName(), renamer.getUnrenamedDirs().get(0).getName());
        dir = new File("Name Username");
        renamer.rename(dir);
        assertEquals(dir.getName(), renamer.getUnrenamedDirs().get(1).getName());
    }

    public class withRealDir {

        File testDir;

        @Before
        public void setUp(){
            testDir = new File("testDir");
            testDir.mkdir();
        }

        @After
        public void tearDown() throws IOException {
            DirectoryFileUtility.deleteDirectoryTree(testDir);
        }

        @Test
        public void canRenameRealDir() {
            File dir = new File(testDir.getPath()+File.separator+"username_zadaca_1");
            dir.mkdir();
            File renamedFile = renamer.rename(dir);
            assertEquals(0, renamer.getUnrenamedDirs().size());
            assertEquals(dir.getParent()+File.separator+"username", renamedFile.getPath());
        }
    }
}
