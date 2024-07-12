package net.nonworkspace.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.nonworkspace.demo.domain.Job;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "JOB API", description = "구인 정보 API 설명")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/job")
public class JobController {

    @Operation(summary = "구인 정보 조회", description = "구인 전체 데이터를 리스트로 조회한다.")
    @GetMapping("")
    public List<Job> getJobList() {
        return null;
    }
}
