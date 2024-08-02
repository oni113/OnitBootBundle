package net.nonworkspace.demo.repository;

import net.nonworkspace.demo.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {

}
