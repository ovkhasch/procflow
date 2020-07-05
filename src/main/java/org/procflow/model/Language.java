package org.procflow.model;

public enum Language {
    js("js"),
    ruby("ruby"),
    python("py"),
    R("r"),
    ;

    private String extension;

    Language(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
