package org.foi.mpc;

import org.foi.mpc.abstractfactories.FactoryProvider;
import org.foi.mpc.usecases.reports.pptestreport.PPTestReportUseCase;

import java.util.Enumeration;

public class MPCContext {

    public static String MATCHES_DIR = "matches";
    public static String PLAG_MATCH_FILE ="plagiarizedMatches.txt";
    public static String PROCESSED_MATCH_FILE = "processedMatches.txt";
    public static String ANALYSIS_DIR = "analysis";
    public static String DETECTION_DIR = "detection";
    public static String PREPROCESS_DIR = "preprocess";
    public static String TEMPLATE_ROOT_DIR = "templateRootDir";
    public static String COMBO_TECHNIQUES_PROPERTIES_FILE;
    public static String DETECTION_TOOLS_PROPERITES_FILE;
    public static String TEMPLATE_EXCLUSION_PROPERTIES_FILE;
    public static String DEFAULT_WORKING_DIR;
    public static String DEFAULT_SOURCE_DIR;
    public static String DEFAULT_INPUT_DIR_PATH;
    public static String DEFAULT_INPUT_DIR_PATH_SOCO;
    public static String DEFAULT_WORKING_DIR_SOCO;
    public static String DEFAULT_SOURCE_DIR_SOCO;
    public static int DEFAULT_INPUT_DIR_DEPTH;
    public static boolean DEFAULT_USE_SOCO_SUMMARY_REPORT;
    public static boolean DEFAULT_USE_SOCO_STATISTIC_REPORT;
    public static boolean ANALISYS_MODE;
    public static boolean CONSOLE_PRINT;
    public static boolean MATCHES_THREAD_READ;


    //TODO this shoul all be hidden inside some reader classes and instead of properties Congiruation models used as in MultipleDetecion
    public static class CalibrationConfigProperties {
        public static String BASE_TOOL_NAME = "BaseToolName";
        public static String CALIBRATE_TOOL_NAME = "CalibrateToolName";
        public static String WORKING_DIR = "CalibrationWorkingDir";
        public static String INPUT_DIR = "CalibrationInputDir";
        public static String BASE_PARAM_NAME = "BaseToolParamName";
        public static String BASE_PARAM_VALUE = "BaseToolParamValue";
    }

    public static class ToolsConfigProperties {
        public static String SIM_TEXT_MIN_RUN_LENGTH = "simTextMinRunLength";
        public static String SIM_JAVA_MIN_RUN_LENGTH = "simJavaMinRunLength";
        public static String JPLAG_JAVA_MIN_TOKEN_MATCH = "jpalgJavaMinTokenMatch";
        public static String JPLAG_TEXT_MIN_TOKEN_MATCH = "jpalgTextMinTokenMatch";
        public static String SHERLOCK_JAVA_MBJ = "sherlockJavaMaxBackwardJump";
        public static String SHERLOCK_JAVA_MFJ = "sherlockJavaMaxForwardJump";
        public static String SHERLOCK_JAVA_MRL = "sherlockJavaMinRunLenght";
        public static String SHERLOCK_JAVA_MSL = "sherlockJavaMinStringLength";
        public static String SHERLOCK_JAVA_MJD = "sherlockJavaMaxJumpDiff";
        public static String SHERLOCK_JAVA_STR = "sherlockJavaStrictness";
        public static String SHERLOCK_TEXT_MBJ = "sherlockTextMaxBackwardJump";
        public static String SHERLOCK_TEXT_MFJ = "sherlockTextMaxForwardJump";
        public static String SHERLOCK_TEXT_MRL = "sherlockTextMinRunLenght";
        public static String SHERLOCK_TEXT_MSL = "sherlockTextMinStringLength";
        public static String SHERLOCK_TEXT_MJD = "sherlockTextMaxJumpDiff";
        public static String SHERLOCK_TEXT_STR = "sherlockTextStrictness";
        public static String SHERLOCK_TEXT_AMALGAMATE = "sherlockTextAmalgamate";
        public static String SHERLOCK_TEXT_CONCATENATE = "sherlockTextConcatenate";
    }
}
