package net.nonworkspace.demo.domain.dto;

import net.nonworkspace.demo.batch.entity.BatchJobExecutionContext;

public record BatchJobExecutionContextDto(
    Long jobExecutionId,
    String shortContext,
    String serializedContext
) {

    public BatchJobExecutionContextDto(BatchJobExecutionContext batchJobExecutionContext) {
        this(batchJobExecutionContext.getId(), batchJobExecutionContext.getShortContext(),
            batchJobExecutionContext.getSerializedContext());
    }
}
