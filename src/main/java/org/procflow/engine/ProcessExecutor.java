package org.procflow.engine;

import org.procflow.processinstance.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ProcessExecutor {
    private static final Logger log = LoggerFactory.getLogger(ProcessExecutor.class);

    @Inject
    private StepExecutor stepExecutor;

    public void run(ProcessInstance instance) {
        log.info("Executing process: " + instance.getProcess().getName());
        instance.getProcess().getSteps()
                .forEach(s -> stepExecutor.run(s, instance));

        instance.getProcessContextMapper().save(instance.getContext());
    }
}
