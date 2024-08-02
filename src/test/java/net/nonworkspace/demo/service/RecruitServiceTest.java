package net.nonworkspace.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        assertEquals(firstPageResult.size(), 6, "첫번째 페이지의 데이터 갯수는 6이어야 한다");
        List<RecruitDto> secondPageResult = recruitService.getPage(null, 2, 6);
        assertEquals(secondPageResult.size(), 1, "두번째 페이지의 데이터 갯수는 1이어야 한다");
        firstPageResult.stream()
            .filter(f -> f.recruitId().equals(secondPageResult.get(0).recruitId()))
            .findFirst().ifPresent(f -> {
                log.error("첫번째 페이지 데이터 중에 두번째 페이지 데이터가 존재하면 안된다");
                fail();
            });
    }

    @Test
    @DisplayName("리크루트 1건 등록 후 조회한 결과에서 리크루트 식별자와 회사 식별자 값이 null 이 아니면 성공")
    void registerRecruit() {
        // given
        RecruitViewDto testNewRecruitViewDto = getTestRecruitViewDto();

        // when
        Long recruitId = recruitService.registerRecruit(testNewRecruitViewDto);
        log.debug("recruitId: {}", recruitId);
        RecruitViewDto result = recruitService.getRecruit(recruitId);

        // then
        assertNotNull(recruitId, "등록 결과로 리턴받은 recruitId 값이 null 이면 안된다");
        assertNotNull(result.company().companyId(), "조회 결과의 companyId 값이 null 이면 안된다");
    }

    @Test
    @DisplayName("회사 1건 등록 후 조회한 결과에서 회사 식별자 값과 등록일시 값이 null 이 아니면 성공")
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
        assertNotNull(companyId, "회사 식별자 값이 null 이 아니어야 한다");
        assertNotNull(company.getCreateDate(), "회사 등록일시 값이 null 이 아니어야 한다");
    }

    @Test
    @DisplayName("회사, 리크루트 등록하고 리크루트 id 으로 조회되면 성공")
    void getRecruit() {
        // given
        RecruitViewDto recruitViewDto = getTestRecruitViewDto();

        Long recruitId = recruitService.registerRecruit(recruitViewDto);
        log.debug("getRecruit() recruitId: {}", recruitId);

        // when
        RecruitViewDto result = recruitService.getRecruit(recruitId);
        log.debug("RecruitViewDto: {}", result.toString());
        log.debug("getRecruit() companyId: {}", result.company().companyId());

        // then
        assertThat(result).as("조회 결과가 null 이면 안된다").isNotNull();
        assertThat(result.recruitId()).as("조회 결과의 식별자 값이 등록 결과로 리턴한 recruitId 값과 동일해야 한다")
            .isEqualTo(recruitId);
        assertThat(result.company().companyId()).as("조회 결과의 회사 식별자 값이 null 이면 안된다").isNotNull();
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
        assertThat(recruitId).as("리크루트 식별자가 수정 완료한 결과로 리턴받은 식별자 값과 동일해야 한다")
            .isEqualTo(editRecruitId);
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

    private RecruitViewDto getTestRecruitViewDto() {

        return new RecruitViewDto(
            null,
            RecruitType.FULL_TIME,
            "Senior React Developer",
            "We are seeking a talented Front-End Developer to join our team in Boston, MA. The ideal candidate will have strong skills in HTML, CSS, and JavaScript, with experience working with modern JavaScript frameworks such as React or Angular.",
            Salary.$70K_80K,
            "Boston, MA",
            new CompanyDto(
                null,
                "NewTek Solutions",
                "NewTek Solutions is a leading technology company specializing in web development and digital solutions. We pride ourselves on delivering high-quality products and services to our clients while fostering a collaborative and innovative work environment.",
                "aaa@aaa.ccc",
                "123123123"
            )
        );
    }
}