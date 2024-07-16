package net.nonworkspace.demo.batch.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "batch_job_execution_params")
public class BatchJobExecutionParam {

    @EmbeddedId
    private BatchJobExecutionParamId id;

    @Size(max = 100)
    @NotNull
    @Column(name = "parameter_type", nullable = false, length = 100)
    private String parameterType;

    @Size(max = 2500)
    @Column(name = "parameter_value", length = 2500)
    private String parameterValue;

    @NotNull
    @Column(name = "identifying", nullable = false, length = Integer.MAX_VALUE)
    private String identifying;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("jobExecutionId")
    @JoinColumn(name = "job_execution_id", referencedColumnName = "job_execution_id")
    private BatchJobExecution batchJobExecution;
}