package net.nonworkspace.demo.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.domain.RecruitType;
import net.nonworkspace.demo.domain.dto.common.CommonResponseDto;
import net.nonworkspace.demo.domain.dto.recruit.CompanyDto;
import net.nonworkspace.demo.domain.dto.recruit.RecruitDto;
import net.nonworkspace.demo.domain.dto.recruit.RecruitViewDto;
import net.nonworkspace.demo.service.RecruitService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "RECRUIT API", description = "구인 정보를 처리하는 API 설명")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/recruit")
@Slf4j
public class RecruitController {

    private final RecruitService recruitService;

    @GetMapping("")
    public List<RecruitDto> getRecruitPage(
        @RequestParam(name = "pageNo", required = true, defaultValue = "1") int pageNo,
        @RequestParam(name = "pageSize", required = false, defaultValue = "6") int pageSize,
        @RequestParam(name = "type", required = false) RecruitType type) {
        return recruitService.getPage(type, pageNo, pageSize);
    }

    @GetMapping("/all")
    public List<RecruitDto> getAllRecruit(
        @RequestParam(name = "type", required = false) RecruitType type) {
        return recruitService.getAllRecruit(type);
    }

    @GetMapping("/{recruitId}")
    public RecruitViewDto getRecruit(@PathVariable Long recruitId) {
        return recruitService.getRecruit(recruitId);
    }

    @PostMapping("")
    public ResponseEntity<CommonResponseDto> registerRecruit(
        @RequestBody RecruitViewDto recruitViewDto) {
        try {
            Long recruitId = recruitService.registerRecruit(recruitViewDto);
            return ResponseEntity.ok(new CommonResponseDto(recruitId, "등록 성공"));
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return ResponseEntity.ok(new CommonResponseDto(-1L, "등록 실패: " + e.getMessage()));
        }
    }

    @PostMapping("/company")
    public ResponseEntity<CommonResponseDto> registerCompany(@RequestBody CompanyDto companyDto) {
        Long companyId = recruitService.registerCompany(companyDto);
        return ResponseEntity.ok(new CommonResponseDto(companyId, "등록 성공"));
    }

    @PutMapping("/{recruitId}")
    public ResponseEntity<CommonResponseDto> modifyRecruit(
        @RequestBody RecruitViewDto recruitViewDto) {
        try {
            Long recruitId = recruitService.modifyRecruit(recruitViewDto);
            return ResponseEntity.ok(new CommonResponseDto(recruitId, "수정 성공"));
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return ResponseEntity.ok(new CommonResponseDto(-1L, "수정 실패: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{recruitId}")
    public ResponseEntity<CommonResponseDto> deleteRecruit(@PathVariable Long recruitId) {
        try {
            return ResponseEntity.ok(
                new CommonResponseDto(recruitService.deleteRecruit(recruitId), "삭제 성공"));
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return ResponseEntity.ok(new CommonResponseDto(-1L, "삭제 실패: " + e.getMessage()));
        }
    }

    @GetMapping("/companies")
    public List<CompanyDto> getCompanies() {
        return recruitService.getCompanies();
    }
}
