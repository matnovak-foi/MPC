package org.foi.mpc.main;

import org.foi.common.filesystem.file.PropertiesFileUtility;
import org.foi.mpc.MPCContext;
import org.foi.mpc.abstractfactories.FactoryProvider;
import org.foi.mpc.executiontools.factories.SimilarityDetectionToolFactory;
import org.foi.mpc.usecases.toolCalibration.ToolCalibrationOutputBoundary;
import org.foi.mpc.usecases.toolCalibration.ToolCalibratorInputBoundary;
import org.foi.mpc.usecases.toolCalibration.ToolCalibratorUseCase;
import org.foi.mpc.usecases.toolCalibration.models.ToolParam;
import org.foi.mpc.usecases.toolCalibration.models.ToolCalibrationRequestModel;
import org.foi.mpc.usecases.toolCalibration.view.ToolCalibrationPresenterCMD;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.foi.mpc.MPCContext.CalibrationConfigProperties.*;

public class ToolCalibration {

    private FactoryProvider factoryProvider = new FactoryProvider(MPCContext.MATCHES_DIR);
    private String[] args;
    private Properties properties;
    private ToolCalibratorInputBoundary calibartionUseCase;
    private ToolCalibrationOutputBoundary presenterCMD;

    public static void main(String[] args){
        ToolCalibration toolCalibration = new ToolCalibration();
        toolCalibration.run(args);
    }

    ToolCalibration() {
        SimilarityDetectionToolFactory toolFactory = (SimilarityDetectionToolFactory) factoryProvider.getToolFactory();
        calibartionUseCase = new ToolCalibratorUseCase(toolFactory,MPCContext.MATCHES_DIR);
        presenterCMD = new ToolCalibrationPresenterCMD();
    }

    void run(String[] args) {
        if(args.length != 1 || !args[0].endsWith(".properties")) {
            String input = "";
            if(args.length>0){
                input = args[0];
            }
            System.out.println("Wrong input parameters!"+input);
            return;
        }
        File propertiesFile = new File(args[0]);
        properties = PropertiesFileUtility.readProperties(propertiesFile);
        ToolCalibrationRequestModel requestModel = createRequestModel();
        calibartionUseCase.runCalibration(presenterCMD,requestModel);
    }

    private ToolCalibrationRequestModel createRequestModel() {
        ToolCalibrationRequestModel requestModel = new ToolCalibrationRequestModel();
        requestModel.baseToolName = properties.getProperty(BASE_TOOL_NAME);
        requestModel.toCalibrateToolName = properties.getProperty(CALIBRATE_TOOL_NAME);
        requestModel.workingDir = new File(properties.getProperty(WORKING_DIR));
        requestModel.inputDirWithCalibrationCases = new File(properties.getProperty(INPUT_DIR));
        requestModel.baseToolParams = createBaseToolParamsRequestPart();

        return requestModel;
    }

    private List<ToolParam> createBaseToolParamsRequestPart() {
        List<ToolParam> baseToolParams = new ArrayList<>();
        for(int i=1;true;i++){
            if(properties.get(BASE_PARAM_NAME+i) == null)
                break;
            ToolParam baseParam = new ToolParam();
            baseParam.paramName = properties.getProperty(BASE_PARAM_NAME+i);
            baseParam.paramValue = Integer.parseInt(properties.getProperty(BASE_PARAM_VALUE+i));
            baseToolParams.add(baseParam);
        }
        return baseToolParams;
    }

    public String[] getArgs() {
        return args;
    }

    public Properties getProperties() {
        return properties;
    }

    public ToolCalibratorInputBoundary getCalibartionUseCase() {
        return calibartionUseCase;
    }

    public void setCalibartionUseCase(ToolCalibratorInputBoundary calibartionUseCase) {
        this.calibartionUseCase = calibartionUseCase;
    }

    public void setPresenterCMD(ToolCalibrationOutputBoundary presenterCMD) {
        this.presenterCMD = presenterCMD;
    }

    public ToolCalibrationOutputBoundary getPresenterCMD() {
        return presenterCMD;
    }

    public String getPresentedText(){
        return ((ToolCalibrationPresenterCMD) presenterCMD).getTextToPrint();
    }
}
