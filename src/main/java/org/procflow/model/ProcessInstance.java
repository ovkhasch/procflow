package org.procflow.model;

public class ProcessInstance {
    private ProcessDefinition process;
    private ProcessContext context;
    private String actionsDir;
    private String outputFileName;

    public ProcessInstance(ProcessDefinition process, ProcessContext context, String actionsDir, String outputFileName) {
        this.process = process;
        this.context = context;
        this.actionsDir = actionsDir;
        this.outputFileName = outputFileName;
    }

    public ProcessDefinition getProcess() {
        return process;
    }

    public void setProcess(ProcessDefinition process) {
        this.process = process;
    }

    public ProcessContext getContext() {
        return context;
    }

    public void setContext(ProcessContext context) {
        this.context = context;
    }

    public String getActionsDir() {
        return actionsDir;
    }

    public void setActionsDir(String actionsDir) {
        this.actionsDir = actionsDir;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }
}
