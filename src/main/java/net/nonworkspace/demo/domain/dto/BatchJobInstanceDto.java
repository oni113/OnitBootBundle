package net.nonworkspace.demo.domain.dto;

import net.nonworkspace.demo.batch.entity.BatchJobInstance;

public record BatchJobInstanceDto(
    Long jobExecutionId,
    Long jobInstanceId,
    Long version,
    String jobName,
    String jobKey
) {
    public BatchJobInstanceDto(BatchJobInstance batchJobInstance) {
        this(
            batchJobInstance.getBatchJobExecutions().stream().findFirst().get().getId(),
            batchJobInstance.getId(),
            batchJobInstance.getVersion(),
            batchJobInstance.getJobName(),
            batchJobInstance.getJobKey()
        );
    }
}
