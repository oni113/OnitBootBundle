package net.nonworkspace.demo.domain.dto;

import java.time.Instant;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import net.nonworkspace.demo.batch.entity.BatchJobExecution;
import net.nonworkspace.demo.batch.entity.BatchJobExecutionParam;

public record BatchJobExecutionDto(
    Long jobExecutionId,
    Long version,
    Instant createTime,
    Instant startTime,
    Instant endTime,
    String status,
    String exitCode,
    String exitMessage,
    Instant lastUpdated,
    BatchJobExecutionContextDto batchJobExecutionContext,
    List<BatchJobExecutionParamDto> batchJobExecutionParams,
    BatchJobInstanceDto batchJobInstance
) {

    public BatchJobExecutionDto(BatchJobExecution batchJobExecution) {
        this(
            batchJobExecution.getId(),
            batchJobExecution.getVersion(),
            batchJobExecution.getCreateTime(),
            batchJobExecution.getStartTime(),
            batchJobExecution.getEndTime(),
            batchJobExecution.getStatus(),
            batchJobExecution.getExitCode(),
            batchJobExecution.getExitMessage(),
            batchJobExecution.getLastUpdated(),
            new BatchJobExecutionContextDto(batchJobExecution.getBatchJobExecutionContext()),
            new AbstractList<BatchJobExecutionParamDto>() {
                @Override
                public BatchJobExecutionParamDto get(int index) {
                    return new BatchJobExecutionParamDto(batchJobExecution.getBatchJobExecutionParams().get(index));
                }

                @Override
                public int size() {
                    return batchJobExecution.getBatchJobExecutionParams().size();
                }
            },
            new BatchJobInstanceDto(batchJobExecution.getBatchJobInstance())
        );
    }
}
