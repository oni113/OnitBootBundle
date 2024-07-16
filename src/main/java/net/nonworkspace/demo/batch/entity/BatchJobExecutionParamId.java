package net.nonworkspace.demo.batch.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

@Getter
@Setter
@Embeddable
public class BatchJobExecutionParamId implements java.io.Serializable {

    private static final long serialVersionUID = -5514366225155198739L;
    @NotNull
    @Column(name = "job_execution_id", nullable = false)
    private Long jobExecutionId;

    @Size(max = 100)
    @NotNull
    @Column(name = "parameter_name", nullable = false, length = 100)
    private String parameterName;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        BatchJobExecutionParamId entity = (BatchJobExecutionParamId) o;
        return Objects.equals(this.jobExecutionId, entity.jobExecutionId) &&
            Objects.equals(this.parameterName, entity.parameterName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobExecutionId, parameterName);
    }

}