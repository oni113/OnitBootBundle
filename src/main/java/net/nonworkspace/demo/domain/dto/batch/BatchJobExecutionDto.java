package net.nonworkspace.demo.domain.dto.batch;

import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import net.nonworkspace.demo.batch.entity.BatchJobExecution;
import net.nonworkspace.demo.batch.entity.BatchJobExecutionContext;

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

    public static Schema getSchema() {
        return new Schema<>().type("object")
            .title("Batch Job Execution")
            .addProperty("jobExecutionId", new NumberSchema())
            .addProperty("version", new NumberSchema())
            .addProperty("createTime", new DateTimeSchema())
            .addProperty("startTime", new DateTimeSchema())
            .addProperty("endTime", new DateTimeSchema())
            .addProperty("status", new StringSchema())
            .addProperty("exitCode", new StringSchema())
            .addProperty("exitMessage", new StringSchema())
            .addProperty("lastUpdated", new DateTimeSchema())
            .addProperty("batchJobExecutionContext", BatchJobExecutionContextDto.getSchema())
            .addProperty("batchJobExecutionParams", BatchJobExecutionParamDto.getSchema())
            .addProperty("batchJobInstance", BatchJobInstanceDto.getSchema());
    }
}
