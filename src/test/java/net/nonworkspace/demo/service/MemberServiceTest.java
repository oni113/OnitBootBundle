package net.nonworkspace.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import net.nonworkspace.demo.exception.common.CommonBizException;
import net.nonworkspace.demo.exception.common.CommonBizExceptionCode;
import net.nonworkspace.demo.model.MemberVO;

@SpringBootTest
@Transactional
@Slf4j
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @BeforeEach
    void cleanData() {

    }

    @Test
    @DisplayName("이름에 '테스트'가 들어간 회원 데이터가 2건이어야 성공")
    void testGetMemberList() {
        // given
        MemberVO member1 = new MemberVO();
        member1.setName("테스트1");
        member1.setEmail("test1@test.ttt");
        member1.setMemberPassword("Rkskekfk1!");
        memberService.join(member1);

        MemberVO member2 = new MemberVO();
        member2.setName("테스트2");
        member2.setEmail("test2@test.ttt");
        member2.setMemberPassword("Rkskekfk1!");
        memberService.join(member2);

        // when
        List<MemberVO> result = memberService.findMembers("테스트");

        // then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.stream().findFirst().get().getName()).isEqualTo("테스트1");
    }

    @Test
    @DisplayName("'중복된 이메일 값 입력' 예외가 발생해야 성공")
    void testJoinException() {
        // given
        MemberVO member1 = new MemberVO();
        member1.setName("테스트1");
        member1.setEmail("test1@test.ttt");
        member1.setMemberPassword("Rkskekfk1!");
        memberService.join(member1);

        MemberVO member2 = new MemberVO();
        member2.setName("테스트2");
        member2.setEmail("test1@test.ttt");
        member2.setMemberPassword("Rkskekfk1!");

        // when
        Exception e = assertThrows(CommonBizException.class, () -> memberService.join(member2));

        // then
        assertThat(e.getMessage()).isEqualTo(new CommonBizException(CommonBizExceptionCode.DATA_EMAIL_DUPLICATE).getMessage());
    }

    @Test
    @DisplayName("조회 결과가 null 이 아니고, 조회한 결과의 이메일 값이 등록한 이메일 값과 일치해야 성공")
    void testGetMember() {
        // given
        MemberVO member1 = new MemberVO();
        member1.setName("테스트1");
        member1.setEmail("test1@test.ttt");
        member1.setMemberPassword("Rkskekfk1#");
        memberService.join(member1);

        // when
        Optional<MemberVO> result = Optional.ofNullable(memberService.findMember(member1.getMemberId()));

        // then
        assertThat(result).isNotNull();
        assertThat(result.get().getEmail()).isEqualTo(member1.getEmail());
    }

    @Test
    @DisplayName("'잘못된 패스워드 값 형식' 예외가 발생해야 성공")
    void testPasswordValidation() {
        // given
        String alpha = "abcdefghijklmnopqrstuvwxyz";
        String numeric = "01234567890";
        String specialchar = "!@#$%^&*()-_=+";
        String tooShort = "ab12#$";
        String validPassword = "ab12D@#$&";

        // when
        MemberVO member1 = new MemberVO();
        member1.setName("테스트1");
        member1.setEmail("test1@test.ttt");
        member1.setMemberPassword(alpha);
        Exception e1 = assertThrows(CommonBizException.class, () -> memberService.join(member1));
        log.debug("passwd test1: {}", e1.getMessage());

        MemberVO member2 = new MemberVO();
        member2.setName("테스트2");
        member2.setEmail("test2@test.ttt");
        member2.setMemberPassword(numeric);
        Exception e2 = assertThrows(CommonBizException.class, () -> memberService.join(member2));
        log.debug("passwd test2: {}", e2.getMessage());

        MemberVO member3 = new MemberVO();
        member3.setName("테스트3");
        member3.setEmail("test3@test.ttt");
        member3.setMemberPassword(specialchar);
        Exception e3 = assertThrows(CommonBizException.class, () -> memberService.join(member3));
        log.debug("passwd test3: {}", e3.getMessage());

        MemberVO member4 = new MemberVO();
        member4.setName("테스트4");
        member4.setEmail("test4@test.ttt");
        member4.setMemberPassword(tooShort);
        Exception e4 = assertThrows(CommonBizException.class, () -> memberService.join(member4));
        log.debug("passwd test4: {}", e4.getMessage());

        MemberVO member5 = new MemberVO();
        member5.setName("테스트5");
        member5.setEmail("test5@test.ttt");
        member5.setMemberPassword(validPassword);
        Long newMemberId = memberService.join(member5);
        Optional<MemberVO> lastMember = Optional.ofNullable(memberService.findMember(newMemberId));
        Long lastMemberId = lastMember.get().getMemberId();

        // then
        assertEquals(new CommonBizException(CommonBizExceptionCode.INVALID_PASSWORD_FORMAT).getMessage(),
                e1.getMessage());
        assertEquals(new CommonBizException(CommonBizExceptionCode.INVALID_PASSWORD_FORMAT).getMessage(),
                e2.getMessage());
        assertEquals(new CommonBizException(CommonBizExceptionCode.INVALID_PASSWORD_FORMAT).getMessage(),
                e3.getMessage());
        assertEquals(new CommonBizException(CommonBizExceptionCode.INVALID_PASSWORD_FORMAT).getMessage(),
                e4.getMessage());
        assertThat(newMemberId).isEqualTo(lastMemberId);
    }

    @Test
    @DisplayName("회원 ID 값이 존재하지 않는 값 (-999) 이므로 '데이터가 존재하지 않습니다' 예외가 발생해야 성공")
    void testGetMemberException() {
        // given
        Long nomemberId = -999L;

        // when
        Exception e = assertThrows(CommonBizException.class, () -> memberService.findMember(nomemberId));

        // then
        assertEquals(new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND).getMessage(), e.getMessage());
    }

    @Test
    @DisplayName("등록한 회원 ID 값과 조회한 회원 ID 값이 일치해야 성공")
    void testJoin() {
        // given
        MemberVO member1 = new MemberVO();
        member1.setName("테스트1");
        member1.setEmail("test1@test.ttt");
        member1.setMemberPassword("Rkskekfk1!");

        // when
        Long newMemberId = memberService.join(member1);
        log.info("newMemberId: {}", newMemberId);
        Optional<MemberVO> lastMember = Optional.ofNullable(memberService.findMember(newMemberId));
        Long lastMemberId = lastMember.get().getMemberId();
        log.info("lastMemberId: {}", lastMemberId);

        // then
        assertThat(newMemberId).isEqualTo(lastMemberId);
    }

    @Test
    @DisplayName("입력한 이름 값과 수정 결과의 이름 값이 일치해야 성공")
    void testEdit() {
        // given
        MemberVO asis = new MemberVO();
        asis.setName("테스트1");
        asis.setEmail("test1@test.ttt");
        asis.setMemberPassword("Rkskekfk1!");
        memberService.join(asis);

        // when
        MemberVO tobe = new MemberVO();
        tobe.setMemberId(asis.getMemberId());
        tobe.setName("테스트2");

        // then
        assertThat(tobe.getName()).isEqualTo(Optional.ofNullable(memberService.editMember(tobe)).get().getName());
    }

    @Test
    @DisplayName("수정할 데이터의 회원 ID 값이 존재하지 않는 값 (-999) 이므로, '데이터가 존재하지 않습니다' 예외가 발생해야 성공")
    void testEditException() {
        // given
        MemberVO noMember = new MemberVO();
        noMember.setMemberId(-999L);
        noMember.setName("테스트1");

        // when
        Exception e = assertThrows(CommonBizException.class, () -> memberService.editMember(noMember));

        // then
        assertThat(e.getMessage())
                .isEqualTo(new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND).getMessage());
    }

    @Test
    @DisplayName("삭제한 데이터를 다시 조회 시도했으므로, '데이터가 존재하지 않습니다' 예외가 발생해야 성공")
    void testDelete() {
        // given
        MemberVO member1 = new MemberVO();
        member1.setName("테스트1");
        member1.setEmail("test1@test.ttt");
        member1.setMemberPassword("Rkskekfk1#");
        memberService.join(member1);
        Long memberId = member1.getMemberId();
        log.info("memberId for delete test: {}", memberId);

        // when
        memberService.deleteMember(memberId);
        Exception e = assertThrows(CommonBizException.class, () -> memberService.findMember(memberId));

        // then
        assertThat(e.getMessage())
                .isEqualTo(new CommonBizException(CommonBizExceptionCode.DATA_NOT_FOUND).getMessage());
    }
}
