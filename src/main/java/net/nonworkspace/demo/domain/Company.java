package net.nonworkspace.demo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Email;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.nonworkspace.demo.domain.dto.recruit.CompanyDto;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long id;

    private String companyName;

    private String description;

    @Email
    private String contactEmail;

    private String contactPhone;

    @OneToOne(mappedBy = "company", fetch = FetchType.EAGER)
    @JsonIgnore
    private Recruit recruit;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "등록일시")
    private LocalDateTime createDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "수정일시")
    private LocalDateTime updateDate;

    @PrePersist
    protected void onCreate() {
        if (this.createDate == null) {
            this.createDate = LocalDateTime.now();
        }
    }

    public static Company createCompany(String companyName, String description,
        String contactEmail, String contactPhone, Recruit recruit) {
        Company company = new Company();
        company.companyName = companyName;
        company.description = description;
        company.contactEmail = contactEmail;
        company.contactPhone = contactPhone;
        company.recruit = recruit;
        return company;
    }

    public void modifyCompany(CompanyDto companyDto) {
        this.companyName  = companyDto.companyName();
        this.description = companyDto.description();
        this.contactEmail = companyDto.contactEmail();
        this.contactPhone = companyDto.contactPhone();
        this.updateDate = LocalDateTime.now();
    }
}
