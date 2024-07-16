package net.nonworkspace.demo.batch.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "batch_job_execution")
public class BatchJobExecution {

    @Id
    @Column(name = "job_execution_id", nullable = false)
    private Long id;

    @Column(name = "version")
    private Long version;

    @NotNull
    @Column(name = "create_time", nullable = false)
    private Instant createTime;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Size(max = 10)
    @Column(name = "status", length = 10)
    private String status;

    @Size(max = 2500)
    @Column(name = "exit_code", length = 2500)
    private String exitCode;

    @Size(max = 2500)
    @Column(name = "exit_message", length = 2500)
    private String exitMessage;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    @OneToOne(mappedBy = "batchJobExecution")
    @JsonIgnoreProperties({"batchJobExecution"})
    private BatchJobExecutionContext batchJobExecutionContext;

    @OneToMany(mappedBy = "batchJobExecution")
    @JsonIgnoreProperties({"batchJobExecution"})
    private List<BatchJobExecutionParam> batchJobExecutionParams = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "job_instance_id")
    private BatchJobInstance batchJobInstance;
}