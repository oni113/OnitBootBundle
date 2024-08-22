package net.nonworkspace.demo.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.batch.entity.BatchJobExecution;
import net.nonworkspace.demo.domain.dto.batch.BatchJobExecutionDto;
import net.nonworkspace.demo.exception.common.CommonBizException;
import net.nonworkspace.demo.exception.common.CommonBizExceptionCode;
import net.nonworkspace.demo.repository.BatchJobExecutionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BatchJobMetaInfoService {

    private final BatchJobExecutionRepository batchJobExecutionRepository;

    public List<BatchJobExecutionDto> getBatchJobExecutions() {
        List<BatchJobExecutionDto> result = new ArrayList<>();
        // batchJobExecutionRepository.findAll().parallelStream().forEach(b -> result.add(new BatchJobExecutionDto(b)));    // transaction error!!
        batchJobExecutionRepository.findAll().stream()
            .forEach(b -> result.add(new BatchJobExecutionDto(b)));

        return result;
    }

    public List<BatchJobExecutionDto> getBatchJobExecutions(int pageNo, String exitCode) {
        Sort sort = Sort.by("createTime").descending();
        Pageable pageable = PageRequest.of(pageNo, 10, sort);
        Page<BatchJobExecution> data;
        if (exitCode == null || exitCode.isEmpty()) {
            data = batchJobExecutionRepository.findAll(pageable);
        } else {
            data = batchJobExecutionRepository.findByExitCodeContains(exitCode, pageable);
        }
        List<BatchJobExecutionDto> result = new ArrayList<>();
        // data.stream().forEach(b -> result.add(new BatchJobExecutionDto(b)));
        data.forEach(b -> result.add(new BatchJobExecutionDto(b)));
        return result;
    }

    public BatchJobExecutionDto getBatchJobExecution(Long id) {
        BatchJobExecution result = batchJobExecutionRepository.findById(id)
            .orElseThrow(() -> new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND));
        return new BatchJobExecutionDto(result);
    }
}
