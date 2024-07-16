package net.nonworkspace.demo.domain.dto;

import lombok.Data;
import net.nonworkspace.demo.batch.entity.BatchJobExecutionParam;

@Data
public class BatchJobExecutionParamDto {

    public BatchJobExecutionParamDto(BatchJobExecutionParam batchJobExecutionParam) {
        this.setJobExecutionId(batchJobExecutionParam.getId().getJobExecutionId());
        this.setParameterName(batchJobExecutionParam.getId().getParameterName());
        this.setParameterType(batchJobExecutionParam.getParameterType());
        this.setParameterValue(batchJobExecutionParam.getParameterValue());
        this.setIdentifying(batchJobExecutionParam.getIdentifying());
    }

    private Long jobExecutionId;

    private String parameterName;

    private String parameterType;

    private String parameterValue;

    private String identifying;
}
