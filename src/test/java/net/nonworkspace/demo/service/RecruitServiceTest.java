package net.nonworkspace.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.domain.RecruitType;
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

    // @Test
    @DisplayName("7건 넣고 페이지 0번 조회해서 조회해서 6건 나오면 성공")
    void getPage() {
    }

    @Test
    void registerRecruit() throws Exception {
        // given
        CompanyDto companyDto = new CompanyDto(
            null,
            "NewTek Solutions",
            "NewTek Solutions is a leading technology company specializing in web development and digital solutions. We pride ourselves on delivering high-quality products and services to our clients while fostering a collaborative and innovative work environment.",
            "aaa@aaa.ccc",
            "123123123"
        );
        Long companyId = recruitService.registerCompany(companyDto);

        RecruitViewDto recruitViewDto = new RecruitViewDto(
            null,
            RecruitType.FULL_TIME,
            "Senior React Developer",
            "We are seeking a talented Front-End Developer to join our team in Boston, MA. The ideal candidate will have strong skills in HTML, CSS, and JavaScript, with experience working with modern JavaScript frameworks such as React or Angular.",
            "$70K - $80K",
            "Boston, MA",
            new CompanyDto(recruitService.getCompany(companyId))
        );

        // when
        Long recruitId = recruitService.registerRecruit(recruitViewDto);
        log.debug("recruitId: {}", recruitId);

        // then
        assertThat(recruitId).isNotNull();
        assertThat(companyId).isEqualTo(recruitViewDto.company().companyId());
    }

    @Test
    @DisplayName("없는 회사 정보로 리크루트를 등록할 때, DATA_NOT_FOUND 예외 발생하면 성공")
    void registerRecruitCompanyNotFound() {
        // given
        RecruitViewDto recruitViewDto = new RecruitViewDto(
            null,
            RecruitType.FULL_TIME,
            "Senior React Developer",
            "We are seeking a talented Front-End Developer to join our team in Boston, MA. The ideal candidate will have strong skills in HTML, CSS, and JavaScript, with experience working with modern JavaScript frameworks such as React or Angular.",
            "$70K - $80K",
            "Boston, MA",
            new CompanyDto(
                -99999L,
                "test company",
                "This is test company",
                "aaa@sdas.coo",
                "123123123"
            )
        );

        // when
        Exception registerRecruitException = assertThrows(CommonBizException.class,
            () -> recruitService.registerRecruit(recruitViewDto));

        // then
        assertThat(registerRecruitException.getMessage()).isEqualTo(CommonBizExceptionCode.DATA_NOT_FOUND.getMessage());
    }

    // @Test
    void getCompany() {
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
        // then
        assertThat(companyId).isNotNull();
    }

    @Test
    @DisplayName("회사, 리크루트 등록하고 리크루트 id 으로 조회되면 성공")
    void getRecruit() throws Exception {
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
            "$70K - $80K",
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
}