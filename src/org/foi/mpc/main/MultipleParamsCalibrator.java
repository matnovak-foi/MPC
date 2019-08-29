package org.foi.mpc.main;

import org.foi.common.filesystem.file.PropertiesFileUtility;
import org.foi.common.filesystem.file.TextFileUtility;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static org.foi.mpc.MPCContext.CalibrationConfigProperties.*;

public class MultipleParamsCalibrator {
    private ToolCalibration toolCalibrator;

    public static void main(String[] args){
        MultipleParamsCalibrator calibrator = new MultipleParamsCalibrator();
        calibrator.run(args);
    }

    public MultipleParamsCalibrator() {
        this.toolCalibrator = new ToolCalibration();
    }

    public void run(String[] args){

        if(args.length != 2 || !args[0].endsWith(".properties")) {
            System.out.println("Wrong input parameters for multiple calibrator!");
            return;
        }
        File resultDir = new  File(args[1]);
        if(!resultDir.exists()){
            System.out.println("Result dir does not exist! "+args[1]);
            return;
        }

        File propertiesFile = new File(args[0]);
        Properties properties = PropertiesFileUtility.readProperties(propertiesFile);
        for(int i=1;i<30;i++) {
            properties.put(BASE_PARAM_VALUE+"1",Integer.toString(i));
            propertiesFile.delete();
            PropertiesFileUtility.createNewPropertiesFile(properties,propertiesFile);
            toolCalibrator.run(new String[]{args[0]});
            TextFileUtility tfu = new TextFileUtility(StandardCharsets.UTF_8);
            File resultFile = new File(resultDir.getPath()+File.separator+properties.getProperty(CALIBRATE_TOOL_NAME)+"_calibratedWith_"+properties.getProperty(BASE_TOOL_NAME)+"_"+properties.getProperty(BASE_PARAM_NAME+"1")+"_"+properties.getProperty(BASE_PARAM_VALUE+"1"));
            try {
                tfu.createFileWithText(resultFile,toolCalibrator.getPresentedText());
            } catch (IOException e) {
                System.err.println("Calibartion not saved for : "+resultFile.getAbsolutePath());
                return;
            }
        }
    }

    public void setToolCalibrator(ToolCalibration toolCalibrator) {
        this.toolCalibrator = toolCalibrator;
    }
}
