package org.procflow.mapper.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Primary;
import org.procflow.Application;
import org.procflow.mapper.ProcessContextMapper;
import org.procflow.mapper.ProcessDefinitionMapper;

import javax.inject.Named;
import javax.inject.Singleton;

@Factory
public class MapperFactory {
    @Named("YAML")
    @Singleton
    ObjectMapper yamlMapper() {
        return new ObjectMapper(new YAMLFactory());
    }

    @Bean
    ProcessContextMapper contextMapper(Application application, @Named("YAML") ObjectMapper mapper) {
        return new FileProcessContextMapper(application.getInputFileName(), application.getOutputFileName(), mapper);
    }

    @Bean
    ProcessDefinitionMapper definitionMapper(Application application, @Named("YAML") ObjectMapper mapper) {
        return new FileProcessDefinitionMapper(application.getProcessFileName(), mapper);
    }
}