package net.nonworkspace.demo.batch.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "batch_job_instance")
public class BatchJobInstance {

    @Id
    @Column(name = "job_instance_id", nullable = false)
    private Long id;

    @Column(name = "version")
    private Long version;

    @Size(max = 100)
    @NotNull
    @Column(name = "job_name", nullable = false, length = 100)
    private String jobName;

    @Size(max = 32)
    @NotNull
    @Column(name = "job_key", nullable = false, length = 32)
    private String jobKey;

    @OneToMany(mappedBy = "batchJobInstance")
    @JsonIgnoreProperties({"batchJobInstance"})
    List<BatchJobExecution> batchJobExecutions = new ArrayList<BatchJobExecution>();
}