import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.file.TextFileUtility;
import org.foi.mpc.MPCContext;
import org.foi.mpc.matches.MPCPlagMatch;
import org.foi.mpc.matches.PlagMatchesReader;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Not a test class used to generate SOCO subset datasets
 */
public class SOCODatasetCreator {
    public static final String PLAGIARISED_DIR_NAME = "Plagiarised";
    public static final String NOT_PLAGIARISED_DIR_NAME = "NotPlagiarised";
    DirectoryFileUtility dfu = new DirectoryFileUtility();
    List<String> soco = new ArrayList<>();
    String socoDatasetDir = "D:\\java\\doktorski_rad\\SOCO\\socoDatasets\\SOCO\\TEST";
    //String socoDatasetDir = "D:\\java\\doktorski_rad\\SOCO\\socoDatasets\\SOCO\\TRAIN";

    @Before
    public void setUp(){
        soco.add("C2");
        soco.add("C1");
        soco.add("A1");
        soco.add("A2");
        soco.add("B1");
        soco.add("B2");
        //soco.add("T1");
    }

    @Test
    @Ignore
    public void extractPlagiarisedPairs() throws IOException {

        File soco_match_file = new File("D:\\java\\doktorski_rad\\SOCO\\soco14-test-java-update.qrel");
        //File soco_match_file = new File("D:\\java\\doktorski_rad\\SOCO\\SOCO14-java.qrel");
        TextFileUtility tfu = new TextFileUtility(StandardCharsets.UTF_8);
        String soco_matches = tfu.readFileContentToString(soco_match_file);

        for(String datasetName : soco){
            File datasetDir = new File(socoDatasetDir+File.separator+datasetName);
            File plagFileDir = new File(socoDatasetDir+File.separator+"Separated-"+datasetName+File.separator+PLAGIARISED_DIR_NAME);
            File noplagFileDir = new File(socoDatasetDir+File.separator+"Separated-"+datasetName+File.separator+NOT_PLAGIARISED_DIR_NAME);

            if(!plagFileDir.exists()) plagFileDir.mkdirs();
            if(!noplagFileDir.exists()) noplagFileDir.mkdirs();

            for(File file : datasetDir.listFiles()){
                String fileName = file.getName();
                File destDir = noplagFileDir;
                if(soco_matches.contains(fileName)){
                       destDir = plagFileDir;
                }
                File copiedFile = dfu.copyFile(file,new File(destDir.getAbsolutePath()));
                copiedFile.renameTo(new File(copiedFile.getAbsolutePath()+".java"));
            }
        }
    }

    @Test
    @Ignore
    public void createNDatasets(){
        int numberOfnotPlagFiles = 102;
        String datasetName = "A1";
        String newDatasetName = "D1";

        String emptyDirInPhdSoco = "D:\\java\\doktorski_rad\\SOCO\\socoDatasetsPhD\\SOCO\\TEST";
        String emptyDirInByDZ = "D:\\java\\doktorski_rad\\SOCO\\socoDatasetsByDZ";
        for(int i=1;i<31;i++){
            createRandomSOCODataset(numberOfnotPlagFiles,datasetName,newDatasetName+"-"+i);
            new File(emptyDirInPhdSoco+File.separator+newDatasetName+"-"+i).mkdirs();
            new File(emptyDirInByDZ+File.separator+"socoDatasets"+newDatasetName+"-"+i+File.separator+"SOCO"+File.separator+"TEST"+File.separator+newDatasetName+"-"+i).mkdirs();
        }
    }

    @Ignore
    public void createRandomSOCODataset(int numberOfnotPlagFiles, String datasetName, String newDatasetName){

        File newDatasetDir = new File(socoDatasetDir+File.separator+newDatasetName);
        if(newDatasetDir.exists())
            return;
        else
            newDatasetDir.mkdir();

        File separatedDatasetDir = new File(socoDatasetDir+File.separator+"Separated-"+datasetName);
        File plagFileDir = new File(separatedDatasetDir+File.separator+PLAGIARISED_DIR_NAME);
        File noplagFileDir = new File(separatedDatasetDir+File.separator+NOT_PLAGIARISED_DIR_NAME);

        for(File file : plagFileDir.listFiles()){
            dfu.copyFile(file,newDatasetDir);
        }

        String analysisDir = "D:\\java\\doktorski_rad\\SOCO\\socoDatasetsWorking\\analysis\\SOCO\\TEST";
        File analysisFile = new File(analysisDir+File.separator+datasetName+File.separator+"plagiarizedMatches.txt");
        File newAnalysisDatasetDir = new File(analysisDir+File.separator+newDatasetName);
        newAnalysisDatasetDir.mkdirs();
        dfu.copyFile(analysisFile,newAnalysisDatasetDir);


        List<File> notPlagiarizedFiles = Arrays.asList(noplagFileDir.listFiles());
        for(int i=0;i<numberOfnotPlagFiles;i++) {
            Random ran = new Random();
            int fileAtPosition = ran.nextInt(notPlagiarizedFiles.size());
            File file =  notPlagiarizedFiles.get(fileAtPosition);
            File newFile = new File(newDatasetDir.getAbsolutePath()+File.separator+file.getName());
            if(newFile.exists())
                i--;
            else
                dfu.copyFile(file,newDatasetDir);
        }
    }
}
