package net.nonworkspace.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.nonworkspace.demo.batch.entity.BatchJobExecution;
import net.nonworkspace.demo.domain.dto.BatchJobExecutionDto;
import net.nonworkspace.demo.service.BatchJobMetaInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BATCH JOB INFO API", description = "배치 작업 정보 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/batch")
public class BatchJobMetaInfoController {

    private final BatchJobMetaInfoService batchJobMetaInfoService;

    @Operation(summary = "배치 작업 목록 조회", description = "배치 작업 목록 데이터 전체를 조회한다.")
    @GetMapping("")
    public List<BatchJobExecutionDto> getBatchJobList() {
        return batchJobMetaInfoService.getBatchJobExecutions();
    }

    @Operation(summary = "배치 작업 목록 조회 (page)", description = "페이징 된 배치 작업 목록 데이터를 조회한다.")
    @GetMapping("/page/{pageNo}")
    public List<BatchJobExecutionDto> getBatchJobPage(
        @PathVariable(name = "pageNo", required = true) int pageNo,
        @RequestParam(name = "exitCode", required = false) String exitCode) {
        return batchJobMetaInfoService.getBatchJobExecutions(pageNo, exitCode);
    }

    @Operation(summary = "배치 작업 단건 조회", description = "배치 작업 정보를 조회한다.")
    @GetMapping("/{id}")
    public BatchJobExecutionDto getBatchJob(@PathVariable(name = "id") Long id) {
        return batchJobMetaInfoService.getBatchJobExecution(id);
    }
}
