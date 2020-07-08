package org.procflow.engine;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.procflow.model.ProcessError;
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

        // Step handler
        Consumer<Step> onNext = (Step step) -> stepExecutor.run(step, instance);
        // Process completion handler
        Action onComplete = () -> instance.getProcessContextMapper().save(instance.getContext());
        // Step error handler
        Consumer<Throwable> onError = (Throwable t) -> {
            log.error("Process error: " + t.getMessage());

            var error = new ProcessError();
            error.setMessage("Process error: " + t.getMessage());
            error.setStacktrace(ExceptionUtils.getStackTrace(t).replaceAll("\\p{C}", ""));

            instance.getContext().setError(error);
            onComplete.run();
        };

        // Process process steps
        instance.getProcess().getSteps().subscribe(onNext, onError, onComplete);
    }
}
