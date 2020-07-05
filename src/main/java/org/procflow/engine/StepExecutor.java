package org.procflow.engine;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyObject;
import org.procflow.model.ProcessInstance;
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
                String assignment = p.getName() + " = pf_" + p.getRef();
                context.eval(languageId, assignment);
            } else {
                context.getBindings(languageId).putMember(p.getName(), p.getValue());
            }
        });
    }

    public void run(Step step, ProcessInstance processInstance) {
        log.info("Starting step: " + step.getName());

        var path = processInstance.getActionsDir() + "/" +
                step.getAction() + "." +
                step.getLanguage().getExtension();
        try {
            var action = Files.readString(new File(path).toPath(), StandardCharsets.UTF_8);
            try (Context context = Context.newBuilder().allowAllAccess(true).build()) {
                String languageId = step.getLanguage().name();
                Value bindings = context.getBindings(languageId);
                bindings.putMember("pf_input", ProxyObject.fromMap(processInstance.getContext().getInput()));
                bindings.putMember("pf_result",ProxyObject.fromMap(processInstance.getContext().getResult()));
                processParameters(step, context);
                Value eval = context.eval(languageId, action);
                if (!eval.isNull()) {
                    processInstance.getContext().getResult().put(step.getName(), eval.as(Object.class));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
