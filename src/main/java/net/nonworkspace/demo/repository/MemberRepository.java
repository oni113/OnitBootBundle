package net.nonworkspace.demo.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import net.nonworkspace.demo.domain.entity.*;
import net.nonworkspace.demo.utils.QuerydslUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    private final JPAQueryFactory queryFactory;

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

    public Member findOneQuery(Long memberId) {
        QMember member = QMember.member;

        return queryFactory
                .selectFrom(member)
                .where(member.memberId.eq(memberId))
                .fetchOne();
    }

    public List<Member> findAll() {
        String query = "select m from Member m order by m.memberId asc";
        List<Member> result = em.createQuery(query, Member.class).getResultList();

        return result;
    }

    public List<Member> findAllQuery() {
        QMember member = QMember.member;

        return queryFactory
                .selectFrom(member)
                .orderBy(member.memberId.asc())
                .fetch();
    }

    public List<Member> findAll(String name) {
        String query = "select m from Member m where name like CONCAT('%', :name, '%') order by m.memberId asc";
        List<Member> result = em.createQuery(query, Member.class).setParameter("name", name)
                .getResultList();

        return result;
    }

    public List<Member> findAllQuery(String name) {
        QMember member = QMember.member;

        return queryFactory
                .selectFrom(member)
                .where(member.name.contains(name))
                .orderBy(member.memberId.asc())
                .fetch();
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

    public List<Member> findAllPageQuery(String name, Pageable pageable) {
        int firstResult = pageable.getPageNumber() * pageable.getPageSize();

        // solve sort
        QMember member = QMember.member;
        PathBuilder<?> pathBuilder = new PathBuilder<>(Member.class, member.getMetadata());
        OrderSpecifier<?>[] orderSpecifiers = QuerydslUtil.toOrderSpecifiers(pageable.getSort(), pathBuilder);

        return queryFactory
                .select(member)
                .from(member)
                .where((name != null && !name.isEmpty()) ? member.name.contains(name) : null)
                .offset(firstResult)
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifiers)
                .fetch();
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

    public List<Password> findQueryPasswordByMemberId(Long memberId) {
        QPassword password = QPassword.password;

        return queryFactory
                .selectFrom(password)
                .where(password.member.memberId.eq(memberId))
                .fetch();
    }

    public List<Role> findRoleByMemberId(Long memberId) {
        List<Role> result = em.createQuery(
                        "select r from Role r where r.member.memberId = :memberId", Role.class)
                .setParameter("memberId", memberId).getResultList();
        return result;
    }

    public List<Role> findQueryRoleByMemberId(Long memberId) {
        QRole role = QRole.role;
        return queryFactory
                .selectFrom(role)
                .where(role.member.memberId.eq(memberId))
                .fetch();
    }

    private static String createSortQuery(Pageable pageable) {
        String sortQuery = "";
        Sort sort = pageable.getSort();
        StringBuilder builder = new StringBuilder();
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
