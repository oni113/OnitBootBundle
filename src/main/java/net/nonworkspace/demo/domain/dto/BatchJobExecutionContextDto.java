package net.nonworkspace.demo.domain.dto;

import lombok.Data;
import net.nonworkspace.demo.batch.entity.BatchJobExecutionContext;

@Data
public class BatchJobExecutionContextDto {

    public BatchJobExecutionContextDto(BatchJobExecutionContext batchJobExecutionContext) {
        this.setJobExecutionId(batchJobExecutionContext.getId());
        this.setShortContext(batchJobExecutionContext.getShortContext());
        this.setSerializedContext(batchJobExecutionContext.getSerializedContext());
    }

    private Long jobExecutionId;

    private String shortContext;

    private String serializedContext;
}
