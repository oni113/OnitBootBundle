package net.nonworkspace.demo.repository;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import net.nonworkspace.demo.domain.Member;
import net.nonworkspace.demo.domain.Password;
import net.nonworkspace.demo.domain.Role;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public Long saveMember(Member member) {
        em.persist(member);
        return member.getMemberId();
    }

    public Long savePassword(Password password) {
        em.persist(password);
        return password.getMemberPasswordId();
    }

    public Member find(Long memberId) {
        return em.find(Member.class, memberId);
    }

    public List<Member> findAll() {
        String query = "select m from Member m order by m.memberId asc";
        List<Member> result = em.createQuery(query, Member.class).getResultList();

        return result;
    }

    public List<Member> findAll(String name) {
        String query = "select m from Member m where name like CONCAT('%', :name, '%') order by m.memberId asc";
        List<Member> result = em.createQuery(query, Member.class).setParameter("name", name)
            .getResultList();

        return result;
    }

    public Optional<Member> findByEmail(String email) {
        List<Member> result = em.createQuery("select m from Member m where email = :email",
                Member.class)
            .setParameter("email", email).getResultList();
        return result.stream().findAny();
    }

    public void delete(Long memberId) {
        Member member = em.find(Member.class, memberId);
        em.createQuery(
                "delete from Password p where p.member.memberId = :memberId")
            .setParameter("memberId", memberId).executeUpdate();
        em.createQuery("delete from Role r where r.member.memberId = :memberId")
            .setParameter("memberId", memberId).executeUpdate();
        em.remove(member);
    }

    public Long saveRole(Role role) {
        em.persist(role);
        return role.getId();
    }
}
