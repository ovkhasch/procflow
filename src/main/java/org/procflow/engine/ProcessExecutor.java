package org.procflow.engine;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import org.procflow.model.Step;
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

        Consumer<Step> onNext = (Step step) -> stepExecutor.run(step, instance);
        Consumer<Throwable> onError = (Throwable t) -> {throw new RuntimeException(t);};
        Action onComplete = () -> instance.getProcessContextMapper().save(instance.getContext());

        instance.getProcess().getSteps().subscribe(onNext, onError, onComplete);
    }
}
