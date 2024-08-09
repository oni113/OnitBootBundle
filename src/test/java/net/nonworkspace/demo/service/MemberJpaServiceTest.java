package net.nonworkspace.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.domain.Member;
import net.nonworkspace.demo.domain.dto.member.MemberDto;
import net.nonworkspace.demo.domain.dto.member.MemberViewDto;
import net.nonworkspace.demo.domain.dto.user.JoinRequestDto;
import net.nonworkspace.demo.exception.common.CommonBizException;
import net.nonworkspace.demo.exception.common.CommonBizExceptionCode;
import net.nonworkspace.demo.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
@Slf4j
public class MemberJpaServiceTest {

    @Autowired
    private MemberJpaService memberJpaService;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void cleanData() {

    }

    @Test
    @DisplayName("이름에 '테스트'가 들어간 회원 데이터가 2건이어야 성공")
    void testGetMemberList() {
        // given
        JoinRequestDto member1 = new JoinRequestDto(
            "테스트1",
            "test1@test.ttt",
            "Rkskekfk1!",
            "Rkskekfk1!"
        );
        memberJpaService.join(member1);

        JoinRequestDto member2 = new JoinRequestDto(
            "테스트2",
            "test2@test.ttt",
            "Rkskekfk1!",
            "Rkskekfk1!"
        );
        memberJpaService.join(member2);

        // when
        List<Member> result = memberJpaService.findMembers("테스트");

        // then
        assertThat(result).isNotNull();
        assertEquals(result.size(), 2, "조회 결과가 2 건이어야 한다");
        assertEquals(result.stream().findFirst().get().getName(), "테스트1",
            "첫번째 데이터의 회원 이름이 '테스트1'이여하 한다");
    }

    @Test
    @DisplayName("'중복된 이메일 값 입력' 예외가 발생해야 성공")
    void testJoinException() {
        // given
        JoinRequestDto member1 = new JoinRequestDto(
            "테스트1",
            "test1@test.ttt",
            "Rkskekfk1!",
            "Rkskekfk1!"
        );
        memberJpaService.join(member1);

        JoinRequestDto member2 = new JoinRequestDto(
            "테스트2",
            "test1@test.ttt",
            "Rkskekfk1!",
            "Rkskekfk1!"
        );

        // when
        Exception e = assertThrows(CommonBizException.class, () -> memberJpaService.join(member2));

        // then
        assertThat(e.getMessage())
            .isEqualTo(
                new CommonBizException(CommonBizExceptionCode.DATA_EMAIL_DUPLICATE).getMessage());
    }

    @Test
    @DisplayName("조회 결과가 null 이 아니고, 조회한 결과의 이메일 값이 등록한 이메일 값과 일치해야 성공")
    void testGetMember() {
        // given
        JoinRequestDto member1 = getTestJoinRequestDto();
        Long memberId = memberJpaService.join(member1);

        // when
        MemberViewDto result = memberJpaService.findMember(memberId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo(member1.email());
    }

    @Test
    @DisplayName("회원 ID 값이 존재하지 않는 값 (-999) 이므로 '데이터가 존재하지 않습니다' 예외가 발생해야 성공")
    void testGetMemberException() {
        // given
        Long nomemberId = -999L;

        // when
        Exception e = assertThrows(CommonBizException.class,
            () -> memberJpaService.findMember(nomemberId));

        // then
        assertEquals(new CommonBizException(CommonBizExceptionCode.NOT_EXIST_MEMBER).getMessage(),
            e.getMessage());
    }

    @Test
    @DisplayName("등록한 회원 ID 값과 조회한 회원 ID 값이 일치해야 성공")
    void testJoin() {
        // given
        JoinRequestDto member1 = getTestJoinRequestDto();

        // when
        Long newMemberId = memberJpaService.join(member1);
        log.info("newMemberId: {}", newMemberId);
        MemberViewDto lastMember = memberJpaService.findMember(newMemberId);
        Long lastMemberId = lastMember.memberId();
        log.info("lastMemberId: {}", lastMemberId);

        // then
        assertEquals(newMemberId, lastMemberId, "등록한 회원 ID 값과 조회한 회원 ID 값이 일치해야 한다.");
    }

    @Test
    @DisplayName("입력한 이름 값과 수정 결과의 이름 값이 일치해야 성공")
    void testEdit() {
        // given
        JoinRequestDto member1 = getTestJoinRequestDto();
        Long memberId1 = memberJpaService.join(member1);

        // when
        Member member2 = new Member();
        member2.setMemberId(memberId1);
        member2.setName("테스트2");

        // then
        assertThat(member2.getName()).isEqualTo(
            Optional.of(memberJpaService.editMember(member2)).get().getName());
    }

    @Test
    @DisplayName("수정할 데이터의 회원 ID 값이 존재하지 않는 값 (-999) 이므로, '데이터가 존재하지 않습니다' 예외가 발생해야 성공")
    void testEditException() {
        // given
        Member noMember = new Member();
        noMember.setMemberId(-999L);
        noMember.setName("테스트1");

        // when
        Exception e = assertThrows(CommonBizException.class, () ->
            memberJpaService.editMember(noMember));

        // then
        assertThat(e.getMessage())
            .isEqualTo(new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND).getMessage());
    }

    @Test
    @DisplayName("삭제한 데이터를 다시 조회 시도했으므로, '데이터가 존재하지 않습니다' 예외가 발생해야 성공")
    void testDelete() {
        // given
        JoinRequestDto member1 = getTestJoinRequestDto();
        Long memberId = memberJpaService.join(member1);
        log.info("memberId for delete test: {}", memberId);

        // when
        memberJpaService.deleteMember(memberId);
        Exception e = assertThrows(CommonBizException.class, () ->
            memberJpaService.findMember(memberId));

        // then
        assertThat(e.getMessage())
            .isEqualTo(
                new CommonBizException(CommonBizExceptionCode.NOT_EXIST_MEMBER).getMessage());
        assertThat(memberRepository.findPasswordByMemberId(memberId).size()).as(
            "삭제된 회원의 패스워드 데이터를 조회한 결과가 0 건이어야 한다").isEqualTo(0);
        assertThat(memberRepository.findRoleByMemberId(memberId).size()).as(
            "삭제된 회원의 권한 데이터를 조회한 결과가 0 건이어야 한다").isEqualTo(0);
    }

    @Test
    @DisplayName("이름에 \"test\" 들어간 회원 7건 넣고 페이지 1, 2 조회해서 중복 발생하지 않으면 성공")
    void getPage() {
        // given
        JoinRequestDto member;
        for (int i = 0; i < 7; i++) {
            member = getTestJoinRequestDto("test" + i, "test" + +i + "@test.too");
            memberJpaService.join(member);
        }

        // when
        List<MemberDto> page1 = memberJpaService.getPage("test", 1, 6);
        List<MemberDto> page2 = memberJpaService.getPage("test", 2, 6);

        // then
        assertThat(page1.size()).as("페이지 1의 데이터 갯수는 6 과 같아야 한다").isEqualTo(6);
        assertThat(page2.size()).as("페이지 2의 데이터 갯수는 1 과 같아야 한다").isEqualTo(1);
        page1.stream().filter(p1 -> p1.memberId().equals(page2.get(0).memberId())).findFirst()
            .ifPresent(p -> {
                log.error("첫번째 페이지 데이터 중에 두번째 페이지 데이터가 존재하면 안된다");
                fail();
            });
    }

    private JoinRequestDto getTestJoinRequestDto() {
        return new JoinRequestDto(
            "테스트1",
            "test1@test.ttt",
            "Rkskekfk1!",
            "Rkskekfk1!"
        );
    }

    private JoinRequestDto getTestJoinRequestDto(String name, String email) {
        return new JoinRequestDto(
            name,
            email,
            "Rkskekfk1!",
            "Rkskekfk1!"
        );
    }
}
