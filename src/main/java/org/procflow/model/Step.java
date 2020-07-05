package org.procflow.model;

import java.util.ArrayList;
import java.util.List;

public class Step {
    private String name;
    private Language language = Language.js;
    private String action;
    private List<StepParameter> parameters = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<StepParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<StepParameter> parameters) {
        this.parameters = parameters;
    }
}
