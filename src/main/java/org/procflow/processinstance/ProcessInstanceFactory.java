package org.procflow.processinstance;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import org.procflow.Application;
import org.procflow.mapper.ProcessContextMapper;
import org.procflow.mapper.ProcessDefinitionMapper;

/**
 * Provides an ability to inject ProcessInstance
 */
@Factory
public class ProcessInstanceFactory {
    @Bean
    ProcessInstance processInstance(
            Application application,
            ProcessDefinitionMapper processDefinitionMapper,
            ProcessContextMapper processContextMapper
            ) {
        return new ProcessInstance(
                processDefinitionMapper.get(),
                processContextMapper.get(),
                processContextMapper,
                application.getActionsDir()
        );
    }
}
