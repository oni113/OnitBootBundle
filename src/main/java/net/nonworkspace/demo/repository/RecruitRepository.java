package net.nonworkspace.demo.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import net.nonworkspace.demo.domain.dto.recruit.CompanyDto;
import net.nonworkspace.demo.domain.entity.Recruit;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecruitRepository {

    private final EntityManager em;

    public List<Recruit> findAllRecruitWithCompany() {
        return em.createQuery(
                        "select distinct r from Recruit r"
                                + " join fetch Company c"
                                + " on r.company = c"
                                + " order by r.createDate desc", Recruit.class)
                .getResultList();
    }
}
