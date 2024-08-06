package net.nonworkspace.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.nonworkspace.demo.domain.Company;
import net.nonworkspace.demo.domain.Recruit;
import net.nonworkspace.demo.domain.RecruitType;
import net.nonworkspace.demo.domain.dto.recruit.CompanyDto;
import net.nonworkspace.demo.domain.dto.recruit.RecruitDto;
import net.nonworkspace.demo.domain.dto.recruit.RecruitViewDto;
import net.nonworkspace.demo.exception.common.CommonBizException;
import net.nonworkspace.demo.exception.common.CommonBizExceptionCode;
import net.nonworkspace.demo.repository.CompanyRepository;
import net.nonworkspace.demo.repository.RecruitRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitService {

    private final RecruitRepository recruitRepository;

    private final CompanyRepository companyRepository;

    public List<RecruitDto> getPage(RecruitType type, int pageNo, int pageSize) {
        Sort sort = Sort.by("createDate").descending();
        Pageable pageable = PageRequest.of((pageNo - 1), pageSize, sort);   // pageNo : zero-based
        Page<Recruit> recruits;
        if (type != null) {
            recruits = recruitRepository.findByType(type, pageable);
        } else {
            recruits = recruitRepository.findAll(pageable);
        }
        List<RecruitDto> result = new ArrayList<>();
        recruits.stream().forEach(r -> result.add(new RecruitDto(r)));
        return result;
    }

    @Transactional
    public Long registerRecruit(RecruitViewDto recruitViewDto) {
        Long companyId = registerCompany(recruitViewDto.company());

        Recruit recruit = Recruit.createRecruit(
            null,
            recruitViewDto.type(),
            recruitViewDto.title(),
            recruitViewDto.description(),
            recruitViewDto.salary(),
            recruitViewDto.location(),
            companyRepository.findById(companyId).orElseThrow(() -> new CommonBizException(
                CommonBizExceptionCode.DATA_NOT_FOUND))
        );
        recruitRepository.save(recruit);
        return recruit.getId();
    }

    public Company getCompany(Long companyId) {
        return companyRepository.findById(companyId).orElseThrow(() -> new CommonBizException(
            CommonBizExceptionCode.DATA_NOT_FOUND));
    }

    @Transactional
    public Long registerCompany(CompanyDto companyDto) {
        Company company = Company.createCompany(
            null,
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
        Recruit result = recruitRepository.findById(recruitId)
            .orElseThrow(() -> new CommonBizException(
                CommonBizExceptionCode.DATA_NOT_FOUND));
        return new RecruitViewDto(result);
    }

    public List<RecruitDto> getAllRecruit(RecruitType type) {
        List<Recruit> recruits;
        if (type != null) {
            recruits = recruitRepository.findByTypeOrderByCreateDateDesc(type);
        } else {
            recruits = recruitRepository.findAllByOrderByCreateDateDesc();
        }
        List<RecruitDto> result = new ArrayList<>();
        recruits.stream().forEach(r -> result.add(new RecruitDto(r)));
        return result;
    }

    @Transactional
    public Long modifyRecruit(RecruitViewDto recruitViewDto) {
        Company company = companyRepository.findById(recruitViewDto.company().companyId())
            .orElseThrow(() -> new CommonBizException(
                CommonBizExceptionCode.DATA_NOT_FOUND));
        company.setCompanyName(recruitViewDto.company().companyName());
        company.setDescription(recruitViewDto.company().description());
        company.setContactEmail(recruitViewDto.company().contactEmail());
        company.setContactPhone(recruitViewDto.company().contactPhone());
        company.setUpdateDate(LocalDateTime.now());
        companyRepository.save(company);

        Recruit recruit = recruitRepository.findById(recruitViewDto.recruitId())
            .orElseThrow(() -> new CommonBizException(
                CommonBizExceptionCode.DATA_NOT_FOUND));
        recruit.setType(recruitViewDto.type());
        recruit.setTitle(recruitViewDto.title());
        recruit.setDescription(recruitViewDto.description());
        recruit.setSalary(recruitViewDto.salary());
        recruit.setLocation(recruitViewDto.location());
        recruit.setUpdateDate(LocalDateTime.now());
        recruitRepository.save(recruit);

        return recruit.getId();
    }

    @Transactional
    public Long deleteRecruit(Long recruitId) {
        Recruit recruit = recruitRepository.findById(recruitId)
            .orElseThrow(() -> new CommonBizException(
                CommonBizExceptionCode.DATA_NOT_FOUND));
        Company company = companyRepository.findById(recruit.getCompany().getId())
            .orElseThrow(() -> new CommonBizException(
                CommonBizExceptionCode.DATA_NOT_FOUND));
        recruitRepository.delete(recruit);
        companyRepository.delete(company);
        return 1L;
    }

    public List<CompanyDto> getCompanies() {
        List<CompanyDto> result = new ArrayList<>();
        companyRepository.findAll().stream().forEach(c -> result.add(new CompanyDto(c)));
        return result;
    }
}
