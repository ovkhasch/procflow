package org.procflow.mapper;

import org.procflow.model.ProcessDefinition;

/**
 * Serialization support for ProcessDefinition (read-only)
 */
public interface ProcessDefinitionMapper {
    ProcessDefinition get();
}
