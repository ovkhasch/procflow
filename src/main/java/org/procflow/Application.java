package org.procflow;

import io.micronaut.configuration.picocli.PicocliRunner;

import org.procflow.engine.ProcessExecutor;
import org.procflow.service.ProcessService;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import javax.inject.Inject;

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
    ProcessService processService;

    public static void main(String[] args) throws Exception {
        PicocliRunner.run(Application.class, args);
    }

    @Override
    public void run() {
        System.out.println("Process definition: " + processFileName);
        System.out.println("Input data: " + inputFileName);
        System.out.println("Output file: " + outputFileName);
        System.out.println("Actions directory: " + actionsDir);

        processService.runProcess(
                processFileName,
                inputFileName,
                actionsDir,
                outputFileName
        );
    }

}
