package net.nonworkspace.demo.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.domain.dto.user.JoinRequestDto;
import net.nonworkspace.demo.domain.entity.Member;
import net.nonworkspace.demo.domain.entity.QMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
class QueryDslTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberJpaService memberJpaService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void testMember() {
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

        JPAQueryFactory query = new JPAQueryFactory(em);
        QMember qMember = QMember.member;

        List<Member> memberList = query
                .selectFrom(qMember)
                .where(qMember.email.like("%test%"))
                .orderBy(qMember.name.asc())
                .fetch();

        log.debug("memberList : " + memberList.toString());

        for (Member member : memberList) {
            log.debug("----------------");
            log.debug("member name : " + member.getName());
            log.debug("Product email : " + member.getEmail());
            log.debug("----------------");
        }

        assertThat(memberList.size()).isEqualTo(2);
        assertThat(memberList.get(0).getName()).isEqualTo(member1.name());
        assertThat(memberList.get(1).getEmail()).isEqualTo(member2.email());
    }
}
