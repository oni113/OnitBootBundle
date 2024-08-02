package net.nonworkspace.demo.domain.dto.batch;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import net.nonworkspace.demo.batch.entity.BatchJobExecution;

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
            new ArrayList<>() {
                @Override
                public BatchJobExecutionParamDto get(int index) {
                    return new BatchJobExecutionParamDto(
                        batchJobExecution.getBatchJobExecutionParams().get(index));
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
