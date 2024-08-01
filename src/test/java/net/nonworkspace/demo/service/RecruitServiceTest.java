package net.nonworkspace.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.domain.Company;
import net.nonworkspace.demo.domain.RecruitType;
import net.nonworkspace.demo.domain.Salary;
import net.nonworkspace.demo.domain.dto.recruit.CompanyDto;
import net.nonworkspace.demo.domain.dto.recruit.RecruitDto;
import net.nonworkspace.demo.domain.dto.recruit.RecruitViewDto;
import net.nonworkspace.demo.exception.common.CommonBizException;
import net.nonworkspace.demo.exception.common.CommonBizExceptionCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Slf4j
class RecruitServiceTest {

    @Autowired
    private RecruitService recruitService;

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("7건 넣고 페이지 1 페이지 조회해서 조회해서 6건, 2 페이지 조회해서 1건 나오면 성공 & 1 페이지 데이터에 2 페이지 데이터 있으면 실패")
    void getPage() {
        RecruitViewDto testNewRecruitViewDto = getTestRecruitViewDto();
        for (int i = 0; i < 7; i++) {
            recruitService.registerRecruit(testNewRecruitViewDto);
        }
        List<RecruitDto> firstPageResult = recruitService.getPage(null, 1, 6);
        assertThat(firstPageResult.size()).isEqualTo(6);
        List<RecruitDto> secondPageResult = recruitService.getPage(null, 2, 6);
        assertThat(secondPageResult.size()).isEqualTo(1);
        firstPageResult.stream().filter(f -> f.recruitId().equals(secondPageResult.get(0).recruitId()))
            .findFirst().ifPresent(f -> {
                log.error("페이지 데이터 중복 발생!");
                fail();
            });
    }

    @Test
    void registerRecruit() {
        // given
        RecruitViewDto testNewRecruitViewDto = getTestRecruitViewDto();

        // when
        Long recruitId = recruitService.registerRecruit(testNewRecruitViewDto);
        log.debug("recruitId: {}", recruitId);
        RecruitViewDto result = recruitService.getRecruit(recruitId);

        // then
        assertThat(recruitId).isNotNull();
        assertThat(result.company().companyId()).isNotNull();
    }

    @Test
    void registerCompany() {
        // given
        CompanyDto companyDto = new CompanyDto(
            null,
            "NewTek Solutions",
            "NewTek Solutions is a leading technology company specializing in web development and digital solutions. We pride ourselves on delivering high-quality products and services to our clients while fostering a collaborative and innovative work environment.",
            "aaa@aaa.ccc",
            "123123123"
        );

        // when
        Long companyId = recruitService.registerCompany(companyDto);
        log.debug("companyId: {}", companyId);
        Company company = recruitService.getCompany(companyId);
        // then
        assertThat(companyId).isNotNull();
        assertThat(company.getCreateDate()).isNotNull();
    }

    @Test
    @DisplayName("회사, 리크루트 등록하고 리크루트 id 으로 조회되면 성공")
    void getRecruit() {
        // given
        CompanyDto companyDto = new CompanyDto(
            null,
            "NewTek Solutions",
            "NewTek Solutions is a leading technology company specializing in web development and digital solutions. We pride ourselves on delivering high-quality products and services to our clients while fostering a collaborative and innovative work environment.",
            "aaa@aaa.ccc",
            "123123123"
        );

        RecruitViewDto recruitViewDto = new RecruitViewDto(
            null,
            RecruitType.FULL_TIME,
            "Senior React Developer",
            "We are seeking a talented Front-End Developer to join our team in Boston, MA. The ideal candidate will have strong skills in HTML, CSS, and JavaScript, with experience working with modern JavaScript frameworks such as React or Angular.",
            Salary.$125K_150K,
            "Boston, MA",
            companyDto
        );

        Long recruitId = recruitService.registerRecruit(recruitViewDto);
        log.debug("getRecruit() recruitId: {}", recruitId);

        // when
        RecruitViewDto result = recruitService.getRecruit(recruitId);
        log.debug("RecruitViewDto: {}", result.toString());
        log.debug("getRecruit() companyId: {}", result.company().companyId());

        // then
        assertThat(result).isNotNull();
        assertThat(result.recruitId()).isEqualTo(recruitId);
        assertThat(result.company().companyId()).isNotNull();
    }

    @Test
    void modifyRecruit() {
        // given
        RecruitViewDto testNewRecruitViewDto = getTestRecruitViewDto();
        Long recruitId = recruitService.registerRecruit(testNewRecruitViewDto);
        log.debug("registered recruitId: {}", recruitId);

        // when
        RecruitViewDto result = recruitService.getRecruit(recruitId);

        CompanyDto editCompanyDto = new CompanyDto(
            result.company().companyId(),
            "Edit Company Name",
            "Edit Company Description",
            "edit@edit.ccc",
            "000000"
        );

        RecruitViewDto editRecruitDto = new RecruitViewDto(
            result.recruitId(),
            RecruitType.REMOTE,
            "Edit Recruit Title",
            "Edit Recruit Description",
            Salary.UNDER_$50K,
            "Edit Recruit Location",
            editCompanyDto
        );

        Long editRecruitId = recruitService.modifyRecruit(editRecruitDto);
        log.debug("modified RecruitId: {}", editRecruitId);
        RecruitViewDto editResult = recruitService.getRecruit(editRecruitId);

        // then
        assertThat(recruitId).isEqualTo(editRecruitId);
        assertThat(editResult.title()).isEqualTo(editRecruitDto.title());
        assertThat(editResult.company().companyId()).isEqualTo(editCompanyDto.companyId());
        assertThat(editResult.company().companyName()).isEqualTo(editCompanyDto.companyName());
    }

    @Test
    void deleteRecruit() {
        // given
        RecruitViewDto testNewRecruitViewDto = getTestRecruitViewDto();
        Long recruitId = recruitService.registerRecruit(testNewRecruitViewDto);
        log.debug("for delete test recruitId: {}", recruitId);
        Long companyId = recruitService.getRecruit(recruitId).company().companyId();
        log.debug("for delete test companyId: {}", companyId);

        // when
        Long deleteCount = recruitService.deleteRecruit(recruitId);

        // then
        assertThat(deleteCount).isEqualTo(1L);
        Exception companyException = assertThrows(CommonBizException.class,
            () -> recruitService.getCompany(companyId));
        assertThat(companyException).hasMessageContaining(
            CommonBizExceptionCode.DATA_NOT_FOUND.getMessage());
        Exception recruitException = assertThrows(CommonBizException.class,
            () -> recruitService.getRecruit(recruitId));
        assertThat(recruitException).hasMessageContaining(
            CommonBizExceptionCode.DATA_NOT_FOUND.getMessage());
    }

    private static RecruitViewDto getTestRecruitViewDto() {
        CompanyDto companyDto = new CompanyDto(
            null,
            "NewTek Solutions",
            "NewTek Solutions is a leading technology company specializing in web development and digital solutions. We pride ourselves on delivering high-quality products and services to our clients while fostering a collaborative and innovative work environment.",
            "aaa@aaa.ccc",
            "123123123"
        );

        return new RecruitViewDto(
            null,
            RecruitType.FULL_TIME,
            "Senior React Developer",
            "We are seeking a talented Front-End Developer to join our team in Boston, MA. The ideal candidate will have strong skills in HTML, CSS, and JavaScript, with experience working with modern JavaScript frameworks such as React or Angular.",
            Salary.$70K_80K,
            "Boston, MA",
            companyDto
        );
    }
}