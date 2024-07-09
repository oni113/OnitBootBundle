package net.nonworkspace.demo.domain;

import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import net.nonworkspace.demo.domain.embeddable.CreateInfo;

@Entity
@Data
@Table(name = "member_password")
@SequenceGenerator(name = "password_id_generator",
        sequenceName = "member_password_member_password_id_seq", initialValue = 1,
        allocationSize = 1)
public class Password {

    @Id
    @Column(name = "member_password_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "password_id_generator")
    @Schema(description = "회원 패스워드 ID (자동생성)")
    private Long memberPasswordId;

    @ManyToOne(/* cascade = CascadeType.ALL, */ fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Schema(description = "회원 패스워드")
    @Column(name = "member_password")
    private String memberPassword;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "패스워드 만료일시")
    private LocalDateTime expireDate;

    @Embedded
    private CreateInfo createInfo = new CreateInfo();

    public void setMember(Member member) {
        this.member = member;
        member.getPasswords().add(this);
    }
}
