package org.foi.mpc.usecases.toolCalibration.view;

import org.foi.mpc.usecases.UseCaseResponseErrorMessages;
import org.foi.mpc.usecases.toolCalibration.ToolCalibrationOutputBoundary;
import org.foi.mpc.usecases.toolCalibration.models.CaseSimilarityBaseTool;
import org.foi.mpc.usecases.toolCalibration.models.CaseSimilarityCalibratedTool;
import org.foi.mpc.usecases.toolCalibration.models.ToolCalibrationResponseModel;
import org.foi.mpc.usecases.toolCalibration.models.ToolParam;

import java.util.List;

public class ToolCalibrationPresenterCMD implements ToolCalibrationOutputBoundary {
    private String textToPrint;

    @Override
    public void presentReport(ToolCalibrationResponseModel responseModel) {
        if(responseModel==null)
            textToPrint = UseCaseResponseErrorMessages.responseModelIsNull;
        else if(!responseModel.errorMessage.isEmpty())
            textToPrint = responseModel.errorMessage;
        else {
            textToPrint = "CALIBRATION SUCCESSFUL\n\n";
            textToPrint += "Calibrated Tool: " + responseModel.calibratedToolName + "\n";
            textToPrint += "Base Tool: " + responseModel.baseToolName + "\n";
            textToPrint += "Base Tool Params: ";
            for (ToolParam baseParam : responseModel.baseToolParams) {
                textToPrint += baseParam.paramName + " = " + baseParam.paramValue+", ";
            }


            textToPrint += "\nCASES\n";

            textToPrint += "\nOptimal params for all cases:\n";
            textToPrint += "  Params: ";
            for (ToolParam optimalParam : responseModel.calibratedToolOptimalParams) {
                textToPrint += optimalParam.paramName + " = " + optimalParam.paramValue+", ";
            }
            textToPrint += "\n";

            for (CaseSimilarityBaseTool baseTool : responseModel.baseToolSimilarities) {
                textToPrint += "\nCase: " + baseTool.caseName + "\n";
                textToPrint += "  Similarity Base Tool: " + baseTool.similarity+"\n";
                for (CaseSimilarityCalibratedTool calibratedTool : responseModel.calibratedToolSimilarities) {
                    if (calibratedTool.caseName.equalsIgnoreCase(baseTool.caseName)) {
                        textToPrint += "  Optimal params similarity Calibrated: "+calibratedTool.optimalSimilarity+"\n";
                        textToPrint += "  Diff to base tool: "+calibratedTool.optimalSimilarityDiff +"\n";
                        textToPrint += "  Best Similarity Calibrated: " + calibratedTool.bestSimilarity + "; ";
                        textToPrint += "  Best Params: ";
                        printToolParams(calibratedTool.calibratedToolParams);

                        break;
                    }
                }
            }
            textToPrint += "\n ALL COMBO PARAM DIFFS \n";
            for(List<ToolParam> paramCombo : responseModel.diffForAllParamCombos.keySet()){
                textToPrint += "  Param combo: ";
                printToolParams(paramCombo);
                textToPrint += "\n  Total diff: "+responseModel.diffForAllParamCombos.get(paramCombo)+"\n";
            }
        }

        System.out.println(textToPrint);
    }

    private void printToolParams(List<ToolParam> toolParams) {
        for (ToolParam calibParams : toolParams) {
            textToPrint += calibParams.paramName + " = " + calibParams.paramValue+", ";
        }
    }

    public String getTextToPrint() {
        return textToPrint;
    }
}
