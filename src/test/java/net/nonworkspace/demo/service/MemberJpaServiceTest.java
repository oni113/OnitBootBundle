package net.nonworkspace.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.domain.Member;
import net.nonworkspace.demo.domain.dto.JoinRequestDto;
import net.nonworkspace.demo.exception.common.CommonBizException;
import net.nonworkspace.demo.exception.common.CommonBizExceptionCode;
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
    // private MemberService memberService;
    private MemberJpaService memberService;

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
        memberService.join(member1);

        JoinRequestDto member2 = new JoinRequestDto(
            "테스트2",
            "test2@test.ttt",
            "Rkskekfk1!",
            "Rkskekfk1!"
        );
        memberService.join(member2);

        // when
        List<Member> result = memberService.findMembers("테스트");

        // then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.stream().findFirst().get().getName()).isEqualTo("테스트1");
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
        memberService.join(member1);

        JoinRequestDto member2 = new JoinRequestDto(
            "테스트2",
            "test1@test.ttt",
            "Rkskekfk1!",
            "Rkskekfk1!"
        );

        // when
        Exception e = assertThrows(CommonBizException.class, () -> memberService.join(member2));

        // then
        assertThat(e.getMessage())
            .isEqualTo(
                new CommonBizException(CommonBizExceptionCode.DATA_EMAIL_DUPLICATE).getMessage());
    }

    @Test
    @DisplayName("조회 결과가 null 이 아니고, 조회한 결과의 이메일 값이 등록한 이메일 값과 일치해야 성공")
    void testGetMember() {
        // given
        JoinRequestDto member1 = new JoinRequestDto(
            "테스트1",
            "test1@test.ttt",
            "Rkskekfk1!",
            "Rkskekfk1!"
        );
        Long memberId = memberService.join(member1);

        // when
        Optional<Member> result = Optional.ofNullable(memberService.findMember(memberId));

        // then
        assertThat(result).isNotNull();
        assertThat(result.get().getEmail()).isEqualTo(member1.email());
    }

    @Test
    @DisplayName("회원 ID 값이 존재하지 않는 값 (-999) 이므로 '데이터가 존재하지 않습니다' 예외가 발생해야 성공")
    void testGetMemberException() {
        // given
        Long nomemberId = -999L;

        // when
        Exception e = assertThrows(CommonBizException.class,
            () -> memberService.findMember(nomemberId));

        // then
        assertEquals(new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND).getMessage(),
            e.getMessage());
    }

    @Test
    @DisplayName("등록한 회원 ID 값과 조회한 회원 ID 값이 일치해야 성공")
    void testJoin() {
        // given
        JoinRequestDto member1 = new JoinRequestDto(
            "테스트1",
            "test1@test.ttt",
            "Rkskekfk1!",
            "Rkskekfk1!"
        );

        // when
        Long newMemberId = memberService.join(member1);
        log.info("newMemberId: {}", newMemberId);
        Optional<Member> lastMember = Optional.ofNullable(memberService.findMember(newMemberId));
        Long lastMemberId = lastMember.get().getMemberId();
        log.info("lastMemberId: {}", lastMemberId);

        // then
        assertThat(newMemberId).isEqualTo(lastMemberId);
    }

    @Test
    @DisplayName("입력한 이름 값과 수정 결과의 이름 값이 일치해야 성공")
    void testEdit() {
        // given
        JoinRequestDto member1 = new JoinRequestDto(
            "테스트1",
            "test1@test.ttt",
            "Rkskekfk1!",
            "Rkskekfk1!"
        );
        Long memberId1 = memberService.join(member1);

        // when
        Member member2 = new Member();
        member2.setMemberId(memberId1);
        member2.setName("테스트2");

        // then
        assertThat(member2.getName()).isEqualTo(
            Optional.of(memberService.editMember(member2)).get().getName());
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
            memberService.editMember(noMember));

        // then
        assertThat(e.getMessage())
            .isEqualTo(new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND).getMessage());
    }

    @Test
    @DisplayName("삭제한 데이터를 다시 조회 시도했으므로, '데이터가 존재하지 않습니다' 예외가 발생해야 성공")
    void testDelete() {
        // given
        JoinRequestDto member1 = new JoinRequestDto(
            "테스트1",
            "test1@test.ttt",
            "Rkskekfk1!",
            "Rkskekfk1!"
        );
        Long memberId = memberService.join(member1);
        log.info("memberId for delete test: {}", memberId);

        // when
        memberService.deleteMember(memberId);
        Exception e = assertThrows(CommonBizException.class, () ->
            memberService.findMember(memberId));

        // then
        assertThat(e.getMessage())
            .isEqualTo(new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND).getMessage());
    }
}
