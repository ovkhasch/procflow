package org.procflow;

import io.micronaut.configuration.picocli.PicocliRunner;
import org.procflow.engine.ProcessExecutor;
import org.procflow.processinstance.ProcessInstance;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * CLI application entry point with options processing capability
 */
@Singleton
@Command(name = "procflow",
        description = "Procflow takes steps from a process definition " +
                "(from a process YAML file) and executes them sequentially, " +
                "using a provided input (from an input YAML file) as a data context. " +
                "Process steps can modify the data context during execution. " +
                "The final context state is saved into an output file.",
        mixinStandardHelpOptions = true)
public class Application implements Runnable {

    @Option(required = true, names = {"-p", "--process"}, description = "Process definition YAML file")
    String processFileName;
    @Option(names = {"-i", "--input"}, description = "Input YAML file")
    String inputFileName;
    @Option(names = {"-o", "--output"}, description = "Output YAML file name", defaultValue = "out.yaml")
    String outputFileName;
    @Option(names = {"-a", "--actions"}, description = "Directory with step action definitions", defaultValue = "data/actions")
    String actionsDir;

    @Inject
    ProcessExecutor processExecutor;
    @Inject
    Provider<ProcessInstance> processInstance;

    public static void main(String[] args) throws Exception {
        PicocliRunner.run(Application.class, args);
    }

    @Override
    public void run() {
        // Application main entry point is here

        System.out.println("Process definition: " + processFileName);
        System.out.println("Input data: " + inputFileName);
        System.out.println("Output file: " + outputFileName);
        System.out.println("Actions directory: " + actionsDir);

        processExecutor.run(processInstance.get());
    }

    public String getProcessFileName() {
        return processFileName;
    }

    public String getInputFileName() {
        return inputFileName;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public String getActionsDir() {
        return actionsDir;
    }
}
