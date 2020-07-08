package org.procflow.model;

import io.reactivex.Flowable;

public class ProcessDefinition {
    private String name;
    private Flowable<Step> steps;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Flowable<Step> getSteps() {
        return steps;
    }

    public void setSteps(Flowable<Step> steps) {
        this.steps = steps;
    }
}
