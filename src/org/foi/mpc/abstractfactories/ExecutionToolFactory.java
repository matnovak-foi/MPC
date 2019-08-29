package org.foi.mpc.abstractfactories;

import org.foi.mpc.phases.executionphases.ExecutionTool;

import java.io.File;
import java.util.List;

public interface ExecutionToolFactory {
     class ClassNameException extends RuntimeException {
        public ClassNameException(String message) {
            super(message);
        }
    }

    ExecutionTool createTool(String selectedTool);
    List<String> getAvailableTools();
}
