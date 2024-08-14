package net.nonworkspace.demo.domain.dto.batch;

import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
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

    public static Schema getSchema() {
        return new Schema<>().type("object")
            .title("BatchJobInstance")
            .addProperty("jobExecutionId", new NumberSchema())
            .addProperty("jobInstanceId", new NumberSchema())
            .addProperty("version", new NumberSchema())
            .addProperty("jobName", new StringSchema())
            .addProperty("jobKey", new StringSchema());
    }
}
