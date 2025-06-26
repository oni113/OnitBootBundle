package net.nonworkspace.demo.service;

import com.querydsl.jpa.impl.JPAQuery;
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

        JPAQuery<Member> query = new JPAQuery<>(em);
        QMember qMember = QMember.member;

        List<Member> memberList = query
                .from(qMember)
                .where(qMember.email.like("%test%"))
                .orderBy(qMember.createInfo.createDate.desc())
                .fetch();

        log.debug("memberList : " + memberList.toString());

        for (Member member : memberList) {
            log.debug("----------------");
            log.debug("member name : " + member.getName());
            log.debug("Product email : " + member.getEmail());
            log.debug("----------------");
        }
    }
}
