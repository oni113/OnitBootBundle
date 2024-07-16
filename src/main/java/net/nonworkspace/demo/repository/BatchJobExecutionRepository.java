package net.nonworkspace.demo.repository;

import net.nonworkspace.demo.batch.entity.BatchJobExecution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchJobExecutionRepository extends
    JpaRepository<BatchJobExecution, Long> {

    public Page<BatchJobExecution> findByExitCodeContains(String exitCode,
        Pageable pageable);
}
