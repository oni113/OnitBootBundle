package net.nonworkspace.demo.service;

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

@Service
@RequiredArgsConstructor
public class RecruitService {

    private final RecruitRepository recruitRepository;

    private final CompanyRepository companyRepository;

    public List<RecruitDto> getPage(RecruitType type, int pageNo, int pageSize) {
        Sort sort = Sort.by("createDate").descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Recruit> recruits;
        if (type != null) {
            recruits = recruitRepository.findByType(type, pageable);
        } else {
            recruits = recruitRepository.findAll(pageable);
        }
        List<RecruitDto> result = new ArrayList<>();
        recruits.stream().forEach(rc -> result.add(new RecruitDto(rc)));
        return result;
    }

    public Long registerRecruit(RecruitViewDto recruitViewDto) {
        Long companyId = registerCompany(recruitViewDto.company());

        Recruit recruit = new Recruit();
        recruit.setType(recruitViewDto.type());
        recruit.setTitle(recruitViewDto.title());
        recruit.setDescription(recruitViewDto.description());
        recruit.setSalary(recruitViewDto.salary());
        recruit.setLocation(recruitViewDto.location());
        recruit.setCompany(
            companyRepository.findById(companyId).orElseThrow(() -> new CommonBizException(
                CommonBizExceptionCode.DATA_NOT_FOUND)));
        recruitRepository.save(recruit);
        return recruit.getId();
    }

    public Company getCompany(Long companyId) {
        return companyRepository.findById(companyId).orElseThrow(() -> new CommonBizException(
            CommonBizExceptionCode.DATA_NOT_FOUND));
    }

    public Long registerCompany(CompanyDto companyDto) {
        Company company = new Company();
        company.setCompanyName(companyDto.companyName());
        company.setDescription(companyDto.description());
        company.setContactEmail(companyDto.contactEmail());
        company.setContactPhone(companyDto.contactPhone());
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
        recruits.stream().forEach(rc -> result.add(new RecruitDto(rc)));
        return result;
    }
}
