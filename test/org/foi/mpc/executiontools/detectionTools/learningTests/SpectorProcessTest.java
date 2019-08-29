package org.foi.mpc.executiontools.detectionTools.learningTests;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.file.TextFileUtility;
import org.foi.common.filesystem.file.TextCreator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import spector.Comparison;
import spector.Presenter;
import spector.Spector;

@Ignore
public class SpectorProcessTest {
    File testDir;
    File file1;
    File file2;
    File result;
    public SpectorProcessTest() {
    }

    @Before
    public void setUp() throws IOException {
        TextFileUtility tfu = new TextFileUtility(StandardCharsets.UTF_8);
        testDir = new File("workingDir");
        testDir.mkdir();
        result = new File(testDir+File.separator+"result");
        result.mkdir();
        file1 = new File(testDir.getPath() + File.separator + "test.java");
        file2 = new File(testDir.getPath() + File.separator + "test2.java");
        File file3 = new File(testDir.getPath() + File.separator + "test3.java");
        File file4 = new File(testDir.getPath() + File.separator + "test4.java");
        File file5 = new File(testDir.getPath() + File.separator + "test5.java");
        File file6 = new File(testDir.getPath() + File.separator + "test6.java");
        File file7 = new File(testDir.getPath() + File.separator + "test7.java");
        
        tfu.createFileWithText(file1, TextCreator.validMiniCodeExcample());
        tfu.createFileWithText(file2, TextCreator.validMiniCodeExcample());
        tfu.createFileWithText(file3, TextCreator.validMini2CodeExcample());
        tfu.createFileWithText(file4, TextCreator.getSourceCodeExample1());
        tfu.createFileWithText(file5, TextCreator.getSourceCodeExample2());
        //tfu.createFileWithText(file6, TextCreator.getSourceCodeExample3());
        //tfu.createFileWithText(file7, TextCreator.getSourceCodeExample4());
    }

    @After
    public void tearDown() throws IOException {
        DirectoryFileUtility.deleteDirectoryTree(testDir);
    }

    @Test
    public void runSpectorHelp() {
        Spector.help();
    }

    @Test
    public void runSpector() throws IOException {
        
        Spector.debug = false;
        Spector.out = new MyPresenter();
        Spector.main((""
                +"--input f"
                + " --output sdhat"
                + " --order d"
                + " --place "+result.getPath()
                + " --recurse"
                + " --inspect "+testDir.getPath()
                + " --bidirectional"
                ).split(" "));
        //
    }
    
    private class MyPresenter extends Presenter {

        @Override
        public void printFullSummary() {
            for(Comparison comp : comparisons) {
            int count = 0;

            ArrayList<String> meastr = new ArrayList<String>();
            for(Double value : comp.getMeasures()) {
                meastr.add(df.format(value));
                if(value == 0.0) { continue; }
                count++;
            }

            String total = df.format(comp.getTotal());

            System.out.print(comp.getFirst().getFile().getName() + " - " + comp.getSecond().getFile().getName() + " : (");

//            Iterator<String> it = meastr.iterator();
//            while(it.hasNext()) {
//                String measure = it.next();
//                System.out.print(measure);
//
//                if(it.hasNext()) {
//                    System.out.print("+");
//                }
//            }

            System.out.println(")/" + count + " = " + total + "%");
            
            System.out.println("Matija"+total);
            }
        }
            
    }
}
