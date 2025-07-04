package net.nonworkspace.demo.service;

import lombok.RequiredArgsConstructor;
import net.nonworkspace.demo.domain.code.RecruitType;
import net.nonworkspace.demo.domain.dto.common.ListResponse;
import net.nonworkspace.demo.domain.dto.recruit.CompanyDto;
import net.nonworkspace.demo.domain.dto.recruit.RecruitDto;
import net.nonworkspace.demo.domain.dto.recruit.RecruitViewDto;
import net.nonworkspace.demo.domain.entity.Company;
import net.nonworkspace.demo.domain.entity.Recruit;
import net.nonworkspace.demo.exception.common.CommonBizException;
import net.nonworkspace.demo.exception.common.CommonBizExceptionCode;
import net.nonworkspace.demo.repository.CompanyRepository;
import net.nonworkspace.demo.repository.JpaRecruitRepository;
import net.nonworkspace.demo.repository.RecruitRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitService {

    private final JpaRecruitRepository jpaRecruitRepository;

    private final RecruitRepository recruitRepository;

    private final CompanyRepository companyRepository;

    public ListResponse<RecruitDto> getPage(RecruitType type, int pageNo, int pageSize) {
        Sort sort = Sort.by("createDate").descending();
        Pageable pageable = PageRequest.of((pageNo - 1), pageSize, sort);   // pageNo : zero-based
        Page<Recruit> recruits;
        if (type != null) {
            recruits = jpaRecruitRepository.findByType(type, pageable);
        } else {
            recruits = jpaRecruitRepository.findAll(pageable);
        }
        List<RecruitDto> result = new ArrayList<>();
        recruits.stream().forEach(r -> result.add(new RecruitDto(r)));
        return new ListResponse<>(result);
    }

    @Transactional
    public Long registerRecruit(RecruitViewDto recruitViewDto) {
        Long companyId = registerCompany(recruitViewDto.company());

        Recruit recruit = Recruit.createRecruit(
                recruitViewDto.type(),
                recruitViewDto.title(),
                recruitViewDto.description(),
                recruitViewDto.salary(),
                recruitViewDto.location(),
                companyRepository.findById(companyId).orElseThrow(() -> new CommonBizException(
                        CommonBizExceptionCode.DATA_NOT_FOUND))
        );
        jpaRecruitRepository.save(recruit);
        return recruit.getId();
    }

    public Company getCompany(Long companyId) {
        return companyRepository.findById(companyId).orElseThrow(() -> new CommonBizException(
                CommonBizExceptionCode.DATA_NOT_FOUND));
    }

    @Transactional
    public Long registerCompany(CompanyDto companyDto) {
        Company company = Company.createCompany(
                companyDto.companyName(),
                companyDto.description(),
                companyDto.contactEmail(),
                companyDto.contactPhone(),
                null
        );
        companyRepository.save(company);
        return company.getId();
    }

    public RecruitViewDto getRecruit(Long recruitId) {
        Recruit result = jpaRecruitRepository.findById(recruitId)
                .orElseThrow(() -> new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND));
        return new RecruitViewDto(result);
    }

    public ListResponse<RecruitDto> getAllRecruit(RecruitType type) {
        List<Recruit> recruits;
        if (type != null) {
            recruits = jpaRecruitRepository.findByTypeOrderByCreateDateDesc(type);
        } else {
            recruits = recruitRepository.findAllRecruitWithCompany();
        }
        List<RecruitDto> result = new ArrayList<>();
        recruits.stream().forEach(r -> result.add(new RecruitDto(r)));
        return new ListResponse<>(result);
    }

    @Transactional
    public Long modifyRecruit(RecruitViewDto recruitViewDto) {
        Company company = companyRepository.findById(recruitViewDto.company().companyId())
                .orElseThrow(() -> new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND));
        company.modifyCompany(recruitViewDto.company());
        // companyRepository.save(company); // why done right? : 변경 감지

        Recruit recruit = jpaRecruitRepository.findById(recruitViewDto.recruitId())
                .orElseThrow(() -> new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND));
        recruit.modifyRecruit(recruitViewDto);
        //  recruitRepository.save(recruit); // why done right? : 변경 감지

        return recruit.getId();
    }

    @Transactional
    public Long deleteRecruit(Long recruitId) {
        Recruit recruit = jpaRecruitRepository.findById(recruitId)
                .orElseThrow(() -> new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND));
        Company company = companyRepository.findById(recruit.getCompany().getId())
                .orElseThrow(() -> new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND));
        jpaRecruitRepository.delete(recruit);
        companyRepository.delete(company);
        return 1L;
    }

    public ListResponse<CompanyDto> getCompanies() {
        List<CompanyDto> result = new ArrayList<>();
        companyRepository.findAll().stream().forEach(c -> result.add(new CompanyDto(c)));
        return new ListResponse<>(result);
    }
}
