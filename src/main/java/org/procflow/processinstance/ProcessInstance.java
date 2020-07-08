package org.procflow.processinstance;

import org.procflow.mapper.ProcessContextMapper;
import org.procflow.model.ProcessContext;
import org.procflow.model.ProcessDefinition;

/**
 * Process instance is a process definition, context and parameters required to run a process
 */
public class ProcessInstance {
    private ProcessDefinition process;
    private ProcessContext context;
    private ProcessContextMapper processContextMapper;
    private String actionsDir;

    public ProcessInstance(
            ProcessDefinition process,
            ProcessContext context,
            ProcessContextMapper processContextMapper,
            String actionsDir
    ) {
        this.process = process;
        this.context = context;
        this.processContextMapper = processContextMapper;
        this.actionsDir = actionsDir;
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

    public ProcessContextMapper getProcessContextMapper() {
        return processContextMapper;
    }

    public void setProcessContextMapper(ProcessContextMapper processContextMapper) {
        this.processContextMapper = processContextMapper;
    }
}
