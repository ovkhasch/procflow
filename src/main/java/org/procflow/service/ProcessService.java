package org.procflow.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.procflow.model.ProcessContext;
import org.procflow.model.ProcessDefinition;
import org.procflow.model.ProcessInstance;

import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@Singleton
public class ProcessService {
    private ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    public ProcessInstance getProcessInstance(String processFileName, String inputFileName, String actionsDir, String outputFileName) {
        var process = readProcess(processFileName);

        ProcessContext context = new ProcessContext();
        if (inputFileName != null) {
            var input = readInput(inputFileName);
            context.setInput(input);
        }

        return new ProcessInstance(process, context, actionsDir, outputFileName);
    }

    private ProcessDefinition readProcess(String processFileName) {
        try {
            return mapper.readValue(new File(processFileName), ProcessDefinition.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot read process definition");
        }
    }

    private Map<String, Object> readInput(String inputFileName) {
        try {
            return mapper.readValue(new File(inputFileName), Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot read process input");
        }
    }
}
