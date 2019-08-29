package org.foi.mpc.executiontools.detectionTools.learningTests;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import jplag.ExitException;
import jplag.Program;
import jplag.options.CommandLineOptions;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.file.TextFileUtility;
import org.foi.common.filesystem.file.TextCreator;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

@Ignore
public class SIMPreprocessTest {
    
    File testDir;
    
    @Before
    public void setUp() throws IOException {
        TextFileUtility tfu = new TextFileUtility(StandardCharsets.UTF_8);
        testDir = new File("SIMProcessTestDir");
        //File file1 = new File(testDir.getPath() + File.separator + "test.txt");
        //File file2 = new File(testDir.getPath() + File.separator + "test2.txt");
        File file3 = new File(testDir.getPath() + File.separator + "test3.txt");
        File file4 = new File(testDir.getPath() + File.separator + "test4.txt");
        //testDir.mkdir();
        //tfu.createFileWithText(file1, TextCreator.validMiniCodeExcample());
        //tfu.createFileWithText(file2, TextCreator.validMiniCodeExcample());
        //tfu.createFileWithText(file3, TextCreator.validMini2CodeExcample());
        //tfu.createFileWithText(file4, TextCreator.validMini2CodeExcample()+TextCreator.validMini2CodeExcample());

    }
    
    @After
    public void tearDown() throws IOException {
        //DirectoryFileUtility.deleteDirectoryTree(new File(testDir.getPath() + File.separator + "temp"));
        //DirectoryFileUtility.deleteDirectoryTree(testDir);
    }
    
    @Test
    public void runSimHelp() throws InterruptedException, IOException{
        runSimJava("-h");
    }
    
    @Test
    public void runSimTestJava() throws InterruptedException, IOException{
        File outputDir = new File(testDir.getPath()+File.separator+"resultJava.txt");
        runSimJava( "-R -e -n -O -r 2 -s" //-a
               +" -o "+outputDir.getPath()
               +" "+testDir.getPath());
        assertTrue(outputDir.exists());
    }
    
    @Test
    public void runSimTestText() throws InterruptedException, IOException{
        File outputDir = new File(testDir.getPath()+File.separator+"resultText.txt");
        runSimText("-R -a -e -n -O -r 2 -s"
               +" -o "+outputDir.getPath()
               +" "+testDir.getPath());
        assertTrue(outputDir.exists());
    }

    @Test
    public void runSIMOnSomeDir() throws IOException, InterruptedException {
        testDir = new File("D:\\java\\doktorski_rad\\calibrationTest\\inputData\\soco1");
        File outputDir = new File(testDir.getPath()+File.separator+"resultText.txt");
        runSimText("-R -a -e -n -O -r 4 -s"
                +" -o "+outputDir.getPath()
                +" "+testDir.getPath());
    }
    
    @Test
    public void runSimTestPrecent() throws InterruptedException, IOException{
       File outputDir = new File(testDir.getPath()+File.separator+"resultPrecent.txt");
        runSimJava( "-R -a -e -p -O -r 2 -s"
               +" -o "+outputDir.getPath()
               +" "+testDir.getPath());
        assertTrue(outputDir.exists());
    }

    @Test
    public void runSimTestPrecentReal() throws InterruptedException, IOException{
        testDir = new File("D:\\java\\doktorski_rad\\ppTest\\optOutputData4\\preprocess\\RemoveComments\\SubmissionFilesUnifier");
        File outputDir = new File(testDir.getPath()+File.separator+"resultPrecent.txt");
        runSimJava( "-R -a -e -p -r 22 -s"
                +" -o "+outputDir.getPath()
                +" "+testDir.getPath());
        assertTrue(outputDir.exists());
        testDir = new File("SIMProcessTestDir");
    }



    
    public void runSimJava(String params) throws IOException, InterruptedException{
        Runtime runTime = Runtime.getRuntime();
        Process process = runTime.exec("lib"+File.separator+"tools"+File.separator+"sim_java.exe "+params);
        printOutput(process);
        process.waitFor();
    }
    
    public void runSimText(String params) throws IOException, InterruptedException{
        Runtime runTime = Runtime.getRuntime();
        Process process = runTime.exec("lib"+File.separator+"tools"+File.separator+"sim_text.exe "+params);
        printOutput(process);
        process.waitFor();
    }
    
    private void printOutput(Process process) throws IOException{
        String line;

            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            input.close();

            BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = error.readLine()) != null) {
                System.out.println(line);
            }
            error.close();
    }
}
