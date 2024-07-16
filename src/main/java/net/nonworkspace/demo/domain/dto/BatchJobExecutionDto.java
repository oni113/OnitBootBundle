package net.nonworkspace.demo.domain.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import net.nonworkspace.demo.batch.entity.BatchJobExecution;

@Data
public class BatchJobExecutionDto {

    public BatchJobExecutionDto(BatchJobExecution batchJobExecution) {
        this.setJobExecutionId(batchJobExecution.getId());
        this.setVersion(batchJobExecution.getVersion());
        this.setStartTime(batchJobExecution.getStartTime());
        this.setEndTime(batchJobExecution.getEndTime());
        this.setStatus(batchJobExecution.getStatus());
        this.setExitCode(batchJobExecution.getExitCode());
        this.setExitMessage(batchJobExecution.getExitMessage());
        this.setCreateTime(batchJobExecution.getCreateTime());
        this.setLastUpdated(batchJobExecution.getLastUpdated());
        batchJobExecution.getBatchJobExecutionParams().parallelStream().forEach(p -> {
            this.getBatchJobExecutionParams().add(new BatchJobExecutionParamDto(p));
        });

        this.setBatchJobExecutionContext(
            new BatchJobExecutionContextDto(batchJobExecution.getBatchJobExecutionContext()));

        this.setBatchJobInstance(new BatchJobInstanceDto(batchJobExecution.getBatchJobInstance()));
    }

    private Long jobExecutionId;

    private Long version;

    private Instant createTime;

    private Instant startTime;

    private Instant endTime;

    private String status;

    private String exitCode;

    private String exitMessage;

    private Instant lastUpdated;

    private BatchJobExecutionContextDto batchJobExecutionContext;

    private List<BatchJobExecutionParamDto> batchJobExecutionParams = new ArrayList<>();

    private BatchJobInstanceDto batchJobInstance;
}
