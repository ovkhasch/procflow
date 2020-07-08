package org.procflow.engine;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyObject;
import org.procflow.processinstance.ProcessInstance;
import org.procflow.model.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Singleton
public class StepExecutor {
    private static final Logger log = LoggerFactory.getLogger(StepExecutor.class);

    private void processParameters(Step step, Context context) {
        String languageId = step.getLanguage().name();
        step.getParameters().forEach(p -> {
            if (p.getRef() != null) {
                // when parameter is a ref we prefix it with pf_ (to avoid naming conflicts),
                // evaluate to resolve the ref and add to the execution context
                Value val = context.eval(languageId, "pf_" + p.getRef());
                context.getBindings(languageId).putMember(p.getName(), val);
            } else {
                // when parameter is a val - just adding the value to the execution context
                context.getBindings(languageId).putMember(p.getName(), p.getVal());
            }
        });
    }

    public void run(Step step, ProcessInstance processInstance) {
        log.info("Starting step: " + step.getName());

        // base dir + action name + language-specific extension
        var path = processInstance.getActionsDir() + "/" +
                step.getAction() + "." +
                step.getLanguage().getExtension();

        try {
            // load action code
            var action = Files.readString(new File(path).toPath(), StandardCharsets.UTF_8);

            // create execution context
            try (Context context = Context.newBuilder().allowAllAccess(true).build()) {
                String languageId = step.getLanguage().name();
                Value bindings = context.getBindings(languageId);

                // add input/result bindings to the execution context
                bindings.putMember("pf_input", ProxyObject.fromMap(processInstance.getContext().getInput()));
                bindings.putMember("pf_result",ProxyObject.fromMap(processInstance.getContext().getResult()));

                // add declared parameters to the execution context
                processParameters(step, context);

                // run an action
                Value eval = context.eval(languageId, action);

                if (!eval.isNull()) {
                    // save action result to the process context
                    processInstance.getContext().getResult().put(step.getName(), eval.as(Object.class));
                }
            }
        } catch (IOException e) {
            log.error("Unable to read action definition from " + path, e);
        }
    }
}
