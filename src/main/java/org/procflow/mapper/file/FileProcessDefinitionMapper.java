package org.procflow.mapper.file;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Emitter;
import io.reactivex.Flowable;
import org.procflow.mapper.ProcessDefinitionMapper;
import org.procflow.model.ProcessDefinition;
import org.procflow.model.Step;
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
            ProcessDefinition processDefinition = new ProcessDefinition();

            // Prepare parser and read the process name
            JsonParser jsonParser = parseProcessMetadata(processDefinition);

            // Read the process steps
            // Create buffered reader with backpressure
            final Flowable<Step> steps = Flowable.generate(
                    () -> jsonParser,
                    this::parseOneStep,
                    JsonParser::close);

            processDefinition.setSteps(steps);

            return processDefinition;
    }

    private JsonParser parseProcessMetadata(ProcessDefinition processDefinition) {
        JsonParser jsonParser = null;
        try {
            jsonParser = mapper.getFactory().createParser(new File(processFileName));

            // Check the first token
            if (jsonParser.nextToken() != JsonToken.START_OBJECT) {
                throw new IllegalStateException("Expected content to be an object");
            }

            // Assume that process metadata is always before the list of steps
            // and it doesn't have any arrays
            while (jsonParser.nextToken() != JsonToken.START_ARRAY) {
                // Read the process name
                if (jsonParser.getCurrentName().equals("name")) {
                    processDefinition.setName(jsonParser.getValueAsString());
                }
            }

            return jsonParser;
        } catch (IOException e) {
            try {
                jsonParser.close();
            } catch (Exception ignored) {
            }
            log.error("Cannot read process definition", e);
            throw new RuntimeException("Cannot read process definition");
        }
    }

    private void parseOneStep(JsonParser parser, Emitter<Step> emitter) {
        try {
            if (parser.nextToken() != JsonToken.END_ARRAY) {
                final Step step = mapper.readValue(parser, Step.class);
                emitter.onNext(step);
            } else {
                emitter.onComplete();
            }
        } catch (IOException e) {
            try {
                parser.close();
            } catch (Exception ignored) {
            }
            emitter.onError(e);
        }
    }
}
