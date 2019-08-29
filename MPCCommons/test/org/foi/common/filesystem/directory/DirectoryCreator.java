package org.foi.common.filesystem.directory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.foi.common.filesystem.file.TextFileUtility;
import org.foi.common.filesystem.file.TextCreator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DirectoryCreator {

    String preprocessDir = "preprocess";

    public enum DirTypes {
        ppTechnique(1, "ppTechnique"),
        course(1, "course"),
        year(1, "year"),
        assignement(2, "asgn"),
        submission(2, "subm"),
        files(2, "testFile");

        private int numberOfCreations;
        private String dirName;

        private DirTypes(int numCount, String name) {
            this.numberOfCreations = numCount;
            this.dirName = name;
        }

        public int getNumberOfCreations() {
            return numberOfCreations;
        }

        public String getDirName() {
            return dirName;
        }
    }

    List<DirTypes> dirTypeHierarchy = new ArrayList<>();
    List<File> createdAsignementDirs = new ArrayList<>();
    DirTypes lastCreationStartDir = null;

    public DirectoryCreator() {
        dirTypeHierarchy.add(DirTypes.ppTechnique);
        dirTypeHierarchy.add(DirTypes.course);
        dirTypeHierarchy.add(DirTypes.year);
        dirTypeHierarchy.add(DirTypes.assignement);
        dirTypeHierarchy.add(DirTypes.submission);
        dirTypeHierarchy.add(DirTypes.files);
    }
    
     public static void assertDirectoryIsEmpty(File directory) {
        String[] directoryContentList = directory.list();
        assertEquals(0,directoryContentList.length);
    }

    public void createTestDirStructure(File testDir, DirTypes startWithDirectory) throws IOException {
        testDir.mkdir();
        int start = dirTypeHierarchy.indexOf(startWithDirectory);
        DirTypes dirTypes = dirTypeHierarchy.get(start);
        createNDirsForDirType(dirTypes, testDir, startWithDirectory, start);
        saveAssignmentToList(startWithDirectory, testDir);
        saveLastUsedStartDir(startWithDirectory);
    }
    
    public List<File> getCreatedAsignementDirs(){
        return createdAsignementDirs;
    }
    
    public void assertThatLastCreatedDirStructureIsOkInTargetDir(File targetDir) throws IOException{
        assertCreatedDirStructureIsOkInTargetDir(targetDir, lastCreationStartDir);
    }

    public static void createTestDirWithNFiles(File sourceDir,int numberOfFiles) throws IOException {
        TextFileUtility tfu = new TextFileUtility(StandardCharsets.UTF_8);
        sourceDir.mkdir();
        for (int i = 1; i <= numberOfFiles; i++) {
            File file = new File(sourceDir.getPath() + File.separator + DirTypes.files.getDirName()+i+".java");
            tfu.createFileWithText(file, TextCreator.getSourceCodeExample1());
        }
    }
    
    public static void assertDirHasFilesCount(int expectedFileCount, File preprocessDir) {
        assertEquals(expectedFileCount,preprocessDir.list().length);
    }
    
    private void createNDirsForDirType(DirTypes dirType, File testDir, DirTypes startWithDirectory, int start) throws IOException {
        for (int i = 1; i <= dirType.getNumberOfCreations(); i++) {
            Path dir = Paths.get(testDir.getPath(), dirType.getDirName() + i);
            dir.toFile().mkdir();
            if (startWithDirectory.equals(DirTypes.submission)) {
                createTestDirWithNFiles(dir.toFile(),DirTypes.files.getNumberOfCreations());
                continue;
            }
            createTestDirStructure(dir.toFile(), dirTypeHierarchy.get(start + 1));
        }
    }

    private void saveLastUsedStartDir(DirTypes startWithDirectory) {
        lastCreationStartDir = startWithDirectory;
    }

    private void saveAssignmentToList(DirTypes dirType, File testDir) {
        if(dirType.equals(DirTypes.submission)){
            createdAsignementDirs.add(testDir);
        }
    }
    
    private void assertCreatedDirStructureIsOkInTargetDir(File targetDir, DirTypes startWithDirectory) throws IOException{
        int start = dirTypeHierarchy.indexOf(startWithDirectory);
        DirTypes dirType = dirTypeHierarchy.get(start);
        
        for (int i = 1; i <= dirType.getNumberOfCreations(); i++) {
            Path dir = Paths.get(targetDir.toString(), dirType.getDirName() + i);
            assertThatDirExistsWithCorrectChilds(dir, start);
            if (startWithDirectory.equals(DirTypes.submission)) {
                continue;
            }
            assertCreatedDirStructureIsOkInTargetDir(dir.toFile(), dirTypeHierarchy.get(start + 1));
        }
    }

    private void assertThatDirExistsWithCorrectChilds(Path dir, int start) {
        assertTrue(dir.toFile().exists());
        assertEquals(dirTypeHierarchy.get(start+1).getNumberOfCreations(),dir.toFile().list().length);
    }
    
   
}
