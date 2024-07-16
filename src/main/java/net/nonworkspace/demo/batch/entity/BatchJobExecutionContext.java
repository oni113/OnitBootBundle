package net.nonworkspace.demo.batch.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "batch_job_execution_context")
public class BatchJobExecutionContext {

    @Id
    @Column(name = "job_execution_id", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_execution_id", nullable = false)
    private BatchJobExecution batchJobExecution;

    @Size(max = 2500)
    @NotNull
    @Column(name = "short_context", nullable = false, length = 2500)
    private String shortContext;

    @Column(name = "serialized_context", length = Integer.MAX_VALUE)
    private String serializedContext;

}