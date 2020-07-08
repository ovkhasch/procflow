package org.procflow.mapper.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.procflow.mapper.ProcessContextMapper;
import org.procflow.model.ProcessContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class FileProcessContextMapper implements ProcessContextMapper {
    private static final Logger log = LoggerFactory.getLogger(FileProcessContextMapper.class);

    private final String inputFileName;
    private final String outputFileName;
    private final ObjectMapper mapper;

    public FileProcessContextMapper(String inputFileName, String outputFileName, ObjectMapper mapper) {
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
        this.mapper = mapper;
    }

    @Override
    public ProcessContext get() {
        var context = new ProcessContext();

        if (inputFileName != null) {
            try {
                log.info("Reading input from " + inputFileName);
                var input = mapper.readValue(new File(inputFileName), Map.class);
                context.setInput(input);
                return context;
            } catch (IOException e) {
                log.error("Cannot read process input", e);
                throw new RuntimeException("Cannot read process input");
            }
        }

        return context;
    }

    @Override
    public void save(ProcessContext processContext) {
        try {
            log.info("Writing output to " + outputFileName);
            mapper.writeValue(new File(outputFileName), processContext);
        } catch (IOException e) {
            log.error("Cannot write process output", e);
            throw new RuntimeException("Cannot write process output");
        }
    }
}
