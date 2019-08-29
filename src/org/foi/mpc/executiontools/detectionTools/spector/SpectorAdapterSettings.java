package org.foi.mpc.executiontools.detectionTools.spector;

import org.foi.mpc.executiontools.detectionTools.SimilarityDetectionToolSettings;

import java.util.ArrayList;
import java.util.List;


public class SpectorAdapterSettings implements SimilarityDetectionToolSettings {
    public enum SpectorInputOption {
        FILE("file"),
        SUBMISSION("submission");

        private String value;

        private SpectorInputOption(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum SpectorOutputOption {
        SUMMARY("s"),
        DETAILS("d"),
        AST("a"),
        HTML("h"),
        TIMING("t");

        private String value;

        private SpectorOutputOption(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum SpectorOrderOption {
        ALPHABETIC("l"),
        ASCENDING("s"),
        DESCENDING("d");

        private String value;

        private SpectorOrderOption(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
    
    public SpectorInputOption inputOption;
    public List<SpectorOutputOption> outputOptions = new ArrayList<>();
    public String resultDirName;
    public SpectorOrderOption orderOption;
    public String matchesDirName;
}