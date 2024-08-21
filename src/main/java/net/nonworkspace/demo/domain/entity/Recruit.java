package net.nonworkspace.demo.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.nonworkspace.demo.domain.code.RecruitType;
import net.nonworkspace.demo.domain.code.Salary;
import net.nonworkspace.demo.domain.dto.recruit.RecruitViewDto;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruit_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private RecruitType type;

    private String title;

    private String description;

    private String location;

    @Enumerated(EnumType.STRING)
    private Salary salary;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    private Company company;

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

    public static Recruit createRecruit(RecruitType type, String title, String description,
        Salary salary, String location, Company company) {
        Recruit recruit = new Recruit();
        recruit.type = type;
        recruit.title = title;
        recruit.description = description;
        recruit.salary = salary;
        recruit.location = location;
        recruit.company = company;
        return recruit;
    }

    public void modifyRecruit(RecruitViewDto recruitViewDto) {
        this.type = recruitViewDto.type();
        this.title = recruitViewDto.title();
        this.description = recruitViewDto.description();
        this.salary = recruitViewDto.salary();
        this.location = recruitViewDto.location();
        this.updateDate = LocalDateTime.now();
    }
}
