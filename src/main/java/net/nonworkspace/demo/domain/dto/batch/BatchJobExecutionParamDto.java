package net.nonworkspace.demo.domain.dto.batch;

import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import net.nonworkspace.demo.batch.entity.BatchJobExecutionParam;

public record BatchJobExecutionParamDto(
    Long jobExecutionId,
    String parameterName,
    String parameterType,
    String parameterValue,
    String identifying
) {

    public BatchJobExecutionParamDto(BatchJobExecutionParam batchJobExecutionParam) {
        this(
            batchJobExecutionParam.getId().getJobExecutionId(),
            batchJobExecutionParam.getId().getParameterName(),
            batchJobExecutionParam.getParameterType(),
            batchJobExecutionParam.getParameterValue(),
            batchJobExecutionParam.getIdentifying()
        );
    }

    public static Schema getSchema() {
        return new Schema<>().type("object")
            .title("Batch Job Execution Parameter")
            .addProperty("jobExecutionId", new NumberSchema())
            .addProperty("parameterName", new StringSchema())
            .addProperty("parameterType", new StringSchema())
            .addProperty("parameterValue", new StringSchema())
            .addProperty("identifying", new StringSchema());
    }
}
