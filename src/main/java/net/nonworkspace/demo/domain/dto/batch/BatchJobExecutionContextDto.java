package net.nonworkspace.demo.domain.dto.batch;

import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
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

    public static Schema getSchema() {
        return new Schema<>().type("object")
            .title("Batch Job Execution Context")
            .addProperty("jobExecutionId", new NumberSchema())
            .addProperty("shortContext", new StringSchema())
            .addProperty("serializedContext", new StringSchema());
    }
}
