package org.foi.mpc.executiontools.calibrator;

import org.foi.common.filesystem.file.PropertiesFileUtility;
import org.foi.mpc.MPCContext;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagAdapter;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagAdapterSettings;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagJavaAdapter;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagTextAdapter;
import org.foi.mpc.executiontools.detectionTools.SimilarityDetectionToolSettings;
import org.foi.mpc.executiontools.detectionTools.sherlock.SherlockAdapterSettings;
import org.foi.mpc.executiontools.detectionTools.sherlock.SherlockOriginalAdapter;
import org.foi.mpc.executiontools.detectionTools.sherlock.SherlockTokenisedAdapter;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneAdapter;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneAdapterSettings;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneJavaAdapter;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneTextAdapter;
import org.foi.mpc.executiontools.factories.SimilarityDetectionTool;
import org.foi.mpc.usecases.toolCalibration.models.ToolParam;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SimilarityDetectionToolSettingFactory {
    public class InvalidSimilarityToolParamException extends RuntimeException {
        public InvalidSimilarityToolParamException(String message) {
            super(message);
        }
    }

    public class UnknownSimilarityToolSettings extends RuntimeException {
        public UnknownSimilarityToolSettings(String message) {
            super(message);
        }
    }

    private ToolsConfiguration toolConfigs;

    public SimilarityDetectionToolSettingFactory() {
    }

    public SimilarityDetectionToolSettingFactory(File detectionToolConfigFileName) {
        this.toolConfigs = (new ToolConfigurationReader()).read(detectionToolConfigFileName);
    }

    //TODO change that the parameters t and r have actualy the name from static SIM_TEXT_MIN_RUN_LENGTH
    public List<ToolParam> getCalibrationParams(SimilarityDetectionToolSettings settings) {
        if (settings instanceof JPlagAdapterSettings) {
            JPlagAdapterSettings jplagSettings = (JPlagAdapterSettings) settings;
            List<ToolParam> responseSettings = new ArrayList<>();
            ToolParam toolParam = new ToolParam();
            toolParam.paramName = "t";
            toolParam.paramValue = jplagSettings.minTokenMatch;
            responseSettings.add(toolParam);
            return responseSettings;
        } else if (settings instanceof SimGruneAdapterSettings) {
            SimGruneAdapterSettings simSettings = (SimGruneAdapterSettings) settings;
            List<ToolParam> responseSettings = new ArrayList<>();
            ToolParam toolParam = new ToolParam();
            toolParam.paramName = "r";
            toolParam.paramValue = simSettings.minRunLength;
            responseSettings.add(toolParam);
            return responseSettings;
        }
        throw new UnknownSimilarityToolSettings(settings.toString());
    }

    public SimilarityDetectionTool setCalibrationParams(SimilarityDetectionTool tool, List<ToolParam> settings) {
        if (tool instanceof JPlagAdapter) {
            JPlagAdapter jPlagAdapter = (JPlagAdapter) tool;
            JPlagAdapterSettings jplagSetting = jPlagAdapter.getSettings();
            if (!settings.get(0).paramName.equalsIgnoreCase("t"))
                throw new InvalidSimilarityToolParamException("Invalid base param");
            jplagSetting.minTokenMatch = settings.get(0).paramValue;
            jPlagAdapter.setSettings(jplagSetting);
            return jPlagAdapter;
        } else if (tool instanceof SimGruneAdapter) {
            SimGruneAdapter simAdapter = (SimGruneAdapter) tool;
            SimGruneAdapterSettings simSettings = simAdapter.getSettings();
            if (!settings.get(0).paramName.equalsIgnoreCase("r"))
                throw new InvalidSimilarityToolParamException("Invalid base param");
            simSettings.minRunLength = settings.get(0).paramValue;
            simAdapter.setSettings(simSettings);
            return simAdapter;
        }
        throw new UnknownSimilarityToolSettings(settings.toString());
    }

    public SimilarityDetectionTool setUpConfigsIfExist(SimilarityDetectionTool tool) {
        SimilarityDetectionToolSettings settings = null;

        if (tool instanceof SimGruneJavaAdapter) {
            SimGruneAdapterSettings adapter = (SimGruneAdapterSettings) tool.getSettings();
            adapter.minRunLength = toolConfigs.minRunLengthSimJava;
            settings = adapter;
        } else if (tool instanceof SimGruneTextAdapter) {
            SimGruneAdapterSettings adapter = (SimGruneAdapterSettings) tool.getSettings();
            adapter.minRunLength = toolConfigs.minRunLengthSimText;
            settings = adapter;
        } else if (tool instanceof JPlagJavaAdapter) {
            JPlagAdapterSettings adapter = (JPlagAdapterSettings) tool.getSettings();
            adapter.minTokenMatch = toolConfigs.minTokenMatchJPlagJava;
            settings = adapter;
        } else if (tool instanceof JPlagTextAdapter) {
            JPlagAdapterSettings adapter = (JPlagAdapterSettings) tool.getSettings();
            adapter.minTokenMatch = toolConfigs.minTokenMatchJPlagText;
            settings = adapter;
        } else if (tool instanceof SherlockTokenisedAdapter) {
            SherlockAdapterSettings adapter = (SherlockAdapterSettings) tool.getSettings();
            adapter.maxJumpDiff = toolConfigs.maxJumpDiffSherlockJava;
            adapter.maxBackwardJump = toolConfigs.maxBackwardJumpSherlockJava;
            adapter.maxForwardJump = toolConfigs.maxForwardJumpSherlockJava;
            adapter.minStringLength = toolConfigs.minStringLengthSherlockJava;
            adapter.minRunLenght = toolConfigs.minRunLenghtSherlockJava;
            adapter.strictness = toolConfigs.strictnessSherlockJava;
            settings = adapter;
        } else if (tool instanceof SherlockOriginalAdapter) {
            SherlockAdapterSettings adapter = (SherlockAdapterSettings) tool.getSettings();
            adapter.maxJumpDiff = toolConfigs.maxJumpDiffSherlockText;
            adapter.maxBackwardJump = toolConfigs.maxBackwardJumpSherlockText;
            adapter.maxForwardJump = toolConfigs.maxForwardJumpSherlockText;
            adapter.minStringLength = toolConfigs.minStringLengthSherlockText;
            adapter.minRunLenght = toolConfigs.minRunLenghtSherlockText;
            adapter.strictness = toolConfigs.strictnessSherlockText;
            settings = adapter;
        }

        tool.setSettings(settings);
        return tool;
    }
}
