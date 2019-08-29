package org.foi.mpc.usecases.reports.avalibleTools;

import org.foi.mpc.MPCContext;
import org.foi.mpc.usecases.UseCaseResponseErrorMessages;
import org.foi.mpc.usecases.reports.avalibleTools.models.AvailableToolsResponseModel;

import java.io.File;
import java.util.ArrayList;

public class AvalibleToolsUseCase {

    public static AvailableToolsResponseModel getAvailableTools(File workingDir) {
        AvailableToolsResponseModel responseModel = new AvailableToolsResponseModel();
        responseModel.tools = new ArrayList<>();
        responseModel.errorMessage = "";

        File dectectionDir = new File(workingDir+File.separator+MPCContext.DETECTION_DIR);

        if(workingDir == null || !workingDir.exists()) {
            responseModel.errorMessage = UseCaseResponseErrorMessages.invalidWorkingDir;
        } else if(!dectectionDir.exists()){
            responseModel.errorMessage = UseCaseResponseErrorMessages.invalidDetectionDir;
        } else {
            for (File tools : dectectionDir.listFiles())
                responseModel.tools.add(tools.getName());
        }

        return responseModel;
    }
}
