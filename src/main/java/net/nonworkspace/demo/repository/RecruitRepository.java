package net.nonworkspace.demo.repository;

import java.util.List;
import net.nonworkspace.demo.domain.entity.Recruit;
import net.nonworkspace.demo.domain.code.RecruitType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitRepository extends JpaRepository<Recruit, Long> {

    List<Recruit> findByTypeOrderByCreateDateDesc(RecruitType type);

    List<Recruit> findAllByOrderByCreateDateDesc();

    Page<Recruit> findByType(RecruitType type, Pageable pageable);
}
