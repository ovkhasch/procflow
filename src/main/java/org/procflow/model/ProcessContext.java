package org.procflow.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Process instance data context
 */
public class ProcessContext {
    private Map<String, Object> input = new HashMap<>();
    private Map<String, Object> result = new HashMap<>();
    private ProcessError error;

    public Map<String, Object> getInput() {
        return input;
    }

    public void setInput(Map<String, Object> input) {
        this.input = input;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public ProcessError getError() {
        return error;
    }

    public void setError(ProcessError error) {
        this.error = error;
    }
}
