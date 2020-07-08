package org.procflow.mapper.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.procflow.mapper.ProcessDefinitionMapper;
import org.procflow.model.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class FileProcessDefinitionMapper implements ProcessDefinitionMapper {
    private static final Logger log = LoggerFactory.getLogger(FileProcessDefinitionMapper.class);

    private final String processFileName;
    private final ObjectMapper mapper;

    public FileProcessDefinitionMapper(String processFileName, ObjectMapper mapper) {
        this.processFileName = processFileName;
        this.mapper = mapper;
    }

    @Override
    public ProcessDefinition get() {
        try {
            return mapper.readValue(new File(processFileName), ProcessDefinition.class);
        } catch (IOException e) {
            log.error("Cannot read process definition", e);
            throw new RuntimeException("Cannot read process definition");
        }
    }
}
