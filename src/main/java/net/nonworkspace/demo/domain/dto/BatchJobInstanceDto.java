package net.nonworkspace.demo.domain.dto;

import lombok.Data;
import net.nonworkspace.demo.batch.entity.BatchJobInstance;

@Data
public class BatchJobInstanceDto {

    public BatchJobInstanceDto(BatchJobInstance batchJobInstance) {
        this.setJobExecutionId(
            batchJobInstance.getBatchJobExecutions().stream().findFirst().get().getId());
        this.setJobInstanceId(batchJobInstance.getId());
        this.setVersion(batchJobInstance.getVersion());
        this.setJobName(batchJobInstance.getJobName());
        this.setJobKey(batchJobInstance.getJobKey());
    }

    private Long jobExecutionId;

    private Long jobInstanceId;

    private Long version;

    private String jobName;

    private String jobKey;
}
