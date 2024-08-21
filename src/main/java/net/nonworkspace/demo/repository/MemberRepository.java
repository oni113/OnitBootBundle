package net.nonworkspace.demo.repository;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import net.nonworkspace.demo.domain.entity.Member;
import net.nonworkspace.demo.domain.entity.Password;
import net.nonworkspace.demo.domain.entity.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public List<Member> findAll(String name, Pageable pageable) {
        List<Member> result;
        int firstResult = pageable.getPageNumber() * pageable.getPageSize();

        // solve sort
        String sortQuery = createSortQuery(pageable);
        String query =
            "select m from Member m " + (!sortQuery.isEmpty() ? sortQuery : "");

        if (name != null && !name.isEmpty()) {
            query = "select m from Member m where m.name like CONCAT('%', :name, '%') " + (
                !sortQuery.isEmpty() ? sortQuery : "");
            result = em.createQuery(query, Member.class)
                .setParameter("name", name)
                .setFirstResult(firstResult)
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        } else {
            result = em.createQuery(query, Member.class)
                .setFirstResult(firstResult)
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        }
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

    public List<Password> findPasswordByMemberId(Long memberId) {
        List<Password> result = em.createQuery(
                "select p from Password p where p.member.memberId = :memberId", Password.class)
            .setParameter("memberId", memberId).getResultList();
        return result;
    }

    public List<Role> findRoleByMemberId(Long memberId) {
        List<Role> result = em.createQuery(
                "select r from Role r where r.member.memberId = :memberId", Role.class)
            .setParameter("memberId", memberId).getResultList();
        return result;
    }

    private static String createSortQuery(Pageable pageable) {
        String sortQuery = "";
        Sort sort = pageable.getSort();
        StringBuilder builder = new StringBuilder("");
        if (sort.isSorted()) {
            builder.append(" order by ");
            sort.forEach(s ->
                builder.append("m.")
                    .append(s.getProperty())
                    .append(" ")
                    .append(s.getDirection().name())
                    .append(" ,")
            );
            sortQuery = builder.substring(0, builder.length() - 2); // remove last comma
        }
        return sortQuery;
    }
}
