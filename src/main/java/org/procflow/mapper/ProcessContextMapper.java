package org.procflow.mapper;

import org.procflow.model.ProcessContext;

public interface ProcessContextMapper {
    ProcessContext get();
    void save(ProcessContext processContext);
}
