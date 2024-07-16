package net.nonworkspace.demo.batch.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "batch_step_execution")
public class BatchStepExecution {

    @Id
    @Column(name = "step_execution_id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "version", nullable = false)
    private Long version;

    @Size(max = 100)
    @NotNull
    @Column(name = "step_name", nullable = false, length = 100)
    private String stepName;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_execution_id", nullable = false)
    private BatchJobExecution jobExecution;

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

    @Column(name = "commit_count")
    private Long commitCount;

    @Column(name = "read_count")
    private Long readCount;

    @Column(name = "filter_count")
    private Long filterCount;

    @Column(name = "write_count")
    private Long writeCount;

    @Column(name = "read_skip_count")
    private Long readSkipCount;

    @Column(name = "write_skip_count")
    private Long writeSkipCount;

    @Column(name = "process_skip_count")
    private Long processSkipCount;

    @Column(name = "rollback_count")
    private Long rollbackCount;

    @Size(max = 2500)
    @Column(name = "exit_code", length = 2500)
    private String exitCode;

    @Size(max = 2500)
    @Column(name = "exit_message", length = 2500)
    private String exitMessage;

    @Column(name = "last_updated")
    private Instant lastUpdated;

}