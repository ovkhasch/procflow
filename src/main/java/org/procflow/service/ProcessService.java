package org.procflow.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.procflow.engine.ProcessExecutor;
import org.procflow.model.ProcessContext;
import org.procflow.model.ProcessDefinition;
import org.procflow.model.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Handle I/O for the process definition/data files, provides interface for running a process using CLI
 */
@Singleton
public class ProcessService {
    private static final Logger log = LoggerFactory.getLogger(ProcessService.class);

    @Inject
    ProcessExecutor processExecutor;

    private ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    public void runProcess(String processFileName, String inputFileName, String actionsDir, String outputFileName) {
        var process = readProcess(processFileName);

        ProcessContext context = new ProcessContext();
        if (inputFileName != null) {
            var input = readInput(inputFileName);
            context.setInput(input);
        }

        var processInstance =  new ProcessInstance(process, context, actionsDir, outputFileName);
        processExecutor.run(processInstance);

        saveOutput(processInstance);
    }

    private ProcessDefinition readProcess(String processFileName) {
        try {
            return mapper.readValue(new File(processFileName), ProcessDefinition.class);
        } catch (IOException e) {
            log.error("Cannot read process definition", e);
            throw new RuntimeException("Cannot read process definition");
        }
    }

    private Map<String, Object> readInput(String inputFileName) {
        try {
            return mapper.readValue(new File(inputFileName), Map.class);
        } catch (IOException e) {
            log.error("Cannot read process input", e);
            throw new RuntimeException("Cannot read process input");
        }
    }

    private void saveOutput(ProcessInstance processInstance) {
        try {
            log.info("Writing output to " + processInstance.getOutputFileName());
            mapper.writeValue(new File(processInstance.getOutputFileName()), processInstance.getContext().getResult());
        } catch (IOException e) {
            log.error("Cannot write process output", e);
            throw new RuntimeException("Cannot write process output");
        }
    }
}
