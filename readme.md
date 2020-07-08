#### **What is procflow?**
Disclaimer: as of now procflow is not a production-ready framework. It was written as a very basic exercise, so it is missing conditions, parallel execution and other important features.

Procflow takes steps from a process definition (from a process YAML file) and executes them sequentially, using a provided input (from an input YAML file) as a data context. It is writing output of each step into the data context as well. Process steps can access and modify data context during execution. The final context state is written into an output file.

Process steps are loaded lazily from a file during process execution (java rx), so there's no requirement to fit process definition into memory. This allows for a large number of steps.  

Procflow is intended to sequence lightweight actions as process steps. An important advantage is that actions can be implemented in any language, supported by polyglot [GraalVM](https://www.graalvm.org/). It is possible to mix actions implemented with multiple languages in a single process definition. Implementing new actions is as simple as adding drop-in code files.  

Procflow can also be run on a stock JVM (starting from Java 11), in this case the only supported language for actions is javascript.

#### **Building procflow**
Procflow can be built with JDK >= 11 and maven. It is as simple as running `mvn package` from the project dir.

#### **Running procflow with a stock JVM**
Procflow is a CLI application. To see the help on available options run 

`java -jar  target/procflow-1.0-SNAPSHOT.jar -h`

There are a few samples available in `data` dir. The following commands will run sample javascript process definitions:

`java -jar target/procflow-1.0-SNAPSHOT.jar -p data/minProcess.yaml`     

`java -jar target/procflow-1.0-SNAPSHOT.jar -p data/process.yaml -i data/input.yaml`

#### **Running procflow with GraalJVM - python sample (linux only)**
Download and unpack graalvm.

` wget https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-20.1.0/graalvm-ce-java11-linux-amd64-20.1.0.tar.gz`

` tar -xvf graalvm-ce-java11-linux-amd64-20.1.0.tar.gz`

Add graalvm bin dir to your PATH or specify a full path each time when running graalvm binaries (gu, java).

Install python support (as root):

`gu install python` 

Now from the project dir run the python sample:

`java -jar target/procflow-1.0-SNAPSHOT.jar -i data/input.yaml -p data/pythonProcess.yaml`    

#### **Creating a process definition**
Process definitions (as well as input and output files) have [YAML](https://yaml.org/spec/1.2/spec.html) format. Here's a sample process definition with all available schema elements explained in comments:
```yaml
name: Calculate a+b # process name
steps: # list of process steps
  - name: a_plus_42 # step name - also used as a name for a step result var
    action: sum # a name of the action (implementation file)
    language: python # action language (default javascript)
    parameters: # list of action parameters 
      - name: arg1 # parameter name
        ref: input.a # parameter value from the process input
      - name: arg2 # parameter name
        val: 42 # parameter value supplied directly
  - name: print_result
    action: print 
    parameters:  
      - name: str 
        ref: result.a_plus_42 # parameter value from a step result
    result: final_result # result variable name for this step (default ie step name)
  - name: print_result_tmpl
    action: printEval 
    parameters:
      - name: str 
# input and result are available as pf_input and pf_result variables inside action code  
        val: "`Result is: ${pf_result.final_result}`" 
```

#### **Process input**
Process input is a simple YAML file which is internally converted into Map<String, Object> with jackson. Example:
```yaml
a: 3
b: 4
x: 2
```
#### **Adding process actions**
Each process action is a program in a separate file, written using any of the available GraalVM languages. It can expect the following variables to be defined in its global scope:
* `pf_input` - process input map
* `pf_result` - a map with step results (return values), where key is a step name (default) or a result variable name from a step definition
* all the parameters listed in a step definition are passed as global variables to an action scope

A result of an action is evaluated from an action code. In languages such as js, ruby, python it is a value of a last statement in action code. 

Here's a sample js action (data/actions/sum.js):
```javascript
let res = arg1 + arg2
print(`${arg1} + ${arg2} = ${res}`)
res
```

Action drop-in code files should be available from a single top-level dir, relative to a working dir, which can be configured with a CLI option. Default path is `data/actions`.

Action code files **should have default language extension** ("js" for javascript, "py" for python, etc.).   
 