package net.nonworkspace.demo.domain.dto;

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
}
